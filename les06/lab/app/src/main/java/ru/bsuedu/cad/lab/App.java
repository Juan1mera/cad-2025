package ru.bsuedu.cad.lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.bsuedu.cad.lab.provider.ConcreteCategoryProvider;
import ru.bsuedu.cad.lab.provider.ConcreteProductProvider;
// import ru.bsuedu.cad.lab.renderer.ConsoleTableRenderer;
import ru.bsuedu.cad.lab.renderer.DataBaseRenderer;
// import ru.bsuedu.cad.lab.renderer.HTMLTableRenderer;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        ConcreteProductProvider productProvider = context.getBean(ConcreteProductProvider.class);
        ConcreteCategoryProvider categoryProvider = context.getBean(ConcreteCategoryProvider.class);

        // Obtener instancias específicas de cada renderer
        // ConsoleTableRenderer consoleRenderer = context.getBean(ConsoleTableRenderer.class);
        DataBaseRenderer dbRenderer = context.getBean(DataBaseRenderer.class);
        // HTMLTableRenderer htmlRenderer = context.getBean(HTMLTableRenderer.class);

        // Renderizar categorías primero para DataBaseRenderer
        System.out.println("=== Renderizando categorías (DataBaseRenderer) ===");
        dbRenderer.renderCategories(categoryProvider.getGoods());

        // Renderizar productos con los tres renderers
        System.out.println("=== Renderizando productos con los tres renderers ===");
        // consoleRenderer.render(productProvider.getGoods());
        dbRenderer.render(productProvider.getGoods());
        // htmlRenderer.render(productProvider.getGoods());
    }

    public String getGreeting() {
        return "Hello, World!";
    }
}