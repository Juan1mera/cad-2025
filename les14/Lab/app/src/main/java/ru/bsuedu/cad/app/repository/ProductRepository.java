package ru.bsuedu.cad.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.app.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}