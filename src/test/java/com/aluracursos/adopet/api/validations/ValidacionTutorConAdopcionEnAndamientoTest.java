package com.aluracursos.adopet.api.validations;

import com.aluracursos.adopet.api.dto.RegistroTutorDto;
import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.Adopcion;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.Tutor;
import com.aluracursos.adopet.api.repository.AdopcionRepository;
import com.aluracursos.adopet.api.repository.TutorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ValidacionTutorConAdopcionEnAndamientoTest {

    @Mock
    private AdopcionRepository adopcionRepository;

    @Mock
    private SolicitudAdopcionDTO solicitudAdopcionDTO;

    @Mock
    private TutorRepository tutorRepository;

    private final List<Adopcion> adopciones = new ArrayList<>();

    @InjectMocks
    private ValidacionTutorConAdopcionEnAndamiento validacionTutorConAdopcionEnAndamiento;


    @Test
    @DisplayName("No debería lanzar excepción cuando el tutor no tiene solicitudes de adopcion en andamiento")
    void escenario01() {
        // ARRANGE
        var registroTutorDTO = new RegistroTutorDto("nombreTutorTest","1234567890","test@email.com");
        var tutor = new Tutor(registroTutorDTO);

        BDDMockito.given(tutorRepository.getReferenceById(anyLong())).willReturn(tutor);


        BDDMockito.given(adopcionRepository.findAll()).willReturn(adopciones);

        // ACT
        // ASSERT
        Assertions.assertThatNoException().isThrownBy(() -> validacionTutorConAdopcionEnAndamiento.validar(solicitudAdopcionDTO));
    }

    @Test
    @DisplayName("No debería lanzar excepción cuando el tutor no tiene solicitudes de adopcion en andamiento")
    void escenario02() {
        // ARRANGE
        var registroTutorDTO = new RegistroTutorDto("nombreTutorTest","1234567890","test@email.com");
        var tutor = new Tutor(registroTutorDTO);

        BDDMockito.given(tutorRepository.getReferenceById(anyLong())).willReturn(tutor);

        var adopcion = new Adopcion(tutor,new Mascota(),"testMotivo1");

        adopciones.add(adopcion);

        BDDMockito.given(adopcionRepository.findAll()).willReturn(adopciones);

        // ACT
        // ASSERT
        Assertions.assertThatThrownBy(() -> validacionTutorConAdopcionEnAndamiento.validar(solicitudAdopcionDTO))
                .isInstanceOf(ValidacionException.class)
                .hasMessage("Tutor ya tiene otra adopción esperando evaluación!");
    }
}