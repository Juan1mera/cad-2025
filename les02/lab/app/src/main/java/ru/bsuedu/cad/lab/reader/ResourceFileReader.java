package ru.bsuedu.cad.lab.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ResourceFileReader implements Reader {
    private final String fileName;

    public ResourceFileReader(String fileName) {
        this.fileName = fileName;
    }

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
}
