package com.challengeFase2.fiap.application.service;

import com.challengeFase2.fiap.application.command.UserCommand;
import com.challengeFase2.fiap.domain.exception.ConflictException;
import com.challengeFase2.fiap.domain.exception.ResourceNotFoundException;
import com.challengeFase2.fiap.domain.model.User;
import com.challengeFase2.fiap.domain.repository.RestaurantRepository;
import com.challengeFase2.fiap.domain.repository.UserRepository;
import com.challengeFase2.fiap.domain.repository.UserTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private static final String RESOURCE = "Usuário";

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final RestaurantRepository restaurantRepository;

    public UserService(UserRepository userRepository,
                       UserTypeRepository userTypeRepository,
                       RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public User create(UserCommand command) {
        User user = new User(null, command.name(), command.email(), command.userTypeId());
        assertUserTypeExists(user.getUserTypeId());
        assertEmailAvailable(user.getEmail(), null);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE, id));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(Long id, UserCommand command) {
        User user = findById(id);
        user.update(command.name(), command.email(), command.userTypeId());
        assertUserTypeExists(user.getUserTypeId());
        assertEmailAvailable(user.getEmail(), id);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        findById(id);
        if (restaurantRepository.existsByOwnerId(id)) {
            throw new ConflictException("Usuário é dono de restaurante(s) e não pode ser removido");
        }
        userRepository.deleteById(id);
    }

    private void assertUserTypeExists(Long userTypeId) {
        userTypeRepository.findById(userTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário", userTypeId));
    }

    private void assertEmailAvailable(String email, Long currentId) {
        userRepository.findByEmail(email)
                .filter(existing -> !existing.getId().equals(currentId))
                .ifPresent(existing -> {
                    throw new ConflictException("Já existe um usuário com o e-mail '%s'".formatted(email));
                });
    }
}
