package com.example.servicio_de_Ususarios.controller;

import com.example.servicio_de_Ususarios.dto.*;
import com.example.servicio_de_Ususarios.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/usuarios")    
@RequiredArgsConstructor
public class usuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@RequestBody UsuarioRequestDTO request){
        UsuarioResponseDTO response = usuarioService.registrar(request);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
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

