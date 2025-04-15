package ru.bsuedu.cad.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.app.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}