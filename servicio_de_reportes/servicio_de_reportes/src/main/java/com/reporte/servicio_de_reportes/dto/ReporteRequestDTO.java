package com.reporte.servicio_de_reportes.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO {
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreReporte;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    @NotBlank(message = "El solicitante no puede estar vacío")
    private String requestdBy;

    @NotNull(message = "La fecha de generación es obligatoria")
    private LocalDate generatedAt;
}
