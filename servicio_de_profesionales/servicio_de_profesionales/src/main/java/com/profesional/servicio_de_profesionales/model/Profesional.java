package com.profesional.servicio_de_profesionales.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profesional")
public class Profesional {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidopa;

    @Column(nullable = false)
    private String apellidoma;

    @Column(nullable = false,unique = true, length = 13)
    private String run;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private LocalDate fechacontrato;

}
