package com.notificacion.servicio_de_notificaciones.repository;

import com.notificacion.model.Notificacion;
import com.notificacion.repository.notificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class NotificacionRepositoryTest {

    @Autowired
    private notificacionRepository notificacionRepository;  // ← nombre exacto del repositorio

    private Notificacion notificacionBase;

    @BeforeEach
    void setUp() {
        notificacionBase = new Notificacion();
        notificacionBase.setIdUsuario(1L);
        notificacionBase.setTitulo("Cita confirmada");
        notificacionBase.setMensaje("Su cita ha sido confirmada para el día lunes");
        notificacionBase.setTipo("CITA");
        notificacionBase.setEstado("ENVIADA");
        notificacionBase.setFechaEnvio(LocalDateTime.now());
    }

    @Test
    void debeGuardarYRecuperarNotificacionPorId() {
        Notificacion guardada = notificacionRepository.save(notificacionBase);

        Optional<Notificacion> resultado = notificacionRepository.findById(guardada.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Cita confirmada");
        assertThat(resultado.get().getEstado()).isEqualTo("ENVIADA");
    }

    @Test
    void debeBuscarNotificacionesPorIdUsuario() {
        notificacionRepository.save(notificacionBase);

        Notificacion segunda = new Notificacion();
        segunda.setIdUsuario(1L);
        segunda.setTitulo("Recordatorio");
        segunda.setMensaje("Recuerde su cita mañana");
        segunda.setTipo("RECORDATORIO");
        segunda.setEstado("ENVIADA");
        segunda.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(segunda);

        List<Notificacion> resultado = notificacionRepository.findByIdUsuario(1L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Notificacion::getIdUsuario).containsOnly(1L);
    }

    @Test
    void debeRetornarListaVaciaSiUsuarioNoTieneNotificaciones() {
        List<Notificacion> resultado = notificacionRepository.findByIdUsuario(999L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void debeEliminarNotificacionPorId() {
        Notificacion guardada = notificacionRepository.save(notificacionBase);
        Long id = guardada.getId();

        notificacionRepository.deleteById(id);

        assertThat(notificacionRepository.findById(id)).isEmpty();
    }

    @Test
    void debeListarTodasLasNotificaciones() {
        notificacionRepository.save(notificacionBase);

        Notificacion segunda = new Notificacion();
        segunda.setIdUsuario(2L);
        segunda.setTitulo("Pago recibido");
        segunda.setMensaje("Su pago ha sido procesado");
        segunda.setTipo("PAGO");
        segunda.setEstado("ENVIADA");
        segunda.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(segunda);

        List<Notificacion> todas = notificacionRepository.findAll();

        assertThat(todas).hasSize(2);
    }

    @Test
    void debeVerificarExistenciaDeNotificacion() {
        Notificacion guardada = notificacionRepository.save(notificacionBase);

        assertThat(notificacionRepository.existsById(guardada.getId())).isTrue();
        assertThat(notificacionRepository.existsById(999L)).isFalse();
    }
}