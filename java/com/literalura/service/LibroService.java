package com.literalura.service;

import com.literalura.dto.AutorGutendexDTO;
import com.literalura.dto.LibroGutendexDTO;
import com.literalura.model.Autor;
import com.literalura.model.Libro;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private GutendexService gutendexService;

    @Transactional
    public Libro buscarYGuardarLibro(String titulo) {
        List<LibroGutendexDTO> resultados = gutendexService.buscarLibrosPorTitulo(titulo);

        if (resultados.isEmpty()) {
            System.out.println("⚠️  No se encontró ningún libro con ese título.");
            return null;
        }

        LibroGutendexDTO libroDTO = resultados.get(0);

        Optional<Libro> libroExistente = libroRepository.findByGutenbergId(libroDTO.id());
        if (libroExistente.isPresent()) {
            System.out.println("ℹ️  El libro ya existe en la base de datos.");
            return libroExistente.get();
        }

        Libro libro = convertirDTOaLibro(libroDTO);
        return libroRepository.save(libro);
    }

    public List<Libro> listarTodosLosLibros() {
        return libroRepository.findAll();
    }

    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }

    public List<Libro> buscarLibrosEnBDPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Libro> obtenerTop10MasDescargados() {
        return libroRepository.findTop10ByOrderByNumeroDescargasDesc();
    }

    public void mostrarEstadisticasDescargas() {
        List<Object[]> stats = libroRepository.getEstadisticasDescargas();
        if (!stats.isEmpty()) {
            Object[] data = stats.get(0);
            System.out.println("📊 Estadísticas de descargas:");
            System.out.println("   Mínimo: " + data[0]);
            System.out.println("   Máximo: " + data[1]);
            System.out.printf("   Promedio: %.0f%n", data[2]);
        }
    }

    public void mostrarConteoLibrosPorIdioma() {
        List<Object[]> conteos = libroRepository.contarLibrosPorIdioma();
        System.out.println("🌍 Cantidad de libros por idioma:");
        conteos.forEach(c -> System.out.println("   " + obtenerNombreIdioma((String) c[0]) + ": " + c[1] + " libro(s)"));
    }

    public List<Libro> buscarLibrosConAutoresVivosEnAnio(Integer anio) {
        return libroRepository.findLibrosConAutoresVivosEnAnio(anio);
    }

    @Transactional
    public int cargarLibrosPopularesDesdeAPI() {
        List<LibroGutendexDTO> populares = gutendexService.obtenerLibrosPopulares();
        int guardados = 0;

        for (LibroGutendexDTO dto : populares) {
            Optional<Libro> existente = libroRepository.findByGutenbergId(dto.id());
            if (existente.isEmpty()) {
                Libro libro = convertirDTOaLibro(dto);
                libroRepository.save(libro);
                guardados++;
            }
        }
        return guardados;
    }

    private Libro convertirDTOaLibro(LibroGutendexDTO dto) {
        Libro libro = new Libro();
        libro.setGutenbergId(dto.id());
        libro.setTitulo(dto.title());
        libro.setPortadaUrl(dto.getPortadaUrl());
        libro.setIdioma(dto.getIdiomaPrincipal());
        libro.setNumeroDescargas(dto.downloadCount());
        libro.setTemas(dto.getTemasString());

        List<Autor> autores = new ArrayList<>();
        if (dto.authors() != null) {
            for (AutorGutendexDTO autorDTO : dto.authors()) {

                Autor autor = autorRepository.findByNombre(autorDTO.name())
                        .orElse(new Autor(autorDTO.name(), autorDTO.birthYear(), autorDTO.deathYear()));
                autores.add(autor);
            }
        }
        libro.setAutores(autores);
        return libro;
    }

    private String obtenerNombreIdioma(String codigo) {
        return switch (codigo.toLowerCase()) {
            case "en" -> "Inglés (en)";
            case "es" -> "Español (es)";
            case "fr" -> "Francés (fr)";
            case "de" -> "Alemán (de)";
            case "pt" -> "Portugués (pt)";
            case "it" -> "Italiano (it)";
            case "la" -> "Latín (la)";
            case "zh" -> "Chino (zh)";
            case "ja" -> "Japonés (ja)";
            default -> codigo;
        };
    }
}