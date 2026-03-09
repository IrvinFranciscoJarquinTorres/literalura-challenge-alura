# 📚 LiteraLura - Catálogo de Libros

> Challenge Backend Java - Alura Latam | Oracle ONE

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-green?style=flat-square&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)
![Gutendex API](https://img.shields.io/badge/API-Gutendex-purple?style=flat-square)

## 📖 Descripción

LiteraLura es una aplicación de catálogo de libros que permite buscar, registrar y explorar libros del Proyecto Gutenberg a través de la API de Gutendex. Cuenta con una interfaz de texto interactiva en consola y una interfaz web con portadas de libros.

## ✨ Funcionalidades

- Buscar libro por título desde la API de Gutendex
- Listar todos los libros registrados
- Listar todos los autores registrados
- Buscar autores vivos en un año específico
- Filtrar libros por idioma
- Top 10 libros más descargados
- Estadísticas de descargas
- Interfaz web con portadas en el puerto 8080

## 🛠️ Tecnologías

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- Gutendex API
- Maven

## ⚙️ Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/IrvinFranciscoJarquinTorres/literalura-desafio-alura.git
```

### 2. Crear la base de datos
```sql
CREATE DATABASE literalura_db;
```

### 3. Configurar credenciales
Copia y edita el archivo de ejemplo:
```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

### 4. Ejecutar
```bash
mvn spring-boot:run
```

## 👨‍💻 Desarrollado para

Challenge Alura Latam - Oracle ONE | Spring Boot Backend
