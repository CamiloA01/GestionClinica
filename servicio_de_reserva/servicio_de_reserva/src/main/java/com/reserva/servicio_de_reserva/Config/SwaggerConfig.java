package com.reserva.servicio_de_reserva.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servicio de Reservas API")
                        .version("1.0")
                        .description("Documentación de la API del Servicio de Reservas"));
    }

}
