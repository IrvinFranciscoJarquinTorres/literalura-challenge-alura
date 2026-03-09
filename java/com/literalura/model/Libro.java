
        package com.literalura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long gutenbergId;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 500)
    private String portadaUrl;

    @Column(length = 1000)
    private String descripcion;

    private String idioma;

    private Double numeroDescargas;

    @Column(length = 500)
    private String temas;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;

    public Libro() {}

    public Libro(Long gutenbergId, String titulo, String portadaUrl,
                 String idioma, Double numeroDescargas, List<Autor> autores) {
        this.gutenbergId = gutenbergId;
        this.titulo = titulo;
        this.portadaUrl = portadaUrl;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
        this.autores = autores;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGutenbergId() { return gutenbergId; }
    public void setGutenbergId(Long gutenbergId) { this.gutenbergId = gutenbergId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getPortadaUrl() { return portadaUrl; }
    public void setPortadaUrl(String portadaUrl) { this.portadaUrl = portadaUrl; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public Double getNumeroDescargas() { return numeroDescargas; }
    public void setNumeroDescargas(Double numeroDescargas) { this.numeroDescargas = numeroDescargas; }

    public String getTemas() { return temas; }
    public void setTemas(String temas) { this.temas = temas; }

    public List<Autor> getAutores() { return autores; }
    public void setAutores(List<Autor> autores) { this.autores = autores; }

    @Override
    public String toString() {
        return "📚 Libro: " + titulo +
                "\n   Autor(es): " + (autores != null ? autores.stream()
                .map(Autor::getNombre).reduce("", (a, b) -> a + ", " + b).replaceFirst(", ", "") : "Desconocido") +
                "\n   Idioma: " + idioma +
                "\n   Descargas: " + (numeroDescargas != null ? numeroDescargas.longValue() : 0) +
                "\n   ID Gutenberg: " + gutenbergId;
    }
}