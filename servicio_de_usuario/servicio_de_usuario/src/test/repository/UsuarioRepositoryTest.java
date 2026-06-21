package com.example.servicio_de_Ususarios.repository;

import com.example.servicio_de_Ususarios.model.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración del repositorio contra una base H2 en memoria
 * (no se mockea: el objetivo es validar que las @Query en JPQL son
 * correctas y que el mapeo de la entidad Usuario funciona).
 */
@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private usuarioRepository usuarioRepository;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        usuario1 = new Usuario(null, "jdoe", "jdoe@gmail.com", "hash1", "ADMIN", "ACTIVO");
        usuario2 = new Usuario(null, "asmith", "asmith@gmail.com", "hash2", "USER", "INACTIVO");

        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);
    }

    @Test
    @DisplayName("existsByGmail debe devolver true si el gmail ya está registrado")
    void existsByGmail_gmailRegistrado_devuelveTrue() {
        boolean existe = usuarioRepository.existsByGmail("jdoe@gmail.com");

        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("existsByGmail debe devolver false si el gmail no está registrado")
    void existsByGmail_gmailNoRegistrado_devuelveFalse() {
        boolean existe = usuarioRepository.existsByGmail("noexiste@gmail.com");

        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("findByUsername debe devolver el usuario que coincide exactamente")
    void findByUsername_usuarioExiste_devuelveCoincidencia() {
        List<Usuario> resultado = usuarioRepository.findByUsername("jdoe");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getGmail()).isEqualTo("jdoe@gmail.com");
    }

    @Test
    @DisplayName("findByUsername debe devolver lista vacía si no hay coincidencias")
    void findByUsername_usuarioNoExiste_devuelveVacio() {
        List<Usuario> resultado = usuarioRepository.findByUsername("noexiste");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("findByGmail debe devolver el usuario correspondiente")
    void findByGmail_devuelveCoincidencia() {
        List<Usuario> resultado = usuarioRepository.findByGmail("asmith@gmail.com");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsername()).isEqualTo("asmith");
    }

    @Test
    @DisplayName("findByRoll debe devolver solo los usuarios con ese rol")
    void findByRoll_devuelveSoloCoincidencias() {
        List<Usuario> admins = usuarioRepository.findByRoll("ADMIN");
        List<Usuario> users = usuarioRepository.findByRoll("USER");

        assertThat(admins).hasSize(1);
        assertThat(admins.get(0).getUsername()).isEqualTo("jdoe");

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("asmith");
    }

    @Test
    @DisplayName("findByEstado debe devolver solo los usuarios con ese estado")
    void findByEstado_devuelveSoloCoincidencias() {
        List<Usuario> activos = usuarioRepository.findByEstado("ACTIVO");
        List<Usuario> inactivos = usuarioRepository.findByEstado("INACTIVO");

        assertThat(activos).hasSize(1);
        assertThat(activos.get(0).getUsername()).isEqualTo("jdoe");

        assertThat(inactivos).hasSize(1);
        assertThat(inactivos.get(0).getUsername()).isEqualTo("asmith");
    }

    @Test
    @DisplayName("save debe persistir el usuario y generarle un id")
    void save_persisteUsuarioConId() {
        Usuario nuevo = new Usuario(null, "mgarcia", "mgarcia@gmail.com", "hash3", "USER", "ACTIVO");

        Usuario guardado = usuarioRepository.save(nuevo);

        assertThat(guardado.getId()).isNotNull();
        assertThat(usuarioRepository.findById(guardado.getId())).isPresent();
    }

    @Test
    @DisplayName("deleteById debe eliminar el usuario de la base de datos")
    void deleteById_eliminaUsuario() {
        Long id = usuario1.getId();

        usuarioRepository.deleteById(id);

        assertThat(usuarioRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("findAll debe devolver todos los usuarios guardados")
    void findAll_devuelveTodosLosUsuarios() {
        List<Usuario> resultado = usuarioRepository.findAll();

        assertThat(resultado).hasSize(2);
    }
}
