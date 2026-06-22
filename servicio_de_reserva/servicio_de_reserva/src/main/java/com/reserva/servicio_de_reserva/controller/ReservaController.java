package com.reserva.servicio_de_reserva.controller;

import com.reserva.servicio_de_reserva.assemblers.ClinicaModelAssembler;
import com.reserva.servicio_de_reserva.dto.ReservaRequestDTO;
import com.reserva.servicio_de_reserva.dto.ReservaResponseDTO;
import com.reserva.servicio_de_reserva.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

// IMPORTS FALTANTES DE HATEOAS Y STREAMS
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reserva")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operaciones relacionadas con las reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ClinicaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Obtener todas las reservas", description = "Retorna una lista de todas las reservas")
    public CollectionModel<EntityModel<ReservaResponseDTO>> getAllReservas() {
        // 1. Obtenemos la lista de DTOs directamente del servicio
        List<ReservaResponseDTO> dtos = reservaService.getAllReservas();

        // 2. Mapeamos cada DTO a un EntityModel usando el assembler
        List<EntityModel<ReservaResponseDTO>> reservasModel = dtos.stream()
                .map(assembler::toModel) 
                .collect(Collectors.toList());

        // 3. Envolvemos la lista en un CollectionModel (CORREGIDO: ReservaController con R mayúscula)
        return CollectionModel.of(reservasModel,
                linkTo(methodOn(ReservaController.class).getAllReservas()).withSelfRel());
    }

    // NUEVO: Método GET por ID para complementar el Assembler HATEOAS
    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID", description = "Retorna la información de una reserva específica por su ID")
    public EntityModel<ReservaResponseDTO> getReservaById(@PathVariable Long id) {
        ReservaResponseDTO dto = reservaService.obtenerPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));
        return assembler.toModel(dto);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva y retorna la información de la reserva creada")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> crearReserva(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
    // 1. Guardamos la reserva en la base de datos
    ReservaResponseDTO reservaResponseDTO = reservaService.guardar(reservaRequestDTO);
    
    // 2. Retornamos un HTTP 201 CREATED con el DTO enriquecido con enlaces HATEOAS
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(reservaResponseDTO));
    }
    
   @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reserva existente", description = "Actualiza la información de una reserva existente por su ID")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> actualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
    
    return reservaService.actualizar(id, reservaRequestDTO)
            .map(assembler::toModel) // 1. Convierte el DTO a EntityModel (añade los links HATEOAS)
            .map(ResponseEntity::ok) // 2. Si existe, lo devuelve con un HTTP 200 OK
            .orElse(ResponseEntity.notFound().build()); // 3. Si no existe, devuelve un HTTP 404
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva existente por su ID")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        if (reservaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}