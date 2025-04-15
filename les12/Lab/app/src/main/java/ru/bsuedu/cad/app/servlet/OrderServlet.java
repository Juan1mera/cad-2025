package ru.bsuedu.cad.app.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.app.entity.Order;
import ru.bsuedu.cad.app.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class OrderServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Lista de Pedidos</h1>");

        try {
            List<Order> orders = orderService.findAllOrders();
            if (orders == null || orders.isEmpty()) {
                out.println("<p>No hay pedidos registrados.</p>");
            } else {
                out.println("<p>Total de pedidos encontrados: " + orders.size() + "</p>");
                for (Order order : orders) {
                    String userName = (order.getUser() != null) ? order.getUser().getUsername() : "Usuario no asignado";
                    String productsList = (order.getOrderProducts() != null && !order.getOrderProducts().isEmpty()) 
                        ? order.getOrderProducts().stream()
                            .map(op -> op.getProduct().getName() + " (Cantidad: " + op.getAmount() + ")")
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("Ninguno")
                        : "Ningún producto";
                    out.println("<p>Pedido #" + order.getId() + ": " + 
                                (order.getDescription() != null ? order.getDescription() : "Sin descripción") + 
                                " | Usuario: " + userName + 
                                " | Productos: " + productsList + "</p>");
                }
            }
        } catch (Exception e) {
            out.println("<p>Error al cargar los pedidos: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<a href='/university/orders/new'><button>Crear Pedido</button></a>");
        out.println("</body></html>");
    }
}