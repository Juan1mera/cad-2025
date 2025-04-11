package ru.bsuedu.cad.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bsuedu.cad.app.entity.Product;
import ru.bsuedu.cad.app.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public void saveProducts(List<Product> products) {
        productRepository.saveAll(products);
    }
}