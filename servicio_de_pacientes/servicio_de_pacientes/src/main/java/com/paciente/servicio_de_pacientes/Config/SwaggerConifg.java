package com.paciente.servicio_de_pacientes.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConifg {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servicio de Pacientes API")
                        .version("1.0")
                        .description("API para el servicio de pacientes"));
    }

}
