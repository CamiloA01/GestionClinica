package com.profesional.servicio_de_profesionales.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.profesional.servicio_de_profesionales.controller.ProfecionalControllerV2;
import com.profesional.servicio_de_profesionales.dto.ProfesionalResponseDTO; // 1. IMPORTANTE: Importar tu DTO
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
// 2. CORREGIDO: Cambiados los genéricos de <Profesional> a <ProfesionalResponseDTO>
public class ProfesionalModelAssemblers implements RepresentationModelAssembler<ProfesionalResponseDTO, EntityModel<ProfesionalResponseDTO>> {

    @Override
    // 3. CORREGIDO: El método ahora recibe y devuelve el DTO estructurado
    public EntityModel<ProfesionalResponseDTO> toModel(ProfesionalResponseDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(ProfecionalControllerV2.class).obtenerPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(ProfecionalControllerV2.class).obtenerTodos()).withRel("profesionales")
        );
    }
}