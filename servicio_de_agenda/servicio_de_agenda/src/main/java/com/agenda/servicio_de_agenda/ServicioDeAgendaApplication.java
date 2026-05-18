package com.agenda.servicio_de_agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients//HABILITAR FEIGN CLIENT PARA COMUNICACION ENTRE MICROSERVICIOS
public class ServicioDeAgendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioDeAgendaApplication.class, args);
	}

}
