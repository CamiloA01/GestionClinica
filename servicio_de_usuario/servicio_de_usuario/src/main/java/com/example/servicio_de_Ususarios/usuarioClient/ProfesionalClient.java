package com.example.servicio_de_Ususarios.usuarioClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.servicio_de_Ususarios.dto.ProfesionalRequest;


@FeignClient(
    name="ms-profesionales",
    url="http://localhost:8084/api/v2/profesional"
)
public interface ProfesionalClient {


    @PostMapping
    ResponseEntity<Object> enviarAlerta(
        @RequestBody ProfesionalRequest solicitud
    );

}