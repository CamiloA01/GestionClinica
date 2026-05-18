package com.agenda.servicio_de_agenda.agendaClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.agenda.servicio_de_agenda.model.dto.NotificacionRequest;

@FeignClient(name = "ms-notificaciones", url = "http://localhost:8081/api/notificaciones")
public interface NotificacionClient {

    @PostMapping("/enviar") // Endpoint para enviar notificaciones al microservicio de notificaciones
    void enviarAlerta(@RequestBody NotificacionRequest solicitud);
}



