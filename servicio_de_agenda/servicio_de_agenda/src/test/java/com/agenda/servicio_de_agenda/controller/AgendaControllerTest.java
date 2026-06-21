package com.agenda.servicio_de_agenda.controller;

import com.agenda.servicio_de_agenda.model.Agenda;
import com.agenda.servicio_de_agenda.model.dto.AgendaRequestDTO;
import com.agenda.servicio_de_agenda.service.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de controlador: solo levanta la capa web (sin BD, sin Feign).
 * AgendaService está mockeado con @MockBean.
 * Se verifica que los endpoints respondan con el HTTP status y body correctos.
 */
@SuppressWarnings("null")
@WebMvcTest(AgendaController.class)
@ActiveProfiles("test")
class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendaService agendaService;

    private ObjectMapper objectMapper;
    private Agenda agendaMock;
    private AgendaRequestDTO dtoValido;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Para serializar LocalTime/LocalDateTime

        agendaMock = new Agenda();
        agendaMock.setId(1L);
        agendaMock.setIdProfesional(1L);
        agendaMock.setEspecialidad("Cardiología");
        agendaMock.setDiaSemana("Lunes");
        agendaMock.setHoraInicio(LocalTime.of(8, 0));
        agendaMock.setHoraFin(LocalTime.of(17, 0));
        agendaMock.setDuracionCita(30);
        agendaMock.setActiva(true);

        dtoValido = new AgendaRequestDTO();
        dtoValido.setIdProfesional(1L);
        dtoValido.setEspecialidad("Cardiología");
        dtoValido.setDiaSemana("Lunes");
        dtoValido.setHoraInicio(LocalTime.of(8, 0));
        dtoValido.setHoraFin(LocalTime.of(17, 0));
        dtoValido.setDuracionCita(30);
    }

    // ───── GET /api/agendas ──────────────────────────────────────────────────────

    @Test
    void listarAgendas_debeRetornar200ConListaDeAgendas() throws Exception {
        when(agendaService.obtenerTodas()).thenReturn(List.of(agendaMock));

        mockMvc.perform(get("/api/agendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].especialidad").value("Cardiología"));
    }

    @Test
    void listarAgendas_debeRetornar200ConListaVacia() throws Exception {
        when(agendaService.obtenerTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/agendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ───── POST /api/agendas ─────────────────────────────────────────────────────

    @Test
    void crearAgenda_debeRetornar201ConAgendaCreada() throws Exception {
        when(agendaService.guardar(any(AgendaRequestDTO.class))).thenReturn(agendaMock);

        mockMvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.especialidad").value("Cardiología"))
                .andExpect(jsonPath("$.activa").value(true));
    }

    @Test
    void crearAgenda_debeRetornar400CuandoEspecialidadEsNula() throws Exception {
        dtoValido.setEspecialidad(null); // viola @NotBlank

        mockMvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isBadRequest());

        verify(agendaService, never()).guardar(any());
    }

    @Test
    void crearAgenda_debeRetornar400CuandoDuracionEsMenorA10() throws Exception {
        dtoValido.setDuracionCita(5); // viola @Min(10)

        mockMvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isBadRequest());

        verify(agendaService, never()).guardar(any());
    }

    @Test
    void crearAgenda_debeRetornar400CuandoIdProfesionalEsNulo() throws Exception {
        dtoValido.setIdProfesional(null); // viola @NotNull

        mockMvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isBadRequest());

        verify(agendaService, never()).guardar(any());
    }

    // ───── GET /api/agendas/{id} ─────────────────────────────────────────────────

    @Test
    void buscarPorId_debeRetornar200CuandoAgendaExiste() throws Exception {
        when(agendaService.obtenerPorId(1L)).thenReturn(agendaMock);

        mockMvc.perform(get("/api/agendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.diaSemana").value("Lunes"));
    }

    @Test
    void buscarPorId_debeRetornar404CuandoAgendaNoExiste() throws Exception {
        when(agendaService.obtenerPorId(99L))
                .thenThrow(new RuntimeException("Agenda no encontrada con ID: 99"));

        mockMvc.perform(get("/api/agendas/99"))
                .andExpect(status().isNotFound());
    }

    // ───── DELETE /api/agendas/{id} ──────────────────────────────────────────────

    @Test
    void eliminar_debeRetornar204CuandoAgendaExiste() throws Exception {
        doNothing().when(agendaService).eliminar(1L);

        mockMvc.perform(delete("/api/agendas/1"))
                .andExpect(status().isNoContent());

        verify(agendaService, times(1)).eliminar(1L);
    }

    @Test
    void eliminar_debeRetornar404CuandoAgendaNoExiste() throws Exception {
        doThrow(new RuntimeException("No se puede eliminar: Agenda no encontrada"))
                .when(agendaService).eliminar(99L);

        mockMvc.perform(delete("/api/agendas/99"))
                .andExpect(status().isNotFound());
    }
}
