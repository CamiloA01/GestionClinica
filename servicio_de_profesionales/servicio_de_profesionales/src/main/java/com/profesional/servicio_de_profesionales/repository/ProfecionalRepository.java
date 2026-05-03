package com.profesional.servicio_de_profesionales.repository;

import com.profesional.servicio_de_profesionales.model.Profecional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ProfecionalRepository extends JpaRepository<Profecional, Long>{
    
    @Query("SELECT p FROM Profecional p WHERE p.nombre = :nombre")
    List<Profecional> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Profecional p WHERE p.apellidopa = :apellidopa")
    List<Profecional> findByapellidopa(@Param("apellidopa") String apellidopa);

    @Query("SELECT p FROM Profecional p WHERE p.apellidoma = :apellidoma")
    List<Profecional> findByapellidoma(@Param("apellidoma") String apellidoma);

    @Query("SELECT p FROM Profecional p WHERE p.run = :run")
    List<Profecional> findByrun(@Param("run") String run);

    @Query("SELECT p FROM Profecional p WHERE p.titulo = :titulo")
    List<Profecional> findBytitulo(@Param("titulo") String titulo);

    @Query("SELECT p FROM Profecional p WHERE p.fechacontrato = :fechacontrato")
    List<Profecional> findByfechacontrato(@Param("fechacontrato") Date fechacontrato);

}
