package com.ficha_clinica.servicio_de_ficha_clinica.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaResponseDTO {

    private Long id;
    private Long pacienteId;
    private Long profesionalId;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private LocalDateTime fechaCreacion;
}

