package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class CategoryRequest {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void queryCategoriesWithMultipleProducts() {
        String sql = "SELECT c.category_id, c.name, COUNT(p.product_id) as product_count " +
                     "FROM CATEGORIES c LEFT JOIN PRODUCTS p ON c.category_id = p.category_id " +
                     "GROUP BY c.category_id, c.name HAVING COUNT(p.product_id) > 1";
        
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            logger.info("Category ID: {}, Name: {}, Product Count: {}", 
                    rs.getInt("category_id"), rs.getString("name"), rs.getInt("product_count"));
            return null;
        });
    }
}