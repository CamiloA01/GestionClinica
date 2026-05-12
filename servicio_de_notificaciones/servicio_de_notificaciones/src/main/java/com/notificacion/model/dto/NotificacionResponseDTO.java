package com.notificacion.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionResponseDTO {

    private Long id;
    private Long usuarioId;

    private String titulo;
    private String mensaje;
    private String tipo;
    private String estado;
    
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaLectura;
}
