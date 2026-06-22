package com.profesional.servicio_de_profesionales.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servicio de Profesionales API")
                        .version("1.0")
                        .description("Documentación de la API del Servicio de Profesionales"));
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivamos CSRF porque para APIs REST (Postman, Feign) no se necesita
            .csrf(csrf -> csrf.disable())

            // Permitimos todas las peticiones sin autenticación.
            // Esto incluye las llamadas internas que llegan desde el
            // microservicio de usuario a través de OpenFeign.
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
