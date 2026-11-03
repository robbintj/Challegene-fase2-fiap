package com.challengeFase2.fiap.infrastructure.persistence.jpa;

import com.challengeFase2.fiap.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuItemJpaRepository extends JpaRepository<MenuItemEntity, Long> {

    Optional<MenuItemEntity> findByIdAndRestaurantId(Long id, Long restaurantId);

    List<MenuItemEntity> findAllByRestaurantId(Long restaurantId);

    @Modifying
    @Query("delete from MenuItemEntity m where m.restaurant.id = :restaurantId")
    void deleteAllByRestaurantId(Long restaurantId);
}
