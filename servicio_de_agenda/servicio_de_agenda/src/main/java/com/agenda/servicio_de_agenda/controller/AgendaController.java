package com.agenda.servicio_de_agenda.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenda.servicio_de_agenda.model.Agenda;
import com.agenda.servicio_de_agenda.model.dto.AgendaRequestDTO;
import com.agenda.servicio_de_agenda.service.AgendaService;

// Nuevas importaciones para la documentación con Swagger (OpenAPI)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
@Tag(name = "Agenda", description = "Operaciones de gestión y control de las agendas médicas de los profesionales") // Etiqueta principal
public class AgendaController {

    private final AgendaService agendaService;

    @Operation(summary = "Listar todas las agendas", description = "Recupera una lista completa de todas las agendas registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Listado de agendas obtenido exitosamente")
    @GetMapping
    public ResponseEntity<List<Agenda>> listarAgendas() {
        return ResponseEntity.ok(agendaService.obtenerTodas());
    }

    @Operation(summary = "Crear nueva agenda", description = "Crea y guarda una nueva agenda en el sistema validando los datos enviados en el DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agenda creada exitosamente en la base de datos"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida (Faltan datos o no cumplen las validaciones)")
    })
    @PostMapping
    public ResponseEntity<Agenda> crearAgenda(@Valid @RequestBody AgendaRequestDTO dto) {
        return new ResponseEntity<>(agendaService.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar agenda por ID", description = "Busca una agenda específica utilizando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agenda encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "La agenda con el ID proporcionado no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.obtenerPorId(id));
    }

    @Operation(summary = "Eliminar agenda", description = "Elimina físicamente una agenda de la base de datos utilizando su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Agenda eliminada exitosamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "La agenda que se intenta eliminar no fue encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        agendaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}