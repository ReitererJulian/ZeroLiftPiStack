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

public class EquipmentService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String URL = "https://api.zerolift.at/api/equipment";


    public CompletableFuture<List<Map<String, Object>>> getAllEquipment() {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).GET().build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(res -> {
                    try {
                        return objectMapper.readValue(res.body(), new TypeReference<>() {});
                    } catch (Exception e) { throw new RuntimeException(e); }
                });
    }

    public CompletableFuture<Integer> saveEquipment(String title, String description) {
        try {
            String json = objectMapper.writeValueAsString(Map.of("title", title, "description", description));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json)).build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::statusCode);
        } catch (Exception e) { return CompletableFuture.failedFuture(e); }
    }
}