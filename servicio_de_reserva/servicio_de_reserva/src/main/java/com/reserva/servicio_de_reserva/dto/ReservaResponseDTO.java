package com.reserva.servicio_de_reserva.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaResponseDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private Long usuarioId;
    private String descripcion;
}
