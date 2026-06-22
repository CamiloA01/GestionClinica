package com.pago.servicio_de_pagos.controller;

import com.pago.servicio_de_pagos.assemblers.PagoModelAssembler;
import com.pago.servicio_de_pagos.service.pagoService;
import com.pago.servicio_de_pagos.dto.PagoRequestDTO;
import com.pago.servicio_de_pagos.dto.PagoResponceDTO;
import com.pago.servicio_de_pagos.model.Pago;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation; 
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pago Controller", description = "Operaciones relacionadas con pagos")
public class pagoController { 

    private final pagoService pagoService;
    private final PagoModelAssembler assembler; 

    @GetMapping
    @Operation(summary = "Obtener todos los pagos", description = "Devuelve una lista de todos los pagos disponibles")
    public ResponseEntity<CollectionModel<EntityModel<PagoResponceDTO>>> obtenerPagos() {
        // Obtenemos las entidades desde el servicio
        List<Pago> pagos = pagoService.obtenerPagos();
        
        // Convertimos las entidades a DTOs y luego a EntityModel (con links)
        List<EntityModel<PagoResponceDTO>> pagosModels = pagos.stream()
                .map(this::convertToResponceDTO) // Conversión a DTO
                .map(assembler::toModel)         // Conversión a HATEOAS
                .collect(Collectors.toList());

        // Envolvemos la lista en un CollectionModel junto con el link de este mismo método
        CollectionModel<EntityModel<PagoResponceDTO>> collectionModel = CollectionModel.of(pagosModels,
                linkTo(methodOn(pagoController.class).obtenerPagos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // Renombrado para que coincida con el linkTo de tu Assembler
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pago por ID", description = "Devuelve un pago específico basado en su ID")
    public ResponseEntity<EntityModel<PagoResponceDTO>> obtenerPagoPorId(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id)
                .map(this::convertToResponceDTO)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // REEMPLAZADO: POST para crear pagos con HATEOAS
    @PostMapping
    @Operation(summary = "Crear un nuevo pago", description = "Crea un nuevo pago y devuelve la información del pago creado con enlaces HATEOAS")
    public ResponseEntity<EntityModel<PagoResponceDTO>> crearPago(@Valid @RequestBody PagoRequestDTO dto) {
        
        // 1. Convertimos el RequestDTO a Entity para el servicio
        Pago nuevoPago = new Pago();
        nuevoPago.setMonto(dto.getMonto());
        nuevoPago.setMetodoPago(dto.getMetodoPago());
        nuevoPago.setEstadoPago(dto.getEstadoPago());
        nuevoPago.setFechaPago(dto.getFechaPago());

        // 2. Guardamos en la base de datos
        Pago pagoGuardado = pagoService.guardarPago(nuevoPago);

        // 3. Convertimos el resultado a ResponseDTO
        PagoResponceDTO responseDTO = convertToResponceDTO(pagoGuardado);

        // 4. Creamos el EntityModel con sus respectivos links de HATEOAS
        EntityModel<PagoResponceDTO> entityModel = assembler.toModel(responseDTO);

        // 5. Retornamos un 201 Created incluyendo la URL del nuevo recurso en la cabecera Location
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pago existente", description = "Actualiza la información de un pago existente por su ID")
    public ResponseEntity<Pago> actualizarPago(
            @PathVariable Long id,
            @Valid @RequestBody Pago pagoActualizado) {
                return pagoService.obtenerPagoPorId(id)
                        .map(existente -> {
                            pagoActualizado.setId(id);
                            return ResponseEntity.ok(pagoService.actualizarPago(id, pagoActualizado));
                        })
                        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago", description = "Elimina un pago existente por su ID")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id){
        if (pagoService.obtenerPagoPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar pagos por estado", description = "Devuelve una lista de pagos filtrados por su estado")
    public ResponseEntity<List<Pago>> buscarPorEstado(@RequestParam String estado) {
        List<Pago> pagos = pagoService.obtenerPagosPorEstado(estado);
        
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(pagos);
    }

    // MÁPER AUXILIAR: Para transformar la Entidad de la BD al DTO que requiere tu Assembler
    private PagoResponceDTO convertToResponceDTO(Pago pago) {
        PagoResponceDTO dto = new PagoResponceDTO();
        dto.setId(pago.getId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }
}