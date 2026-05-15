package com.agenda.servicio_de_agenda.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agendas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //función: Identificador único de la agenda.
    //tipo: Clave primaria autogenerada.
    //uso: Permite distinguir cada agenda en la base de datos.
    private Long id;

    //función: Identificador del profesional asociado a la agenda.
    //tipo: Clave foránea que referencia al profesional (médico).
    //uso: Permite relacionar la agenda con el profesional correspondiente.
    private Long idProfesional;

    //función: Especialidad médica asociada a la agenda.
    //tipo: Cadena de texto que describe la especialidad (ej: Cardiología).
    //uso: Permite clasificar la agenda según la especialidad del profesional.
    @Column(nullable = false)
    private String especialidad;

    //función: Día de la semana para el cual se establece la agenda.
    //tipo: Cadena de texto que representa el día (ej: Lunes, Martes).
    //uso: Define el día específico en que el profesional tiene disponibilidad.
    @Column(nullable = false, length = 20)
    private String diaSemana;

    //función: Hora de inicio de la jornada laboral del profesional.
    //tipo: Hora en formato HH:mm (ej: 08:00).
    //uso: Define el horario de inicio de trabajo del profesional.
    @Column(nullable = false)
    private LocalTime horaInicio;

    //función: Hora de finalización de la jornada laboral del profesional.
    //tipo: Hora en formato HH:mm (ej: 17:00).
    //uso: Define el horario de finalización de trabajo del profesional.
    @Column(nullable = false)
    private LocalTime horaFin;

    //función: Duración de cada cita.
    //tipo: Número entero que representa la duración en minutos.
    //uso: Define cuánto tiempo dura cada cita en la agenda.
    @Column(nullable = false)
    private Integer duracionCita;

    //función: Indica si la agenda está activa.
    //tipo: Valor booleano (true/false).
    //uso: Permite activar o desactivar una agenda.
    @Column(nullable = false)
    private Boolean activa;

    //función: Fecha y hora de creación de la agenda.
    //tipo: Marca de tiempo que registra cuándo se creó la agenda.
    //uso: Permite llevar un registro de cuándo se estableció la agenda.
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

}
