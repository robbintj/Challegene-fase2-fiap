package com.challengeFase2.fiap.application.command;

import java.math.BigDecimal;

public record MenuItemCommand(String name, String description, BigDecimal price,
                              boolean dineInOnly, String photoPath) {
}
