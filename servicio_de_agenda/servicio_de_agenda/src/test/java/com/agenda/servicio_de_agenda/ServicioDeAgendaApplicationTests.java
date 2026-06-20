package com.agenda.servicio_de_agenda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.agenda.servicio_de_agenda.agendaClient.NotificacionClient;

@SpringBootTest
@ActiveProfiles("test")
class ServicioDeAgendaApplicationTests {

	@MockBean
    NotificacionClient notificacionClient;  // ← evita que Feign busque el MS externo


	@Test
	void contextLoads() {
	}

}
