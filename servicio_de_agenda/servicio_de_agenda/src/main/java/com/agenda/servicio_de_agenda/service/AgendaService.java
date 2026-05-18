package com.agenda.servicio_de_agenda.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.agenda.servicio_de_agenda.agendaClient.NotificacionClient; // Importamos el cliente
import com.agenda.servicio_de_agenda.model.Agenda;
import com.agenda.servicio_de_agenda.model.dto.AgendaRequestDTO;
import com.agenda.servicio_de_agenda.model.dto.NotificacionRequest; // Importamos el DTO que creaste
import com.agenda.servicio_de_agenda.repository.AgendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Opcional: para ver logs si falla el envío

@Service
@RequiredArgsConstructor
@Slf4j // Agregamos esto para poder usar log.error
@SuppressWarnings("all")// Supresión de advertencias para simplificar el ejemplo
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final NotificacionClient notificacionClient; // Inyectamos el cliente Feign

    // Obtener todas las agendas activas
    public List<Agenda> obtenerTodas() {
        return agendaRepository.findAll();
    }

    // Guardar agenda con lógica de negocio e integración Feign
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

        // 1. Guardamos en nuestra base de datos primero
        Agenda agendaGuardada = agendaRepository.save(agenda);

        // 2. Intentamos enviar la notificación (Lógica Feign)
        try {
            NotificacionRequest aviso = new NotificacionRequest();
            // Adaptamos los datos de Agenda al formato que espera Notificaciones
            aviso.setUsuarioId(dto.getIdProfesional()); // Usamos el ID del profesional como destinatario
            aviso.setTitulo("Nueva Agenda Creada");
            aviso.setMensaje("Se ha registrado una nueva agenda de " + dto.getEspecialidad() + 
            " para los días " + dto.getDiaSemana());
            aviso.setTipo("SISTEMA");

            notificacionClient.enviarAlerta(aviso);
            
        } catch (Exception e) {
            // Importante: No dejamos que un error en notificaciones bloquee la creación de la agenda
            log.error("Error al comunicar con MS Notificaciones via Feign: {}", e.getMessage());
        }

        return agendaGuardada;
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
