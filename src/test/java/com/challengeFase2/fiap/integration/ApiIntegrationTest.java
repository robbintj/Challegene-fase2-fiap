package com.challengeFase2.fiap.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    private ResultActions send(MockHttpServletRequestBuilder request, String body) throws Exception {
        return mvc.perform(request.contentType(MediaType.APPLICATION_JSON).content(body));
    }

    private long idOf(ResultActions result) throws Exception {
        String json = result.andReturn().getResponse().getContentAsString();
        return Long.parseLong(json.replaceAll(".*?\"id\":(\\d+).*", "$1"));
    }

    private long ownerTypeId() throws Exception {
        String json = mvc.perform(get("/api/user-types")).andReturn().getResponse().getContentAsString();
        return Long.parseLong(json.replaceAll(".*?\"id\":(\\d+),\"name\":\"Dono de Restaurante\".*", "$1"));
    }

    @Test
    void defaultUserTypesAreSeeded() throws Exception {
        mvc.perform(get("/api/user-types")).andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name=='Cliente')]").exists())
                .andExpect(jsonPath("$[?(@.name=='Dono de Restaurante')]").exists());
    }

    @Test
    void userTypeCrudAndConflicts() throws Exception {
        long id = idOf(send(post("/api/user-types"), "{\"name\":\"Gerente\"}").andExpect(status().isCreated()));
        send(post("/api/user-types"), "{\"name\":\"Gerente\"}").andExpect(status().isConflict());
        send(post("/api/user-types"), "{\"name\":\"\"}").andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasSize(1)));
        mvc.perform(get("/api/user-types/" + id)).andExpect(jsonPath("$.name").value("Gerente"));
        send(put("/api/user-types/" + id), "{\"name\":\"Gerente Geral\"}")
                .andExpect(jsonPath("$.name").value("Gerente Geral"));
        mvc.perform(delete("/api/user-types/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/user-types/" + id)).andExpect(status().isNotFound());
        mvc.perform(get("/api/user-types/abc")).andExpect(status().isBadRequest());
        send(post("/api/user-types"), "{invalid").andExpect(status().isBadRequest());
    }

    @Test
    void fullFlowUserRestaurantMenuItem() throws Exception {
        long typeId = ownerTypeId();
        long userId = idOf(send(post("/api/users"),
                "{\"name\":\"Maria\",\"email\":\"maria@flow.com\",\"userTypeId\":%d}".formatted(typeId))
                .andExpect(status().isCreated()));
        send(post("/api/users"), "{\"name\":\"Dup\",\"email\":\"MARIA@flow.com\",\"userTypeId\":%d}".formatted(typeId))
                .andExpect(status().isConflict());
        send(post("/api/users"), "{\"name\":\"X\",\"email\":\"x@flow.com\",\"userTypeId\":99999}")
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/user-types/" + typeId)).andExpect(status().isConflict());

        String restaurantBody = ("{\"name\":\"Cantina\",\"address\":\"Rua 1\",\"cuisineType\":\"Italiana\","
                + "\"opensAt\":\"11:00\",\"closesAt\":\"23:00\",\"ownerId\":%d}").formatted(userId);
        long restaurantId = idOf(send(post("/api/restaurants"), restaurantBody).andExpect(status().isCreated())
                .andExpect(jsonPath("$.opensAt").value("11:00:00")));
        send(put("/api/restaurants/" + restaurantId), restaurantBody.replace("Cantina", "Cantina 2"))
                .andExpect(jsonPath("$.name").value("Cantina 2"));
        mvc.perform(get("/api/restaurants")).andExpect(status().isOk());
        mvc.perform(delete("/api/users/" + userId)).andExpect(status().isConflict());
        send(post("/api/restaurants"), restaurantBody.replace("11:00", "23:00"))
                .andExpect(status().isUnprocessableEntity());

        String itemBody = "{\"name\":\"Lasanha\",\"description\":\"Boa\",\"price\":42.90,"
                + "\"dineInOnly\":true,\"photoPath\":\"/img/l.jpg\"}";
        String itemsUrl = "/api/restaurants/" + restaurantId + "/menu-items";
        long itemId = idOf(send(post(itemsUrl), itemBody).andExpect(status().isCreated())
                .andExpect(jsonPath("$.dineInOnly").value(true)));
        send(put(itemsUrl + "/" + itemId), itemBody.replace("42.90", "50.00"))
                .andExpect(jsonPath("$.price").value(50.00));
        mvc.perform(get(itemsUrl)).andExpect(jsonPath("$", hasSize(1)));
        send(post(itemsUrl), itemBody.replace("42.90", "-1")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/restaurants/99999/menu-items")).andExpect(status().isNotFound());

        mvc.perform(delete(itemsUrl + "/" + itemId)).andExpect(status().isNoContent());
        mvc.perform(get(itemsUrl + "/" + itemId)).andExpect(status().isNotFound());
        send(post(itemsUrl), itemBody);
        mvc.perform(delete("/api/restaurants/" + restaurantId)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/users/" + userId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/users/" + userId)).andExpect(status().isNotFound());
    }

    @Test
    void restaurantOwnerMustHaveOwnerType() throws Exception {
        String json = mvc.perform(get("/api/user-types")).andReturn().getResponse().getContentAsString();
        long customerType = Long.parseLong(json.replaceAll(".*?\"id\":(\\d+),\"name\":\"Cliente\".*", "$1"));
        long customerId = idOf(send(post("/api/users"),
                "{\"name\":\"Joao\",\"email\":\"joao@c.com\",\"userTypeId\":%d}".formatted(customerType)));
        send(put("/api/users/" + customerId),
                "{\"name\":\"Joao S\",\"email\":\"joao@c.com\",\"userTypeId\":%d}".formatted(customerType))
                .andExpect(jsonPath("$.name").value("Joao S"));
        send(post("/api/restaurants"), ("{\"name\":\"R\",\"address\":\"A\",\"cuisineType\":\"C\","
                + "\"opensAt\":\"10:00\",\"closesAt\":\"20:00\",\"ownerId\":%d}").formatted(customerId))
                .andExpect(status().isUnprocessableEntity());
    }
}
