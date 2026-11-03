package com.challengeFase2.fiap.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI restaurantOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Sistema de Gestão de Restaurantes")
                .description("API do Tech Challenge - Fase 2")
                .version("1.0.0"));
    }
}
