package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication(scanBasePackages = "ru.bsuedu.cad.lab")
public class App implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Cargar datos desde CSV
        loadCategoriesFromCsv("category.csv");
        loadCustomersFromCsv("customer.csv");
        loadProductsFromCsv("products.csv");

        // Crear un nuevo pedido
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Product product = productRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setProducts(Arrays.asList(product));

        OrderDetail detail = new OrderDetail();
        detail.setProduct(product);
        detail.setQuantity(2);
        detail.setPrice(product.getPrice());

        order.setOrderDetails(Collections.singletonList(detail));

        CustomerOrder savedOrder = orderService.createOrder(order);
        logger.info("Pedido creado con ID: {}", savedOrder.getId());
    }

    /**
     * Carga categorías desde un archivo CSV.
     */
    private void loadCategoriesFromCsv(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) { // categoryId, name, description
                    Category category = new Category();
                    category.setName(data[1].trim());
                    category.setDescription(data[2].trim());
                    categoryRepository.save(category);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar categorías desde CSV", e);
        }
    }

    /**
     * Carga clientes desde un archivo CSV.
     */
    private void loadCustomersFromCsv(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) { // customerId, name, email, phone, address
                    Customer customer = new Customer();
                    customer.setName(data[1].trim());
                    customer.setEmail(data[2].trim());
                    customer.setPhone(data[3].trim());
                    customer.setAddress(data[4].trim());
                    customerRepository.save(customer);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar clientes desde CSV", e);
        }
    }

    /**
     * Carga productos desde un archivo CSV.
     */
    private void loadProductsFromCsv(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) { // productId, name, description, categoryId, price, stockQuantity, imageUrl, createdAt
                    Product product = new Product();
                    product.setName(data[1].trim());
                    product.setDescription(data[2].trim());
                    product.setCategory(categoryRepository.findById(Long.parseLong(data[3].trim())).orElseThrow());
                    product.setPrice(new BigDecimal(data[4].trim()));
                    product.setStockQuantity(Integer.parseInt(data[5].trim()));
                    product.setImageUrl(data[6].trim());
                    product.setCreatedAt(LocalDateTime.parse(data[7].trim()));
                    productRepository.save(product);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar productos desde CSV", e);
        }
    }

    public String getGreeting() {
        return "Hello, World!";
    }
}