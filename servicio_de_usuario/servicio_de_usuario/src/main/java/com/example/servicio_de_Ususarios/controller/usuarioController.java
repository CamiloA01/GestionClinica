package com.example.servicio_de_Ususarios.controller;


import com.example.servicio_de_Ususarios.assembler.*;
import com.example.servicio_de_Ususarios.dto.*;
import com.example.servicio_de_Ususarios.model.Usuario;
import com.example.servicio_de_Ususarios.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/api/usuarios")    
@RequiredArgsConstructor
public class usuarioController {

    
    private final ClinicaModelAssembler assambler;
    
    private final UsuarioService usuarioService;

    @GetMapping
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

    @GetMapping
    public EntityModel<UsuarioResponseDTO>


    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@RequestBody UsuarioRequestDTO request){
        UsuarioResponseDTO response = usuarioService.registrar(request);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    
    //@GetMapping
    //public ResponseEntity<?> getAllUsuarios() {
    //    return ResponseEntity.ok(usuarioService.getAllUsuarios());
   //}
    
    //@GetMapping("/{id}")
    //public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id) {
    //    return usuarioService.getUsuarioById(id)
    //        .map(ResponseEntity::ok)
    //        .orElse(ResponseEntity.notFound().build());
    //}
    
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

