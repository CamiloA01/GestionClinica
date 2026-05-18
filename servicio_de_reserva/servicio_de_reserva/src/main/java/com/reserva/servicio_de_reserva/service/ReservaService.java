package com.reserva.servicio_de_reserva.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.reserva.servicio_de_reserva.dto.ReservaRequestDTO;
import com.reserva.servicio_de_reserva.dto.ReservaResponseDTO;
import com.reserva.servicio_de_reserva.model.Reserva;
import com.reserva.servicio_de_reserva.repository.ReservaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;

    private ReservaResponseDTO mapToDTO(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getEstado(),
                reserva.getUsuarioId(),
                reserva.getDescripcion()
        );
    }

    public List<ReservaResponseDTO> obtenerTodos() {
        return reservaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReservaResponseDTO> obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .map(this::mapToDTO);
    }

    public ReservaResponseDTO guardar(ReservaRequestDTO dto) {
        Reserva reserva = new Reserva(
                null,
                dto.getFecha(),
                dto.getHora(),
                dto.getEstado(),
                dto.getUsuarioId(),
                dto.getDescripcion()
        );
        return mapToDTO(reservaRepository.save(reserva));
    }

    public Optional<ReservaResponseDTO> actualizar(Long id, ReservaRequestDTO dto) {
        return reservaRepository.findById(id).map(existente -> {
            existente.setFecha(dto.getFecha());
            existente.setHora(dto.getHora());
            existente.setEstado(dto.getEstado());
            existente.setUsuarioId(dto.getUsuarioId());
            existente.setDescripcion(dto.getDescripcion());
            return mapToDTO(reservaRepository.save(existente));
        });
    }

    public Optional<ReservaResponseDTO> actualizarEstado(Long id, String estado) {
        return reservaRepository.findById(id).map(reserva -> {reserva.setEstado(estado);
            return mapToDTO(reservaRepository.save(reserva));
        });
    }

    public List<ReservaResponseDTO>
    obtenerPorUsuarioId(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservaResponseDTO>
    obtenerPorFecha(LocalDate fecha) {
        return reservaRepository.findByFecha(fecha)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
    }
}