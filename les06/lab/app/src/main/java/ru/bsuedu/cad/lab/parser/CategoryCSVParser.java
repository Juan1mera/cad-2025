package ru.bsuedu.cad.lab.parser;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryCSVParser {
    public List<Category> parse(String csvContent) { 
        return Arrays.stream(csvContent.split("\n"))
                .skip(1) 
                .map(line -> {
                    String[] values = line.split(",");
                    return new Category(
                            Integer.parseInt(values[0].trim()),
                            values[1].trim(),
                            values[2].trim()
                    );
                })
                .collect(Collectors.toList());
    }
}