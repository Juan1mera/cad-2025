package ru.bsuedu.cad.lab.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.model.Product;

import java.sql.Date;
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
        // Mostrar los productos guardados para verificación
        displayProducts();
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
        // Mostrar las categorías guardadas para verificación
        displayCategories();
    }

    private void saveProducts(List<Product> products) {
        String sql = "INSERT INTO PRODUCTS (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (Product p : products) {
            try {
                jdbcTemplate.update(sql, p.getProductId(), p.getName(), p.getDescription(), p.getCategoryId(),
                        p.getPrice(), p.getStockQuantity(), p.getImageUrl(), 
                        p.getCreatedAt() != null ? Date.valueOf(p.getCreatedAt()) : null,
                        p.getUpdatedAt() != null ? Date.valueOf(p.getUpdatedAt()) : null);
            } catch (Exception e) {
                System.err.println("Error al guardar producto ID " + p.getProductId() + ": " + e.getMessage());
            }
        }
    }

    private void saveCategories(List<Category> categories) {
        String sql = "INSERT INTO CATEGORIES (category_id, name, description) VALUES (?, ?, ?)";
        for (Category c : categories) {
            try {
                jdbcTemplate.update(sql, c.getCategoryId(), c.getName(), c.getDescription());
            } catch (Exception e) {
                System.err.println("Error al guardar categoría ID " + c.getCategoryId() + ": " + e.getMessage());
            }
        }
    }

    // Método para leer y mostrar categorías
    private void displayCategories() {
        System.out.println("=== Contenido actual de la tabla CATEGORIES ===");
        String sql = "SELECT * FROM CATEGORIES";
        List<Category> categories = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Category(
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("description")
            );
        });

        if (categories.isEmpty()) {
            System.out.println("No hay categorías en la base de datos.");
        } else {
            for (Category c : categories) {
                System.out.printf("ID: %d, Nombre: %s, Descripción: %s%n",
                        c.getCategoryId(), c.getName(), c.getDescription());
            }
        }
    }

    // Método para leer y mostrar productos
    private void displayProducts() {
        System.out.println("=== Contenido actual de la tabla PRODUCTS ===");
        String sql = "SELECT * FROM PRODUCTS";
        List<Product> products = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getBigDecimal("price"),
                rs.getInt("stock_quantity"),
                rs.getString("image_url"),
                rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null,
                rs.getDate("updated_at") != null ? rs.getDate("updated_at").toLocalDate() : null
            );
        });

        if (products.isEmpty()) {
            System.out.println("No hay productos en la base de datos.");
        } else {
            for (Product p : products) {
                System.out.printf("ID: %d, Nombre: %s, Descripción: %s, Categoría ID: %d, Precio: %.2f, Stock: %d, URL Imagen: %s, Creado: %s, Actualizado: %s%n",
                        p.getProductId(), p.getName(), p.getDescription(), p.getCategoryId(), p.getPrice(),
                        p.getStockQuantity(), p.getImageUrl(), p.getCreatedAt(), p.getUpdatedAt());
            }
        }
    }
}