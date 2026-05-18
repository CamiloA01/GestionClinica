package com.especialidad.servicio_de_especialidades.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.especialidad.servicio_de_especialidades.model.Especialidad;

public interface EspecialidadRepository
    extends JpaRepository<Especialidad, Long> {

    @Query("SELECT e FROM Especialidad e WHERE e.nombre = :nombre")
    List<Especialidad> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT e FROM Especialidad e WHERE e.descripcion = :descripcion")
    List<Especialidad> findByDescripcion(@Param("descripcion") String descripcion);

    @Query("SELECT e FROM Especialidad e WHERE e.estado = :estado")
    List<Especialidad> findByEstado(@Param("estado") String estado);

    @Query("SELECT e FROM Especialidad e WHERE e.fechaCreacion = :fechaCreacion")
    List<Especialidad> findByFechaCreacion(@Param("fechaCreacion") LocalDateTime fechaCreacion);

    @Query("SELECT e FROM Especialidad e WHERE e.fechaActualizacion = :fechaActualizacion")
    List<Especialidad> findByFechaActualizacion(@Param("fechaActualizacion") LocalDateTime fechaActualizacion);
}