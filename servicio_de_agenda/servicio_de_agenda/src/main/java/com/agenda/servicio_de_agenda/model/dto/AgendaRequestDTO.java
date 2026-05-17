package com.agenda.servicio_de_agenda.model.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaRequestDTO {

    @NotNull(message = "El ID del profesional es obligatorio")
    private Long idProfesional;
    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;
    @NotBlank(message = "El día de la semana es obligatorio")
    private String diaSemana;
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;
    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;
    @Min(value = 10, message = "La duración mínima de cita debe ser de 10 minutos")
    private Integer duracionCita;
}

