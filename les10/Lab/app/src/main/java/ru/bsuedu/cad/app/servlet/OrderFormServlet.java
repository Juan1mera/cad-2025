package ru.bsuedu.cad.app.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.app.entity.Product;
import ru.bsuedu.cad.app.entity.User;
import ru.bsuedu.cad.app.service.OrderService;
import ru.bsuedu.cad.app.service.ProductService;
import ru.bsuedu.cad.app.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderFormServlet extends HttpServlet {
    private OrderService orderService;
    private UserService userService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
        this.userService = context.getBean(UserService.class);
        this.productService = context.getBean(ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        
        List<User> users = userService.findAllUsers();
        List<Product> products = productService.findAllProducts();

        out.println("<html><body>");
        out.println("<h1>Crear Nuevo Pedido</h1>");
        out.println("<form method='post' action='/university/orders/new'>");
        
        // Descripción
        out.println("<label for='description'>Descripción:</label>");
        out.println("<input type='text' name='description' id='description'><br><br>");
        
        // Seleccionar usuario
        out.println("<label for='userId'>Usuario:</label>");
        out.println("<select name='userId' id='userId'>");
        for (User user : users) {
            out.println("<option value='" + user.getId() + "'>" + user.getUsername() + "</option>");
        }
        out.println("</select><br><br>");
        
        // Seleccionar productos con cantidades
        out.println("<label>Productos:</label><br>");
        for (Product product : products) {
            out.println("<input type='checkbox' name='productIds' value='" + product.getId() + "'>" + 
                        product.getName() + " - $" + product.getPrice() + 
                        " <input type='number' name='amount_" + product.getId() + "' min='0' value='0'><br>");
        }
        out.println("<br>");
        
        out.println("<input type='submit' value='Crear Pedido'>");
        out.println("</form>");
        out.println("<a href='/university/orders'>Volver a la lista</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String description = req.getParameter("description");
        Long userId = Long.parseLong(req.getParameter("userId"));
        String[] productIdsArray = req.getParameterValues("productIds");

        List<ProductOrderRequest> productOrders = new ArrayList<>();
        if (productIdsArray != null) {
            for (String productId : productIdsArray) {
                int amount = Integer.parseInt(req.getParameter("amount_" + productId));
                if (amount > 0) {
                    productOrders.add(new ProductOrderRequest(Long.parseLong(productId), amount));
                }
            }
        }

        orderService.createOrder(description, userId, productOrders);
        resp.sendRedirect("/university/orders");
    }

    // Clase auxiliar para pasar datos del formulario al servicio
    public static class ProductOrderRequest {
        private final Long productId;
        private final int amount;

        public ProductOrderRequest(Long productId, int amount) {
            this.productId = productId;
            this.amount = amount;
        }

        public Long getProductId() { return productId; }
        public int getAmount() { return amount; }
    }
}