package com.tecsup.edu.pe.exam_perez.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para documentación de la API
 * Incluye configuración de seguridad JWT y metadatos de la API
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Exam Perez API")
                        .description("API REST completa con Spring Boot para gestión de productos y usuarios. " +
                                   "Incluye autenticación JWT, autorización por roles, validaciones y documentación completa.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Estudiante Pérez")
                                .email("estudiante@tecsup.edu.pe")
                                .url("https://tecsup.edu.pe"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo local"),
                        new Server()
                                .url("https://exam-perez-api.onrender.com")
                                .description("Servidor de producción")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT en el formato: Bearer {token}")
                        )
                );
    }
}
