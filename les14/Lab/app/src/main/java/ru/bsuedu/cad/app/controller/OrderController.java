package ru.bsuedu.cad.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.app.entity.Order;
import ru.bsuedu.cad.app.entity.OrderProduct;
import ru.bsuedu.cad.app.repository.ProductRepository;
import ru.bsuedu.cad.app.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String listOrders(Model model, Authentication authentication) {
        model.addAttribute("orders", orderService.findAllOrders());
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return "user-orders";
        }
        return "orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("productIds", new ArrayList<Long>());
        model.addAttribute("amounts", new ArrayList<Integer>());
        return "order-form";
    }

    @PostMapping
    public String createOrder(
            @RequestParam("description") String description,
            @RequestParam(value = "productIds", required = false) List<Long> productIds,
            @RequestParam(value = "amounts", required = false) List<Integer> amounts) {
        if (productIds == null || amounts == null) {
            productIds = new ArrayList<>();
            amounts = new ArrayList<>();
        }
        orderService.createOrder(description, productIds, amounts);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        if (order == null) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("productIds", order.getOrderProducts().stream().map(op -> op.getProduct().getId()).toList());
        model.addAttribute("amounts", order.getOrderProducts().stream().map(OrderProduct::getAmount).toList());
        return "order-form";
    }

    @PostMapping("/update/{id}")
    public String updateOrder(
            @PathVariable Long id,
            @RequestParam("description") String description,
            @RequestParam(value = "productIds", required = false) List<Long> productIds,
            @RequestParam(value = "amounts", required = false) List<Integer> amounts) {
        if (productIds == null || amounts == null) {
            productIds = new ArrayList<>();
            amounts = new ArrayList<>();
        }
        orderService.updateOrder(id, description, productIds, amounts);
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}