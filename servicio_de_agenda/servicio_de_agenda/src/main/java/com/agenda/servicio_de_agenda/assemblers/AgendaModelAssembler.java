package com.agenda.servicio_de_agenda.assemblers;

import com.agenda.servicio_de_agenda.model.Agenda;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class AgendaModelAssembler implements RepresentationModelAssembler <Agenda, EntityModel<Agenda>>{

    @Override
    public EntityModel<Agenda> toModel(Agenda agenda){
        return EntityModel.of(agenda, )
    }

}
