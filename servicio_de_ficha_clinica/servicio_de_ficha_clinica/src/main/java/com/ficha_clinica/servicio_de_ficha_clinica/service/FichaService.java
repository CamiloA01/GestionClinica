package com.ficha_clinica.servicio_de_ficha_clinica.service;

import com.ficha_clinica.servicio_de_ficha_clinica.model.Ficha;
import com.ficha_clinica.servicio_de_ficha_clinica.model.dto.FichaRequestDTO;
import com.ficha_clinica.servicio_de_ficha_clinica.repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepository;

    public List<Ficha> obtenerTodas() {
        return fichaRepository.findAll();
    }

    public Ficha obtenerPorId(Long id) {
        return fichaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ficha clínica no encontrada con ID: " + id));
    }

    public List<Ficha> obtenerPorPaciente(Long idPaciente) {
        return fichaRepository.findByPacienteId(idPaciente);
    }

    public Ficha guardar(FichaRequestDTO dto) {
        Ficha ficha = new Ficha();
        ficha.setPacienteId(dto.getPacienteId());
        ficha.setProfesionalId(dto.getProfesionalId());
        ficha.setDiagnostico(dto.getDiagnostico());
        ficha.setTratamiento(dto.getTratamiento());
        ficha.setObservaciones(dto.getObservaciones());
        ficha.setFechaCreacion(LocalDateTime.now());
        return fichaRepository.save(ficha);
    }

    public Ficha actualizar(Long id, FichaRequestDTO dto) {
        Ficha ficha = obtenerPorId(id);
        ficha.setDiagnostico(dto.getDiagnostico());
        ficha.setTratamiento(dto.getTratamiento());
        ficha.setObservaciones(dto.getObservaciones());
        return fichaRepository.save(ficha);
    }

    public void eliminar(Long id) {
        if (!fichaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Ficha clínica no encontrada con ID: " + id);
        }
        fichaRepository.deleteById(id);
    }
}