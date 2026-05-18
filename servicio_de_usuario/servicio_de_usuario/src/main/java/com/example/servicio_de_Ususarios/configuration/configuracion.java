package com.example.servicio_de_Ususarios.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class configuracion {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivamos CSRF porque para APIs REST con Postman no se necesita (y suele dar problemas)
            .csrf(csrf -> csrf.disable()) 
            
            // Configuramos los permisos de las URLs
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite que CUALQUIER petición entre sin autenticación
            );

        return http.build();
    }
}
