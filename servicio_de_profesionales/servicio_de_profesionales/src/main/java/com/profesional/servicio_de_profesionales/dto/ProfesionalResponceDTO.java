package com.profesional.servicio_de_profesionales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionalResponceDTO {
    private Long id;
    private String nombre;
    private String apellidopa;
    private String apellidoma;
    private String run;
    private String titulo;
    private LocalDate fechacontrato;
}
