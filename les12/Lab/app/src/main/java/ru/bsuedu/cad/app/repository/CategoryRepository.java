package ru.bsuedu.cad.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.app.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}