package com.profesional.servicio_de_profesionales.service;

import com.profesional.servicio_de_profesionales.dto.ProfesionalRequestDTO;
import com.profesional.servicio_de_profesionales.dto.ProfesionalResponseDTO;
import com.profesional.servicio_de_profesionales.model.Profesional;
import com.profesional.servicio_de_profesionales.repository.ProfesionalRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesionalService {
    private final ProfesionalRepository profesionalRepository;

    private ProfesionalResponseDTO mapToDTO(Profesional profesional){
        return new ProfesionalResponseDTO(
            profesional.getId(),
            profesional.getUsuarioId(),
            profesional.getNombre(),
            profesional.getApellidopa(),
            profesional.getApellidoma(),
            profesional.getRun(),
            profesional.getTitulo(),
            profesional.getFechacontrato()
        );
    }

    public List<ProfesionalResponseDTO> obtenerTodos() {
        return profesionalRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProfesionalResponseDTO> obtenerPorId(Long id) {
        return profesionalRepository.findById(id).map(this::mapToDTO);
    }

    public ProfesionalResponseDTO guardar(ProfesionalRequestDTO dto) {
        Profesional profesional = new Profesional(
                null,
                dto.getUsuarioId(),
                dto.getNombre(),
                dto.getApellidopa(),
                dto.getApellidoma(),
                dto.getRun(),
                dto.getTitulo(),
                dto.getFechacontrato()
        );
        return mapToDTO(profesionalRepository.save(profesional));
    }

    public Optional<ProfesionalResponseDTO> actualizar(Long id, ProfesionalRequestDTO dto) {
    return profesionalRepository.findById(id).map(existente -> {
        existente.setUsuarioId(dto.getUsuarioId());
        existente.setNombre(dto.getNombre());
        existente.setApellidopa(dto.getApellidopa());
        existente.setApellidoma(dto.getApellidoma());
        existente.setRun(dto.getRun());
        existente.setTitulo(dto.getTitulo());
        existente.setFechacontrato(dto.getFechacontrato());
        return mapToDTO(profesionalRepository.save(existente));
    });
}

    public void eliminar(Long id) {
        profesionalRepository.deleteById(id);
    }

    
}
