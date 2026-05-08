package com.example.servicio_de_Ususarios.controller;

import com.example.servicio_de_Ususarios.dto.*;
import com.example.servicio_de_Ususarios.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")    
@RequiredArgsConstructor
public class usuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(usuarioService.createUsuario(requestDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        return usuarioService.actualizarUsuario(id, requestDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }       

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorId(
        @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));

    }
}

