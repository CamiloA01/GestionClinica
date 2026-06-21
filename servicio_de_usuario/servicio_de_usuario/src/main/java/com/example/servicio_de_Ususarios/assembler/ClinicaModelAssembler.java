package com.example.servicio_de_Ususarios.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;    

import com.example.servicio_de_Ususarios.dto.UsuarioResponseDTO;
import com.example.servicio_de_Ususarios.controller.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")
@Component
public class ClinicaModelAssembler implements RepresentationModelAssembler<UsuarioResponseDTO, EntityModel<UsuarioResponseDTO>> {

    @Override
    public EntityModel<UsuarioResponseDTO> toModel(UsuarioResponseDTO dto) {
        // Creamos el modelo HATEOAS envolviendo al DTO en lugar de la entidad
        return EntityModel.of(dto,
            // Link al recurso individual (asumiendo que corriges el endpoint /buscar/{id} o /{id})
            linkTo(methodOn(usuarioController.class).getUsuarioById(dto.getId())).withSelfRel(),
            // Link a la lista completa
            linkTo(methodOn(usuarioController.class).getAllUsuarios()).withRel("usuarios")
        );
    }
}