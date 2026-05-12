package com.notificacion.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionResponseDTO {

    @NotBlank(message = "El titulo es obligatorio")
    
    private String titulo;
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
    @NotBlank(message = "El tipo de notificación es obligatorio")
    private String tipo;
    @NotBlank(message = "El estado de la notificación es obligatorio")
    private String estado;
    @NotNull(message = "La fecha de envio es obligatoria")
    private LocalDateTime fechaEnvio;
    @NotNull(message = "La fecha de lectura es obligatoria")
    private LocalDateTime fechaLectura;
}
