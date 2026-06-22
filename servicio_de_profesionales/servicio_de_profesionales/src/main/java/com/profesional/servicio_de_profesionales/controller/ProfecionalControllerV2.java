package com.profesional.servicio_de_profesionales.controller;

import com.profesional.servicio_de_profesionales.assemblers.ProfesionalModelAssemblers;
import com.profesional.servicio_de_profesionales.dto.ProfesionalRequestDTO;
import com.profesional.servicio_de_profesionales.dto.ProfesionalResponseDTO;
import com.profesional.servicio_de_profesionales.model.Profesional;
import com.profesional.servicio_de_profesionales.service.ProfesionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/profesional")
@Tag(name = "Profesionales V2 (HATEOAS)", description = "API con soporte HATEOAS para gestión de profesionales")
public class ProfecionalControllerV2 {

    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private ProfesionalModelAssemblers assembler;

    @Operation(summary = "Obtener todos los profesionales con HATEOAS")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente") })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Profesional>> obtenerTodos() {
        List<Profesional> entidades = profesionalService.obtenerTodosEntidad();
        List<EntityModel<Profesional>> profesionales = entidades.stream()
                .map(p -> assembler.toModel(p))
                .collect(Collectors.toList());
        Link selfLink = Link.of("/api/v2/profesional").withSelfRel();
        return CollectionModel.of(profesionales, selfLink);
    }

    @Operation(summary = "Obtener profesional por ID con HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profesional encontrado"),
        @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Profesional> obtenerPorId(
            @Parameter(description = "ID del profesional", example = "1")
            @PathVariable Long id) {
        Profesional profesional = profesionalService.obtenerEntidadPorId(id);
        return assembler.toModel(profesional);
    }

    @Operation(summary = "Crear profesional")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProfesionalResponseDTO> crear(@Valid @RequestBody ProfesionalRequestDTO dto) {
        return ResponseEntity.status(201).body(profesionalService.guardar(dto));
    }

    @Operation(summary = "Actualizar profesional")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalResponseDTO> actualizar(
            @Parameter(description = "ID del profesional", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProfesionalRequestDTO dto) {
        return profesionalService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar profesional")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del profesional", example = "1")
            @PathVariable Long id) {
        if (profesionalService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        profesionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
