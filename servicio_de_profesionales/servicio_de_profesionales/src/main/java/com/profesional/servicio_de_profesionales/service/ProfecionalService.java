package com.profesional.servicio_de_profesionales.service;

import com.profesional.servicio_de_profesionales.dto.ProfecionalRequestDTO;
import com.profesional.servicio_de_profesionales.dto.ProfecionalResponceDTO;
import com.profesional.servicio_de_profesionales.model.Profecional;
import com.profesional.servicio_de_profesionales.repository.ProfecionalRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfecionalService {
    private final ProfecionalRepository profecionalRepository;

    private ProfecionalResponceDTO mapToDTO(Profecional profecional){
        return new ProfecionalResponceDTO(
            profecional.getId(),
            profecional.getNombre(),
            profecional.getApellidopa(),
            profecional.getApellidoma(),
            profecional.getRun(),
            profecional.getTitulo(),
            profecional.getFechacontrato()
        );
    }

    public List<ProfecionalResponceDTO> obtenerTodos() {
        return profecionalRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProfecionalResponceDTO> obtenerPorId(Long id) {
        return profecionalRepository.findById(id).map(this::mapToDTO);
    }

    public ProfecionalResponceDTO guardar(ProfecionalRequestDTO dto) {
        Profecional profecional = new Profecional(
            null,
            dto.getNombre(),
            dto.getApellidopa(),
            dto.getApellidoma(),
            dto.getRun(),
            dto.getTitulo(),
            dto.getFechacontrato()
        );
        return mapToDTO(profecionalRepository.save(profecional));
    }

    public Optional<ProfecionalResponceDTO> actualizar(Long id, ProfecionalRequestDTO dto) {
    return profecionalRepository.findById(id).map(existente -> {
        existente.setNombre(dto.getNombre());
        existente.setApellidopa(dto.getApellidopa());
        existente.setApellidoma(dto.getApellidoma());
        existente.setRun(dto.getRun());
        existente.setTitulo(dto.getTitulo());
        existente.setFechacontrato(dto.getFechacontrato());
        return mapToDTO(profecionalRepository.save(existente));
    });
}

    public void eliminar(Long id) {
        profecionalRepository.deleteById(id);
    }

    
}
