package com.especialidad.servicio_de_especialidades.controller;

import java.util.List;

import com.especialidad.servicio_de_especialidades.dto.EspecialidadRequestDTO;
import com.especialidad.servicio_de_especialidades.dto.EspecialidadResponseDTO;
import com.especialidad.servicio_de_especialidades.service.EspecialidadService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>>
    obtenerEspecialidades() {

        return ResponseEntity.ok(
                especialidadService.getAllEspecialidades()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO>
    obtenerEspecialidadPorId(@PathVariable Long id) {
        return especialidadService
                .BuscarEspecialidadPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponseDTO>
    crearEspecialidad(
            @Valid
            @RequestBody
            EspecialidadRequestDTO dto) {

        return ResponseEntity.status(201)
                .body(
                        especialidadService
                                .crearEspecialidad(dto)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO>
    actualizarEspecialidad(
            @PathVariable Long id,
            @Valid
            @RequestBody
            EspecialidadRequestDTO dto) {

        return especialidadService
                .actualizarEspecialidad(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminarEspecialidad(@PathVariable Long id) {

        if (especialidadService
                .BuscarEspecialidadPorId(id)
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        especialidadService.eliminarEspecialidad(id);

        return ResponseEntity.noContent().build();
    }
}