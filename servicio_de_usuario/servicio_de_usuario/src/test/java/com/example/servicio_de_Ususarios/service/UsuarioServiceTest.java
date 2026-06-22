package com.example.servicio_de_Ususarios.service;

import com.example.servicio_de_Ususarios.dto.UsuarioRequestDTO;
import com.example.servicio_de_Ususarios.dto.UsuarioResponseDTO;
import com.example.servicio_de_Ususarios.model.Usuario;
import com.example.servicio_de_Ususarios.repository.usuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de UsuarioService.
 * Se mockean el repositorio y el PasswordEncoder para no depender de
 * base de datos ni de la implementación real de BCrypt.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private usuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(
                1L,
                "jdoe",
                "jdoe@gmail.com",
                "passwordEncriptado",
                "USER",
                "ACTIVO"
        );

        requestDTO = new UsuarioRequestDTO(
                "jdoe",
                "jdoe@gmail.com",
                "password123",
                "USER",
                "ACTIVO"
        );
    }

    @Test
    @DisplayName("getAllUsuarios debe devolver la lista de usuarios mapeada a DTO")
    void getAllUsuarios_devuelveListaDeDTOs() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.getAllUsuarios();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsername()).isEqualTo("jdoe");
        assertThat(resultado.get(0).getGmail()).isEqualTo("jdoe@gmail.com");
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllUsuarios debe devolver lista vacía si no hay usuarios")
    void getAllUsuarios_listaVacia() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<UsuarioResponseDTO> resultado = usuarioService.getAllUsuarios();

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("getUsuarioById debe devolver el DTO cuando el usuario existe")
    void getUsuarioById_usuarioExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<UsuarioResponseDTO> resultado = usuarioService.getUsuarioById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getUsuarioById debe devolver Optional vacío cuando el usuario no existe")
    void getUsuarioById_usuarioNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UsuarioResponseDTO> resultado = usuarioService.getUsuarioById(99L);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("registrar debe encriptar la contraseña y guardar el usuario")
    void registrar_usuarioNuevo_seGuardaConPasswordEncriptado() {
        when(usuarioRepository.existsByGmail("jdoe@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("HASH_BCRYPT");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponseDTO resultado = usuarioService.registrar(requestDTO);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getUsername()).isEqualTo("jdoe");

        // Verificamos explícitamente que se guardó la contraseña ENCRIPTADA y no la original
        verify(usuarioRepository).save(argThat(u -> u.getPassword().equals("HASH_BCRYPT")));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    @DisplayName("registrar debe lanzar excepción si el gmail ya está registrado")
    void registrar_gmailDuplicado_lanzaExcepcion() {
        when(usuarioRepository.existsByGmail("jdoe@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.registrar(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya existe");

        // No debe intentar guardar si el usuario ya existe
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("actualizarUsuario debe encriptar la nueva contraseña al actualizar")
    void actualizarUsuario_usuarioExiste_actualizaYEncripta() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("password123")).thenReturn("NUEVO_HASH");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<UsuarioResponseDTO> resultado = usuarioService.actualizarUsuario(1L, requestDTO);

        assertThat(resultado).isPresent();
        verify(usuarioRepository).save(argThat(u -> u.getPassword().equals("NUEVO_HASH")));
    }

    @Test
    @DisplayName("actualizarUsuario debe devolver Optional vacío si el usuario no existe")
    void actualizarUsuario_usuarioNoExiste_devuelveVacio() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UsuarioResponseDTO> resultado = usuarioService.actualizarUsuario(99L, requestDTO);

        assertThat(resultado).isEmpty();
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("eliminarUsuario debe invocar deleteById con el id correcto")
    void eliminarUsuario_invocaDeleteById() {
        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("buscarPorId debe devolver lista con un elemento si el usuario existe")
    void buscarPorId_usuarioExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorId(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("buscarPorId debe devolver lista vacía si el usuario no existe")
    void buscarPorId_usuarioNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorId(99L);

        assertThat(resultado).isEmpty();
    }
}
