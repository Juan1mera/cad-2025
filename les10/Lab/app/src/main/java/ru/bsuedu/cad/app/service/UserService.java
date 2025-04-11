package ru.bsuedu.cad.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.app.entity.User;
import ru.bsuedu.cad.app.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createAdminUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRole("ADMIN");
        userRepository.save(admin);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}