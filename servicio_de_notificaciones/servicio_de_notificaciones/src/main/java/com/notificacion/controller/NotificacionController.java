package com.notificacion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.notificacion.service.NotificacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificación", description = "Operaciones relacionadas con las notificaciones")
public class NotificacionController {

    @Autowired
    private final NotificacionService notificacionService;

    @GetMapping
    @Operation(summary = "Obtener todas las notificaciones", description = "Devuelve una lista de todas las notificaciones")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificaciones() {
        return ResponseEntity.ok(notificacionService.obtenerTodas());
    }

    
    @PostMapping
    @Operation(summary = "Crear una nueva notificación", description = "Crea una nueva notificación y la devuelve")
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(@Valid @RequestBody NotificacionRequestDTO notificacion) {
        return ResponseEntity.status(201).body(notificacionService.guardar(notificacion));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una notificación por ID", description = "Devuelve la notificación correspondiente al ID proporcionado")
    public ResponseEntity<NotificacionResponseDTO> obtenerNotificacionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Obtener notificaciones por ID de usuario", description = "Devuelve una lista de notificaciones correspondientes al ID de usuario proporcionado")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuarioId(id));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una notificación por ID", description = "Elimina la notificación correspondiente al ID proporcionado")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminar(id);
            return ResponseEntity.noContent().build();
    }

    //comunicación con el microservicio de agenda para recibir notificaciones
    @PostMapping("/enviar") 
    @Operation(summary = "Enviar una notificación", description = "Envía una nueva notificación")
    public void enviarAlerta(@RequestBody NotificacionRequestDTO solicitud) { 
        System.out.println("====== LOG DE NOTIFICACIONES ======");
        System.out.println("Mensaje recibido: " + solicitud.getMensaje()); 
        System.out.println("====================================");

    }

}




