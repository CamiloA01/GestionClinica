package com.agenda.servicio_de_agenda.controller;

import com.agenda.servicio_de_agenda.assemblers.AgendaModelAssembler;
import com.agenda.servicio_de_agenda.model.Agenda;
import com.agenda.servicio_de_agenda.model.dto.AgendaRequestDTO;
import com.agenda.servicio_de_agenda.service.AgendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/agendas")
public class AgendaControllerV2 {

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private AgendaModelAssembler agendaModelAssembler;

    @GetMapping
    public ResponseEntity<List<Agenda>> listarAgendas() {
        return ResponseEntity.ok(agendaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<Agenda> crearAgenda(@Valid @RequestBody AgendaRequestDTO dto) {
        return new ResponseEntity<>(agendaService.guardar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        agendaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
