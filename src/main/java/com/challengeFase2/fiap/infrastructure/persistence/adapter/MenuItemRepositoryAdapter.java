package com.challengeFase2.fiap.infrastructure.persistence.adapter;

import com.challengeFase2.fiap.domain.model.MenuItem;
import com.challengeFase2.fiap.domain.repository.MenuItemRepository;
import com.challengeFase2.fiap.infrastructure.persistence.entity.MenuItemEntity;
import com.challengeFase2.fiap.infrastructure.persistence.jpa.MenuItemJpaRepository;
import com.challengeFase2.fiap.infrastructure.persistence.jpa.RestaurantJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuItemRepositoryAdapter implements MenuItemRepository {

    private final MenuItemJpaRepository jpa;
    private final RestaurantJpaRepository restaurantJpa;

    public MenuItemRepositoryAdapter(MenuItemJpaRepository jpa, RestaurantJpaRepository restaurantJpa) {
        this.jpa = jpa;
        this.restaurantJpa = restaurantJpa;
    }

    @Override
    public MenuItem save(MenuItem item) {
        MenuItemEntity entity = new MenuItemEntity();
        entity.setId(item.getId());
        entity.setRestaurant(restaurantJpa.getReferenceById(item.getRestaurantId()));
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        entity.setPrice(item.getPrice());
        entity.setDineInOnly(item.isDineInOnly());
        entity.setPhotoPath(item.getPhotoPath());
        return toDomain(jpa.save(entity));
    }

    @Override
    public Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId) {
        return jpa.findByIdAndRestaurantId(id, restaurantId).map(this::toDomain);
    }

    @Override
    public List<MenuItem> findAllByRestaurantId(Long restaurantId) {
        return jpa.findAllByRestaurantId(restaurantId).stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public void deleteAllByRestaurantId(Long restaurantId) {
        jpa.deleteAllByRestaurantId(restaurantId);
    }

    private MenuItem toDomain(MenuItemEntity e) {
        return new MenuItem(e.getId(), e.getRestaurant().getId(), e.getName(), e.getDescription(),
                e.getPrice(), e.isDineInOnly(), e.getPhotoPath());
    }
}
