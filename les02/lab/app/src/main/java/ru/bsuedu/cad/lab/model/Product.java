package ru.bsuedu.cad.lab.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Product {
    private int productId;
    private String name;
    private String description;
    private int categoryId;
    private BigDecimal price;
    private int stockQuantity;
    private String imageUrl;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Product(int productId, String name, String description, int categoryId, BigDecimal price, int stockQuantity, String imageUrl, LocalDate createdAt, LocalDate updatedAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ✅ Agregando los métodos getter
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %-8.2f | %-5d |", productId, name, price, stockQuantity);
    }
}
