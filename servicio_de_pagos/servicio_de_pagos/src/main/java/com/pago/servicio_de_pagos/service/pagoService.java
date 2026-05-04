package com.pago.servicio_de_pagos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.pago.servicio_de_pagos.model.Pago;
import com.pago.servicio_de_pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class PagoService {

    private final PagoRepository pagoRepository;
    
    public List<Pago> obtenerPagos() {
        return pagoRepository.findAll();
    }

    public Pago guardarPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Optional<Pago> obtenerPagoPorId(Long pagoId) {
        return pagoRepository.findById(pagoId);
    }

    public Pago actualizarPago(Long pagoId, Pago pagoActualizado) {
        return pagoRepository.findById(pagoId)
                .map(pago -> {
                    pago.setMonto(pagoActualizado.getMonto());
                    pago.setFechaPago(pagoActualizado.getFechaPago());
                    pago.setMetodoPago(pagoActualizado.getMetodoPago());
                    return pagoRepository.save(pago);
                })
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + pagoId));
    }

    public void eliminarPago(Long pagoId) {
        pagoRepository.deleteById(pagoId);
    }
}