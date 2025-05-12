package ru.bsuedu.cad.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserCreationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreationController.class);

    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @GetMapping("/create-user")
    public String showCreateUserForm() {
        LOGGER.debug("Showing create user form");
        return "create-user";
    }

    @GetMapping("/create-manager")
    public String showCreateManagerForm() {
        LOGGER.debug("Showing create manager form");
        return "create-manager";
    }

    @PostMapping("/create-user")
    public String createUser(@RequestParam String username, @RequestParam String password) {
        LOGGER.debug("Creating user: {}", username);
        try {
            userDetailsManager.createUser(
                    User.withUsername(username)
                            .password("{noop}" + password)
                            .roles("USER")
                            .build()
            );
            LOGGER.info("Successfully created user: {}", username);
        } catch (Exception e) {
            LOGGER.error("Failed to create user: {}", username, e);
            throw e;
        }
        return "redirect:/login";
    }

    @PostMapping("/create-manager")
    public String createManager(@RequestParam String username, @RequestParam String password) {
        LOGGER.debug("Creating manager: {}", username);
        try {
            userDetailsManager.createUser(
                    User.withUsername(username)
                            .password("{noop}" + password)
                            .roles("MANAGER")
                            .build()
            );
            LOGGER.info("Successfully created manager: {}", username);
        } catch (Exception e) {
            LOGGER.error("Failed to create manager: {}", username, e);
            throw e;
        }
        return "redirect:/login";
    }
}