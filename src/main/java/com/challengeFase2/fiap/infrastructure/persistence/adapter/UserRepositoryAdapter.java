package com.challengeFase2.fiap.infrastructure.persistence.adapter;

import com.challengeFase2.fiap.domain.model.User;
import com.challengeFase2.fiap.domain.repository.UserRepository;
import com.challengeFase2.fiap.infrastructure.persistence.entity.UserEntity;
import com.challengeFase2.fiap.infrastructure.persistence.entity.UserTypeEntity;
import com.challengeFase2.fiap.infrastructure.persistence.jpa.UserJpaRepository;
import com.challengeFase2.fiap.infrastructure.persistence.jpa.UserTypeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpa;
    private final UserTypeJpaRepository userTypeJpa;

    public UserRepositoryAdapter(UserJpaRepository jpa, UserTypeJpaRepository userTypeJpa) {
        this.jpa = jpa;
        this.userTypeJpa = userTypeJpa;
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        UserTypeEntity type = userTypeJpa.getReferenceById(user.getUserTypeId());
        entity.setUserType(type);
        return toDomain(jpa.save(entity));
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByUserTypeId(Long userTypeId) {
        return jpa.existsByUserTypeId(userTypeId);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    private User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getUserType().getId());
    }
}
