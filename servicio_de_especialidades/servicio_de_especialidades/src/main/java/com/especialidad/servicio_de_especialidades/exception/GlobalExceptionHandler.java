package com.especialidad.servicio_de_especialidades.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

     // ── ERROR DE VALIDACIÓN (@Valid) ─────────────────
    // Se ejecuta cuando los datos enviados en el RequestDTO
    // no cumplen las restricciones (@NotBlank, @NotNull, etc.)
    // Ejemplo: titulo vacío, tipo nulo, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Se usa LinkedHashMap para mantener el orden de los errores
        Map<String, String> errores = new LinkedHashMap<>();

        // Recorre todos los errores de validación
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.put(error.getField(), error.getDefaultMessage())
        );

        // Se devuelve 400 porque el cliente envió datos inválidos
        return ResponseEntity.badRequest().body(errores);
    }

    // ── ERROR DE NEGOCIO (pago no encontrada, etc.) ──
    // Se ejecuta cuando el Service lanza una RuntimeException
    // Ejemplo: "Pago no encontrado con id: X"
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException ex) {

        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", ex.getMessage());

        // Se devuelve 404 porque el recurso no existe
        return ResponseEntity.status(404).body(error);
    }

    // ── ERROR GENERAL (fallback) ─────────────────
    // Captura cualquier error no controlado en la aplicación
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(
            Exception ex) {

        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", "Error interno del servidor");

        return ResponseEntity.status(500).body(error);
    }

}
