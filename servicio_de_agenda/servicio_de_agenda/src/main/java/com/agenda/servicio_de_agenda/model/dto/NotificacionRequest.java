package com.agenda.servicio_de_agenda.model.dto;

import lombok.Data;

@Data
public class NotificacionRequest {
    //DEBEN SER LOS MISMOS CAMPOS QUE EN EL DTO DE NOTIFICACION
    private Long usuarioId;
    private String titulo;
    private String mensaje;
    private String tipo;
}


