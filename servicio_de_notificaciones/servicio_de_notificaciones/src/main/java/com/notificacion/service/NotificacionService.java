package com.notificacion.service;

import java.util.List;

import com.notificacion.model.Notificacion;
import com.notificacion.model.dto.NotificacionResponseDTO;
import com.notificacion.model.dto.notificacionRequestDTO;

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
        .stream().map(this::mapToDTO).collect(collectors.toList());
    }

    //OBTENER POR ID
    public Optional<NotificacionResponseDTO> obtenerPorId(Long id){
        return notificacionRepository.findById(id).map(this::mapToDTO);
    }

    //GUARDAR NOTIFICACIÓN
    public NotificacionResponseDTO guardar (notificacionRequestDTO dto){
        Notificacion notificacion = notificacionRepository.findById(dto.)
    }

}
