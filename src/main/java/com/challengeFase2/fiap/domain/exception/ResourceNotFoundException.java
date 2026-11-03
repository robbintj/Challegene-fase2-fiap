package com.challengeFase2.fiap.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super("%s com id %d não encontrado(a)".formatted(resource, id));
    }
}
