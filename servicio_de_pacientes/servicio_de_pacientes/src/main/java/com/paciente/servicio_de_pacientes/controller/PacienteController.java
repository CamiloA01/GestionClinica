package com.paciente.servicio_de_pacientes.controller;

import com.paciente.servicio_de_pacientes.dto.*;
import com.paciente.servicio_de_pacientes.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/pacientes")   
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteServices pacienteServices;    

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> createPaciente(@Valid @RequestBody PacienteRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(pacienteServices.crearPaciente(requestDTO));
    }

    @PostMapping 
    public PacienteResponseDTO crearPaciente(@RequestBody PacienteRequestDTO requestDTO) {
        return pacienteServices.crearPaciente(requestDTO);
    }
    
    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        return ResponseEntity.ok(pacienteServices.getAllPacientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> getPacienteById(@PathVariable Long id) {
        return pacienteServices.BuscarPacientePorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build()); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> actualizarPaciente(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO requestDTO) {
        return pacienteServices.actualizarPaciente(id, requestDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacienteServices.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }       

    @GetMapping("/buscarPaciente")
    public ResponseEntity<List<PacienteResponseDTO>> buscarPacientePorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(pacienteServices.BuscarPacientePorNombre(nombre));
    }
}
