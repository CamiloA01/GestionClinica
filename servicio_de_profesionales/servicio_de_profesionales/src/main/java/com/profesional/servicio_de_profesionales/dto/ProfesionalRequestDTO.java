package com.profesional.servicio_de_profesionales.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionalRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "El apellido paterno no puede estar vacio")
    private String apellidopa;

    @NotBlank(message = "El apellido materno no puede estar vacio")
    private String apellidoma;

    @NotBlank(message = "El run no puede estar vacio")
    private String run;

    //Es lo que estudio
    @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;

    @NotNull(message = "Los años de experiencia no puede estar vacio")
    private LocalDate fechacontrato;
}
