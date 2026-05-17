package com.reporte.servicio_de_reportes.controller;

import com.reporte.servicio_de_reportes.dto.*;
import com.reporte.servicio_de_reportes.service.*;  

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping
    public ResponseEntity<ReporteResponseDTO> createReporte(@Valid @RequestBody ReporteRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(reporteService.crearReporte(requestDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllReportes() {
        return ResponseEntity.ok(reporteService.getAllReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> getReporteById(@PathVariable Long id) {
        return reporteService.BuscarReportePorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build()); 
    }   

    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> actualizarReporte(@PathVariable Long id, @Valid @RequestBody ReporteRequestDTO requestDTO) {
        return reporteService.actualizarReporte(id, requestDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }   

    @GetMapping("/buscarPorTipo")
    public ResponseEntity<List<ReporteResponseDTO>> buscarReportePorTipo(@RequestParam String tipo) {
        return ResponseEntity.ok(reporteService.BuscarReportesPorTipo(tipo));
    }

}
