package com.profesional.servicio_de_profesionales.controller;

import com.profesional.servicio_de_profesionales.dto.*;
import com.profesional.servicio_de_profesionales.service.ProfesionalService;
import com.profesional.servicio_de_profesionales.assemblers.ProfesionalModelAssemblers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class ProfecionalControllerV2 {

    private final ProfesionalService profesionalService;
    private final ProfesionalModelAssemblers assemblers;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
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
    public EntityModel<ProfesionalResponseDTO> obtenerPorId(@PathVariable Long id) {
        return profesionalService.obtenerPorId(id)
                .map(assemblers::toModel) // Enriquecemos con HATEOAS
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesional no encontrado"));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProfesionalResponseDTO>> crear(@Valid @RequestBody ProfesionalRequestDTO dto) {
        log.info("✅ Comunicación exitosa: petición recibida desde el microservicio de usuario para crear al profesional '{}' (usuarioId={})",
                dto.getNombre(), dto.getUsuarioId());

        ProfesionalResponseDTO nuevoProfesional = profesionalService.guardar(dto);

        log.info("✅ Profesional creado correctamente con id={}", nuevoProfesional.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assemblers.toModel(nuevoProfesional)); // Respuesta de creación con HATEOAS
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProfesionalResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfesionalRequestDTO dto) {
            
        return profesionalService.actualizar(id, dto)
                .map(assemblers::toModel) // Transforma a EntityModel si existe
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