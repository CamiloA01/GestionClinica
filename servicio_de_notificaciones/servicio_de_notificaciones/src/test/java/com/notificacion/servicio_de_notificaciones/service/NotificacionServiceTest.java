package com.notificacion.servicio_de_notificaciones.service;

import com.notificacion.model.Notificacion;
import com.notificacion.model.dto.NotificacionRequestDTO;
import com.notificacion.model.dto.NotificacionResponseDTO;
import com.notificacion.repository.notificacionRepository;
import com.notificacion.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private notificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacionMock;
    private NotificacionRequestDTO dtoValido;

    @BeforeEach
    void setUp() {
        notificacionMock = new Notificacion();
        notificacionMock.setId(1L);
        notificacionMock.setIdUsuario(1L);
        notificacionMock.setTitulo("Cita confirmada");
        notificacionMock.setMensaje("Su cita ha sido confirmada");
        notificacionMock.setTipo("CITA");
        notificacionMock.setEstado("ENVIADA");
        notificacionMock.setFechaEnvio(LocalDateTime.now());

        dtoValido = new NotificacionRequestDTO();
        dtoValido.setUsuarioId(1L);
        dtoValido.setTitulo("Cita confirmada");
        dtoValido.setMensaje("Su cita ha sido confirmada");
        dtoValido.setTipo("CITA");
    }

    @Test
    void obtenerTodas_debeRetornarListaDeNotificaciones() {
        when(notificacionRepository.findAll()).thenReturn(List.of(notificacionMock));

        List<NotificacionResponseDTO> resultado = notificacionService.obtenerTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Cita confirmada");
    }

    @Test
    void obtenerTodas_debeRetornarListaVaciaSiNoHayNotificaciones() {
        when(notificacionRepository.findAll()).thenReturn(List.of());

        List<NotificacionResponseDTO> resultado = notificacionService.obtenerTodas();

        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerPorId_debeRetornarNotificacionCuandoExiste() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionMock));

        NotificacionResponseDTO resultado = notificacionService.obtenerPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("ENVIADA");
    }

    @Test
    void obtenerPorId_debeLanzarExcepcionCuandoNoExiste() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.obtenerPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Notificación no encontrada");
    }

    @Test
    void obtenerPorUsuarioId_debeRetornarNotificacionesDelUsuario() {
        when(notificacionRepository.findByIdUsuario(1L)).thenReturn(List.of(notificacionMock));

        List<NotificacionResponseDTO> resultado = notificacionService.obtenerPorUsuarioId(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsuarioId()).isEqualTo(1L);
    }

    @Test
    void guardar_debeAsignarEstadoENVIADAYFechaEnvioAutomaticamente() {
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionMock);

        NotificacionResponseDTO resultado = notificacionService.guardar(dtoValido);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("ENVIADA");
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void guardar_debeMapearCorrectamenteLosCamposDelDTO() {
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionMock);

        NotificacionResponseDTO resultado = notificacionService.guardar(dtoValido);

        assertThat(resultado.getTitulo()).isEqualTo("Cita confirmada");
        assertThat(resultado.getMensaje()).isEqualTo("Su cita ha sido confirmada");
    }

    @Test
    void eliminar_debeEliminarCuandoNotificacionExiste() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);

        notificacionService.eliminar(1L);

        verify(notificacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_debeLanzarExcepcionCuandoNoExiste() {
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> notificacionService.eliminar(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No se encontró la notificación con ID: 99");

        verify(notificacionRepository, never()).deleteById(any());
    }

    @Test
    void marcarComoLeida_debeActualizarEstadoALEIDA() {
        Notificacion notiParaLeer = new Notificacion();
        notiParaLeer.setId(1L);
        notiParaLeer.setIdUsuario(1L);
        notiParaLeer.setTitulo("Cita confirmada");
        notiParaLeer.setMensaje("Su cita ha sido confirmada");
        notiParaLeer.setTipo("CITA");
        notiParaLeer.setEstado("ENVIADA");
        notiParaLeer.setFechaEnvio(LocalDateTime.now());

        Notificacion notiLeida = new Notificacion();
        notiLeida.setId(1L);
        notiLeida.setIdUsuario(1L);
        notiLeida.setTitulo("Cita confirmada");
        notiLeida.setMensaje("Su cita ha sido confirmada");
        notiLeida.setTipo("CITA");
        notiLeida.setEstado("LEIDA");
        notiLeida.setFechaEnvio(LocalDateTime.now());
        notiLeida.setFechaLectura(LocalDateTime.now());

        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notiParaLeer));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notiLeida);

        NotificacionResponseDTO resultado = notificacionService.marcarComoLeida(1L);

        assertThat(resultado.getEstado()).isEqualTo("LEIDA");
        assertThat(resultado.getFechaLectura()).isNotNull();
    }

    @Test
    void marcarComoLeida_debeLanzarExcepcionSiNoExiste() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.marcarComoLeida(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Notificación no encontrada");
    }
}