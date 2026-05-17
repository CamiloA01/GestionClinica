package com.profesional.servicio_de_profesionales.controller;

import com.profesional.servicio_de_profesionales.dto.*;
import com.profesional.servicio_de_profesionales.service.ProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/profesional")
@RequiredArgsConstructor
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    @GetMapping
    public ResponseEntity<List<ProfesionalResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(profesionalService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalResponseDTO> obtenerPorId(@PathVariable Long id) {
        return profesionalService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfesionalResponseDTO> crear(
            @Valid @RequestBody ProfesionalRequestDTO dto) {
        return ResponseEntity.status(201).body(profesionalService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalResponseDTO> actualizar (
            @PathVariable Long id,
            @Valid @RequestBody ProfesionalRequestDTO dto) {
        return profesionalService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (profesionalService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        profesionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    
}
