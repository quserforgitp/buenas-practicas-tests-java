package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.ActualizacionTutorDto;
import com.aluracursos.adopet.api.dto.RegistroTutorDto;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.Tutor;
import com.aluracursos.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {
    @Mock
    private TutorRepository repository;

    @Captor
    private ArgumentCaptor<Tutor> tutorCaptor;

    @Captor
    private ArgumentCaptor<ActualizacionTutorDto> tutorActualizacionDTOCaptor;

    @Spy
    private Tutor spyTutor;

    @InjectMocks
    private TutorService service;

    @Nested
    @DisplayName("Casos para método registrar")
    class Registrar {

        @Test
        @DisplayName("Debería registrar un tutor si los datos informados no existen para otro tutor ya registrado")
        void escenario01() {
            // ARRANGE
            var registroTutorDTO = new RegistroTutorDto("nombreTest","1234567890","test@email.com");
            var tutor = new Tutor(registroTutorDTO);

            BDDMockito.given(repository.findByTelefonoOrEmail(anyString(), anyString())).willReturn(false);

            // ACT
            service.registrar(registroTutorDTO);

            // ASSERT
            BDDMockito.then(repository).should(times(1)).save(tutorCaptor.capture());
            var tutorEnviadoAguardar = tutorCaptor.getValue();

            Assertions.assertAll(
                    () -> assertEquals(tutor.getId(), tutorEnviadoAguardar.getId(), "El id del tutor no coincide"),
                    () -> assertEquals(tutor.getNombre(), tutorEnviadoAguardar.getNombre(), "El nombre del tutor no coincide"),
                    () -> assertEquals(tutor.getTelefono(), tutorEnviadoAguardar.getTelefono(), "El telefono del tutor no coincide"),
                    () -> assertEquals(tutor.getEmail(), tutorEnviadoAguardar.getEmail(), "El email del tutor no coincide")

            );
        }

        @Test
        @DisplayName("Debería lanzar excepcion cuando el telefono informado existe para otro tutor ya registrado")
        void escenario02() {
            // ARRANGE
            var registroTutorDTO = new RegistroTutorDto("nombreTest","1234567890","test@email.com");

            BDDMockito.given(repository.findByTelefonoOrEmail(anyString(), anyString())).willReturn(true);

            // ACT
            // ASSERT
            assertThatThrownBy(() -> service.registrar(registroTutorDTO))
                    .isInstanceOf(ValidacionException.class)
                    .hasMessage("Datos ya registrados por otro tutor!");
        }

        @Test
        @DisplayName("Debería lanzar excepcion cuando el email informado existe para otro tutor ya registrado")
        void escenario03() {
            // ARRANGE
            var registroTutorDTO = new RegistroTutorDto("nombreTest","1234567890","test@email.com");

            BDDMockito.given(repository.findByTelefonoOrEmail(anyString(), anyString())).willReturn(true);

            // ACT
            // ASSERT
            assertThatThrownBy(() -> service.registrar(registroTutorDTO))
                    .isInstanceOf(ValidacionException.class)
                    .hasMessage("Datos ya registrados por otro tutor!");
        }

    }

    @Nested
    @DisplayName("Casos para método actualizar")
    class Actualizar {
        @Test
        @DisplayName("Debería mandar a actualizar los datos informados del tutor")
        void escenario01() {
            // ARRANGE
            var dtoActualizacion = new ActualizacionTutorDto(1L, "nombreTest", "1234567890", "test@email.com");
            BDDMockito.given(repository.getReferenceById(anyLong())).willReturn(spyTutor);

            // ACT
            service.actualizar(dtoActualizacion);

            // ASSERT
            BDDMockito.then(spyTutor).should(times(1)).actualizarDatos(tutorActualizacionDTOCaptor.capture());

            var datosMandadosAactualizar = tutorActualizacionDTOCaptor.getValue();

            Assertions.assertAll(
                    () -> assertEquals(dtoActualizacion.id(), datosMandadosAactualizar.id(), "El id del tutor no coincide"),
                    () -> assertEquals(dtoActualizacion.nombre(), datosMandadosAactualizar.nombre(), "El nombre del tutor no coincide"),
                    () -> assertEquals(dtoActualizacion.telefono(), datosMandadosAactualizar.telefono(), "El telefono del tutor no coincide"),
                    () -> assertEquals(dtoActualizacion.email(), datosMandadosAactualizar.email(), "El email del tutor no coincide")
            );
        }
    }
}