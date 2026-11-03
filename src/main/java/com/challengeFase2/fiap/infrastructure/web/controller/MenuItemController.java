package com.challengeFase2.fiap.infrastructure.web.controller;

import com.challengeFase2.fiap.application.service.MenuItemService;
import com.challengeFase2.fiap.infrastructure.web.dto.MenuItemRequest;
import com.challengeFase2.fiap.infrastructure.web.dto.MenuItemResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@Tag(name = "Itens do cardápio")
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse create(@PathVariable Long restaurantId, @Valid @RequestBody MenuItemRequest request) {
        return MenuItemResponse.from(service.create(restaurantId, request.toCommand()));
    }

    @GetMapping
    public List<MenuItemResponse> findAll(@PathVariable Long restaurantId) {
        return service.findAll(restaurantId).stream().map(MenuItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public MenuItemResponse findById(@PathVariable Long restaurantId, @PathVariable Long id) {
        return MenuItemResponse.from(service.findById(restaurantId, id));
    }

    @PutMapping("/{id}")
    public MenuItemResponse update(@PathVariable Long restaurantId, @PathVariable Long id,
                                   @Valid @RequestBody MenuItemRequest request) {
        return MenuItemResponse.from(service.update(restaurantId, id, request.toCommand()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long restaurantId, @PathVariable Long id) {
        service.delete(restaurantId, id);
    }
}
