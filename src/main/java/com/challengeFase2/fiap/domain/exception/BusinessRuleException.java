package com.challengeFase2.fiap.domain.exception;

/** Violação de regra de negócio ou de invariante de domínio. */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
