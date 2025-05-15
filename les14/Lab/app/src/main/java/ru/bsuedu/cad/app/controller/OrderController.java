package ru.bsuedu.cad.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.app.entity.Order;
import ru.bsuedu.cad.app.entity.OrderProduct;
import ru.bsuedu.cad.app.entity.Product;
import ru.bsuedu.cad.app.repository.ProductRepository;
import ru.bsuedu.cad.app.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String listOrders(Model model, Authentication authentication) {
        model.addAttribute("orders", orderService.findAllOrders());
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return "user-orders";
        }
        return "orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<Product> products = productRepository.findAll();
        LOGGER.info("Productos cargados para nuevo pedido: {}", products);
        model.addAttribute("order", new Order());
        model.addAttribute("products", products);
        model.addAttribute("productIds", new ArrayList<Long>());
        model.addAttribute("amounts", new ArrayList<Integer>());
        return "order-form";
    }

    @PostMapping
    public String createOrder(
            @RequestParam("description") String description,
            @RequestParam(value = "productIds", required = false) List<Long> productIds,
            @RequestParam(value = "amounts", required = false) List<Integer> amounts,
            Model model) {
        try {
            LOGGER.info("Creando pedido con descripción: {}, productIds: {}, amounts: {}", description, productIds, amounts);
            if (productIds == null) {
                productIds = new ArrayList<>();
            }
            if (amounts == null) {
                amounts = new ArrayList<>();
            }
            // Ajustar amounts para coincidir con productIds
            while (amounts.size() < productIds.size()) {
                amounts.add(1); // Valor predeterminado
            }
            // Truncar amounts si es más larga que productIds
            if (amounts.size() > productIds.size()) {
                amounts = amounts.subList(0, productIds.size());
            }
            orderService.createOrder(description, productIds, amounts);
            return "redirect:/orders";
        } catch (Exception e) {
            LOGGER.error("Error al crear pedido: {}", e.getMessage());
            model.addAttribute("error", "Error al crear el pedido: " + e.getMessage());
            model.addAttribute("order", new Order());
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("productIds", productIds);
            model.addAttribute("amounts", amounts);
            return "order-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        if (order == null) {
            return "redirect:/orders";
        }
        List<Product> products = productRepository.findAll();
        LOGGER.info("Productos cargados para editar pedido {}: {}", id, products);
        model.addAttribute("order", order);
        model.addAttribute("products", products);
        model.addAttribute("productIds", order.getOrderProducts().stream().map(op -> op.getProduct().getId()).toList());
        model.addAttribute("amounts", order.getOrderProducts().stream().map(OrderProduct::getAmount).toList());
        return "order-form";
    }

    @PostMapping("/update/{id}")
    public String updateOrder(
            @PathVariable Long id,
            @RequestParam("description") String description,
            @RequestParam(value = "productIds", required = false) List<Long> productIds,
            @RequestParam(value = "amounts", required = false) List<Integer> amounts,
            Model model) {
        try {
            LOGGER.info("Actualizando pedido {} con descripción: {}, productIds: {}, amounts: {}", id, description, productIds, amounts);
            if (productIds == null) {
                productIds = new ArrayList<>();
            }
            if (amounts == null) {
                amounts = new ArrayList<>();
            }
            // Ajustar amounts para coincidir con productIds
            while (amounts.size() < productIds.size()) {
                amounts.add(1); // Valor predeterminado
            }
            // Truncar amounts si es más larga que productIds
            if (amounts.size() > productIds.size()) {
                amounts = amounts.subList(0, productIds.size());
            }
            orderService.updateOrder(id, description, productIds, amounts);
            return "redirect:/orders";
        } catch (Exception e) {
            LOGGER.error("Error al actualizar pedido {}: {}", id, e.getMessage());
            model.addAttribute("error", "Error al actualizar el pedido: " + e.getMessage());
            model.addAttribute("order", orderService.findById(id));
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("productIds", productIds);
            model.addAttribute("amounts", amounts);
            return "order-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}