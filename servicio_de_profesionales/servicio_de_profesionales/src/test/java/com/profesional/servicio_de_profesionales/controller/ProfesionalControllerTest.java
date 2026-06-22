package com.profesional.servicio_de_profesionales.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profesional.servicio_de_profesionales.dto.ProfesionalRequestDTO;
import com.profesional.servicio_de_profesionales.dto.ProfesionalResponseDTO;
import com.profesional.servicio_de_profesionales.service.ProfesionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ProfesionalController.class)
@WithMockUser
public class ProfesionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesionalService profesionalService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfesionalResponseDTO responseDTO;
    private ProfesionalRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new ProfesionalResponseDTO(1L, 10L, "Juan", "Pérez", "González",
                "12345678-9", "Médico Cirujano", LocalDate.of(2023, 3, 15));

        requestDTO = new ProfesionalRequestDTO(10L, "Juan", "Pérez", "González",
                "12345678-9", "Médico Cirujano", LocalDate.of(2023, 3, 15));
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(profesionalService.obtenerTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/profesional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].run").value("12345678-9"));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(profesionalService.obtenerPorId(1L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/profesional/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.titulo").value("Médico Cirujano"));
    }

    @Test
    public void testObtenerPorId_noEncontrado() throws Exception {
        when(profesionalService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profesional/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrear() throws Exception {
        when(profesionalService.guardar(any(ProfesionalRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/profesional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    public void testActualizar() throws Exception {
        when(profesionalService.actualizar(eq(1L), any(ProfesionalRequestDTO.class)))
                .thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/profesional/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Médico Cirujano"));
    }

    @Test
    public void testEliminar() throws Exception {
        when(profesionalService.obtenerPorId(1L)).thenReturn(Optional.of(responseDTO));
        doNothing().when(profesionalService).eliminar(1L);

        mockMvc.perform(delete("/api/profesional/1"))
                .andExpect(status().isNoContent());

        verify(profesionalService, times(1)).eliminar(1L);
    }

    @Test
    public void testEliminar_noEncontrado() throws Exception {
        when(profesionalService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/profesional/99"))
                .andExpect(status().isNotFound());
    }
}
