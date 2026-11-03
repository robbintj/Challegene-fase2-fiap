package com.challengeFase2.fiap.application;

import com.challengeFase2.fiap.application.command.*;
import com.challengeFase2.fiap.application.service.*;
import com.challengeFase2.fiap.domain.exception.*;
import com.challengeFase2.fiap.domain.model.*;
import com.challengeFase2.fiap.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServicesTest {

    @Mock UserTypeRepository userTypeRepository;
    @Mock UserRepository userRepository;
    @Mock RestaurantRepository restaurantRepository;
    @Mock MenuItemRepository menuItemRepository;

    UserTypeService userTypeService;
    UserService userService;
    RestaurantService restaurantService;
    MenuItemService menuItemService;

    final OpeningHours hours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(18, 0));

    @BeforeEach
    void setUp() {
        userTypeService = new UserTypeService(userTypeRepository, userRepository);
        userService = new UserService(userRepository, userTypeRepository, restaurantRepository);
        restaurantService = new RestaurantService(restaurantRepository, menuItemRepository,
                userRepository, userTypeRepository);
        menuItemService = new MenuItemService(menuItemRepository, restaurantRepository);
        when(userTypeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(restaurantRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(menuItemRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    // ---- UserType
    @Test
    void userTypeCreateSavesWhenNameAvailable() {
        when(userTypeRepository.findByName("Cliente")).thenReturn(Optional.empty());
        assertEquals("Cliente", userTypeService.create(new UserTypeCommand("Cliente")).getName());
    }

    @Test
    void userTypeCreateFailsOnDuplicateName() {
        when(userTypeRepository.findByName("Cliente")).thenReturn(Optional.of(new UserType(1L, "Cliente")));
        assertThrows(ConflictException.class, () -> userTypeService.create(new UserTypeCommand("Cliente")));
    }

    @Test
    void userTypeUpdateAllowsSameRecordName() {
        UserType existing = new UserType(1L, "Cliente");
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userTypeRepository.findByName("Cliente")).thenReturn(Optional.of(existing));
        assertEquals("Cliente", userTypeService.update(1L, new UserTypeCommand("Cliente")).getName());
    }

    @Test
    void userTypeFindAndNotFound() {
        when(userTypeRepository.findAll()).thenReturn(List.of(new UserType(1L, "A")));
        assertEquals(1, userTypeService.findAll().size());
        assertThrows(ResourceNotFoundException.class, () -> userTypeService.findById(9L));
    }

    @Test
    void userTypeDeleteFailsWhenInUse() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(new UserType(1L, "A")));
        when(userRepository.existsByUserTypeId(1L)).thenReturn(true);
        assertThrows(ConflictException.class, () -> userTypeService.delete(1L));
        verify(userTypeRepository, never()).deleteById(any());
    }

    @Test
    void userTypeDeleteRemovesWhenFree() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(new UserType(1L, "A")));
        userTypeService.delete(1L);
        verify(userTypeRepository).deleteById(1L);
    }

    // ---- User
    @Test
    void userCreateRequiresExistingType() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.create(new UserCommand("Ana", "a@a.com", 1L)));
    }

    @Test
    void userCreateFailsOnDuplicateEmail() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(new UserType(1L, "A")));
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(new User(7L, "X", "a@a.com", 1L)));
        assertThrows(ConflictException.class, () -> userService.create(new UserCommand("Ana", "a@a.com", 1L)));
    }

    @Test
    void userCreateUpdateFindDelete() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(new UserType(1L, "A")));
        assertEquals("Ana", userService.create(new UserCommand("Ana", "a@a.com", 1L)).getName());

        User existing = new User(5L, "Old", "old@a.com", 1L);
        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        assertEquals("New", userService.update(5L, new UserCommand("New", "new@a.com", 1L)).getName());
        assertEquals(existing, userService.findById(5L));
        when(userRepository.findAll()).thenReturn(List.of(existing));
        assertEquals(1, userService.findAll().size());

        userService.delete(5L);
        verify(userRepository).deleteById(5L);
    }

    @Test
    void userDeleteFailsWhenOwnsRestaurant() {
        when(userRepository.findById(5L)).thenReturn(Optional.of(new User(5L, "O", "o@a.com", 1L)));
        when(restaurantRepository.existsByOwnerId(5L)).thenReturn(true);
        assertThrows(ConflictException.class, () -> userService.delete(5L));
    }

    @Test
    void userFindByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    // ---- Restaurant
    private void givenOwner(String typeName) {
        when(userRepository.findById(5L)).thenReturn(Optional.of(new User(5L, "O", "o@a.com", 1L)));
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(new UserType(1L, typeName)));
    }

    private RestaurantCommand restaurantCommand() {
        return new RestaurantCommand("Bella", "Rua 1", "Italiana",
                LocalTime.of(9, 0), LocalTime.of(18, 0), 5L);
    }

    @Test
    void restaurantCreateSucceedsForOwnerType() {
        givenOwner(UserType.RESTAURANT_OWNER);
        assertEquals("Bella", restaurantService.create(restaurantCommand()).getName());
    }

    @Test
    void restaurantCreateRejectsNonOwnerType() {
        givenOwner(UserType.CUSTOMER);
        assertThrows(BusinessRuleException.class, () -> restaurantService.create(restaurantCommand()));
    }

    @Test
    void restaurantCreateRejectsMissingTypeOrOwner() {
        when(userRepository.findById(5L)).thenReturn(Optional.of(new User(5L, "O", "o@a.com", 1L)));
        when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BusinessRuleException.class, () -> restaurantService.create(restaurantCommand()));
        when(userRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> restaurantService.create(restaurantCommand()));
    }

    @Test
    void restaurantUpdateFindDelete() {
        givenOwner(UserType.RESTAURANT_OWNER);
        Restaurant existing = new Restaurant(3L, "Old", "R", "C", hours, 5L);
        when(restaurantRepository.findById(3L)).thenReturn(Optional.of(existing));
        assertEquals("Bella", restaurantService.update(3L, restaurantCommand()).getName());
        when(restaurantRepository.findAll()).thenReturn(List.of(existing));
        assertEquals(1, restaurantService.findAll().size());

        restaurantService.delete(3L);
        verify(menuItemRepository).deleteAllByRestaurantId(3L);
        verify(restaurantRepository).deleteById(3L);
    }

    @Test
    void restaurantFindByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> restaurantService.findById(1L));
    }

    // ---- MenuItem
    private MenuItemCommand itemCommand() {
        return new MenuItemCommand("Pizza", "Queijo", new BigDecimal("30"), false, "/p.jpg");
    }

    @Test
    void menuItemOperationsRequireRestaurant() {
        assertThrows(ResourceNotFoundException.class, () -> menuItemService.create(1L, itemCommand()));
        assertThrows(ResourceNotFoundException.class, () -> menuItemService.findAll(1L));
    }

    @Test
    void menuItemCrud() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new Restaurant(1L, "R", "A", "C", hours, 5L)));
        assertEquals("Pizza", menuItemService.create(1L, itemCommand()).getName());

        MenuItem item = new MenuItem(2L, 1L, "Old", "D", BigDecimal.ONE, false, "/o.jpg");
        when(menuItemRepository.findByIdAndRestaurantId(2L, 1L)).thenReturn(Optional.of(item));
        assertEquals("Pizza", menuItemService.update(1L, 2L, itemCommand()).getName());
        when(menuItemRepository.findAllByRestaurantId(1L)).thenReturn(List.of(item));
        assertEquals(1, menuItemService.findAll(1L).size());

        menuItemService.delete(1L, 2L);
        verify(menuItemRepository).deleteById(2L);
        assertThrows(ResourceNotFoundException.class, () -> menuItemService.findById(1L, 99L));
    }
}
