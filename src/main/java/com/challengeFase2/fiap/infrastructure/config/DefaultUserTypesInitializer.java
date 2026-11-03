package com.challengeFase2.fiap.infrastructure.config;

import com.challengeFase2.fiap.application.command.UserTypeCommand;
import com.challengeFase2.fiap.application.service.UserTypeService;
import com.challengeFase2.fiap.domain.exception.ConflictException;
import com.challengeFase2.fiap.domain.model.UserType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Garante a existência dos tipos de usuário padrão exigidos pelo negócio. */
@Component
public class DefaultUserTypesInitializer implements ApplicationRunner {

    private final UserTypeService service;

    public DefaultUserTypesInitializer(UserTypeService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        List.of(UserType.RESTAURANT_OWNER, UserType.CUSTOMER).forEach(this::createIfMissing);
    }

    private void createIfMissing(String name) {
        try {
            service.create(new UserTypeCommand(name));
        } catch (ConflictException alreadyExists) {
            // tipo já cadastrado: nada a fazer
        }
    }
}
