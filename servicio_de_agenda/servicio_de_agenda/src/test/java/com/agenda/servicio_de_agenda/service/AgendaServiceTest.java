package com.agenda.servicio_de_agenda.service;

import com.agenda.servicio_de_agenda.agendaClient.NotificacionClient;
import com.agenda.servicio_de_agenda.model.Agenda;
import com.agenda.servicio_de_agenda.model.dto.AgendaRequestDTO;
import com.agenda.servicio_de_agenda.repository.AgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test de servicio: no toca la BD ni Feign, todo está mockeado.
 * Se enfoca en la lógica de negocio pura de AgendaService.
 */
@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private NotificacionClient notificacionClient; // Mockeamos Feign para no necesitar el microservicio

    @InjectMocks
    private AgendaService agendaService;

    private AgendaRequestDTO dtoValido;
    private Agenda agendaMock;

    @BeforeEach
    void setUp() {
        dtoValido = new AgendaRequestDTO();
        dtoValido.setIdProfesional(1L);
        dtoValido.setEspecialidad("Cardiología");
        dtoValido.setDiaSemana("Lunes");
        dtoValido.setHoraInicio(LocalTime.of(8, 0));
        dtoValido.setHoraFin(LocalTime.of(17, 0));
        dtoValido.setDuracionCita(30);

        agendaMock = new Agenda();
        agendaMock.setId(1L);
        agendaMock.setIdProfesional(1L);
        agendaMock.setEspecialidad("Cardiología");
        agendaMock.setDiaSemana("Lunes");
        agendaMock.setHoraInicio(LocalTime.of(8, 0));
        agendaMock.setHoraFin(LocalTime.of(17, 0));
        agendaMock.setDuracionCita(30);
        agendaMock.setActiva(true);
    }

    // ───── guardar ─────────────────────────────────────────────────────────────

    @Test
    void guardar_debeRetornarAgendaGuardadaConDatosCorrectos() {
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agendaMock);

        Agenda resultado = agendaService.guardar(dtoValido);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEspecialidad()).isEqualTo("Cardiología");
        assertThat(resultado.getActiva()).isTrue();
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    void guardar_debeLanzarExcepcionCuandoHoraInicioEsPosteriorAHoraFin() {
        dtoValido.setHoraInicio(LocalTime.of(18, 0)); // inicio DESPUÉS del fin
        dtoValido.setHoraFin(LocalTime.of(8, 0));

        assertThatThrownBy(() -> agendaService.guardar(dtoValido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("La hora de inicio no puede ser posterior a la hora de fin");

        verify(agendaRepository, never()).save(any()); // nunca debe guardar
    }

    @Test
    void guardar_debeSeguirFuncionandoSiNotificacionClienteFalla() {
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agendaMock);
        doThrow(new RuntimeException("MS Notificaciones caído"))
                .when(notificacionClient).enviarAlerta(any());

        // El servicio no debe propagar la excepción de Feign
        Agenda resultado = agendaService.guardar(dtoValido);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
    }

    // ───── obtenerTodas ─────────────────────────────────────────────────────────

    @Test
    void obtenerTodas_debeRetornarListaDeAgendas() {
        when(agendaRepository.findAll()).thenReturn(List.of(agendaMock));

        List<Agenda> resultado = agendaService.obtenerTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEspecialidad()).isEqualTo("Cardiología");
    }

    @Test
    void obtenerTodas_debeRetornarListaVaciaSiNoHayAgendas() {
        when(agendaRepository.findAll()).thenReturn(List.of());

        List<Agenda> resultado = agendaService.obtenerTodas();

        assertThat(resultado).isEmpty();
    }

    // ───── obtenerPorId ─────────────────────────────────────────────────────────

    @Test
    void obtenerPorId_debeRetornarAgendaCuandoExiste() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agendaMock));

        Agenda resultado = agendaService.obtenerPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void obtenerPorId_debeLanzarExcepcionCuandoNoExiste() {
        when(agendaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> agendaService.obtenerPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Agenda no encontrada con ID: 99");
    }

    // ───── eliminar ─────────────────────────────────────────────────────────────

    @Test
    void eliminar_debeEliminarCuandoAgendaExiste() {
        when(agendaRepository.existsById(1L)).thenReturn(true);

        agendaService.eliminar(1L);

        verify(agendaRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_debeLanzarExcepcionCuandoAgendaNoExiste() {
        when(agendaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> agendaService.eliminar(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No se puede eliminar: Agenda no encontrada");

        verify(agendaRepository, never()).deleteById(any());
    }
}
