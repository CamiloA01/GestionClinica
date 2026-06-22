package com.pago.servicio_de_pagos.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pago.servicio_de_pagos.assemblers.PagoModelAssembler;
import com.pago.servicio_de_pagos.dto.PagoRequestDTO;
import com.pago.servicio_de_pagos.dto.PagoResponceDTO;
import com.pago.servicio_de_pagos.model.Pago;
import com.pago.servicio_de_pagos.service.pagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de controlador: solo capa web, sin BD.
 * pagoService y PagoModelAssembler mockeados con @MockBean.
 */
@SuppressWarnings("null")
@WebMvcTest(pagoController.class)
@ActiveProfiles("test")
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private pagoService pagoService;

    @MockBean
    private PagoModelAssembler assembler;

    private ObjectMapper objectMapper;
    private Pago pagoMock;
    private PagoResponceDTO responseMock;
    private PagoRequestDTO dtoValido;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        pagoMock = new Pago();
        pagoMock.setId(1L);
        pagoMock.setMonto(new BigDecimal("50000.00"));
        pagoMock.setMetodoPago("TARJETA");
        pagoMock.setEstadoPago("PENDIENTE");
        pagoMock.setFechaPago(LocalDate.now());

        responseMock = new PagoResponceDTO();
        responseMock.setId(1L);
        responseMock.setMonto(new BigDecimal("50000.00"));
        responseMock.setMetodoPago("TARJETA");
        responseMock.setEstadoPago("PENDIENTE");
        responseMock.setFechaPago(LocalDate.now());

        dtoValido = new PagoRequestDTO();
        dtoValido.setMonto(new BigDecimal("50000.00"));
        dtoValido.setMetodoPago("TARJETA");
        dtoValido.setEstadoPago("PENDIENTE");
        dtoValido.setFechaPago(LocalDate.now());

        when(assembler.toModel(any(PagoResponceDTO.class)))
                .thenAnswer(inv -> EntityModel.of(inv.getArgument(0)));
    }

    // ───── GET /api/pagos ────────────────────────────────────────────────────

    @Test
    void listar_debeRetornar200ConListaDePagos() throws Exception {
        when(pagoService.obtenerPagos()).thenReturn(List.of(pagoMock));

        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk());
    }

    // ───── GET /api/pagos/{id} ───────────────────────────────────────────────

    @Test
    void obtenerPorId_debeRetornar200CuandoExiste() throws Exception {
        when(pagoService.obtenerPagoPorId(1L)).thenReturn(Optional.of(pagoMock));

        mockMvc.perform(get("/api/pagos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_debeRetornar404CuandoNoExiste() throws Exception {
        when(pagoService.obtenerPagoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pagos/99"))
                .andExpect(status().isNotFound());
    }

    // ───── POST /api/pagos ───────────────────────────────────────────────────

    @Test
    void crear_debeRetornar201ConPagoCreado() throws Exception {
        when(pagoService.guardarPago(any(Pago.class))).thenReturn(pagoMock);

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isCreated());
    }

    @Test
    void crear_debeRetornar400CuandoMontoEsNulo() throws Exception {
        dtoValido.setMonto(null);

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isBadRequest());

        verify(pagoService, never()).guardarPago(any());
    }

    // ───── DELETE /api/pagos/{id} ────────────────────────────────────────────

    @Test
    void eliminar_debeRetornar204CuandoExiste() throws Exception {
        when(pagoService.obtenerPagoPorId(1L)).thenReturn(Optional.of(pagoMock));
        doNothing().when(pagoService).eliminarPago(1L);

        mockMvc.perform(delete("/api/pagos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_debeRetornar404CuandoNoExiste() throws Exception {
        when(pagoService.obtenerPagoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/pagos/99"))
                .andExpect(status().isNotFound());
    }

    // ───── GET /api/pagos/buscar?estado= ────────────────────────────────────

    @Test
    void buscarPorEstado_debeRetornar200CuandoHayResultados() throws Exception {
        when(pagoService.obtenerPagosPorEstado("PENDIENTE")).thenReturn(List.of(pagoMock));

        mockMvc.perform(get("/api/pagos/buscar").param("estado", "PENDIENTE"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorEstado_debeRetornar204CuandoNoHayResultados() throws Exception {
        when(pagoService.obtenerPagosPorEstado("CANCELADO")).thenReturn(List.of());

        mockMvc.perform(get("/api/pagos/buscar").param("estado", "CANCELADO"))
                .andExpect(status().isNoContent());
    }
}
