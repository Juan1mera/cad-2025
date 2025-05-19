package ru.bsuedu.cad.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.bsuedu.cad.app.config.ConfigDB;
import ru.bsuedu.cad.app.config.ConfigSecurity;
import ru.bsuedu.cad.app.config.ConfigWeb;
import ru.bsuedu.cad.app.entity.Order;
import ru.bsuedu.cad.app.entity.Product;
import ru.bsuedu.cad.app.entity.User;
import ru.bsuedu.cad.app.repository.OrderRepository;
import ru.bsuedu.cad.app.repository.ProductRepository;
import ru.bsuedu.cad.app.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigDB.class, ConfigSecurity.class, ConfigWeb.class})
@WebAppConfiguration
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        // Configurar usuario
        user = new User();
        user.setUsername("testuser");
        user.setPassword("testpass");
        user.setRole("USER");
        user = userRepository.save(user);

        // Configurar producto
        product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(10.0));
        product.setStockQuantity(100);
        product = productRepository.save(product);

        // Configurar contexto de seguridad
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("testuser", null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void createOrder_successful() {
        // Ejecutar método
        orderService.createOrder("Test Order", Arrays.asList(product.getId()), Arrays.asList(2));

        // Verificaciones
        List<Order> orders = orderRepository.findAll();
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertEquals("Test Order", order.getDescription());
        assertEquals(user.getUsername(), order.getUser().getUsername());
        assertEquals(1, order.getOrderProducts().size());
        assertEquals(2, order.getOrderProducts().get(0).getAmount());
        assertEquals(product.getId(), order.getOrderProducts().get(0).getProduct().getId());
    }

    @Test
    void createOrder_invalidProduct_doesNotAddProducts() {
        // Ejecutar método con un ID de producto inválido
        orderService.createOrder("Test Order", Arrays.asList(999L), Arrays.asList(2));

        // Verificaciones
        List<Order> orders = orderRepository.findAll();
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertEquals("Test Order", order.getDescription());
        assertEquals(0, order.getOrderProducts().size()); // No se añadieron productos
    }

    @Test
    void updateOrder_successful() {
        // Crear orden inicial
        orderService.createOrder("Initial Order", Arrays.asList(product.getId()), Arrays.asList(1));
        Order order = orderRepository.findAll().get(0);

        // Actualizar orden
        orderService.updateOrder(order.getId(), "Updated Order", Arrays.asList(product.getId()), Arrays.asList(3));

        // Verificaciones
        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertEquals("Updated Order", updatedOrder.getDescription());
        assertEquals(1, updatedOrder.getOrderProducts().size());
        assertEquals(3, updatedOrder.getOrderProducts().get(0).getAmount());
    }

    @Test
    void updateOrder_orderNotFound_doesNothing() {
        // Ejecutar método con un ID de orden inválido
        orderService.updateOrder(999L, "Updated Order", Arrays.asList(product.getId()), Arrays.asList(3));

        // Verificaciones
        List<Order> orders = orderRepository.findAll();
        assertEquals(0, orders.size()); // No se creó ninguna orden
    }

    @Test
    void deleteOrder_successful() {
        // Crear orden inicial
        orderService.createOrder("Test Order", Arrays.asList(product.getId()), Arrays.asList(1));
        Order order = orderRepository.findAll().get(0);

        // Eliminar orden
        orderService.deleteOrder(order.getId());

        // Verificaciones
        assertFalse(orderRepository.findById(order.getId()).isPresent());
    }

    @Test
    void deleteOrder_orderNotFound_doesNothing() {
        // Ejecutar método con un ID de orden inválido
        orderService.deleteOrder(999L);

        // Verificaciones
        List<Order> orders = orderRepository.findAll();
        assertEquals(0, orders.size()); // No se creó ni eliminó nada
    }
}