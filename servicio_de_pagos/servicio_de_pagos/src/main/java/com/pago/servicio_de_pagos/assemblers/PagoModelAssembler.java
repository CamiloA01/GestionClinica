package com.pago.servicio_de_pagos.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.pago.servicio_de_pagos.controller.*;
import com.pago.servicio_de_pagos.dto.PagoResponceDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")
@Component
public class PagoModelAssembler implements RepresentationModelAssembler<PagoResponceDTO, EntityModel<PagoResponceDTO>> {

    @Override
    public EntityModel<PagoResponceDTO> toModel(PagoResponceDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(pagoController.class).obtenerPagoPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(pagoController.class).obtenerPagos()).withRel("pagos")
        );
    }
}
