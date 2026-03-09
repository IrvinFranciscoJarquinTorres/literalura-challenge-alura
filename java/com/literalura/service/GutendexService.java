package com.literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.dto.GutendexResponseDTO;
import com.literalura.dto.LibroGutendexDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class GutendexService {

    @Value("${gutendex.api.url}")
    private String apiUrl;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GutendexService() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<LibroGutendexDTO> buscarLibrosPorTitulo(String titulo) {
        try {
            String tituloEncoded = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String url = apiUrl + "?search=" + tituloEncoded;
            System.out.println("Consultando API: " + url);
            String jsonResponse = realizarPeticion(url);
            GutendexResponseDTO response = objectMapper.readValue(jsonResponse, GutendexResponseDTO.class);
            if (response.results() != null && !response.results().isEmpty()) {
                System.out.println("Encontrados " + response.results().size() + " resultado(s).");
                return response.results();
            } else {
                System.out.println("No se encontraron libros con ese titulo.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("Error al consultar la API: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<LibroGutendexDTO> buscarLibrosPorIdioma(String idioma) {
        try {
            String url = apiUrl + "?languages=" + idioma;
            String jsonResponse = realizarPeticion(url);
            GutendexResponseDTO response = objectMapper.readValue(jsonResponse, GutendexResponseDTO.class);
            return response.results() != null ? response.results() : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error al consultar la API: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<LibroGutendexDTO> obtenerLibrosPopulares() {
        try {
            String url = apiUrl + "?sort=popular";
            String jsonResponse = realizarPeticion(url);
            GutendexResponseDTO response = objectMapper.readValue(jsonResponse, GutendexResponseDTO.class);
            return response.results() != null ? response.results() : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error al obtener libros populares: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public LibroGutendexDTO buscarLibroPorId(Long gutenbergId) {
        try {
            String url = apiUrl + "/" + gutenbergId;
            String jsonResponse = realizarPeticion(url);
            return objectMapper.readValue(jsonResponse, LibroGutendexDTO.class);
        } catch (Exception e) {
            System.err.println("Error al buscar libro por ID: " + e.getMessage());
            return null;
        }
    }

    private String realizarPeticion(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "LiteraLura/1.0")
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Error HTTP: " + response.statusCode());
        }
        return response.body();
    }
}