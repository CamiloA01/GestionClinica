package com.ficha_clinica.servicio_de_ficha_clinica.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ficha_clinica.servicio_de_ficha_clinica.model.Ficha;

public interface FichaRepository extends JpaRepository<Ficha, Long> {

    // Para ver todas las fichas clínicas de un paciente específico
    List<Ficha> findByIdPaciente(Long idPaciente);

}
