package com.example.servicio_de_Ususarios.controller;


import com.example.servicio_de_Ususarios.assembler.*;
import com.example.servicio_de_Ususarios.dto.*;
import com.example.servicio_de_Ususarios.model.Usuario;
import com.example.servicio_de_Ususarios.service.*;
import com.example.servicio_de_Ususarios.usuarioClient.ProfesionalClient;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/api/usuarios")    
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class usuarioController {

    
    private final ClinicaModelAssembler assambler;
    
    private final UsuarioService usuarioService;

    private final ProfesionalClient profesionalClient;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados")
    public CollectionModel<EntityModel<UsuarioResponseDTO>> getAllUsuarios() {
        // 1. Obtenemos la lista de DTOs directamente del servicio
        List<UsuarioResponseDTO> dtos = usuarioService.getAllUsuarios();

        // 2. Mapeamos cada DTO a un EntityModel usando el assembler modificado
        List<EntityModel<UsuarioResponseDTO>> usuariosModel = dtos.stream()
                .map(assambler::toModel) // Ahora assambler acepta UsuarioResponseDTO
                .collect(Collectors.toList());

        // 3. Envolvemos la lista en un CollectionModel y añadimos el link del método actual
        return CollectionModel.of(usuariosModel,
                linkTo(methodOn(usuarioController.class).getAllUsuarios()).withSelfRel());
    }

    @GetMapping("/{id}") // 1. Corregida la ruta para recibir el ID
    @Operation(summary = "Obtener usuario por ID", description = "Retorna la información de un usuario específico por su ID")

    public EntityModel<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id) {
    // 2. Se maneja el Optional con orElseThrow y se agrega el punto y coma faltante
        UsuarioResponseDTO dto = usuarioService.getUsuarioById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
            
    return assambler.toModel(dto);
        }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@RequestBody UsuarioRequestDTO request){
        UsuarioResponseDTO response = usuarioService.registrar(request);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    

    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario por su ID")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        return usuarioService.actualizarUsuario(id, requestDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID")
    public ResponseEntity<RepresentationModel<?>> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        
        RepresentationModel<?> modeloResponse = new RepresentationModel<>();
        modeloResponse.add(linkTo(methodOn(usuarioController.class).getAllUsuarios()).withRel("usuarios"));
        
        return ResponseEntity.ok(modeloResponse);
    }    

    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuario por ID", description = "Retorna la información de un usuario específico por su ID")
    // CORREGIDO: Cambiado de @PathVariable a @RequestParam para URLs tipo: /buscar?id=1
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorId(@RequestParam Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping("/crear-profesional")
    @Operation(summary = "Crear profesional", description = "Crea un nuevo profesional y envía una alerta al servicio de pacientes")
    public ResponseEntity<String> enviar(@RequestBody ProfesionalRequest request){
    profesionalClient.enviarAlerta(request);
        return ResponseEntity.ok("Enviado correctamente");
    }
}

