package com.literalura.controller;

import com.literalura.model.Libro;
import com.literalura.service.AutorService;
import com.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorService autorService;

    @GetMapping
    public String index(Model model) {
        List<Libro> libros = libroService.listarTodosLosLibros();
        model.addAttribute("libros", libros);
        model.addAttribute("totalLibros", libros.size());
        model.addAttribute("titulo", "LiteraLura - Catalogo de Libros");
        return "index";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "titulo", required = false) String titulo, Model model) {
        if (titulo != null && !titulo.trim().isEmpty()) {
            Libro guardado = libroService.buscarYGuardarLibro(titulo);
            List<Libro> resultados = libroService.buscarLibrosEnBDPorTitulo(titulo);
            model.addAttribute("libros", resultados);
            model.addAttribute("busqueda", titulo);
            model.addAttribute("totalResultados", resultados.size());
            if (guardado != null) {
                model.addAttribute("mensaje", "Libro guardado: " + guardado.getTitulo());
            }
        }
        return "buscar";
    }

    @GetMapping("/libro/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        libroService.listarTodosLosLibros().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .ifPresentOrElse(
                        l -> model.addAttribute("libro", l),
                        () -> model.addAttribute("error", "Libro no encontrado.")
                );
        return "detalle";
    }

    @GetMapping("/idioma/{codigo}")
    public String librosPorIdioma(@PathVariable String codigo, Model model) {
        List<Libro> libros = libroService.listarLibrosPorIdioma(codigo);
        model.addAttribute("libros", libros);
        model.addAttribute("titulo", "Libros en " + codigo);
        return "index";
    }

    @GetMapping("/autores")
    public String autores(Model model) {
        model.addAttribute("autores", autorService.listarTodosLosAutores());
        return "autores";
    }

    @GetMapping("/top10")
    public String top10(Model model) {
        model.addAttribute("libros", libroService.obtenerTop10MasDescargados());
        model.addAttribute("titulo", "Top 10 Mas Descargados");
        return "index";
    }

    @GetMapping("/cargar-populares")
    public String cargarPopulares(Model model) {
        int guardados = libroService.cargarLibrosPopularesDesdeAPI();
        model.addAttribute("mensaje", "Se cargaron " + guardados + " nuevos libros.");
        List<Libro> libros = libroService.listarTodosLosLibros();
        model.addAttribute("libros", libros);
        model.addAttribute("totalLibros", libros.size());
        return "index";
    }
}