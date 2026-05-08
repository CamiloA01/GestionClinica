package com.pago.servicio_de_pagos.controller;

import com.pago.servicio_de_pagos.service.pagoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pago.servicio_de_pagos.model.Pago;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class pagoController {

    private final pagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> obtenerPagos() {
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> crearPago(@Valid @RequestBody Pago pago) {
        return ResponseEntity.status(201).body(pagoService.guardarPago(pago));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id){
        if (pagoService.obtenerPagoPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Pago>> buscarPorEstado(@RequestParam String estado) {
    List<Pago> pagos = pagoService.obtenerPagosPorEstado(estado);
    
    if (pagos.isEmpty()) {
        return ResponseEntity.noContent().build(); // Devuelve 204 si no hay resultados
    }
    
    return ResponseEntity.ok(pagos); // Devuelve 200 con la lista de pagos
    }
}