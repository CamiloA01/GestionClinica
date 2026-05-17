package com.agenda.servicio_de_agenda.model.dto;

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
    private String horaInicio;
    private String horaFin;
    private Integer duracionCita;
}
