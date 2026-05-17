package com.profesional.servicio_de_profesionales.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionalRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    private String apellidopa;

    @NotBlank(message = "El apellido materno es obligatorio")
    private String apellidoma;

    @NotBlank(message = "El RUN es obligatorio")
    private String run;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotNull(message = "La fecha de contrato es obligatoria")
    private LocalDate fechacontrato;
}

