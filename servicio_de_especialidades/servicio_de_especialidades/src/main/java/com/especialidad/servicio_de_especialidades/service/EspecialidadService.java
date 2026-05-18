package com.especialidad.servicio_de_especialidades.service;

import com.especialidad.servicio_de_especialidades.model.Especialidad;
import com.especialidad.servicio_de_especialidades.repository.EspecialidadRepository;
import com.especialidad.servicio_de_especialidades.dto.EspecialidadRequestDTO;
import com.especialidad.servicio_de_especialidades.dto.EspecialidadResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    private EspecialidadResponseDTO mapToDTO(Especialidad especialidad) {
        return new EspecialidadResponseDTO(
                especialidad.getId(),
                especialidad.getNombre(),
                especialidad.getDescripcion(),
                especialidad.getEstado(),
                especialidad.getFechaCreacion(),
                especialidad.getFechaActualizacion()
        );
    }

    public List<EspecialidadResponseDTO> getAllEspecialidades() {
        return especialidadRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public EspecialidadResponseDTO crearEspecialidad(EspecialidadRequestDTO requestDTO) {
        Especialidad especialidad = new Especialidad(
            null,
            requestDTO.getNombre(),
            requestDTO.getDescripcion(),
            requestDTO.getEstado(),
            requestDTO.getFechaCreacion(),
            requestDTO.getFechaActualizacion()
        );
        return mapToDTO(especialidadRepository.save(especialidad));
    }

    public Optional<EspecialidadResponseDTO> actualizarEspecialidad(Long id, EspecialidadRequestDTO requestDTO) {
        return especialidadRepository.findById(id)
            .map(especialidad -> {
                especialidad.setNombre(requestDTO.getNombre());
                especialidad.setDescripcion(requestDTO.getDescripcion());
                especialidad.setEstado(requestDTO.getEstado());
                especialidad.setFechaActualizacion(requestDTO.getFechaActualizacion());
                return mapToDTO(especialidadRepository.save(especialidad));
            });
    }

    public void eliminarEspecialidad(Long id) {
        especialidadRepository.deleteById(id);
    }

    public Optional<EspecialidadResponseDTO> BuscarEspecialidadPorId(Long id) {
        return especialidadRepository.findById(id)
            .map(this::mapToDTO);
    }
    
}
