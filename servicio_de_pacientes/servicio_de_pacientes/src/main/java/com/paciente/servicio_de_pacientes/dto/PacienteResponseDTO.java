package com.paciente.servicio_de_pacientes.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PacienteResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String rut;
    private LocalDate fecha_nacimiento;
    private int telefono;
    private String direccion;

}
