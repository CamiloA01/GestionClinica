package com.ficha_clinica.servicio_de_ficha_clinica.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaRequestDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;
    @NotNull(message = "El ID del profesional es obligatorio")
    private Long profesionalId;
    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnostico;
    @NotBlank(message = "El tratamiento es obligatorio")
    private String tratamiento;
    @NotBlank(message = "Las observaciones son obligatorias")
    private String observaciones;


}
