package com.challengeFase2.fiap.infrastructure.persistence.jpa;

import com.challengeFase2.fiap.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUserTypeId(Long userTypeId);
}
