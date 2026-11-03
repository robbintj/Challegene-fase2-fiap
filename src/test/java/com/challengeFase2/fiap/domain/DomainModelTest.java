package com.challengeFase2.fiap.domain;

import com.challengeFase2.fiap.domain.exception.BusinessRuleException;
import com.challengeFase2.fiap.domain.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {

    private static final OpeningHours HOURS = new OpeningHours(LocalTime.of(10, 0), LocalTime.of(22, 0));

    @Test
    void userTypeShouldRecognizeRestaurantOwnerIgnoringCase() {
        assertTrue(new UserType(1L, "dono de restaurante").isRestaurantOwner());
        assertFalse(new UserType(2L, UserType.CUSTOMER).isRestaurantOwner());
    }

    @Test
    void userTypeShouldRejectBlankName() {
        assertThrows(BusinessRuleException.class, () -> new UserType(null, " "));
        UserType type = new UserType(1L, "A");
        assertThrows(BusinessRuleException.class, () -> type.rename(null));
    }

    @Test
    void userShouldNormalizeEmailAndRequireType() {
        User user = new User(1L, "Ana", "ANA@Email.com", 1L);
        assertEquals("ana@email.com", user.getEmail());
        assertEquals("Ana", user.getName());
        assertEquals(1L, user.getId());
        assertEquals(1L, user.getUserTypeId());
        assertThrows(BusinessRuleException.class, () -> new User(null, "Ana", "a@a.com", null));
    }

    @Test
    void openingHoursShouldRejectEqualOrMissingTimes() {
        LocalTime t = LocalTime.NOON;
        assertThrows(BusinessRuleException.class, () -> new OpeningHours(t, t));
        assertThrows(BusinessRuleException.class, () -> new OpeningHours(null, t));
        assertThrows(BusinessRuleException.class, () -> new OpeningHours(t, null));
    }

    @Test
    void restaurantShouldValidateAndUpdate() {
        Restaurant r = new Restaurant(1L, "Bella", "Rua 1", "Italiana", HOURS, 5L);
        r.update("Nova", "Rua 2", "Japonesa", HOURS, 6L);
        assertEquals("Nova", r.getName());
        assertEquals("Rua 2", r.getAddress());
        assertEquals("Japonesa", r.getCuisineType());
        assertEquals(HOURS, r.getOpeningHours());
        assertEquals(6L, r.getOwnerId());
        assertEquals(1L, r.getId());
        assertThrows(BusinessRuleException.class, () -> new Restaurant(null, "A", "B", "C", null, 1L));
        assertThrows(BusinessRuleException.class, () -> new Restaurant(null, "A", "B", "C", HOURS, null));
    }

    @Test
    void menuItemShouldValidateFields() {
        MenuItem item = new MenuItem(1L, 2L, "Pizza", "Queijo", new BigDecimal("30.00"), true, "/p.jpg");
        assertTrue(item.isDineInOnly());
        assertEquals(2L, item.getRestaurantId());
        assertEquals("Queijo", item.getDescription());
        assertEquals("/p.jpg", item.getPhotoPath());
        assertEquals(new BigDecimal("30.00"), item.getPrice());
        assertEquals("Pizza", item.getName());
        assertEquals(1L, item.getId());
        assertThrows(BusinessRuleException.class,
                () -> new MenuItem(null, 2L, "Pizza", "Queijo", BigDecimal.ZERO, false, "/p.jpg"));
        assertThrows(BusinessRuleException.class,
                () -> new MenuItem(null, 2L, "Pizza", "Queijo", null, false, "/p.jpg"));
        assertThrows(BusinessRuleException.class,
                () -> new MenuItem(null, null, "Pizza", "Queijo", BigDecimal.ONE, false, "/p.jpg"));
    }
}
