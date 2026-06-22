package com.reporte.servicio_de_reportes.controller;

import com.reporte.servicio_de_reportes.dto.*;
import com.reporte.servicio_de_reportes.service.*;  

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Operaciones relacionadas con los reportes")
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping
    @Operation(summary = "Crear un nuevo reporte", description = "Crea un nuevo reporte y retorna la información del reporte creado")
    public ResponseEntity<ReporteResponseDTO> createReporte(@Valid @RequestBody ReporteRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(reporteService.crearReporte(requestDTO));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los reportes", description = "Retorna una lista de todos los reportes")
    public ResponseEntity<?> getAllReportes() {
        return ResponseEntity.ok(reporteService.getAllReportes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reporte por ID", description = "Retorna la información de un reporte específico por su ID")
    public ResponseEntity<ReporteResponseDTO> getReporteById(@PathVariable Long id) {
        return reporteService.BuscarReportePorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build()); 
    }   

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un reporte existente", description = "Actualiza la información de un reporte existente por su ID")
    public ResponseEntity<ReporteResponseDTO> actualizarReporte(@PathVariable Long id, @Valid @RequestBody ReporteRequestDTO requestDTO) {
        return reporteService.actualizarReporte(id, requestDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reporte", description = "Elimina un reporte existente por su ID")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }   

    @GetMapping("/buscarPorTipo")
    @Operation(summary = "Buscar reportes por tipo", description = "Retorna una lista de reportes filtrados por tipo")
    public ResponseEntity<List<ReporteResponseDTO>> buscarReportePorTipo(@RequestParam String tipo) {
        return ResponseEntity.ok(reporteService.BuscarReportesPorTipo(tipo));
    }

}
