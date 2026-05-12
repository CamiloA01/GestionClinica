package com.especialidad.servicio_de_especialidades.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "la descripcion no puede estar vacio")
    private String descripcion;

    @NotBlank(message = "El estado no puede estar vacio")
    private String estado;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDateTime fechaCreacion;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDateTime fechaActualizacion;
}
