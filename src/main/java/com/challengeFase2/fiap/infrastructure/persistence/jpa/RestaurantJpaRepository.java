package com.challengeFase2.fiap.infrastructure.persistence.jpa;

import com.challengeFase2.fiap.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Long> {

    boolean existsByOwnerId(Long ownerId);
}
