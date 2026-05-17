package com.reporte.servicio_de_reportes.service;

import com.reporte.servicio_de_reportes.model.ModelReporte;
import com.reporte.servicio_de_reportes.dto.*;
import com.reporte.servicio_de_reportes.repository.RepositoryReporte;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final RepositoryReporte reporteRepository;

    private ReporteResponseDTO mapToDTO(ModelReporte reporte) {
        return new ReporteResponseDTO(
                reporte.getId(),
                reporte.getNombreReporte(),
                reporte.getTipo(),
                reporte.getRequestdBy(),
                reporte.getGeneratedAt()
        );
    }

    public List<ReporteResponseDTO> getAllReportes() {
        return reporteRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public ReporteResponseDTO crearReporte(ReporteRequestDTO requestDTO) {
        ModelReporte reporte = new ModelReporte(
            null,
            requestDTO.getNombreReporte(),
            requestDTO.getTipo(),
            requestDTO.getRequestdBy(),
            requestDTO.getGeneratedAt()
        );
        return mapToDTO(reporteRepository.save(reporte));
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    public Optional<ReporteResponseDTO> BuscarReportePorId(Long id) {
        return reporteRepository.findById(id)
            .map(this::mapToDTO);
    }

    public List<ReporteResponseDTO> BuscarReportesPorTipo(String tipo) {
        return reporteRepository.findByTipo(tipo)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public Optional<ReporteResponseDTO> actualizarReporte(Long id, ReporteRequestDTO requestDTO) {
        return reporteRepository.findById(id)
            .map(reporte -> {
                reporte.setNombreReporte(requestDTO.getNombreReporte());
                reporte.setTipo(requestDTO.getTipo());
                reporte.setRequestdBy(requestDTO.getRequestdBy());
                reporte.setGeneratedAt(requestDTO.getGeneratedAt());
                return mapToDTO(reporteRepository.save(reporte));
            });
    }   

}



