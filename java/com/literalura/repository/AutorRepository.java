package com.literalura.repository;

import com.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    List<Autor> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento <= :anio " +
            "AND (a.anioFallecimiento IS NULL OR a.anioFallecimiento >= :anio)")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") Integer anio);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento BETWEEN :inicio AND :fin")
    List<Autor> findAutoresNacidosEntre(@Param("inicio") Integer inicio, @Param("fin") Integer fin);

    @Query("SELECT DISTINCT a FROM Autor a WHERE SIZE(a.libros) > 0")
    List<Autor> findAutoresConLibros();
}