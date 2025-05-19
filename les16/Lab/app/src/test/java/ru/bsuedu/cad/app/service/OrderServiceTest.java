package ru.bsuedu.cad.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.bsuedu.cad.app.entity.Order;
import ru.bsuedu.cad.app.entity.OrderProduct;
import ru.bsuedu.cad.app.entity.Product;
import ru.bsuedu.cad.app.entity.User;
import ru.bsuedu.cad.app.repository.OrderRepository;
import ru.bsuedu.cad.app.repository.ProductRepository;
import ru.bsuedu.cad.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setRole("USER");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setDescription("Test Order");
        order.setOrderProducts(new ArrayList<>());
    }

    @Test
    void createOrder_successful() {
        // Configurar contexto de seguridad
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        // Configurar mocks
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Ejecutar método
        orderService.createOrder("Test Order", Arrays.asList(1L), Arrays.asList(2));

        // Verificaciones
        verify(orderRepository, times(2)).saveAndFlush(any(Order.class));
        verify(productRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void createOrder_userNotFound_throwsException() {
        // Configurar contexto de seguridad
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        // Configurar mocks
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        // Verificar excepción
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder("Test Order", Arrays.asList(1L), Arrays.asList(2));
        });

        // Verificaciones
        assertEquals("User not found", exception.getMessage());
        verify(orderRepository, never()).saveAndFlush(any(Order.class));
        verify(productRepository, never()).findById(anyLong());
    }

    @Test
    void updateOrder_successful() {
        // Configurar mocks
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Ejecutar método
        orderService.updateOrder(1L, "Updated Order", Arrays.asList(1L), Arrays.asList(3));

        // Verificaciones
        verify(orderRepository, times(2)).saveAndFlush(any(Order.class));
        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
        assertEquals("Updated Order", order.getDescription());
    }

    @Test
    void updateOrder_orderNotFound_doesNothing() {
        // Configurar mocks
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar método
        orderService.updateOrder(1L, "Updated Order", Arrays.asList(1L), Arrays.asList(3));

        // Verificaciones
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).saveAndFlush(any(Order.class));
        verify(productRepository, never()).findById(anyLong());
    }

    @Test
    void deleteOrder_successful() {
        // Configurar mocks
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);

        // Ejecutar método
        orderService.deleteOrder(1L);

        // Verificaciones
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrder_orderNotFound_doesNothing() {
        // Configurar mocks
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar método
        orderService.deleteOrder(1L);

        // Verificaciones
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).saveAndFlush(any(Order.class));
        verify(orderRepository, never()).deleteById(anyLong());
    }

    @Test
    void findById_orderExists() {
        // Configurar mocks
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Ejecutar método
        Order result = orderService.findById(1L);

        // Verificaciones
        assertNotNull(result);
        assertEquals("Test Order", result.getDescription());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void findById_orderNotFound() {
        // Configurar mocks
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar método
        Order result = orderService.findById(1L);

        // Verificaciones
        assertNull(result);
        verify(orderRepository, times(1)).findById(1L);
    }
}