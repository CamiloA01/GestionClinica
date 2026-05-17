package com.reporte.servicio_de_reportes.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reportes")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ModelReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreReporte;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String requestdBy;

    @Column(nullable = false)
    private LocalDate generatedAt;


}
