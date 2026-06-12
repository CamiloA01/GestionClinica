package com.reserva.servicio_de_reserva.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;    


import com.reserva.servicio_de_reserva.dto.*;
import com.reserva.servicio_de_reserva.controller.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ClinicaModelAssembler implements RepresentationModelAssembler<ReservaResponseDTO, EntityModel<ReservaResponseDTO>> {

    @Override
    public EntityModel<ReservaResponseDTO> toModel(ReservaResponseDTO dto) {
        // Creamos el modelo HATEOAS envolviendo al DTO en lugar de la entidad
        return EntityModel.of(dto,
            // Link al recurso individual (asumiendo que corriges el endpoint /buscar/{id} o /{id})
            linkTo(methodOn(ReservaController.class).obtenerReservaPorId(dto.getId())).withSelfRel(),
            // Link a la lista completa
            linkTo(methodOn(ReservaController.class).obtenerTodasLasReservas()).withRel("reservas")
        );
    }
}
