package com.pago.servicio_de_pagos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pago.servicio_de_pagos.model.Pago;

public interface pagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByEstadoPago(String estado);


}
