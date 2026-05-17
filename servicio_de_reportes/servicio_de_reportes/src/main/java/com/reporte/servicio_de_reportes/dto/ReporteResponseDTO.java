package com.reporte.servicio_de_reportes.dto;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponseDTO {
   
    private Long id;
    private String nombreReporte;
    private String tipo;
    private String requestdBy;
    private LocalDate generatedAt;


}
