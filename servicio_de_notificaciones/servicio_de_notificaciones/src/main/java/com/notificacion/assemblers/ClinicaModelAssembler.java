package com.notificacion.assemblers;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;    

import com.notificacion.controller.*;
import com.notificacion.model.dto.NotificacionResponseDTO;
import org.springframework.hateoas.EntityModel; 
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")
@Component
public class ClinicaModelAssembler implements RepresentationModelAssembler<NotificacionResponseDTO, EntityModel<NotificacionResponseDTO>> {

    @Override
    public EntityModel<NotificacionResponseDTO> toModel(NotificacionResponseDTO dto) {
        // Creamos el modelo HATEOAS envolviendo al DTO en lugar de la entidad
        return EntityModel.of(dto,
            // Link al recurso individual (asumiendo que corriges el endpoint /buscar/{id} o /{id})
            linkTo(methodOn(NotificacionController.class).obtenerNotificacionPorId(dto.getId())).withSelfRel(),
            // Link a la lista completa
            linkTo(methodOn(NotificacionController.class).obtenerNotificaciones()).withRel("notificaciones")
        );
    }
}
