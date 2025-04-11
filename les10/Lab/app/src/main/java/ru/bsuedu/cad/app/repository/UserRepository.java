package ru.bsuedu.cad.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.app.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}