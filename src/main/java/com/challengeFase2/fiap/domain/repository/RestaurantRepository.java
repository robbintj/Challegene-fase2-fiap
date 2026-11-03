package com.challengeFase2.fiap.domain.repository;

import com.challengeFase2.fiap.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAll();

    boolean existsByOwnerId(Long ownerId);

    void deleteById(Long id);
}
