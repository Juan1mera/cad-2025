package ru.bsuedu.cad.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bsuedu.cad.app.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.user JOIN FETCH o.orderProducts op JOIN FETCH op.product")
    List<Order> findAll();
}