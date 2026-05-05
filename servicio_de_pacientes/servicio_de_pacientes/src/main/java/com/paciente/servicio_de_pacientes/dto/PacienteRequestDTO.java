package com.paciente.servicio_de_pacientes.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El rut no puede estar vacía")
    private String rut;

    @NotNull(message = "La fecha no puede estar vacía")
    private LocalDate fecha_nacimiento;

    @NotNull(message = "El numero de telefono no puede estar vacío")
    private int telefono;

    @NotBlank(message = "La direccion es abligaroria ")
    private String direccion;

}
