package ru.bsuedu.cad.lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.bsuedu.cad.lab.provider.ConcreteProductProvider;
import ru.bsuedu.cad.lab.renderer.Renderer;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        ConcreteProductProvider provider = context.getBean(ConcreteProductProvider.class);
        Renderer renderer = context.getBean(Renderer.class);

        renderer.render(provider.getGoods());
    }
    public String getGreeting() {
        return "Hello, World!";
    }
}