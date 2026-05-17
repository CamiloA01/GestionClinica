package com.agenda.servicio_de_agenda.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.agenda.servicio_de_agenda.model.Agenda;
import com.agenda.servicio_de_agenda.model.dto.AgendaRequestDTO;
import com.agenda.servicio_de_agenda.repository.AgendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;

    // Obtener todas las agendas activas
    public List<Agenda> obtenerTodas() {
        return agendaRepository.findAll();
    }

    // Guardar agenda con lógica de negocio
    public Agenda guardar(AgendaRequestDTO dto) {
        if (dto.getHoraInicio().isAfter(dto.getHoraFin())) {
            throw new RuntimeException("La hora de inicio no puede ser posterior a la hora de fin");
        }

        Agenda agenda = new Agenda();
        agenda.setIdProfesional(dto.getIdProfesional());
        agenda.setEspecialidad(dto.getEspecialidad());
        agenda.setDiaSemana(dto.getDiaSemana());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDuracionCita(dto.getDuracionCita());
        agenda.setActiva(true);
        agenda.setFechaCreacion(LocalDateTime.now());

        return agendaRepository.save(agenda);
    }

    // Obtener agenda por ID
    public Agenda obtenerPorId(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada con ID: " + id));
    }

    // Eliminar agenda por ID
    public void eliminar(Long id) {
        if (!agendaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Agenda no encontrada");
        }
        agendaRepository.deleteById(id);
    }

}
