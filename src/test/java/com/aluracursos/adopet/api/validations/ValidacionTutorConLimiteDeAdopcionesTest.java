package com.aluracursos.adopet.api.validations;

import com.aluracursos.adopet.api.dto.RegistroTutorDto;
import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.Adopcion;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.StatusAdopcion;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ValidacionTutorConLimiteDeAdopcionesTest {
    @Mock
    private AdopcionRepository adopcionRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private SolicitudAdopcionDTO solicitudAdopcionDTO;

    private final List<Adopcion> adopciones = new ArrayList<>();

    @InjectMocks
    private ValidacionTutorConLimiteDeAdopciones validacionTutorConLimiteDeAdopciones;

    @Test
    @DisplayName("No debería lanzar excepcion cuando el tutor no ha alcanzado el limite de adopciones")
    void escenario01() {
        // ARRANGE
        var registroTutorDTO = new RegistroTutorDto("nombreTutorTest","1234567890","test@email.com");
        var tutor = new Tutor(registroTutorDTO);

        BDDMockito.given(tutorRepository.getReferenceById(anyLong())).willReturn(tutor);

        var adopcion1 = new Adopcion(tutor,new Mascota(),"testMotivo1");
        var adopcion2 = new Adopcion(tutor,new Mascota(),"testMotivo2");
        var adopcion3 = new Adopcion(tutor,new Mascota(),"testMotivo3");
        var adopcion4 = new Adopcion(tutor,new Mascota(),"testMotivo4");

        adopciones.add(adopcion1);
        adopciones.add(adopcion2);
        adopciones.add(adopcion3);
        adopciones.add(adopcion4);

        adopciones.forEach(Adopcion::marcarComoAprobada);

        BDDMockito.given(adopcionRepository.findAll()).willReturn(adopciones);

        // ACT
        // ASSERT
        Assertions.assertThatNoException().isThrownBy(() -> validacionTutorConLimiteDeAdopciones.validar(solicitudAdopcionDTO));
    }

    @Test
    @DisplayName("Debería lanzar excepcion cuando el tutor ha alcanzado el limite de adopciones")
    void escenario02() {
        // ARRANGE
        var registroTutorDTO = new RegistroTutorDto("nombreTutorTest","1234567890","test@email.com");
        var tutor = new Tutor(registroTutorDTO);

        BDDMockito.given(tutorRepository.getReferenceById(anyLong())).willReturn(tutor);

        var adopcion1 = new Adopcion(tutor,new Mascota(),"testMotivo1");
        var adopcion2 = new Adopcion(tutor,new Mascota(),"testMotivo2");
        var adopcion3 = new Adopcion(tutor,new Mascota(),"testMotivo3");
        var adopcion4 = new Adopcion(tutor,new Mascota(),"testMotivo4");
        var adopcion5 = new Adopcion(tutor,new Mascota(),"testMotivo5");

        adopciones.add(adopcion1);
        adopciones.add(adopcion2);
        adopciones.add(adopcion3);
        adopciones.add(adopcion4);
        adopciones.add(adopcion5);

        adopciones.forEach(Adopcion::marcarComoAprobada);

        BDDMockito.given(adopcionRepository.findAll()).willReturn(adopciones);

        // ACT
        // ASSERT
        Assertions.assertThatThrownBy(() -> validacionTutorConLimiteDeAdopciones.validar(solicitudAdopcionDTO))
                .isInstanceOf(ValidacionException.class)
                .hasMessage("Tutor llegó al limite máximo de 5 adopciones!");
    }
}