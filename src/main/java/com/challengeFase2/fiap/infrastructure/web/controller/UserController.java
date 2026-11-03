package com.challengeFase2.fiap.infrastructure.web.controller;

import com.challengeFase2.fiap.application.service.UserService;
import com.challengeFase2.fiap.infrastructure.web.dto.UserRequest;
import com.challengeFase2.fiap.infrastructure.web.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return UserResponse.from(service.create(request.toCommand()));
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return service.findAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.from(service.findById(id));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return UserResponse.from(service.update(id, request.toCommand()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
