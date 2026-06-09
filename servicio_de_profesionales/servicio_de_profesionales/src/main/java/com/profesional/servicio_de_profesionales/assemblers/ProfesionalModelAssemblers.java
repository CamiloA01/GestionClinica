package com.profesional.servicio_de_profesionales.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.profesional.servicio_de_profesionales.controller.*;
import com.profesional.servicio_de_profesionales.model.Profesional;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProfesionalModelAssemblers implements RepresentationModelAssembler<Profesional, EntityModel<Profesional>>{

    @Override
    public EntityModel<Profesional> toModel(Profesional profesional){
        return EntityModel.of(profesional,
            linkTo(methodOn((ProfesionalControllerV2.class).getobtenerTodos(profesional.getId())).withSelfRel);
        )
    }
}
