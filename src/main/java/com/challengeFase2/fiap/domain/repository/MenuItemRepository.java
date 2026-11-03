package com.challengeFase2.fiap.domain.repository;

import com.challengeFase2.fiap.domain.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId);

    List<MenuItem> findAllByRestaurantId(Long restaurantId);

    void deleteById(Long id);

    void deleteAllByRestaurantId(Long restaurantId);
}
