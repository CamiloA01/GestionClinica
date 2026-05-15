package com.agenda.servicio_de_agenda.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agenda.servicio_de_agenda.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByDuracionCita(Integer duracionCita);

}
