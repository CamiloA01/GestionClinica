package com.profesional.servicio_de_profesionales.controller;

import com.profesional.servicio_de_profesionales.dto.*;
import com.profesional.servicio_de_profesionales.service.ProfesionalService;
import com.profesional.servicio_de_profesionales.assemblers.ProfesionalModelAssemblers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/profesional")
@RequiredArgsConstructor // Reemplaza @Autowired en los campos por inyección mediante constructor implícito de Lombok
@Tag(name = "Profesionales", description = "Controlador para gestionar profesionales")
public class ProfecionalControllerV2 {

    private final ProfesionalService profesionalService;
    private final ProfesionalModelAssemblers assemblers;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los profesionales", description = "Retorna una lista de todos los profesionales")
    public CollectionModel<EntityModel<ProfesionalResponseDTO>> obtenerTodos() {
        // 1. Convertimos la lista del servicio en modelos HATEOAS usando el assembler
        List<EntityModel<ProfesionalResponseDTO>> profesionalesModel = profesionalService.obtenerTodos().stream()
                .map(assemblers::toModel) // Tu assembler debe recibir ProfesionalResponseDTO
                .collect(Collectors.toList());
        
        // 2. Retornamos el CollectionModel con su respectivo Self Link corregido
        return CollectionModel.of(profesionalesModel,
                linkTo(methodOn(ProfecionalControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener profesional por ID", description = "Retorna la información de un profesional específico por su ID")
    public EntityModel<ProfesionalResponseDTO> obtenerPorId(@PathVariable Long id) {
        return profesionalService.obtenerPorId(id)
                .map(assemblers::toModel) // Enriquecemos con HATEOAS
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesional no encontrado"));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo profesional", description = "Crea un nuevo profesional y lo retorna")
    public ResponseEntity<EntityModel<ProfesionalResponseDTO>> crear(@Valid @RequestBody ProfesionalRequestDTO dto) {
        ProfesionalResponseDTO nuevoProfesional = profesionalService.guardar(dto);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assemblers.toModel(nuevoProfesional)); // Respuesta de creación con HATEOAS
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un profesional existente", description = "Actualiza la información de un profesional existente por su ID")
    public ResponseEntity<EntityModel<ProfesionalResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfesionalRequestDTO dto) {
            
        return profesionalService.actualizar(id, dto)
                .map(assemblers::toModel) // Transforma a EntityModel si existe
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un profesional", description = "Elimina un profesional existente por su ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (profesionalService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        profesionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
