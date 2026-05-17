package com.reporte.servicio_de_reportes.repository;
import com.reporte.servicio_de_reportes.model.ModelReporte;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryReporte extends JpaRepository<ModelReporte, Long> {


     @Query("SELECT p FROM ModelReporte p WHERE p.nombreReporte = :nombreReporte")
    List<ModelReporte> findByNombreReporte(@Param("nombreReporte") String nombreReporte);

    @Query("SELECT p FROM ModelReporte p WHERE p.tipo = :tipo") 
    List<ModelReporte> findByTipo(@Param("tipo") String tipo);

    @Query("SELECT p FROM ModelReporte p WHERE p.requestdBy = :requestdBy")
    List<ModelReporte> findByRequestdBy(@Param("requestdBy") String requestdBy);

    @Query("SELECT p FROM ModelReporte p WHERE p.generatedAt = :generatedAt")   
    List<ModelReporte> findByGeneratedAt(@Param("generatedAt") String generatedAt);


}
