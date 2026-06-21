package com.agenda.servicio_de_agenda.model.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponseDTO {
    
    private Long id;
    private Long idProfesional;
    private String especialidad;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer duracionCita;
}
