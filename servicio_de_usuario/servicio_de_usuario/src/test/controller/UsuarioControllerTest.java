package com.example.servicio_de_Ususarios.controller;

import com.example.servicio_de_Ususarios.assembler.ClinicaModelAssembler;
import com.example.servicio_de_Ususarios.dto.UsuarioRequestDTO;
import com.example.servicio_de_Ususarios.dto.UsuarioResponseDTO;
import com.example.servicio_de_Ususarios.service.UsuarioService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de la capa web de usuarioController usando MockMvc.
 * Se excluye la autoconfiguración de seguridad para aislar el test
 * del comportamiento de Spring Security (se evalúa por separado).
 */
@WebMvcTest(controllers = usuarioController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ClinicaModelAssembler assambler;

    private UsuarioResponseDTO responseDTO;

    private UsuarioResponseDTO buildResponseDTO() {
        return new UsuarioResponseDTO(1L, "jdoe", "jdoe@gmail.com", "USER", "ACTIVO");
    }

    @Test
    @DisplayName("GET /api/usuarios devuelve 200 y la lista de usuarios")
    void getAllUsuarios_devuelve200ConListado() throws Exception {
        responseDTO = buildResponseDTO();
        when(usuarioService.getAllUsuarios()).thenReturn(List.of(responseDTO));
        when(assambler.toModel(any(UsuarioResponseDTO.class)))
                .thenReturn(EntityModel.of(responseDTO));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioResponseDTOList[0].username").value("jdoe"));

        verify(usuarioService, times(1)).getAllUsuarios();
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} devuelve 200 cuando el usuario existe")
    void getUsuarioById_existente_devuelve200() throws Exception {
        responseDTO = buildResponseDTO();
        when(usuarioService.getUsuarioById(1L)).thenReturn(Optional.of(responseDTO));
        when(assambler.toModel(any(UsuarioResponseDTO.class)))
                .thenReturn(EntityModel.of(responseDTO));

        mockMvc.perform(get("/api/usuarios/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gmail").value("jdoe@gmail.com"));
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} devuelve 404 cuando el usuario no existe")
    void getUsuarioById_inexistente_devuelve404() throws Exception {
        when(usuarioService.getUsuarioById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/usuarios/registrar devuelve 201 con el usuario creado")
    void registrarUsuario_datosValidos_devuelve201() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO(
                "jdoe", "jdoe@gmail.com", "password123", "USER", "ACTIVO");
        responseDTO = buildResponseDTO();

        when(usuarioService.registrar(any(UsuarioRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("jdoe"));

        verify(usuarioService, times(1)).registrar(any(UsuarioRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} devuelve 400 si el body no es válido (campos en blanco)")
    void actualizarUsuario_datosInvalidos_devuelve400() throws Exception {
        UsuarioRequestDTO requestInvalido = new UsuarioRequestDTO(
                "", "", "", "", ""); // todos los campos están en @NotBlank

        mockMvc.perform(put("/api/usuarios/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).actualizarUsuario(any(), any());
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} devuelve 200 cuando la actualización es exitosa")
    void actualizarUsuario_datosValidos_devuelve200() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO(
                "jdoe", "jdoe@gmail.com", "password123", "USER", "ACTIVO");
        responseDTO = buildResponseDTO();

        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioRequestDTO.class)))
                .thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/usuarios/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jdoe"));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} devuelve 404 cuando el usuario no existe")
    void actualizarUsuario_usuarioInexistente_devuelve404() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO(
                "jdoe", "jdoe@gmail.com", "password123", "USER", "ACTIVO");

        when(usuarioService.actualizarUsuario(eq(99L), any(UsuarioRequestDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} devuelve 200 e invoca al service")
    void eliminarUsuario_devuelve200() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/{id}", 1L))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).eliminarUsuario(1L);
    }

    @Test
    @DisplayName("GET /api/usuarios/buscar?id= devuelve 200 con la lista de coincidencias")
    void buscarPorId_devuelve200() throws Exception {
        responseDTO = buildResponseDTO();
        when(usuarioService.buscarPorId(1L)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/usuarios/buscar").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("jdoe"));
    }
}
