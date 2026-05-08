package com.example.servicio_de_Ususarios.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String gmail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String roll;
  
    @Column(nullable = false)
    private String estado;
}

