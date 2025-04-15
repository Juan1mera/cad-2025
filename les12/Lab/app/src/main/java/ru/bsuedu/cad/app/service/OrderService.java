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

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order createOrder(String description, Long userId, List<ProductOrderRequest> productOrders) {
        Order order = new Order();
        order.setDescription(description);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));
        order.setUser(user);

        if (productOrders != null && !productOrders.isEmpty()) {
            for (ProductOrderRequest request : productOrders) {
                Product product = productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + request.getProductId()));
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(order);
                orderProduct.setProduct(product);
                orderProduct.setAmount(request.getAmount());
                order.getOrderProducts().add(orderProduct);
            }
        }

        return orderRepository.save(order);
    }

    public static class ProductOrderRequest {
        private final Long productId;
        private final int amount;

        public ProductOrderRequest(Long productId, int amount) {
            this.productId = productId;
            this.amount = amount;
        }

        public Long getProductId() {
            return productId;
        }

        public int getAmount() {
            return amount;
        }
    }
}