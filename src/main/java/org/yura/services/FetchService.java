package org.yura.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FetchService {
    public <T> CompletableFuture<List<T>> fetchData(String url, TypeReference<List<T>> typeReference){
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    String json = response.body();
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<T> data;

                    try {
                        data = objectMapper.readValue(json, typeReference);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    return data;
                });
    }
}
