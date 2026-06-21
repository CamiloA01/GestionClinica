package com.agenda.servicio_de_agenda.controller;

import com.agenda.servicio_de_agenda.assemblers.AgendaModelAssembler;
import com.agenda.servicio_de_agenda.model.Agenda;
import com.agenda.servicio_de_agenda.model.dto.AgendaRequestDTO;
import com.agenda.servicio_de_agenda.model.dto.AgendaResponseDTO;
import com.agenda.servicio_de_agenda.service.AgendaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@SuppressWarnings("null")
@RestController
@RequestMapping("/api/v2/agendas")
public class AgendaControllerV2 {


    @Autowired
    private AgendaService agendaService;


    @Autowired
    private AgendaModelAssembler agendaModelAssembler;



    // LISTAR TODAS LAS AGENDAS
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<AgendaResponseDTO>> listarAgendas() {


        List<EntityModel<AgendaResponseDTO>> agendas =
                agendaService.obtenerTodas()
                .stream()
                .map(this::convertirDTO)
                .map(agendaModelAssembler::toModel)
                .toList();


        return CollectionModel.of(
                agendas,
                linkTo(
                    methodOn(AgendaControllerV2.class)
                    .listarAgendas()
                ).withSelfRel()
        );
    }





    // CREAR AGENDA
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AgendaResponseDTO>> crearAgenda(
            @Valid @RequestBody AgendaRequestDTO dto) {


        Agenda nuevaAgenda = agendaService.guardar(dto);


        AgendaResponseDTO respuesta = convertirDTO(nuevaAgenda);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    agendaModelAssembler.toModel(respuesta)
                );
    }





    // BUSCAR POR ID
    @GetMapping(value="/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AgendaResponseDTO>> buscarPorId(
            @PathVariable Long id) {


        Agenda agenda = agendaService.obtenerPorId(id);


        AgendaResponseDTO respuesta = convertirDTO(agenda);


        return ResponseEntity.ok(
                agendaModelAssembler.toModel(respuesta)
        );
    }






    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {


        agendaService.eliminar(id);


        return ResponseEntity.noContent().build();
    }





    // CONVERSOR ENTITY -> DTO
    private AgendaResponseDTO convertirDTO(Agenda agenda) {
        return new AgendaResponseDTO(
                agenda.getId(),
                agenda.getIdProfesional(),
                agenda.getEspecialidad(),
                agenda.getDiaSemana(),
                agenda.getHoraInicio(),
                agenda.getHoraFin(),
                agenda.getDuracionCita());
    }
}