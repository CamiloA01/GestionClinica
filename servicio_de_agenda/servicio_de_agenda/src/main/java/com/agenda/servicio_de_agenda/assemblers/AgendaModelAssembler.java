package com.agenda.servicio_de_agenda.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.agenda.servicio_de_agenda.model.dto.AgendaResponseDTO;

@Component
public class AgendaModelAssembler implements RepresentationModelAssembler <AgendaResponseDTO, EntityModel<AgendaResponseDTO>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<AgendaResponseDTO> toModel(AgendaResponseDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(com.agenda.servicio_de_agenda.controller.AgendaController.class).buscarPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(com.agenda.servicio_de_agenda.controller.AgendaController.class).listarAgendas()).withRel("agendas")
        );
    }

}
