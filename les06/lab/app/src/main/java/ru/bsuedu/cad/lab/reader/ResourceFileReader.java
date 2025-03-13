package ru.bsuedu.cad.lab.reader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct; // Cambia javax a jakarta
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class ResourceFileReader implements Reader {
    @Value("${csv.filename}")
    private String fileName;

    @Override
    public String read() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new RuntimeException("No se pudo encontrar el archivo: " + fileName);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo el archivo CSV", e);
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean ResourceFileReader inicializado el: " + new Date());
    }
}