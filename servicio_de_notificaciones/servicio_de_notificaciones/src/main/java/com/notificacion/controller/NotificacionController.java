package com.notificacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.notificacion.model.dto.NotificacionRequestDTO;
import com.notificacion.model.dto.NotificacionResponseDTO;
import com.notificacion.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notificaciones", description = "Gestión y envío de notificaciones del sistema clínico")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Operation(summary = "Listar todas las notificaciones",
               description = "Recupera la lista completa de notificaciones registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Listado de notificaciones obtenido exitosamente")
    @GetMapping
    @Operation(summary = "Obtener todas las notificaciones", description = "Devuelve una lista de todas las notificaciones")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificaciones() {
        return ResponseEntity.ok(notificacionService.obtenerTodas());
    }

    @Operation(summary = "Crear notificación",
               description = "Registra una nueva notificación en el sistema validando los datos recibidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos en la solicitud")
    })
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(
            @Valid @RequestBody NotificacionRequestDTO notificacion) {
        return ResponseEntity.status(201).body(notificacionService.guardar(notificacion));
    }

    @Operation(summary = "Buscar notificación por ID",
               description = "Recupera una notificación específica usando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe una notificación con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> obtenerNotificacionPorId(
            @Parameter(description = "ID único de la notificación") @PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorId(id));
    }

    @Operation(summary = "Listar notificaciones por usuario",
               description = "Recupera todas las notificaciones asociadas a un usuario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones del usuario obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones para ese usuario")
    })
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuarioId(id));
    }

    @Operation(summary = "Eliminar notificación",
               description = "Elimina permanentemente una notificación del sistema usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe una notificación con ese ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(
            @Parameter(description = "ID único de la notificación a eliminar") @PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para recibir notificaciones desde el microservicio de agenda (via Feign)
    @Operation(summary = "Recibir alerta desde agenda",
               description = "Endpoint interno que recibe notificaciones enviadas por el microservicio de agenda vía OpenFeign.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alerta recibida y procesada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la alerta inválidos")
    })
    @PostMapping("/enviar")
    public ResponseEntity<Void> enviarAlerta(@RequestBody NotificacionRequestDTO solicitud) {
        log.info("✅ Comunicación exitosa: notificación recibida desde el microservicio de agenda");
        log.info("   Mensaje: {}", solicitud.getMensaje());
        return ResponseEntity.ok().build();
    }
}
