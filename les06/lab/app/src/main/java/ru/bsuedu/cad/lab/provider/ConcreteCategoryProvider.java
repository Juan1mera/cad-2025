package ru.bsuedu.cad.lab.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.parser.CategoryCSVParser;
import ru.bsuedu.cad.lab.reader.Reader;

import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {
    private final Reader reader;
    private final CategoryCSVParser parser;

    @Autowired
    public ConcreteCategoryProvider(Reader reader, CategoryCSVParser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Category> getGoods() {
        return parser.parse(reader.read());
    }
}