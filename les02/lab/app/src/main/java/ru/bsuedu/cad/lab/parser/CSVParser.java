package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVParser implements Parser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<Product> parse(String csvContent) {
        return Arrays.stream(csvContent.split("\n"))
                .skip(1) 
                .map(line -> {
                    String[] values = line.split(",");

                    return new Product(
                        Integer.parseInt(values[0].trim()),     
                        values[1].trim(),                       
                        values[2].trim(),                       
                        Integer.parseInt(values[3].trim()),     
                        new BigDecimal(values[4].trim()),       
                        Integer.parseInt(values[5].trim()),     
                        values[6].trim(),                       
                        LocalDate.parse(values[7].trim(), DATE_FORMATTER), 
                        LocalDate.parse(values[8].trim(), DATE_FORMATTER)  
                    );
                })
                .collect(Collectors.toList());
    }
}
