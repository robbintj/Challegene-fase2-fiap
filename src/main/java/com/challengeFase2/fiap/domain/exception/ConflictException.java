package com.challengeFase2.fiap.domain.exception;

/** Operação conflita com o estado atual dos dados (duplicidade ou vínculo existente). */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
