package com.profesional.servicio_de_profesionales.controller;

import com.profesional.servicio_de_profesionales.dto.*;
import com.profesional.servicio_de_profesionales.service.ProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/profecional")
@RequiredArgsConstructor
public class ProfesionalController {

    private final ProfesionalService profecionalService;

    @GetMapping
    public ResponseEntity<List<ProfesionalResponceDTO>> obtenerTodos() {
        return ResponseEntity.ok(profecionalService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalResponceDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfesionalRequestDTO dto) {
        return profecionalService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    
}
