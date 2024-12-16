package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.model.Adopcion;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.Refugio;
import com.aluracursos.adopet.api.model.Tutor;
import com.aluracursos.adopet.api.repository.AdopcionRepository;
import com.aluracursos.adopet.api.repository.MascotaRepository;
import com.aluracursos.adopet.api.repository.TutorRepository;
import com.aluracursos.adopet.api.validations.ValidacionesSolicitudAdopcion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AdopcionServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;
    @Mock
    private AdopcionRepository adopcionRepository;
    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;
    @Mock
    private Mascota mascota;

    @Mock
    private SolicitudAdopcionDTO mockDto;

    @Spy
    private List<ValidacionesSolicitudAdopcion> validaciones = new ArrayList<>();
    @Mock
    private ValidacionesSolicitudAdopcion validador1;
    @Mock
    private ValidacionesSolicitudAdopcion validador2;


    @Mock
    private EmailService mockEmailService;

    @Mock
    private Refugio mockRefugio;

    @InjectMocks
    private AdopcionService adopcionService = new AdopcionService();

    @Captor
    private ArgumentCaptor<Adopcion> captorAdopcion;


    @Test
    @DisplayName("Debería guardar la solicitud de adopción cuando se proporciona un DTO válido")
    void solicitarEscenario01() {

        // ARRANGE
        BDDMockito.given(mascotaRepository.getReferenceById(any())).willReturn(mascota);
        BDDMockito.given(tutorRepository.getReferenceById(any())).willReturn(tutor);
        BDDMockito.given(mascota.getRefugio()).willReturn(mockRefugio);

        // ACT
        var dtoConDatosACorroborar = new SolicitudAdopcionDTO(10L,20L,"test motivo");
        adopcionService.solicitar(dtoConDatosACorroborar);

        // ASSERT
        BDDMockito.then(adopcionRepository).should(times(1)).save(captorAdopcion.capture());

        Adopcion adopcionPasadaAsave = captorAdopcion.getValue();

        Assertions.assertEquals(mascota, adopcionPasadaAsave.getMascota());
        Assertions.assertEquals(tutor, adopcionPasadaAsave.getTutor());
        Assertions.assertEquals("test motivo", adopcionPasadaAsave.getMotivo());

    }

    @Test
    @DisplayName("Debería llamar los validadores al solicitar")
    void solicitarEscenario02() {

        // ARRANGE
        BDDMockito.given(mascotaRepository.getReferenceById(any())).willReturn(mascota);
        BDDMockito.given(tutorRepository.getReferenceById(any())).willReturn(tutor);
        BDDMockito.given(mascota.getRefugio()).willReturn(mockRefugio);
        validaciones.add(validador1);
        validaciones.add(validador2);

        // ACT
        var dtoConDatosACorroborar = new SolicitudAdopcionDTO(10L,20L,"test motivo");
        adopcionService.solicitar(dtoConDatosACorroborar);

        // ASSERT
        BDDMockito.then(validador1).should(times(1)).validar(dtoConDatosACorroborar);
        BDDMockito.then(validador2).should(times(1)).validar(dtoConDatosACorroborar);
    }
}