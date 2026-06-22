package com.pago.servicio_de_pagos.service;

import com.pago.servicio_de_pagos.model.Pago;
import com.pago.servicio_de_pagos.repository.pagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test de servicio: sin BD, todo mockeado con Mockito.
 * Verifica la lógica de negocio de pagoService.
 */
@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private pagoRepository pagoRepository;

    @InjectMocks
    private pagoService pagoService;

    private Pago pagoMock;

    @BeforeEach
    void setUp() {
        pagoMock = new Pago();
        pagoMock.setId(1L);
        pagoMock.setMonto(new BigDecimal("50000.00"));
        pagoMock.setMetodoPago("TARJETA");
        pagoMock.setEstadoPago("PENDIENTE");
        pagoMock.setFechaPago(LocalDate.now());
    }

    // ───── obtenerPagos ──────────────────────────────────────────────────────

    @Test
    void obtenerPagos_debeRetornarListaDePagos() {
        when(pagoRepository.findAll()).thenReturn(List.of(pagoMock));

        List<Pago> resultado = pagoService.obtenerPagos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getMetodoPago()).isEqualTo("TARJETA");
    }

    @Test
    void obtenerPagos_debeRetornarListaVaciaSiNoHayPagos() {
        when(pagoRepository.findAll()).thenReturn(List.of());

        List<Pago> resultado = pagoService.obtenerPagos();

        assertThat(resultado).isEmpty();
    }

    // ───── obtenerPagoPorId ──────────────────────────────────────────────────

    @Test
    void obtenerPagoPorId_debeRetornarPagoCuandoExiste() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoMock));

        Optional<Pago> resultado = pagoService.obtenerPagoPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    void obtenerPagoPorId_debeRetornarVacioCuandoNoExiste() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pago> resultado = pagoService.obtenerPagoPorId(99L);

        assertThat(resultado).isEmpty();
    }

    // ───── guardarPago ───────────────────────────────────────────────────────

    @Test
    void guardarPago_debeRetornarPagoGuardado() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoMock);

        Pago resultado = pagoService.guardarPago(pagoMock);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getMonto()).isEqualByComparingTo("50000.00");
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    // ───── actualizarPago ────────────────────────────────────────────────────

    @Test
    void actualizarPago_debeActualizarCamposCorrectamente() {
        Pago pagoActualizado = new Pago();
        pagoActualizado.setMonto(new BigDecimal("75000.00"));
        pagoActualizado.setMetodoPago("EFECTIVO");
        pagoActualizado.setEstadoPago("PAGADO");
        pagoActualizado.setFechaPago(LocalDate.now());

        Pago resultado = new Pago();
        resultado.setId(1L);
        resultado.setMonto(new BigDecimal("75000.00"));
        resultado.setMetodoPago("EFECTIVO");
        resultado.setEstadoPago("PAGADO");
        resultado.setFechaPago(LocalDate.now());

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoMock));
        when(pagoRepository.save(any(Pago.class))).thenReturn(resultado);

        Pago actualizado = pagoService.actualizarPago(1L, pagoActualizado);

        assertThat(actualizado.getMonto()).isEqualByComparingTo("75000.00");
        assertThat(actualizado.getMetodoPago()).isEqualTo("EFECTIVO");
    }

    @Test
    void actualizarPago_debeLanzarExcepcionSiNoExiste() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.actualizarPago(99L, pagoMock))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pago no encontrado con id: 99");
    }

    // ───── eliminarPago ──────────────────────────────────────────────────────

    @Test
    void eliminarPago_debeEliminarCuandoExiste() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoMock));
        doNothing().when(pagoRepository).deleteById(1L);

        pagoService.eliminarPago(1L);

        verify(pagoRepository, times(1)).deleteById(1L);
    }

    // ───── obtenerPagosPorEstado ─────────────────────────────────────────────

    @Test
    void obtenerPagosPorEstado_debeRetornarSoloLosFiltrados() {
        when(pagoRepository.findByEstadoPago("PENDIENTE")).thenReturn(List.of(pagoMock));

        List<Pago> resultado = pagoService.obtenerPagosPorEstado("PENDIENTE");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstadoPago()).isEqualTo("PENDIENTE");
    }

    @Test
    void obtenerPagosPorEstado_debeRetornarVacioSiNoHay() {
        when(pagoRepository.findByEstadoPago("CANCELADO")).thenReturn(List.of());

        List<Pago> resultado = pagoService.obtenerPagosPorEstado("CANCELADO");

        assertThat(resultado).isEmpty();
    }
}
