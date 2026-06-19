package com.reserva.servicio_de_reserva.controller;

import com.reserva.servicio_de_reserva.assemblers.ClinicaModelAssembler;
import com.reserva.servicio_de_reserva.dto.ReservaRequestDTO;
import com.reserva.servicio_de_reserva.dto.ReservaResponseDTO;
import com.reserva.servicio_de_reserva.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
public class ReservaController {

    private final ReservaService reservaService;
    private final ClinicaModelAssembler assembler;

    @GetMapping
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
    public EntityModel<ReservaResponseDTO> getReservaById(@PathVariable Long id) {
        ReservaResponseDTO dto = reservaService.obtenerPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));
        return assembler.toModel(dto);
    }

   @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> actualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
    
    return reservaService.actualizar(id, reservaRequestDTO)
            .map(assembler::toModel) // 1. Convierte el DTO a EntityModel (añade los links HATEOAS)
            .map(ResponseEntity::ok) // 2. Si existe, lo devuelve con un HTTP 200 OK
            .orElse(ResponseEntity.notFound().build()); // 3. Si no existe, devuelve un HTTP 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        if (reservaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}