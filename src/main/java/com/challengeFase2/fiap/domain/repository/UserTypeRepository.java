package com.challengeFase2.fiap.domain.repository;

import com.challengeFase2.fiap.domain.model.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeRepository {

    UserType save(UserType userType);

    Optional<UserType> findById(Long id);

    Optional<UserType> findByName(String name);

    List<UserType> findAll();

    void deleteById(Long id);
}
