package com.notificacion.servicio_de_notificaciones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.notificacion.controller.NotificacionController;
import com.notificacion.model.dto.NotificacionRequestDTO;
import com.notificacion.model.dto.NotificacionResponseDTO;
import com.notificacion.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
@ActiveProfiles("test")
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    private ObjectMapper objectMapper;
    private NotificacionResponseDTO responseMock;
    private NotificacionRequestDTO dtoValido;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        responseMock = new NotificacionResponseDTO(
                1L, 1L, "Cita confirmada", "Su cita ha sido confirmada",
                "CITA", "ENVIADA", LocalDateTime.now(), null
        );

        dtoValido = new NotificacionRequestDTO();
        dtoValido.setUsuarioId(1L);
        dtoValido.setTitulo("Cita confirmada");
        dtoValido.setMensaje("Su cita ha sido confirmada");
        dtoValido.setTipo("CITA");
    }

    @Test
    void listar_debeRetornar200ConListaDeNotificaciones() throws Exception {
        when(notificacionService.obtenerTodas()).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").value("Cita confirmada"))
                .andExpect(jsonPath("$[0].estado").value("ENVIADA"));
    }

    @Test
    void listar_debeRetornar200ConListaVacia() throws Exception {
        when(notificacionService.obtenerTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void obtenerPorId_debeRetornar200CuandoExiste() throws Exception {
        when(notificacionService.obtenerPorId(1L)).thenReturn(responseMock);

        mockMvc.perform(get("/api/notificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Cita confirmada"));
    }

    @Test
    void obtenerPorId_debeRetornar404CuandoNoExiste() throws Exception {
        when(notificacionService.obtenerPorId(99L))
                .thenThrow(new RuntimeException("Notificación no encontrada"));

        mockMvc.perform(get("/api/notificaciones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorUsuario_debeRetornar200ConSusNotificaciones() throws Exception {
        when(notificacionService.obtenerPorUsuarioId(1L)).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/notificaciones/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1));
    }

    @Test
    void crear_debeRetornar201ConNotificacionCreada() throws Exception {
        when(notificacionService.guardar(any(NotificacionRequestDTO.class))).thenReturn(responseMock);

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Cita confirmada"))
                .andExpect(jsonPath("$.estado").value("ENVIADA"));
    }

    @Test
    void crear_debeRetornar400CuandoTituloEsNulo() throws Exception {
        dtoValido.setTitulo(null);

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isBadRequest());

        verify(notificacionService, never()).guardar(any());
    }

    @Test
    void crear_debeRetornar400CuandoMensajeEsNulo() throws Exception {
        dtoValido.setMensaje(null);

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isBadRequest());

        verify(notificacionService, never()).guardar(any());
    }

    @Test
    void eliminar_debeRetornar204CuandoExiste() throws Exception {
        doNothing().when(notificacionService).eliminar(1L);

        mockMvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isNoContent());

        verify(notificacionService, times(1)).eliminar(1L);
    }

    @Test
    void eliminar_debeRetornar404CuandoNoExiste() throws Exception {
        doThrow(new RuntimeException("No se encontró la notificación con ID: 99"))
                .when(notificacionService).eliminar(99L);

        mockMvc.perform(delete("/api/notificaciones/99"))
                .andExpect(status().isNotFound());
    }
}