package com.literalura.repository;

import com.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByGutenbergId(Long gutenbergId);

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByIdioma(String idioma);

    List<Libro> findTop10ByOrderByNumeroDescargasDesc();

    @Query("SELECT l.idioma, COUNT(l) FROM Libro l GROUP BY l.idioma ORDER BY COUNT(l) DESC")
    List<Object[]> contarLibrosPorIdioma();

    @Query("SELECT DISTINCT l FROM Libro l JOIN l.autores a " +
            "WHERE a.anioNacimiento <= :anio " +
            "AND (a.anioFallecimiento IS NULL OR a.anioFallecimiento >= :anio)")
    List<Libro> findLibrosConAutoresVivosEnAnio(@Param("anio") Integer anio);

    @Query("SELECT MIN(l.numeroDescargas), MAX(l.numeroDescargas), AVG(l.numeroDescargas) FROM Libro l")
    List<Object[]> getEstadisticasDescargas();

    List<Libro> findTop5ByOrderByIdDesc();
}