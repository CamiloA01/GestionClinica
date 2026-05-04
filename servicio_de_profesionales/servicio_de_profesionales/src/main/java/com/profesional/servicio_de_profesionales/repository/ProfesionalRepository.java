package com.profesional.servicio_de_profesionales.repository;

import com.profesional.servicio_de_profesionales.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long>{
    
    @Query("SELECT p FROM Profecional p WHERE p.nombre = :nombre")
    List<Profesional> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Profecional p WHERE p.apellidopa = :apellidopa")
    List<Profesional> findByapellidopa(@Param("apellidopa") String apellidopa);

    @Query("SELECT p FROM Profecional p WHERE p.apellidoma = :apellidoma")
    List<Profesional> findByapellidoma(@Param("apellidoma") String apellidoma);

    @Query("SELECT p FROM Profecional p WHERE p.run = :run")
    List<Profesional> findByrun(@Param("run") String run);

    @Query("SELECT p FROM Profecional p WHERE p.titulo = :titulo")
    List<Profesional> findBytitulo(@Param("titulo") String titulo);

    @Query("SELECT p FROM Profecional p WHERE p.fechacontrato = :fechacontrato")
    List<Profesional> findByfechacontrato(@Param("fechacontrato") Date fechacontrato);

}
