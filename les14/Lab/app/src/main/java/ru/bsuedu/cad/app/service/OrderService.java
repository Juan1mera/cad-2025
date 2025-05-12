package ru.bsuedu.cad.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.bsuedu.cad.app.entity.Order;
import ru.bsuedu.cad.app.entity.OrderProduct;
import ru.bsuedu.cad.app.entity.Product;
import ru.bsuedu.cad.app.entity.User;
import ru.bsuedu.cad.app.repository.OrderRepository;
import ru.bsuedu.cad.app.repository.ProductRepository;
import ru.bsuedu.cad.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void createOrder(String description, List<Long> productIds, List<Integer> amounts) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Order order = new Order();
        order.setDescription(description);
        order.setUser(user);
        order.setOrderProducts(new ArrayList<>());

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer amount = amounts.get(i);
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isPresent()) {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(order);
                orderProduct.setProduct(productOpt.get());
                orderProduct.setAmount(amount);
                order.getOrderProducts().add(orderProduct);
            }
        }

        orderRepository.save(order);
    }

    public void updateOrder(Long id, String description, List<Long> productIds, List<Integer> amounts) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setDescription(description);
            order.getOrderProducts().clear();

            for (int i = 0; i < productIds.size(); i++) {
                Long productId = productIds.get(i);
                Integer amount = amounts.get(i);
                Optional<Product> productOpt = productRepository.findById(productId);
                if (productOpt.isPresent()) {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setOrder(order);
                    orderProduct.setProduct(productOpt.get());
                    orderProduct.setAmount(amount);
                    order.getOrderProducts().add(orderProduct);
                }
            }

            orderRepository.save(order);
        }
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}