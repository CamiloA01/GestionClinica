package com.especialidad.servicio_de_especialidades.controller;

import java.util.List;

import com.especialidad.servicio_de_especialidades.dto.EspecialidadRequestDTO;
import com.especialidad.servicio_de_especialidades.dto.EspecialidadResponseDTO;
import com.especialidad.servicio_de_especialidades.service.EspecialidadService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidad Controller", description = "Operaciones relacionadas con las especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    @Operation(summary = "Obtener todas las especialidades", description = "Devuelve una lista de todas las especialidades disponibles")
    public ResponseEntity<List<EspecialidadResponseDTO>>
    obtenerEspecialidades() {

        return ResponseEntity.ok(
                especialidadService.getAllEspecialidades()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener especialidad por ID", description = "Devuelve la especialidad correspondiente al ID proporcionado")
    public ResponseEntity<EspecialidadResponseDTO>
    obtenerEspecialidadPorId(@PathVariable Long id) {
        return especialidadService
                .BuscarEspecialidadPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva especialidad", description = "Crea una nueva especialidad con los datos proporcionados")
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
    @Operation(summary = "Actualizar una especialidad existente", description = "Actualiza la especialidad correspondiente al ID proporcionado con los nuevos datos")
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
    @Operation(summary = "Eliminar una especialidad", description = "Elimina la especialidad correspondiente al ID proporcionado")
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