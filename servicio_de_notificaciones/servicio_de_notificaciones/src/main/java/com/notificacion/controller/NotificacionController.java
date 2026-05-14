package com.notificacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notificacion.model.dto.NotificacionRequestDTO;
import com.notificacion.model.dto.NotificacionResponseDTO;

import com.notificacion.service.NotificacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificaciones() {
        return ResponseEntity.ok(notificacionService.obtenerTodas());
    }

    
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(@Valid @RequestBody NotificacionRequestDTO notificacion) {
        return ResponseEntity.status(201).body(notificacionService.guardar(notificacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> obtenerNotificacionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuarioId(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminar(id);
            return ResponseEntity.noContent().build();
    }

}




