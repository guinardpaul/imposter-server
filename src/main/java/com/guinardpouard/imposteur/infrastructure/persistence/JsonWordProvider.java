package com.guinardpouard.imposteur.infrastructure.persistence;

import com.guinardpouard.imposteur.domain.model.WordPair;
import com.guinardpouard.imposteur.domain.port.ApiWordProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class JsonWordProvider implements ApiWordProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WordMapper wordMapper = new WordMapper();
    private List<WordEntry> allWords;

    @PostConstruct
    public void loadWords() {
        try (InputStream is = new ClassPathResource("words.json").getInputStream()) {
            allWords = objectMapper.readValue(is, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load words.json", e);
        }
    }

    @Override
    public List<WordPair> findAll() {
        return allWords.stream().map(wordMapper::toDomain).toList();
    }

    @Override
    public List<WordPair> getWords(int count) {
        return List.of(new WordPair("Banane", "Avion"));
    }
}
