package com.aluracursos.adopet.api.validations;

import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.repository.AdopcionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ValidacionMascotaConAdopcionEnAndamientoTest {

    @Mock
    private SolicitudAdopcionDTO solicitudAdopcionDTO;

    @Mock
    private AdopcionRepository adopcionRepository;

    @InjectMocks
    private ValidacionMascotaConAdopcionEnAndamiento validacionMascotaConAdopcionEnAndamiento;

    @Test
    @DisplayName("No debería lanzar excepción cuando cuando la mascota no está esperando evaluación para ser adoptada")
    void escenario01() {
        // ARRANGE
        BDDMockito.given(adopcionRepository.existsByMascotaIdAndStatus(anyLong(),any())).willReturn(false);

        // ACT
        // ASSERT
        Assertions.assertThatNoException().isThrownBy(() -> validacionMascotaConAdopcionEnAndamiento.validar(solicitudAdopcionDTO));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando cuando la mascota está esperando evaluación para ser adoptada")
    void escenario02() {
        // ARRANGE
        BDDMockito.given(adopcionRepository.existsByMascotaIdAndStatus(anyLong(),any())).willReturn(true);

        // ACT
        // ASSERT
        Assertions.assertThatThrownBy(() -> validacionMascotaConAdopcionEnAndamiento.validar(solicitudAdopcionDTO))
                .isInstanceOf(ValidacionException.class)
                .hasMessage("Mascota ya esta esperando evaluación para ser adoptada!");
    }
}