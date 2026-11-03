package com.challengeFase2.fiap.infrastructure.web.controller;

import com.challengeFase2.fiap.application.service.RestaurantService;
import com.challengeFase2.fiap.infrastructure.web.dto.RestaurantRequest;
import com.challengeFase2.fiap.infrastructure.web.dto.RestaurantResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurantes")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse create(@Valid @RequestBody RestaurantRequest request) {
        return RestaurantResponse.from(service.create(request.toCommand()));
    }

    @GetMapping
    public List<RestaurantResponse> findAll() {
        return service.findAll().stream().map(RestaurantResponse::from).toList();
    }

    @GetMapping("/{id}")
    public RestaurantResponse findById(@PathVariable Long id) {
        return RestaurantResponse.from(service.findById(id));
    }

    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id, @Valid @RequestBody RestaurantRequest request) {
        return RestaurantResponse.from(service.update(id, request.toCommand()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
