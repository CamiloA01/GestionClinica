package com.profesional.servicio_de_profesionales.service;

import com.profesional.servicio_de_profesionales.dto.ProfesionalRequestDTO;
import com.profesional.servicio_de_profesionales.dto.ProfesionalResponceDTO;
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

    private ProfesionalResponceDTO mapToDTO(Profesional profesional){
        return new ProfesionalResponceDTO(
            profesional.getId(),
            profesional.getNombre(),
            profesional.getApellidopa(),
            profesional.getApellidoma(),
            profesional.getRun(),
            profesional.getTitulo(),
            profesional.getFechacontrato()
        );
    }

    public List<ProfesionalResponceDTO> obtenerTodos() {
        return profesionalRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProfesionalResponceDTO> obtenerPorId(Long id) {
        return profesionalRepository.findById(id).map(this::mapToDTO);
    }

    public ProfesionalResponceDTO guardar(ProfesionalRequestDTO dto) {
        Profesional profesional = new Profesional(
            null,
            dto.getNombre(),
            dto.getApellidopa(),
            dto.getApellidoma(),
            dto.getRun(),
            dto.getTitulo(),
            dto.getFechacontrato()
        );
        return mapToDTO(profesionalRepository.save(profesional));
    }

    public Optional<ProfesionalResponceDTO> actualizar(Long id, ProfesionalRequestDTO dto) {
    return profesionalRepository.findById(id).map(existente -> {
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
