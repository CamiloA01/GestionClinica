package com.pago.servicio_de_pagos.repository;

import com.pago.servicio_de_pagos.model.Pago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de repositorio: usa H2 en memoria con @DataJpaTest.
 * Verifica operaciones JPA y queries personalizadas de pagoRepository.
 */
@SuppressWarnings("null")
@DataJpaTest
@ActiveProfiles("test")
class PagoRepositoryTest {

    @Autowired
    private pagoRepository pagoRepository;

    private Pago pagoBase;

    @BeforeEach
    void setUp() {
        pagoBase = new Pago();
        pagoBase.setMonto(new BigDecimal("50000.00"));
        pagoBase.setMetodoPago("TARJETA");
        pagoBase.setEstadoPago("PENDIENTE");
        pagoBase.setFechaPago(LocalDate.now());
    }

    @Test
    void debeGuardarYRecuperarPagoPorId() {
        Pago guardado = pagoRepository.save(pagoBase);

        Optional<Pago> resultado = pagoRepository.findById(guardado.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getMonto()).isEqualByComparingTo("50000.00");
        assertThat(resultado.get().getMetodoPago()).isEqualTo("TARJETA");
    }

    @Test
    void debeBuscarPagosPorEstado() {
        pagoRepository.save(pagoBase);

        Pago pagoPagado = new Pago();
        pagoPagado.setMonto(new BigDecimal("30000.00"));
        pagoPagado.setMetodoPago("EFECTIVO");
        pagoPagado.setEstadoPago("PAGADO");
        pagoPagado.setFechaPago(LocalDate.now());
        pagoRepository.save(pagoPagado);

        List<Pago> pendientes = pagoRepository.findByEstadoPago("PENDIENTE");
        List<Pago> pagados = pagoRepository.findByEstadoPago("PAGADO");

        assertThat(pendientes).hasSize(1);
        assertThat(pagados).hasSize(1);
    }

    @Test
    void debeRetornarListaVaciaSiNoHayPagosConEseEstado() {
        List<Pago> resultado = pagoRepository.findByEstadoPago("CANCELADO");

        assertThat(resultado).isEmpty();
    }

    @Test
    void debeEliminarPagoPorId() {
        Pago guardado = pagoRepository.save(pagoBase);
        Long id = guardado.getId();

        pagoRepository.deleteById(id);

        assertThat(pagoRepository.findById(id)).isEmpty();
    }

    @Test
    void debeListarTodosLosPagos() {
        pagoRepository.save(pagoBase);

        Pago segundo = new Pago();
        segundo.setMonto(new BigDecimal("15000.00"));
        segundo.setMetodoPago("TRANSFERENCIA");
        segundo.setEstadoPago("PAGADO");
        segundo.setFechaPago(LocalDate.now());
        pagoRepository.save(segundo);

        List<Pago> todos = pagoRepository.findAll();

        assertThat(todos).hasSize(2);
    }
}
