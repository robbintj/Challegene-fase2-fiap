package com.challengeFase2.fiap.infrastructure.persistence.jpa;

import com.challengeFase2.fiap.infrastructure.persistence.entity.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTypeJpaRepository extends JpaRepository<UserTypeEntity, Long> {

    Optional<UserTypeEntity> findByNameIgnoreCase(String name);
}
