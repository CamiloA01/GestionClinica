package com.profesional.servicio_de_profesionales.repository;

import com.profesional.servicio_de_profesionales.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {

    @Query("SELECT p FROM Profesional p WHERE p.nombre = :nombre")
    List<Profesional> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Profesional p WHERE p.apellidopa = :apellidopa")
    List<Profesional> findByApellidopa(@Param("apellidopa") String apellidopa);

    @Query("SELECT p FROM Profesional p WHERE p.apellidoma = :apellidoma")
    List<Profesional> findByApellidoma(@Param("apellidoma") String apellidoma);

    @Query("SELECT p FROM Profesional p WHERE p.run = :run")
    Optional<Profesional> findByRun(@Param("run") String run);

    @Query("SELECT p FROM Profesional p WHERE p.titulo = :titulo")
    List<Profesional> findByTitulo(@Param("titulo") String titulo);

    @Query("SELECT p FROM Profesional p WHERE p.fechacontrato = :fechacontrato")
    List<Profesional> findByFechacontrato(@Param("fechacontrato") LocalDate fechacontrato);
}