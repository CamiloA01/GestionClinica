package com.agenda.servicio_de_agenda.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API 2026 RESERVA DE AGENDAS")
                        .version("1.0")
                        .description("DOCUMENTACIÓN DE LA API PARA EL SISTEMA DE GESTION CLINICA"));
    }
}
