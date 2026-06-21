package com.example.servicio_de_Ususarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.servicio_de_Ususarios.usuarioClient")
public class ServicioDeUsusariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioDeUsusariosApplication.class, args);
	}

}
