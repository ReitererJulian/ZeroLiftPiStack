package org.zerolift.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ExerciseService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String URL = "http://localhost:8080/api/exercises";

    // Holt alle Übungen vom Server
    public CompletableFuture<List<Map<String, Object>>> getAllExercises() {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).GET().build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(res -> {
                    try {
                        return objectMapper.readValue(res.body(), new TypeReference<>() {});
                    } catch (Exception e) { throw new RuntimeException(e); }
                });
    }

    // Sendet eine neue Übung ans Backend
    public CompletableFuture<Integer> saveExercise(String name, String description, List<String> equipmentIds) {
        try {
            String json = objectMapper.writeValueAsString(
                    Map.of("title", name, "description", description, "equipmentIds", equipmentIds)
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json)).build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::statusCode);
        } catch (Exception e) { return CompletableFuture.failedFuture(e); }
    }
}