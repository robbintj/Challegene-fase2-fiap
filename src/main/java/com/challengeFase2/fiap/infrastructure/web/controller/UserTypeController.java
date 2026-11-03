package com.challengeFase2.fiap.infrastructure.web.controller;

import com.challengeFase2.fiap.application.service.UserTypeService;
import com.challengeFase2.fiap.infrastructure.web.dto.UserTypeRequest;
import com.challengeFase2.fiap.infrastructure.web.dto.UserTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-types")
@Tag(name = "Tipos de usuário")
public class UserTypeController {

    private final UserTypeService service;

    public UserTypeController(UserTypeService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserTypeResponse create(@Valid @RequestBody UserTypeRequest request) {
        return UserTypeResponse.from(service.create(request.toCommand()));
    }

    @GetMapping
    public List<UserTypeResponse> findAll() {
        return service.findAll().stream().map(UserTypeResponse::from).toList();
    }

    @GetMapping("/{id}")
    public UserTypeResponse findById(@PathVariable Long id) {
        return UserTypeResponse.from(service.findById(id));
    }

    @PutMapping("/{id}")
    public UserTypeResponse update(@PathVariable Long id, @Valid @RequestBody UserTypeRequest request) {
        return UserTypeResponse.from(service.update(id, request.toCommand()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
