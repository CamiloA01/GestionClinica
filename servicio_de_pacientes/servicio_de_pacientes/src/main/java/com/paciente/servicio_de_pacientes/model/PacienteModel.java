package com.paciente.servicio_de_pacientes.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.persistence.*;


@Entity
@Table(name = "paciente")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PacienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true, length = 13)
    private String rut;

    @Column(nullable = false)
    private LocalDate fecha_nacimiento;

    @Column(nullable = false, length = 10)
    private int telefono;
    
    @Column(nullable = false)
    private String direccion;

    



}
