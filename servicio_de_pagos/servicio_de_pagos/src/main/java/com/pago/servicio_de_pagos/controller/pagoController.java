package com.pago.servicio_de_pagos.controller;

import com.pago.servicio_de_pagos.service.pagoService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pago.servicio_de_pagos.dto.PagoRequestDTO;
import com.pago.servicio_de_pagos.model.Pago;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class pagoController {

    @Autowired
    private final pagoService pagoService;

    // Obtener todos los pagos
    @GetMapping
    public ResponseEntity<List<Pago>> obtenerPagos() {
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    // Obtener un pago por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    


    @PostMapping
    // 1. Usa PagoRequestDTO en lugar de Pago
    // 2. Asegúrate de que @Valid esté ahí
    public ResponseEntity<Pago> crearPago(@Valid @RequestBody PagoRequestDTO dto) {
        
        // Aquí debes convertir el DTO a Entity manualmente o con ModelMapper
        Pago nuevoPago = new Pago();
        nuevoPago.setMonto(dto.getMonto());
        nuevoPago.setMetodoPago(dto.getMetodoPago());
        nuevoPago.setEstadoPago(dto.getEstadoPago());
        nuevoPago.setFechaPago(dto.getFechaPago());

        return ResponseEntity.status(201).body(pagoService.guardarPago(nuevoPago));
    }

    // Actualización de un pago existente
    @PutMapping("/{id}")
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

    // Eliminación de un pago por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id){
        if (pagoService.obtenerPagoPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }


    //consulta personalizada para buscar un pago por el estado
    @GetMapping("/buscar")
    public ResponseEntity<List<Pago>> buscarPorEstado(@RequestParam String estado) {
    List<Pago> pagos = pagoService.obtenerPagosPorEstado(estado);
    
    if (pagos.isEmpty()) {
        return ResponseEntity.noContent().build(); // Devuelve 204 si no hay resultados
    }
    
    return ResponseEntity.ok(pagos); // Devuelve 200 con la lista de pagos
    }

}