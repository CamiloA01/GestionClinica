package com.example.servicio_de_Ususarios.repository;

import com.example.servicio_de_Ususarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface usuarioRepository extends JpaRepository<Usuario, Long> {







    @Query("SELECT u FROM Usuario u WHERE u.username = :username")
    List<Usuario> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM Usuario u WHERE u.gmail = :gmail")
    List<Usuario> findByGmail(@Param("gmail") String gmail);

    @Query("SELECT u FROM Usuario u WHERE u.roll = :roll")
    List<Usuario> findByRoll(@Param("roll") String roll);

    @Query("SELECT u FROM Usuario u WHERE u.estado = :estado")
    List<Usuario> findByEstado(@Param("estado") String estado);


}
    