package com.pago.servicio_de_pagos.controller;

import com.pago.servicio_de_pagos.assemblers.PagoModelAssembler;
import com.pago.servicio_de_pagos.service.pagoService;
import com.pago.servicio_de_pagos.dto.PagoRequestDTO;
import com.pago.servicio_de_pagos.dto.PagoResponceDTO;
import com.pago.servicio_de_pagos.model.Pago;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@SuppressWarnings("null")
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Gestión de pagos del sistema clínico (HATEOAS)")
public class pagoController {

    private final pagoService pagoService;
    private final PagoModelAssembler assembler;

    @Operation(summary = "Listar todos los pagos",
               description = "Recupera la lista completa de pagos registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Listado de pagos obtenido exitosamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagoResponceDTO>>> obtenerPagos() {
        List<Pago> pagos = pagoService.obtenerPagos();

        List<EntityModel<PagoResponceDTO>> pagosModels = pagos.stream()
                .map(this::convertToResponceDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PagoResponceDTO>> collectionModel = CollectionModel.of(pagosModels,
                linkTo(methodOn(pagoController.class).obtenerPagos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Buscar pago por ID",
               description = "Recupera un pago específico usando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe un pago con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagoResponceDTO>> obtenerPagoPorId(
            @Parameter(description = "ID único del pago") @PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id)
                .map(this::convertToResponceDTO)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo pago",
               description = "Registra un nuevo pago en el sistema validando los datos del cuerpo de la petición.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos en la solicitud")
    })
    @PostMapping
    public ResponseEntity<EntityModel<PagoResponceDTO>> crearPago(
            @Valid @RequestBody PagoRequestDTO dto) {

        Pago nuevoPago = new Pago();
        nuevoPago.setMonto(dto.getMonto());
        nuevoPago.setMetodoPago(dto.getMetodoPago());
        nuevoPago.setEstadoPago(dto.getEstadoPago());
        nuevoPago.setFechaPago(dto.getFechaPago());

        Pago pagoGuardado = pagoService.guardarPago(nuevoPago);
        PagoResponceDTO responseDTO = convertToResponceDTO(pagoGuardado);
        EntityModel<PagoResponceDTO> entityModel = assembler.toModel(responseDTO);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Actualizar pago",
               description = "Actualiza los datos de un pago existente usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe un pago con ese ID"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizarPago(
            @Parameter(description = "ID único del pago a actualizar") @PathVariable Long id,
            @Valid @RequestBody Pago pagoActualizado) {
        return pagoService.obtenerPagoPorId(id)
                .map(existente -> {
                    pagoActualizado.setId(id);
                    return ResponseEntity.ok(pagoService.actualizarPago(id, pagoActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar pago",
               description = "Elimina permanentemente un pago del sistema usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un pago con ese ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "ID único del pago a eliminar") @PathVariable Long id) {
        if (pagoService.obtenerPagoPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar pagos por estado",
               description = "Recupera todos los pagos que coinciden con el estado especificado (ej: PENDIENTE, COMPLETADO, CANCELADO).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
        @ApiResponse(responseCode = "204", description = "No hay pagos con ese estado")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<Pago>> buscarPorEstado(
            @Parameter(description = "Estado del pago (ej: PENDIENTE, COMPLETADO, CANCELADO)") @RequestParam String estado) {
        List<Pago> pagos = pagoService.obtenerPagosPorEstado(estado);
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pagos);
    }

    // MÁPER AUXILIAR
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
