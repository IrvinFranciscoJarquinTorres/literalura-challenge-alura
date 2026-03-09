package com.literalura;

import com.literalura.service.MenuService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiteraluraApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(MenuService menuService) {
        return args -> {
            Thread hilo = new Thread(() -> menuService.mostrarMenu());
            hilo.setDaemon(true);
            hilo.start();
        };
    }
}