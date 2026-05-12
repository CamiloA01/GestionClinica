package com.notificacion.model;

import java.time.LocalDateTime;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUsuario;

    @Column( nullable = false, length = 100)
    private String titulo;

    @Column( nullable = false, length = 200)
    private String mensaje;

    @Column( nullable = false, length = 50)
    private String tipo;

    @Column( nullable = false, length = 50)
    private String estado;

    @Column( nullable = false)
    private LocalDateTime fechaEnvio;
    
    @Column( nullable = true)
    private LocalDateTime fechaLectura;
}
