package com.paciente.servicio_de_pacientes.repository;

import com.paciente.servicio_de_pacientes.model.PacienteModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PacienteRepository extends JpaRepository<PacienteModel, Long>{


    @Query("SELECT p FROM PacienteModel p WHERE p.nombre = :nombre")
    List<PacienteModel> findByUsername(@Param("nombre") String nombre);

    @Query("SELECT p FROM PacienteModel p WHERE p.apellido = :apellido")
    List<PacienteModel> findByApellido(@Param("apellido") String apellido);

    @Query("SELECT p FROM PacienteModel p WHERE p.rut = :rut")
    List<PacienteModel> findByRut(@Param("rut") String rut);    

    @Query("SELECT p FROM PacienteModel p WHERE p.fecha_nacimiento = :fecha_nacimiento")
    List<PacienteModel> findByFechaNacimiento(@Param("fecha_nacimiento") String fecha_nacimiento);      

    @Query("SELECT p FROM PacienteModel p WHERE p.telefono = :telefono")
    List<PacienteModel> findByTelefono(@Param("telefono") int telefono);        

    @Query("SELECT p FROM PacienteModel p WHERE p.direccion = :direccion")
    List<PacienteModel> findByDireccion(@Param("direccion") String direccion);  



    
}

