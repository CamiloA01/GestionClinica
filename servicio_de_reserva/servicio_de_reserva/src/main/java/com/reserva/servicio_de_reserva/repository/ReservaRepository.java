package com.reserva.servicio_de_reserva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reserva.servicio_de_reserva.model.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha")
    List<Reserva> findByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT r FROM Reserva r WHERE r.hora = :hora")
    List<Reserva> findByHora(@Param("hora") LocalTime hora);

    @Query("SELECT r FROM Reserva r WHERE r.estado = :estado")
    List<Reserva> findByEstado(@Param("estado") String estado);

    @Query("SELECT r FROM Reserva r WHERE r.usuarioId = :usuarioId")
    List<Reserva> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}