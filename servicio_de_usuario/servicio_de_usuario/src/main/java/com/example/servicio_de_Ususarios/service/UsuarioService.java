package com.example.servicio_de_Ususarios.service;

import com.example.servicio_de_Ususarios.dto.*;
import com.example.servicio_de_Ususarios.model.Usuario;
import com.example.servicio_de_Ususarios.repository.usuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
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

    public Optional<UsuarioResponseDTO> getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
            .map(this::mapToDTO);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<UsuarioResponseDTO> actualizarUsuario(Long id, UsuarioRequestDTO requestDTO) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setUsername(requestDTO.getUsername());
                usuario.setGmail(requestDTO.getGmail());
                usuario.setPassword(requestDTO.getPassword());
                usuario.setRoll(requestDTO.getRoll());
                usuario.setEstado(requestDTO.getEstado());
                return mapToDTO(usuarioRepository.save(usuario));
            });
    }

    public List<UsuarioResponseDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id).stream().map(this::mapToDTO)
        .collect(Collectors.toList());
    }


}
