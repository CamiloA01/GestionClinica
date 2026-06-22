package com.profesional.servicio_de_profesionales.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.profesional.servicio_de_profesionales.dto.ProfesionalRequestDTO;
import com.profesional.servicio_de_profesionales.dto.ProfesionalResponseDTO;
import com.profesional.servicio_de_profesionales.model.Profesional;
import com.profesional.servicio_de_profesionales.repository.ProfesionalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProfesionalServiceTest {

    @Autowired
    private ProfesionalService profesionalService;

    @MockBean
    private ProfesionalRepository profesionalRepository;

    @Test
    public void testObtenerTodos() {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con un Profesional.
        Profesional p = new Profesional(1L, 10L, "Juan", "Pérez", "González",
                "12345678-9", "Médico Cirujano", LocalDate.of(2023, 3, 15));
        when(profesionalRepository.findAll()).thenReturn(List.of(p));

        // Llama al método obtenerTodos() del servicio.
        List<ProfesionalResponseDTO> resultado = profesionalService.obtenerTodos();

        // Verifica que la lista no sea nula y contenga exactamente un Profesional.
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    public void testObtenerPorId() {
        Long id = 1L;
        Profesional p = new Profesional(id, 10L, "Juan", "Pérez", "González",
                "12345678-9", "Médico Cirujano", LocalDate.of(2023, 3, 15));

        // Define el comportamiento del mock: cuando se llame a findById() con 1L, devuelve el Profesional.
        when(profesionalRepository.findById(id)).thenReturn(Optional.of(p));

        // Llama al método obtenerPorId() del servicio.
        Optional<ProfesionalResponseDTO> resultado = profesionalService.obtenerPorId(id);

        // Verifica que el Profesional encontrado no sea nulo y tenga el RUN esperado.
        assertTrue(resultado.isPresent());
        assertEquals("12345678-9", resultado.get().getRun());
    }

    @Test
    public void testObtenerPorId_noExiste() {
        // Define el comportamiento del mock: cuando se llame a findById() con 99L, devuelve vacío.
        when(profesionalRepository.findById(99L)).thenReturn(Optional.empty());

        // Llama al método obtenerPorId() del servicio.
        Optional<ProfesionalResponseDTO> resultado = profesionalService.obtenerPorId(99L);

        // Verifica que el resultado esté vacío.
        assertFalse(resultado.isPresent());
    }

    @Test
    public void testGuardar() {
        ProfesionalRequestDTO dto = new ProfesionalRequestDTO(10L, "Juan", "Pérez", "González",
                "12345678-9", "Médico Cirujano", LocalDate.of(2023, 3, 15));
        Profesional p = new Profesional(1L, 10L, "Juan", "Pérez", "González",
                "12345678-9", "Médico Cirujano", LocalDate.of(2023, 3, 15));

        // Define el comportamiento del mock: cuando se llame a save(), devuelve el Profesional guardado.
        when(profesionalRepository.save(any(Profesional.class))).thenReturn(p);

        // Llama al método guardar() del servicio.
        ProfesionalResponseDTO resultado = profesionalService.guardar(dto);

        // Verifica que el Profesional guardado no sea nulo y tenga el nombre esperado.
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Médico Cirujano", resultado.getTitulo());
    }

    @Test
    public void testActualizar() {
        Long id = 1L;
        Profesional existente = new Profesional(id, 10L, "Juan", "Pérez", "González",
                "12345678-9", "Médico Cirujano", LocalDate.of(2023, 3, 15));
        Profesional actualizado = new Profesional(id, 10L, "Juan Carlos", "Pérez", "González",
                "12345678-9", "Traumatólogo", LocalDate.of(2024, 1, 1));
        ProfesionalRequestDTO dto = new ProfesionalRequestDTO(10L, "Juan Carlos", "Pérez", "González",
                "12345678-9", "Traumatólogo", LocalDate.of(2024, 1, 1));

        when(profesionalRepository.findById(id)).thenReturn(Optional.of(existente));
        when(profesionalRepository.save(any(Profesional.class))).thenReturn(actualizado);

        // Llama al método actualizar() del servicio.
        Optional<ProfesionalResponseDTO> resultado = profesionalService.actualizar(id, dto);

        // Verifica que el Profesional actualizado tenga el nuevo título.
        assertTrue(resultado.isPresent());
        assertEquals("Traumatólogo", resultado.get().getTitulo());
        assertEquals("Juan Carlos", resultado.get().getNombre());
    }

    @Test
    public void testEliminar() {
        Long id = 1L;

        // Define el comportamiento del mock: cuando se llame a deleteById(), no hace nada.
        doNothing().when(profesionalRepository).deleteById(id);

        // Llama al método eliminar() del servicio.
        profesionalService.eliminar(id);

        // Verifica que deleteById() se haya llamado exactamente una vez con el ID proporcionado.
        verify(profesionalRepository, times(1)).deleteById(id);
    }
}
