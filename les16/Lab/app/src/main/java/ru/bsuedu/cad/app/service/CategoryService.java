package ru.bsuedu.cad.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bsuedu.cad.app.entity.Category;
import ru.bsuedu.cad.app.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public void saveCategories(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }
}