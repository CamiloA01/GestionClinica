package com.example.servicio_de_Ususarios.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;    

import com.example.servicio_de_Ususarios.controller.*;
import com.example.servicio_de_Ususarios.model.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ClinicaModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario> {
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario){
        return EntityModel.of(usuario,
                linkTo(methodOn(usuarioController.class).getAllUsuarios()).withRel("carreras"));
        
    }

}
