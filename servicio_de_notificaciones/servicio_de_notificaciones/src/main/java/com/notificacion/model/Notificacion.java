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
    //Función: Identificador único de la notificación.
    //Tipo: Clave primaria autogenerada.
    //Uso: Permite distinguir cada notificación en la base de datos.
    private Long id;
    //Función: Identifica al usuario que recibe la notificación.
    //Uso: Relaciona la notificación con un paciente, médico o administrador.
    private Long idUsuario;

    @Column( nullable = false, length = 100)
    //Función: Título de la notificación.
    //Uso: Proporciona un resumen breve del contenido de la notificación.
    private String titulo;

    @Column( nullable = false, length = 200)
    //Función: Contenido detallado de la notificación.
    //Uso: Proporciona información específica sobre la notificación.
    private String mensaje;

    @Column( nullable = false, length = 50)
    //Función: Tipo de notificación.
    //Uso: Permite clasificar las notificaciones por su naturaleza.
    private String tipo;

    @Column( nullable = false, length = 50)
    //Función: Estado de la notificación.
    //Uso: Indica si la notificación ha sido leída, enviada, etc.
    private String estado;

    @Column( nullable = false)
    //Función: Fecha y hora de envío de la notificación.
    //Uso: Registra cuándo se envió la notificación.
    private LocalDateTime fechaEnvio;
    
    @Column( nullable = true)
    //Función: Fecha y hora de lectura de la notificación.
    //Uso: Registra cuándo el usuario leyó la notificación.
    private LocalDateTime fechaLectura;
}
