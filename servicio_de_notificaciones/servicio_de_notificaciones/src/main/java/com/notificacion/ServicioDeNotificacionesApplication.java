package com.notificacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan("com.notificacion.model")
@EnableJpaRepositories("com.notificacion.repository")
@ComponentScan("com.notificacion")
public class ServicioDeNotificacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioDeNotificacionesApplication.class, args);
	}

}
