package com.challengeFase2.fiap.application.service;

import com.challengeFase2.fiap.application.command.RestaurantCommand;
import com.challengeFase2.fiap.domain.exception.BusinessRuleException;
import com.challengeFase2.fiap.domain.exception.ResourceNotFoundException;
import com.challengeFase2.fiap.domain.model.OpeningHours;
import com.challengeFase2.fiap.domain.model.Restaurant;
import com.challengeFase2.fiap.domain.model.User;
import com.challengeFase2.fiap.domain.model.UserType;
import com.challengeFase2.fiap.domain.repository.MenuItemRepository;
import com.challengeFase2.fiap.domain.repository.RestaurantRepository;
import com.challengeFase2.fiap.domain.repository.UserRepository;
import com.challengeFase2.fiap.domain.repository.UserTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RestaurantService {

    private static final String RESOURCE = "Restaurante";

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository,
                             UserRepository userRepository,
                             UserTypeRepository userTypeRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
    }

    public Restaurant create(RestaurantCommand command) {
        Restaurant restaurant = new Restaurant(null, command.name(), command.address(), command.cuisineType(),
                new OpeningHours(command.opensAt(), command.closesAt()), command.ownerId());
        assertOwnerIsValid(restaurant.getOwnerId());
        return restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE, id));
    }

    @Transactional(readOnly = true)
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant update(Long id, RestaurantCommand command) {
        Restaurant restaurant = findById(id);
        restaurant.update(command.name(), command.address(), command.cuisineType(),
                new OpeningHours(command.opensAt(), command.closesAt()), command.ownerId());
        assertOwnerIsValid(restaurant.getOwnerId());
        return restaurantRepository.save(restaurant);
    }

    public void delete(Long id) {
        findById(id);
        menuItemRepository.deleteAllByRestaurantId(id);
        restaurantRepository.deleteById(id);
    }

    private void assertOwnerIsValid(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", ownerId));
        boolean isOwnerType = userTypeRepository.findById(owner.getUserTypeId())
                .map(UserType::isRestaurantOwner)
                .orElse(false);
        if (!isOwnerType) {
            throw new BusinessRuleException(
                    "Usuário informado não possui o tipo '%s'".formatted(UserType.RESTAURANT_OWNER));
        }
    }
}
