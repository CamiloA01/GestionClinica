package com.profesional.servicio_de_profesionales.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

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
