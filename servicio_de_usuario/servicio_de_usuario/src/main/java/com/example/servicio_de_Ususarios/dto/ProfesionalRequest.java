package com.example.servicio_de_Ususarios.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProfesionalRequest {
    
    // Campos que coinciden con el ProfesionalRequestDTO del servicio de profesionales
    private Long usuarioId;
    private String nombre;
    private String apellidopa;
    private String apellidoma;
    private String run;
    private String titulo;
    private LocalDate fechacontrato;

}
