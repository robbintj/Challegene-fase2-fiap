package com.challengeFase2.fiap.application.service;

import com.challengeFase2.fiap.application.command.MenuItemCommand;
import com.challengeFase2.fiap.domain.exception.ResourceNotFoundException;
import com.challengeFase2.fiap.domain.model.MenuItem;
import com.challengeFase2.fiap.domain.repository.MenuItemRepository;
import com.challengeFase2.fiap.domain.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuItemService {

    private static final String RESOURCE = "Item do cardápio";

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public MenuItem create(Long restaurantId, MenuItemCommand command) {
        assertRestaurantExists(restaurantId);
        MenuItem item = new MenuItem(null, restaurantId, command.name(), command.description(),
                command.price(), command.dineInOnly(), command.photoPath());
        return menuItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public MenuItem findById(Long restaurantId, Long id) {
        assertRestaurantExists(restaurantId);
        return menuItemRepository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE, id));
    }

    @Transactional(readOnly = true)
    public List<MenuItem> findAll(Long restaurantId) {
        assertRestaurantExists(restaurantId);
        return menuItemRepository.findAllByRestaurantId(restaurantId);
    }

    public MenuItem update(Long restaurantId, Long id, MenuItemCommand command) {
        MenuItem item = findById(restaurantId, id);
        item.update(command.name(), command.description(), command.price(),
                command.dineInOnly(), command.photoPath());
        return menuItemRepository.save(item);
    }

    public void delete(Long restaurantId, Long id) {
        findById(restaurantId, id);
        menuItemRepository.deleteById(id);
    }

    private void assertRestaurantExists(Long restaurantId) {
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", restaurantId));
    }
}
