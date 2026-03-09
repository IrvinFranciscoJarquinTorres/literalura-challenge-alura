package com.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroGutendexDTO(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<AutorGutendexDTO> authors,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("download_count") Double downloadCount,
        @JsonAlias("formats") Map<String, String> formats,
        @JsonAlias("subjects") List<String> subjects,
        @JsonAlias("bookshelves") List<String> bookshelves
) {

    public String getPortadaUrl() {
        if (formats == null) return null;

        if (formats.containsKey("image/jpeg")) return formats.get("image/jpeg");

        if (formats.containsKey("image/png")) return formats.get("image/png");

        return formats.entrySet().stream()
                .filter(e -> e.getKey().contains("image"))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public String getIdiomaPrincipal() {
        if (languages == null || languages.isEmpty()) return "Desconocido";
        return languages.get(0);
    }

    public String getTemasString() {
        if (subjects == null || subjects.isEmpty()) return "";
        return String.join(", ", subjects.subList(0, Math.min(3, subjects.size())));
    }
}