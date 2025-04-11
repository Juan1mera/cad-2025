package ru.bsuedu.cad.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.app.entity.Order;
import ru.bsuedu.cad.app.entity.OrderProduct;
import ru.bsuedu.cad.app.entity.Product;
import ru.bsuedu.cad.app.entity.User;
import ru.bsuedu.cad.app.repository.OrderRepository;
import ru.bsuedu.cad.app.repository.ProductRepository;
import ru.bsuedu.cad.app.repository.UserRepository;
import ru.bsuedu.cad.app.servlet.OrderFormServlet.ProductOrderRequest;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void createOrder(String description, Long userId, List<ProductOrderRequest> productOrders) {
        Order order = new Order();
        order.setDescription(description);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));
        order.setUser(user);

        for (ProductOrderRequest po : productOrders) {
            Product product = productRepository.findById(po.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + po.getProductId()));
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setAmount(po.getAmount());
            order.addOrderProduct(orderProduct);
        }

        orderRepository.save(order);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}