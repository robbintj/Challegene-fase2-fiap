package com.challengeFase2.fiap.domain.repository;

import com.challengeFase2.fiap.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    boolean existsByUserTypeId(Long userTypeId);

    void deleteById(Long id);
}
