package ru.bsuedu.cad.lab.renderer;

import ru.bsuedu.cad.lab.model.Product;
import java.util.List;

public interface Renderer {
    void render(List<Product> products);
}
