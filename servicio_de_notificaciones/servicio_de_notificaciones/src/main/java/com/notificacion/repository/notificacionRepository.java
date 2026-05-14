package com.notificacion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.notificacion.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByIdUsuario(Long idUsuario);

}
