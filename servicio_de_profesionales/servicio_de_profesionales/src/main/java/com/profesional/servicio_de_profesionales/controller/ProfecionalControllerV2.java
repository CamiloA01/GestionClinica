package com.profesional.servicio_de_profesionales.controller;

import com.profesional.servicio_de_profesionales.dto.*;
import com.profesional.servicio_de_profesionales.service.ProfesionalService;
import com.profesional.servicio_de_profesionales.assemblers.ProfesionalModelAssemblers;
import com.profesional.servicio_de_profesionales.model.Profesional;
import com.profesional.servicio_de_profesionales.controller.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/profesional")
public class ProfecionalControllerV2 {

    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private ProfesionalModelAssemblers assemblers;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProfesionalResponseDTO>> obtenerTodos() {
        List<EntityModel<Profesional>> profesional = profesionalService.findAll().stream()
                    .map(assemblers::toModel)
                    .collect(Collectors.toList());
        
        return CollectionModel.of(profesional,
                linkTo(methodOn(ProfecionalControllerV2.class).obtenerTodos().withSelfRel()));
        
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
