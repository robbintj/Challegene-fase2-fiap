package com.challengeFase2.fiap.infrastructure.persistence.adapter;

import com.challengeFase2.fiap.domain.model.OpeningHours;
import com.challengeFase2.fiap.domain.model.Restaurant;
import com.challengeFase2.fiap.domain.repository.RestaurantRepository;
import com.challengeFase2.fiap.infrastructure.persistence.entity.RestaurantEntity;
import com.challengeFase2.fiap.infrastructure.persistence.jpa.RestaurantJpaRepository;
import com.challengeFase2.fiap.infrastructure.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RestaurantRepositoryAdapter implements RestaurantRepository {

    private final RestaurantJpaRepository jpa;
    private final UserJpaRepository userJpa;

    public RestaurantRepositoryAdapter(RestaurantJpaRepository jpa, UserJpaRepository userJpa) {
        this.jpa = jpa;
        this.userJpa = userJpa;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(restaurant.getId());
        entity.setName(restaurant.getName());
        entity.setAddress(restaurant.getAddress());
        entity.setCuisineType(restaurant.getCuisineType());
        entity.setOpensAt(restaurant.getOpeningHours().opensAt());
        entity.setClosesAt(restaurant.getOpeningHours().closesAt());
        entity.setOwner(userJpa.getReferenceById(restaurant.getOwnerId()));
        return toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByOwnerId(Long ownerId) {
        return jpa.existsByOwnerId(ownerId);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    private Restaurant toDomain(RestaurantEntity entity) {
        return new Restaurant(entity.getId(), entity.getName(), entity.getAddress(), entity.getCuisineType(),
                new OpeningHours(entity.getOpensAt(), entity.getClosesAt()), entity.getOwner().getId());
    }
}
