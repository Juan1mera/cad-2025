package ru.bsuedu.cad.lab.renderer;

// import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
// @Primary
public class HTMLTableRenderer implements Renderer {
    @Override
    public void render(List<Product> products) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>Productos</title></head><body>");
        html.append("<table border='1'><tr><th>ID</th><th>Nombre</th><th>Descripción</th><th>Categoría</th><th>Precio</th><th>Stock</th><th>URL Imagen</th><th>Creado</th><th>Actualizado</th></tr>");

        for (Product product : products) {
            html.append("<tr>")
                .append("<td>").append(product.getProductId()).append("</td>")
                .append("<td>").append(product.getName()).append("</td>")
                .append("<td>").append(product.getDescription()).append("</td>")
                .append("<td>").append(product.getCategoryId()).append("</td>")
                .append("<td>").append(product.getPrice()).append("</td>")
                .append("<td>").append(product.getStockQuantity()).append("</td>")
                .append("<td>").append(product.getImageUrl()).append("</td>")
                .append("<td>").append(product.getCreatedAt()).append("</td>")
                .append("<td>").append(product.getUpdatedAt()).append("</td>")
                .append("</tr>");
        }

        html.append("</table></body></html>");

        try (FileWriter writer = new FileWriter("products.html")) {
            writer.write(html.toString());
            System.out.println("Tabla HTML generada en products.html");
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo HTML", e);
        }
    }
}