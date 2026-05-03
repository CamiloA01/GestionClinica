package com.profesional.servicio_de_profesionales.controller;

import com.profesional.servicio_de_profesionales.dto.*;
import com.profesional.servicio_de_profesionales.service.ProfecionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/profecional")
@RequiredArgsConstructor
public class ProfecionalController {

    private final ProfecionalService profecionalService;

    @GetMapping
    public ResponseEntity<List<ProfecionalResponceDTO>> obtenerTodos() {
        return ResponseEntity.ok(profecionalService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfecionalResponceDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfecionalRequestDTO dto) {
        return profecionalService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    
}
