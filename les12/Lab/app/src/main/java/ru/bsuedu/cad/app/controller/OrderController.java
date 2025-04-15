package ru.bsuedu.cad.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.app.entity.Order;
import ru.bsuedu.cad.app.entity.OrderProduct;
import ru.bsuedu.cad.app.entity.User;
import ru.bsuedu.cad.app.entity.Product;
import ru.bsuedu.cad.app.service.OrderService;
import ru.bsuedu.cad.app.service.ProductService;
import ru.bsuedu.cad.app.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @GetMapping
    public String listOrders(Model model) {
        try {
            LOGGER.info("Attempting to load orders");
            List<Order> orders = orderService.findAllOrders();
            LOGGER.info("Loaded {} orders", orders.size());
            for (Order order : orders) {
                LOGGER.debug("Order ID: {}, Description: {}, User: {}, Products: {}",
                        order.getId(), order.getDescription(),
                        order.getUser() != null ? order.getUser().getUsername() : "null",
                        order.getOrderProducts() != null ? order.getOrderProducts().size() : 0);
            }
            model.addAttribute("orders", orders);
            return "orders";
        } catch (Exception e) {
            LOGGER.error("Error loading orders", e);
            model.addAttribute("error", "No se pudieron cargar los pedidos: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/new")
    public String showOrderForm(Model model) {
        try {
            LOGGER.info("Loading order form");
            Order order = new Order();
            order.setOrderProducts(new ArrayList<>()); // Inicializar orderProducts
            model.addAttribute("order", order);
            List<User> users = userService.findAllUsers();
            List<Product> products = productService.findAllProducts();
            LOGGER.info("Loaded {} users and {} products", users.size(), products.size());
            model.addAttribute("users", users);
            model.addAttribute("products", products);
            return "order-form";
        } catch (Exception e) {
            LOGGER.error("Error loading order form: {}", e.getMessage(), e);
            model.addAttribute("error", "No se pudo cargar el formulario: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/new")
    public String createOrder(@ModelAttribute("order") Order order,
                             @RequestParam(value = "productIds", required = false) List<Long> productIds,
                             @RequestParam Map<String, String> allParams,
                             Model model) {
        try {
            LOGGER.info("Creating order with description: {}", order.getDescription());
            if (order.getUser() == null || order.getUser().getId() == null) {
                throw new IllegalArgumentException("Debe seleccionar un usuario.");
            }
            List<OrderService.ProductOrderRequest> productOrders = new ArrayList<>();
            if (productIds != null && !productIds.isEmpty()) {
                for (Long productId : productIds) {
                    String amountKey = "amount_" + productId;
                    String amountStr = allParams.getOrDefault(amountKey, "0");
                    int amount = Integer.parseInt(amountStr.isEmpty() ? "0" : amountStr);
                    if (amount > 0) {
                        productOrders.add(new OrderService.ProductOrderRequest(productId, amount));
                    }
                }
            }
            if (productOrders.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos un producto con cantidad mayor a 0.");
            }
            orderService.createOrder(order.getDescription(), order.getUser().getId(), productOrders);
            LOGGER.info("Successfully created order");
            return "redirect:/orders";
        } catch (Exception e) {
            LOGGER.error("Error creating order: {}", e.getMessage(), e);
            model.addAttribute("error", "No se pudo crear el pedido: " + e.getMessage());
            model.addAttribute("order", order);
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("products", productService.findAllProducts());
            return "order-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            LOGGER.info("Loading edit form for order {}", id);
            Order order = orderService.findOrderById(id);
            if (order == null) {
                LOGGER.warn("Order with id {} not found", id);
                model.addAttribute("error", "El pedido no existe.");
                return "error";
            }
            // Asegurarse de que orderProducts esté inicializado
            if (order.getOrderProducts() == null) {
                order.setOrderProducts(new ArrayList<>());
            }
            // Crear un mapa de productId a amount
            Map<Long, Integer> productAmounts = new HashMap<>();
            for (OrderProduct op : order.getOrderProducts()) {
                if (op.getProduct() != null) {
                    productAmounts.put(op.getProduct().getId(), op.getAmount());
                }
            }
            model.addAttribute("order", order);
            model.addAttribute("productAmounts", productAmounts);
            List<User> users = userService.findAllUsers();
            List<Product> products = productService.findAllProducts();
            LOGGER.info("Loaded {} users and {} products for edit form", users.size(), products.size());
            model.addAttribute("users", users);
            model.addAttribute("products", products);
            return "order-form-update"; // Usar la nueva vista
        } catch (Exception e) {
            LOGGER.error("Error loading edit form for order {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "No se pudo cargar el formulario de edición: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable("id") Long id,
                            @ModelAttribute("order") Order order,
                            @RequestParam(value = "productIds", required = false) List<Long> productIds,
                            @RequestParam Map<String, String> allParams,
                            Model model) {
        try {
            LOGGER.info("Updating order {}", id);
            Order existingOrder = orderService.findOrderById(id);
            if (existingOrder == null) {
                LOGGER.warn("Order with id {} not found", id);
                model.addAttribute("error", "El pedido no existe.");
                return "error";
            }
            if (order.getUser() == null || order.getUser().getId() == null) {
                throw new IllegalArgumentException("Debe seleccionar un usuario.");
            }
            List<OrderService.ProductOrderRequest> productOrders = new ArrayList<>();
            if (productIds != null && !productIds.isEmpty()) {
                for (Long productId : productIds) {
                    String amountKey = "amount_" + productId;
                    String amountStr = allParams.getOrDefault(amountKey, "0");
                    int amount = Integer.parseInt(amountStr.isEmpty() ? "0" : amountStr);
                    if (amount > 0) {
                        productOrders.add(new OrderService.ProductOrderRequest(productId, amount));
                    }
                }
            }
            if (productOrders.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos un producto con cantidad mayor a 0.");
            }
            orderService.deleteOrder(id);
            orderService.createOrder(order.getDescription(), order.getUser().getId(), productOrders);
            LOGGER.info("Successfully updated order with id {}", id);
            return "redirect:/orders";
        } catch (Exception e) {
            LOGGER.error("Error updating order {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "No se pudo actualizar el pedido: " + e.getMessage());
            model.addAttribute("order", order);
            // Restaurar productAmounts en caso de error
            Map<Long, Integer> productAmounts = new HashMap<>();
            if (productIds != null) {
                for (Long productId : productIds) {
                    String amountKey = "amount_" + productId;
                    String amountStr = allParams.getOrDefault(amountKey, "0");
                    int amount = Integer.parseInt(amountStr.isEmpty() ? "0" : amountStr);
                    if (amount > 0) {
                        productAmounts.put(productId, amount);
                    }
                }
            }
            model.addAttribute("productAmounts", productAmounts);
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("products", productService.findAllProducts());
            return "order-form-update"; // Devolver la vista de edición en caso de error
        }
    }
    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id, Model model) {
        try {
            LOGGER.info("Deleting order {}", id);
            Order order = orderService.findOrderById(id);
            if (order == null) {
                LOGGER.warn("Order with id {} not found", id);
                model.addAttribute("error", "El pedido no existe.");
                return "error";
            }
            orderService.deleteOrder(id);
            LOGGER.info("Successfully deleted order with id {}", id);
            return "redirect:/orders";
        } catch (Exception e) {
            LOGGER.error("Error deleting order {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "No se pudo eliminar el pedido: " + e.getMessage());
            return "error";
        }
    }
}