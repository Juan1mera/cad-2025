package ru.bsuedu.cad.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bsuedu.cad.app.service.UserService;

@Controller
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username, 
            @RequestParam("password") String password, 
            @RequestParam("role") String role, 
            Model model) {
        try {
            if (userService.findByUsername(username) != null) {
                model.addAttribute("error", "El usuario ya existe.");
                return "register";
            }
            userService.saveUser(username, password, role.toUpperCase());
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            return "register";
        }
    }
}