package com.paciente.servicio_de_pacientes.service;

import com.paciente.servicio_de_pacientes.model.PacienteModel;

import com.paciente.servicio_de_pacientes.dto.PacienteRequestDTO;
import com.paciente.servicio_de_pacientes.dto.PacienteResponseDTO;
import com.paciente.servicio_de_pacientes.repository.PacienteRepository;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PacienteServices {

    private final PacienteRepository pacienteRepository;

    private PacienteResponseDTO mapToDTO(PacienteModel paciente) {
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getRut(),
                paciente.getFecha_nacimiento(),
                paciente.getTelefono(),
                paciente.getDireccion()
        );
    }
    
    public List<PacienteResponseDTO> getAllPacientes() {
        return pacienteRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public PacienteResponseDTO crearPaciente(PacienteRequestDTO requestDTO) {
        PacienteModel paciente = new PacienteModel(
            null,
            requestDTO.getNombre(),
            requestDTO.getApellido(),
            requestDTO.getRut(),
            requestDTO.getFecha_nacimiento(),
            requestDTO.getTelefono(),
            requestDTO.getDireccion()
        );
        return mapToDTO(pacienteRepository.save(paciente));
    }

    public void eliminarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }

    public Optional<PacienteResponseDTO> BuscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
            .map(this::mapToDTO);
    }

    public List<PacienteResponseDTO> BuscarPacientePorNombre(String nombre) {
        return pacienteRepository.findByUsername(nombre)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

   public Optional<PacienteResponseDTO> actualizarPaciente(Long id, PacienteRequestDTO requestDTO) {
        return pacienteRepository.findById(id)
            .map(paciente -> {
                paciente.setNombre(requestDTO.getNombre());
                paciente.setApellido(requestDTO.getApellido());
                paciente.setRut(requestDTO.getRut());
                paciente.setFecha_nacimiento(requestDTO.getFecha_nacimiento());
                paciente.setTelefono(requestDTO.getTelefono());
                paciente.setDireccion(requestDTO.getDireccion());
                return mapToDTO(pacienteRepository.save(paciente));
            });
    }       




}
     





