package com.challengeFase2.fiap.domain.model;

import com.challengeFase2.fiap.domain.exception.BusinessRuleException;

import java.math.BigDecimal;

final class Guard {

    private Guard() {
    }

    static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException(field + " é obrigatório");
        }
        return value.trim();
    }

    static <T> T requireNonNull(T value, String field) {
        if (value == null) {
            throw new BusinessRuleException(field + " é obrigatório");
        }
        return value;
    }

    static BigDecimal requirePositive(BigDecimal value, String field) {
        if (requireNonNull(value, field).signum() <= 0) {
            throw new BusinessRuleException(field + " deve ser maior que zero");
        }
        return value;
    }
}
