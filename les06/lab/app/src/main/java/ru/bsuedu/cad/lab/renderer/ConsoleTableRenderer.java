package ru.bsuedu.cad.lab.renderer;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;
import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {
    @Override
    public void render(List<Product> products) {
        System.setProperty("file.encoding", "UTF-8");

        System.out.println("+----+----------------------+-------------------------+-----------+---------+-------+--------------------------------+------------+------------+");
        System.out.println("| ID | Nombre               | Descripcion            | Categoria | Precio  | Stock | URL Imagen                     | Creado     | Actualizado |");
        System.out.println("+----+----------------------+-------------------------+-----------+---------+-------+--------------------------------+------------+------------+");

        for (Product product : products) {
            System.out.printf("| %-2d | %-20s | %-25s | %-9d | %-7.2f | %-5d | %-30s | %-10s | %-10s |\n",
                    product.getProductId(),
                    truncate(product.getName(), 20),
                    truncate(product.getDescription(), 25),
                    product.getCategoryId(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    truncate(product.getImageUrl(), 30),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            );
        }

        System.out.println("+----+----------------------+-------------------------+-----------+---------+-------+--------------------------------+------------+------------+");
    }

    private String truncate(String text, int length) {
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
}