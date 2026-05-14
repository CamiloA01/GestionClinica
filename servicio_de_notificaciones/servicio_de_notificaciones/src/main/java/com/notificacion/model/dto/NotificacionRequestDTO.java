package com.notificacion.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
    @NotBlank(message = "El tipo de notificación es obligatorio")
    private String tipo;
}
