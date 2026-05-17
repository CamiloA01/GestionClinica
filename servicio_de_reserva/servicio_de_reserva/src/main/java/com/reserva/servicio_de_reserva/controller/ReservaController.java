package com.reserva.servicio_de_reserva.controller;

import com.reserva.servicio_de_reserva.dto.ReservaRequestDTO;
import com.reserva.servicio_de_reserva.dto.ReservaResponseDTO;
import com.reserva.servicio_de_reserva.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reserva")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>>obtenerTodasLasReservas() {
        return ResponseEntity.ok(
                reservaService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO>obtenerReservaPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO>crearReserva(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
        ReservaResponseDTO reservaResponseDTO = reservaService.guardar(reservaRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO>actualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
        return reservaService
                .actualizar(id, reservaRequestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>eliminarReserva(@PathVariable Long id) {
        if (reservaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}