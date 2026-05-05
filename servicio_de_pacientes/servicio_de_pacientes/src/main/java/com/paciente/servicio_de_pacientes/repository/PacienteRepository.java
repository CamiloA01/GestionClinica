package com.paciente.servicio_de_pacientes.repository;

import com.paciente.servicio_de_pacientes.model.PacienteModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PacienteRepository extends JpaRepository<PacienteModel, Long>{


    @Query("SELECT p FROM Usuario p WHERE p.nombre = :nombre")
    List<PacienteModel> findByUsername(@Param("nomrbe") String nombre);
}
