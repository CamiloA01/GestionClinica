package com.example.servicio_de_Ususarios.configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerCongif {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servicio de Usuarios API")
                        .version("1.0")
                        .description("Documentación de la API del Servicio de Usuarios"));
    }

}
