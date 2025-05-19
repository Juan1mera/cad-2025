package ru.bsuedu.cad.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Test
    void createOrder_success() {
        // Crea un pedido válido y verifica que se guarda en la base de datos
    }

    @Test
    void createOrder_failure() {
        // Crea un pedido inválido y verifica que falla
    }
}