package com.aluracursos.adopet.api.validations;

import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.repository.MascotaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacionMascotaDisponibleTest {

    @Mock
    private MascotaRepository mockMascotaRepository;

    @Mock
    private Mascota mockMascota;

    @Mock
    private SolicitudAdopcionDTO mockSolicitudAdopcionDTO;

    @InjectMocks
    private ValidacionMascotaDisponible validacionMascotaDisponible = new ValidacionMascotaDisponible();

    @Test
    @DisplayName("Deberia permitir la adopcion si la mascota pedida no ha sido adoptada")
    void escenario01() {

        // GIVEN: Se configura el repositorio para devolver una mascota no adoptada
        given(mockMascotaRepository.getReferenceById(any())).willReturn(mockMascota);
        given(mockMascota.getAdoptada()).willReturn(false);

        // WHEN & THEN: Se verifica que no se lance ninguna excepción al validar la disponibilidad
        assertThatNoException().isThrownBy(() -> validacionMascotaDisponible.validar(mockSolicitudAdopcionDTO));

    }
    @Test
    @DisplayName("Deberia lanzar una excepcion si la mascota pedida ya fué adoptada")
    void escenario02() {

        // GIVEN: Se configura el repositorio para devolver una mascota ya adoptada
        given(mockMascotaRepository.getReferenceById(any())).willReturn(mockMascota);
        given(mockMascota.getAdoptada()).willReturn(true);

        // WHEN: Se intenta validar la disponibilidad de adopción
        // THEN: Verificamos que se lance una excepción
        assertThatThrownBy(() -> validacionMascotaDisponible.validar(mockSolicitudAdopcionDTO))
                .isInstanceOf(ValidacionException.class)
                .hasMessage("Mascota ya fue adoptada!");

    }
}