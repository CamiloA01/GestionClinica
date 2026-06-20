package com.pago.servicio_de_pagos.assemblers;

import com.pago.servicio_de_pagos.model.Pago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler <Pago, EntityModel<Pago>> {

    @Override
    public EntityModel<Pago>  toModel(Pago pago) {return Entity}
}
