package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.provider.ConcreteProductProvider;
import ru.bsuedu.cad.lab.renderer.ConsoleTableRenderer;

public class App {
    public String getGreeting() {
        return "Hello, World!";
    }
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        ConcreteProductProvider provider = context.getBean(ConcreteProductProvider.class);
        ConsoleTableRenderer renderer = context.getBean(ConsoleTableRenderer.class);

        renderer.render(provider.getGoods());

        context.close();
    }
}
