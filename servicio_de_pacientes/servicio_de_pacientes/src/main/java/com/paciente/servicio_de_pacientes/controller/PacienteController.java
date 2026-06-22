package com.paciente.servicio_de_pacientes.controller;

import com.paciente.servicio_de_pacientes.dto.*;
import com.paciente.servicio_de_pacientes.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pacientes")   
@RequiredArgsConstructor
@Tag(name = "Paciente Controller", description = "Endpoints para gestionar pacientes")
public class PacienteController {

    private final PacienteServices pacienteServices;    

    @PostMapping
    @Operation(summary = "Crear un nuevo paciente", description = "Crea un nuevo paciente en el sistema")
    public ResponseEntity<PacienteResponseDTO> createPaciente(@Valid @RequestBody PacienteRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(pacienteServices.crearPaciente(requestDTO));
    }

    
    
    @GetMapping
    @Operation(summary = "Obtener todos los pacientes", description = "Devuelve una lista de todos los pacientes en el sistema")
    public ResponseEntity<?> getAllUsuarios() {
        return ResponseEntity.ok(pacienteServices.getAllPacientes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un paciente por ID", description = "Devuelve la información de un paciente específico por su ID")
    public ResponseEntity<PacienteResponseDTO> getPacienteById(@PathVariable Long id) {
        return pacienteServices.BuscarPacientePorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build()); 
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un paciente", description = "Actualiza la información de un paciente existente por su ID")
    public ResponseEntity<PacienteResponseDTO> actualizarPaciente(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO requestDTO) {
        return pacienteServices.actualizarPaciente(id, requestDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un paciente", description = "Elimina un paciente existente por su ID")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacienteServices.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }       

    @GetMapping("/buscarPaciente")
    @Operation(summary = "Buscar paciente por nombre", description = "Busca pacientes por su nombre y devuelve una lista de coincidencias")
    public ResponseEntity<List<PacienteResponseDTO>> buscarPacientePorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(pacienteServices.BuscarPacientePorNombre(nombre));
    }
}
