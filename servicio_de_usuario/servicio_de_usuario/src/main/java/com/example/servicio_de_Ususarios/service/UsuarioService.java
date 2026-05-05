package com.example.servicio_de_Ususarios.service;

import com.example.servicio_de_Ususarios.dto.*;
import com.example.servicio_de_Ususarios.model.Usuario;
import com.example.servicio_de_Ususarios.repository.usuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final usuarioRepository usuarioRepository;

    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getGmail(),
                usuario.getPassword(),
                usuario.getRoll(),
                usuario.getEstado()
        );
    }
    
    public List<UsuarioResponseDTO> getAllUsuarios() {
        return usuarioRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO requestDTO) {
        Usuario usuario = new Usuario(
            null,
            requestDTO.getUsername(),
            requestDTO.getGmail(),
            requestDTO.getPassword(),
            requestDTO.getRoll(),
            requestDTO.getEstado()

        );
        return mapToDTO(usuarioRepository.save(usuario));
    }

   


}
