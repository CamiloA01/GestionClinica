package com.agenda.servicio_de_agenda.repository;

import com.agenda.servicio_de_agenda.model.Agenda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de repositorio: usa H2 en memoria (application-test.properties).
 * @DataJpaTest levanta solo la capa JPA, sin contexto web ni Feign.
 * Cada test hace rollback automático → la BD siempre queda limpia.
 */
@SuppressWarnings("null")
@DataJpaTest
@ActiveProfiles("test")
class AgendaRepositoryTest {

    @Autowired
    private AgendaRepository agendaRepository;

    private Agenda agendaBase;

    @BeforeEach
    void setUp() {
        agendaBase = new Agenda();
        agendaBase.setIdProfesional(1L);
        agendaBase.setEspecialidad("Cardiología");
        agendaBase.setDiaSemana("Lunes");
        agendaBase.setHoraInicio(LocalTime.of(8, 0));
        agendaBase.setHoraFin(LocalTime.of(17, 0));
        agendaBase.setDuracionCita(30);
        agendaBase.setActiva(true);
        agendaBase.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void debeGuardarYRecuperarAgendaPorId() {
        Agenda guardada = agendaRepository.save(agendaBase);

        Optional<Agenda> resultado = agendaRepository.findById(guardada.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEspecialidad()).isEqualTo("Cardiología");
        assertThat(resultado.get().getDiaSemana()).isEqualTo("Lunes");
    }

    @Test
    void debeRetornarAgendasPorIdProfesional() {
        agendaRepository.save(agendaBase);

        Agenda otraAgenda = new Agenda();
        otraAgenda.setIdProfesional(1L);
        otraAgenda.setEspecialidad("Neurología");
        otraAgenda.setDiaSemana("Martes");
        otraAgenda.setHoraInicio(LocalTime.of(9, 0));
        otraAgenda.setHoraFin(LocalTime.of(14, 0));
        otraAgenda.setDuracionCita(20);
        otraAgenda.setActiva(true);
        otraAgenda.setFechaCreacion(LocalDateTime.now());
        agendaRepository.save(otraAgenda);

        List<Agenda> agendas = agendaRepository.findByIdProfesional(1L);

        assertThat(agendas).hasSize(2);
        assertThat(agendas).extracting(Agenda::getEspecialidad)
                .containsExactlyInAnyOrder("Cardiología", "Neurología");
    }

    @Test
    void debeRetornarListaVaciaSiProfesionalNoTieneAgendas() {
        List<Agenda> agendas = agendaRepository.findByIdProfesional(999L);

        assertThat(agendas).isEmpty();
    }

    @Test
    void debeEliminarAgendaPorId() {
        Agenda guardada = agendaRepository.save(agendaBase);
        Long id = guardada.getId();

        agendaRepository.deleteById(id);

        assertThat(agendaRepository.findById(id)).isEmpty();
    }

    @Test
    void debeVerificarExistenciaDeunaAgenda() {
        Agenda guardada = agendaRepository.save(agendaBase);

        assertThat(agendaRepository.existsById(guardada.getId())).isTrue();
        assertThat(agendaRepository.existsById(999L)).isFalse();
    }

    @Test
    void debeListarTodasLasAgendas() {
        agendaRepository.save(agendaBase);

        Agenda segunda = new Agenda();
        segunda.setIdProfesional(2L);
        segunda.setEspecialidad("Pediatría");
        segunda.setDiaSemana("Miércoles");
        segunda.setHoraInicio(LocalTime.of(10, 0));
        segunda.setHoraFin(LocalTime.of(15, 0));
        segunda.setDuracionCita(15);
        segunda.setActiva(true);
        segunda.setFechaCreacion(LocalDateTime.now());
        agendaRepository.save(segunda);

        List<Agenda> todas = agendaRepository.findAll();

        assertThat(todas).hasSize(2);
    }
}
