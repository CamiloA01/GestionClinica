package com.ficha_clinica.servicio_de_ficha_clinica.controller;

import com.ficha_clinica.servicio_de_ficha_clinica.model.Ficha;
import com.ficha_clinica.servicio_de_ficha_clinica.model.dto.FichaRequestDTO;
import com.ficha_clinica.servicio_de_ficha_clinica.service.FichaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/ficha-clinica")
@Tag(name = "Ficha Clínica", description = "Operaciones relacionadas con fichas clínicas")
public class FichaController {

    @Autowired
    private FichaService fichaService;

    // GET /api/ficha-clinica
    @GetMapping
    @Operation(summary = "Listar todas las fichas clínicas", description = "Devuelve una lista de todas las fichas clínicas")
    public ResponseEntity<List<Ficha>> listarTodas() {
        return ResponseEntity.ok(fichaService.obtenerTodas());
    }

    // GET /api/ficha-clinica/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Obtener ficha clínica por ID", description = "Devuelve una ficha clínica específica según su ID")
    public ResponseEntity<Ficha> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fichaService.obtenerPorId(id));
    }

    // GET /api/ficha-clinica/paciente/{idPaciente}
    @GetMapping("/paciente/{idPaciente}")
    @Operation(summary = "Obtener fichas clínicas por ID de paciente", description = "Devuelve una lista de fichas clínicas asociadas a un paciente específico según su ID")
    public ResponseEntity<List<Ficha>> obtenerPorPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(fichaService.obtenerPorPaciente(idPaciente));
    }

    // POST /api/ficha-clinica
    @PostMapping
    @Operation(summary = "Crear una nueva ficha clínica", description = "Crea una nueva ficha clínica con los datos proporcionados")
    public ResponseEntity<Ficha> crear(@Valid @RequestBody FichaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fichaService.guardar(dto));
    }

    // PUT /api/ficha-clinica/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una ficha clínica", description = "Actualiza los datos de una ficha clínica específica según su ID")
    public ResponseEntity<Ficha> actualizar(@PathVariable Long id,
                                             @Valid @RequestBody FichaRequestDTO dto) {
        return ResponseEntity.ok(fichaService.actualizar(id, dto));
    }

    // DELETE /api/ficha-clinica/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una ficha clínica", description = "Elimina una ficha clínica específica según su ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        fichaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
