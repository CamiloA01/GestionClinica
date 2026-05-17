package com.notificacion.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.notificacion.model.Notificacion;
import com.notificacion.model.dto.NotificacionResponseDTO;
import com.notificacion.model.dto.NotificacionRequestDTO;
import com.notificacion.repository.NotificacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    //MAPEO PRIVADO ENTRE ENTIDAD Y ResponseDTO
    private NotificacionResponseDTO mapToDTO(Notificacion notificacion){
        return new NotificacionResponseDTO(
            notificacion.getId(),
            notificacion.getIdUsuario(),
            notificacion.getTitulo(),
            notificacion.getMensaje(),
            notificacion.getTipo(),
            notificacion.getEstado(),
            notificacion.getFechaEnvio(),
            notificacion.getFechaLectura()
        );
    }

    //OBTENER TODAS LAS NOTIFICAIONES
    public List<NotificacionResponseDTO> obtenerTodas(){
        return notificacionRepository.findAll()
        .stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    //OBTENER POR ID
    public NotificacionResponseDTO obtenerPorId(Long id){
    Notificacion noti = notificacionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

    return mapToDTO(noti);
    }

    //OBTENER POR USUARIO
    public List<NotificacionResponseDTO> obtenerPorUsuarioId(Long usuarioId){
        return notificacionRepository.findByIdUsuario(usuarioId)
        .stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    //GUARDAR NOTIFICACIÓN
    public NotificacionResponseDTO guardar (NotificacionRequestDTO dto){
        Notificacion notificacion = new Notificacion();

        notificacion.setIdUsuario(dto.getUsuarioId());
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());

        // Por defecto, el estado de la notificación se establece como "Enviado"
        notificacion.setEstado("ENVIADA");
        notificacion.setFechaEnvio(LocalDateTime.now());


        notificacion = notificacionRepository.save(notificacion);
        return mapToDTO(notificacion);
    }

    //ELIMINAR NOTIFICACIÓN
    public void eliminar(Long id) {
        // 1. Verificamos si existe antes de hacer nada
        if (!notificacionRepository.existsById(id)) {
            // 2. Si no existe, lanzamos la excepción que tu GlobalExceptionHandler ya sabe leer
            throw new RuntimeException("No se encontró la notificación con ID: " + id);
        }
        // 3. Si existe, procedemos a borrar
        notificacionRepository.deleteById(id);
    }

    //MARCAR COMO LEÍDA
    public NotificacionResponseDTO marcarComoLeida(Long id){
        Notificacion noti = notificacionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        noti.setEstado("LEIDA");
        noti.setFechaLectura(LocalDateTime.now());
        return mapToDTO(notificacionRepository.save(noti));
    }

}
