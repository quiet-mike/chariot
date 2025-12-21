package com.example.pinewood.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonFileStore {
    private final Path dataDir;
    private final ObjectMapper objectMapper;

    public JsonFileStore(@Value("${pinewood.dataDir:data}") String dataDir, ObjectMapper objectMapper) {
        this.dataDir = Paths.get(dataDir).toAbsolutePath().normalize();
        this.objectMapper = objectMapper;
        try {
            Files.createDirectories(this.dataDir);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create data dir: " + this.dataDir, e);
        }
    }

    public <T> List<T> readList(String fileName, Class<T> clazz) {
        Path p = dataDir.resolve(fileName);
        if (!Files.exists(p)) return new ArrayList<>();
        try {
            return objectMapper.readValue(p.toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + p, e);
        }
    }

    public <T> void writeList(String fileName, List<T> data) {
        Path p = this.dataDir.resolve(fileName);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(p.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + p, e);
        }
    }

    public Path getDataDir() { return this.dataDir; }
}
