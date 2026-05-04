package com.pago.servicio_de_pagos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoRequestDTO {
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "El metodo de pago es obligatorio")
    private String metodoPago;

    @NotBlank(message = "El estado de pago es obligatorio")
    private String estadoPago;

    @NotNull(message = "la fecha de pago es obligatoria")
    @PastOrPresent(message = "la fecha de pago no puede ser futura")
    private LocalDate fechaPago;

    

}
