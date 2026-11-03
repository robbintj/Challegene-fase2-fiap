package com.challengeFase2.fiap.infrastructure.persistence.adapter;

import com.challengeFase2.fiap.domain.model.UserType;
import com.challengeFase2.fiap.domain.repository.UserTypeRepository;
import com.challengeFase2.fiap.infrastructure.persistence.entity.UserTypeEntity;
import com.challengeFase2.fiap.infrastructure.persistence.jpa.UserTypeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserTypeRepositoryAdapter implements UserTypeRepository {

    private final UserTypeJpaRepository jpa;

    public UserTypeRepositoryAdapter(UserTypeJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public UserType save(UserType userType) {
        UserTypeEntity entity = new UserTypeEntity();
        entity.setId(userType.getId());
        entity.setName(userType.getName());
        return toDomain(jpa.save(entity));
    }

    @Override
    public Optional<UserType> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<UserType> findByName(String name) {
        return jpa.findByNameIgnoreCase(name).map(this::toDomain);
    }

    @Override
    public List<UserType> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    private UserType toDomain(UserTypeEntity entity) {
        return new UserType(entity.getId(), entity.getName());
    }
}
