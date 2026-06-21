package com.ficha_clinica.servicio_de_ficha_clinica.controller;

import com.ficha_clinica.servicio_de_ficha_clinica.model.Ficha;
import com.ficha_clinica.servicio_de_ficha_clinica.model.dto.FichaRequestDTO;
import com.ficha_clinica.servicio_de_ficha_clinica.service.FichaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ficha-clinica")
public class FichaController {

    @Autowired
    private FichaService fichaService;

    // GET /api/ficha-clinica
    @GetMapping
    public ResponseEntity<List<Ficha>> listarTodas() {
        return ResponseEntity.ok(fichaService.obtenerTodas());
    }

    // GET /api/ficha-clinica/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Ficha> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fichaService.obtenerPorId(id));
    }

    // GET /api/ficha-clinica/paciente/{idPaciente}
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Ficha>> obtenerPorPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(fichaService.obtenerPorPaciente(idPaciente));
    }

    // POST /api/ficha-clinica
    @PostMapping
    public ResponseEntity<Ficha> crear(@Valid @RequestBody FichaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fichaService.guardar(dto));
    }

    // PUT /api/ficha-clinica/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Ficha> actualizar(@PathVariable Long id,
                                             @Valid @RequestBody FichaRequestDTO dto) {
        return ResponseEntity.ok(fichaService.actualizar(id, dto));
    }

    // DELETE /api/ficha-clinica/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        fichaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
