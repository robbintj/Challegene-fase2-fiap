package com.challengeFase2.fiap.domain.model;

import com.challengeFase2.fiap.domain.exception.BusinessRuleException;

import java.time.LocalTime;

public record OpeningHours(LocalTime opensAt, LocalTime closesAt) {

    public OpeningHours {
        Guard.requireNonNull(opensAt, "Horário de abertura");
        Guard.requireNonNull(closesAt, "Horário de fechamento");
        if (opensAt.equals(closesAt)) {
            throw new BusinessRuleException("Horário de abertura e fechamento não podem ser iguais");
        }
    }
}
