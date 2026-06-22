package com.profesional.servicio_de_profesionales.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.profesional.servicio_de_profesionales.controller.ProfecionalControllerV2;
import com.profesional.servicio_de_profesionales.model.Profesional;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProfesionalModelAssemblers implements RepresentationModelAssembler<Profesional, EntityModel<Profesional>> {

    @Override
    public EntityModel<Profesional> toModel(Profesional profesional) {
        Link selfLink = linkTo(methodOn(ProfecionalControllerV2.class)
                .obtenerPorId(profesional.getId())).withSelfRel();
        Link coleccionLink = Link.of("/api/v2/profesional").withRel("profesionales");
        return EntityModel.of(profesional, selfLink, coleccionLink);
    }
}
