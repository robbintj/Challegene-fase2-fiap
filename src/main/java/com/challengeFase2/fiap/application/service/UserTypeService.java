package com.challengeFase2.fiap.application.service;

import com.challengeFase2.fiap.application.command.UserTypeCommand;
import com.challengeFase2.fiap.domain.exception.ConflictException;
import com.challengeFase2.fiap.domain.exception.ResourceNotFoundException;
import com.challengeFase2.fiap.domain.model.UserType;
import com.challengeFase2.fiap.domain.repository.UserRepository;
import com.challengeFase2.fiap.domain.repository.UserTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserTypeService {

    private static final String RESOURCE = "Tipo de usuário";

    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;

    public UserTypeService(UserTypeRepository userTypeRepository, UserRepository userRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
    }

    public UserType create(UserTypeCommand command) {
        UserType userType = new UserType(null, command.name());
        assertNameAvailable(userType.getName(), null);
        return userTypeRepository.save(userType);
    }

    @Transactional(readOnly = true)
    public UserType findById(Long id) {
        return userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE, id));
    }

    @Transactional(readOnly = true)
    public List<UserType> findAll() {
        return userTypeRepository.findAll();
    }

    public UserType update(Long id, UserTypeCommand command) {
        UserType userType = findById(id);
        userType.rename(command.name());
        assertNameAvailable(userType.getName(), id);
        return userTypeRepository.save(userType);
    }

    public void delete(Long id) {
        findById(id);
        if (userRepository.existsByUserTypeId(id)) {
            throw new ConflictException("Tipo de usuário está associado a usuários e não pode ser removido");
        }
        userTypeRepository.deleteById(id);
    }

    private void assertNameAvailable(String name, Long currentId) {
        userTypeRepository.findByName(name)
                .filter(existing -> !existing.getId().equals(currentId))
                .ifPresent(existing -> {
                    throw new ConflictException("Já existe um tipo de usuário com o nome '%s'".formatted(name));
                });
    }
}
