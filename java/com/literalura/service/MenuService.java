package com.literalura.service;

import com.literalura.model.Autor;
import com.literalura.model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class MenuService {

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorService autorService;

    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        int opcion = -1;

        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║         📚  BIENVENIDO A LITERALURA  📚       ║");
        System.out.println("║      Tu catálogo personal de libros           ║");
        System.out.println("║      Powered by Gutendex API                  ║");
        System.out.println("╚══════════════════════════════════════════════╝\n");

        while (opcion != 0) {
            mostrarOpciones();

            try {
                System.out.print("\n➡  Selecciona una opción: ");
                opcion = Integer.parseInt(scanner.nextLine().trim());
                procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Por favor ingresa un número válido.");
            }
        }

        System.out.println("\n👋 ¡Hasta luego! Gracias por usar LiteraLura.");
    }

    private void mostrarOpciones() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│              MENÚ PRINCIPAL              │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│  1. 🔍 Buscar libro por título (API)     │");
        System.out.println("│  2. 📋 Listar libros registrados         │");
        System.out.println("│  3. 👤 Listar autores registrados        │");
        System.out.println("│  4. 🗓️  Autores vivos en un año          │");
        System.out.println("│  5. 🌍 Libros por idioma                 │");
        System.out.println("│  6. 🏆 Top 10 más descargados            │");
        System.out.println("│  7. 📊 Estadísticas de descargas         │");
        System.out.println("│  8. 🌐 Abrir catálogo web                │");
        System.out.println("│  9. 🚀 Cargar libros populares (API)     │");
        System.out.println("│  0. 🚪 Salir                             │");
        System.out.println("└─────────────────────────────────────────┘");
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> buscarLibroPorTitulo();
            case 2 -> listarLibrosRegistrados();
            case 3 -> listarAutoresRegistrados();
            case 4 -> listarAutoresVivosEnAnio();
            case 5 -> listarLibrosPorIdioma();
            case 6 -> mostrarTop10MasDescargados();
            case 7 -> mostrarEstadisticas();
            case 8 -> abrirCatalogoWeb();
            case 9 -> cargarLibrosPopulares();
            case 0 -> System.out.println("Cerrando menú...");
            default -> System.out.println("⚠️  Opción inválida. Elige entre 0 y 9.");
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.print("\n📖 Ingresa el título del libro a buscar: ");
        String titulo = scanner.nextLine().trim();

        if (titulo.isEmpty()) {
            System.out.println("⚠️  El título no puede estar vacío.");
            return;
        }

        System.out.println("🔄 Buscando en Gutendex API...");
        Libro libro = libroService.buscarYGuardarLibro(titulo);

        if (libro != null) {
            System.out.println("\n✅ Libro encontrado y guardado:");
            System.out.println("─".repeat(50));
            System.out.println(libro);
            System.out.println("─".repeat(50));
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroService.listarTodosLosLibros();

        if (libros.isEmpty()) {
            System.out.println("\n📭 No hay libros registrados aún. ¡Busca alguno primero!");
            return;
        }

        System.out.println("\n📚 Libros registrados en la base de datos (" + libros.size() + "):");
        System.out.println("═".repeat(60));
        libros.forEach(l -> {
            System.out.println(l);
            System.out.println("─".repeat(60));
        });
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorService.listarTodosLosAutores();

        if (autores.isEmpty()) {
            System.out.println("\n📭 No hay autores registrados aún.");
            return;
        }

        System.out.println("\n✍️  Autores registrados (" + autores.size() + "):");
        System.out.println("═".repeat(60));
        autores.forEach(a -> {
            System.out.println(a);
            System.out.println("─".repeat(60));
        });
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("\n🗓️  Ingresa el año a consultar (ej: 1800): ");
        try {
            int anio = Integer.parseInt(scanner.nextLine().trim());
            List<Autor> autores = autorService.listarAutoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println("📭 No hay autores registrados vivos en el año " + anio + ".");
            } else {
                System.out.println("\n✍️  Autores vivos en el año " + anio + " (" + autores.size() + "):");
                System.out.println("═".repeat(60));
                autores.forEach(a -> {
                    System.out.println(a);
                    System.out.println("─".repeat(60));
                });
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️  Ingresa un año válido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\n🌍 Idiomas disponibles:");
        System.out.println("   es → Español");
        System.out.println("   en → Inglés");
        System.out.println("   fr → Francés");
        System.out.println("   de → Alemán");
        System.out.println("   pt → Portugués");
        System.out.println("   it → Italiano");
        System.out.println("   la → Latín");
        System.out.print("Ingresa el código de idioma: ");

        String idioma = scanner.nextLine().trim().toLowerCase();
        List<Libro> libros = libroService.listarLibrosPorIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("📭 No hay libros en idioma '" + idioma + "' registrados.");
        } else {
            System.out.println("\n📚 Libros en '" + idioma + "' (" + libros.size() + "):");
            System.out.println("═".repeat(60));
            libros.forEach(l -> {
                System.out.println(l);
                System.out.println("─".repeat(60));
            });
        }
    }

    private void mostrarTop10MasDescargados() {
        List<Libro> top10 = libroService.obtenerTop10MasDescargados();

        if (top10.isEmpty()) {
            System.out.println("\n📭 No hay libros registrados aún.");
            return;
        }

        System.out.println("\n🏆 TOP 10 LIBROS MÁS DESCARGADOS:");
        System.out.println("═".repeat(60));
        int i = 1;
        for (Libro libro : top10) {
            System.out.println("#" + i++ + " - " + libro.getTitulo() +
                    " (" + (libro.getNumeroDescargas() != null ? libro.getNumeroDescargas().longValue() : 0) + " descargas)");
        }
    }

    private void mostrarEstadisticas() {
        System.out.println("\n📊 ESTADÍSTICAS DE LITERALURA:");
        System.out.println("═".repeat(60));
        libroService.mostrarEstadisticasDescargas();
        System.out.println();
        libroService.mostrarConteoLibrosPorIdioma();
    }

    private void abrirCatalogoWeb() {
        System.out.println("\n🌐 El catálogo web está disponible en:");
        System.out.println("   ➡  http://localhost:8080");
                System.out.println("   Abre tu navegador y visita esa URL para ver las portadas.");
    }

    private void cargarLibrosPopulares() {
        System.out.println("\n🚀 Cargando libros populares desde Gutendex API...");
        int guardados = libroService.cargarLibrosPopularesDesdeAPI();
        System.out.println("✅ Se guardaron " + guardados + " nuevos libros en la base de datos.");
    }
}
