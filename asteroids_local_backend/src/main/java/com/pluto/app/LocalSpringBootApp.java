package com.pluto.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class contains code for starting the Spring Boot Framework and
 * Application Web Server for the local backend. It handles the configuration
 * of Spring Beans, manages command-line arguments, and launches the
 * application.
 */
@SpringBootApplication
public class LocalSpringBootApp {
    /**
     * Starts the Spring Boot Framework and Web Server for the local backend on
     * localhost:8080.
     *
     * @param args - unused
     */
    public static void main(String[] args) {
        SpringApplication.run(LocalSpringBootApp.class, args);
    }
}
