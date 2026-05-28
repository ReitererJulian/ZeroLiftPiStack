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

public class WorkoutService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/api/workouts";

    public CompletableFuture<List<Map<String, Object>>> getAllWorkouts() {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(res -> {
                    try { return objectMapper.readValue(res.body(), new TypeReference<>() {}); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
    }

    public CompletableFuture<Integer> saveWorkout(String name, String description, List<String> exerciseIds) {
        try {
            String json = objectMapper.writeValueAsString(
                    Map.of("title", name, "exerciseIds", exerciseIds)
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json)).build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode);
        } catch (Exception e) { return CompletableFuture.failedFuture(e); }
    }
}