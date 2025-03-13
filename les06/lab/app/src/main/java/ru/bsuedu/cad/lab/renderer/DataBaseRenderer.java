package ru.bsuedu.cad.lab.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

@Component
@Primary
public class DataBaseRenderer implements Renderer, CategoryRenderer {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataBaseRenderer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void render(List<Product> products) {
        System.out.println("=== Iniciando renderizado de productos en la base de datos ===");
        if (products.isEmpty()) {
            System.out.println("No hay productos para guardar.");
            return;
        }
        System.out.println("Guardando " + products.size() + " productos...");
        saveProducts(products);
        System.out.println("Productos guardados exitosamente.");
    }

    @Override
    public void renderCategories(List<Category> categories) {
        System.out.println("=== Iniciando renderizado de categorías en la base de datos ===");
        if (categories.isEmpty()) {
            System.out.println("No hay categorías para guardar.");
            return;
        }
        System.out.println("Guardando " + categories.size() + " categorías...");
        saveCategories(categories);
        System.out.println("Categorías guardadas exitosamente.");
    }

    private void saveProducts(List<Product> products) {
        String sql = "INSERT INTO PRODUCTS (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (Product p : products) {
            jdbcTemplate.update(sql, p.getProductId(), p.getName(), p.getDescription(), p.getCategoryId(),
                    p.getPrice(), p.getStockQuantity(), p.getImageUrl(), p.getCreatedAt(), p.getUpdatedAt());
        }
    }

    private void saveCategories(List<Category> categories) {
        String sql = "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)";
        for (Category c : categories) {
            jdbcTemplate.update(sql, c.getCategoryId(), c.getName(), c.getDescription());
        }
    }
}