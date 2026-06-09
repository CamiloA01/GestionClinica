package com.ficha_clinica.servicio_de_ficha_clinica.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;  
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelFicha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    private LocalDate fecha;

}
