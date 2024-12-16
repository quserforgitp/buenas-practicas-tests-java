package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.RegistroMascotaDto;
import com.aluracursos.adopet.api.dto.RegistroRefugioDto;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.Refugio;
import com.aluracursos.adopet.api.model.TipoMascota;
import com.aluracursos.adopet.api.repository.MascotaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository repository;

    @InjectMocks
    private MascotaService service;

    private final List<Mascota> mascotas = new ArrayList<>();

    @Nested
    @DisplayName("Casos para método buscarMascotasDisponibles")
    class BuscarMascotaDisponible {

        @Test
        @DisplayName("Debería devolver una lista de mascotas disponibles")
        void escenario01() {
            // ARRANGE
            var mascota1 = new Mascota();
            var mascota2 = new Mascota();

            mascotas.add(mascota1);
            mascotas.add(mascota2);

            BDDMockito.given(repository.findAllByAdoptadaFalse()).willReturn(mascotas);

            // ACT
            var mascotasDevueltasDisponibles = service.buscarMascotasDisponibles();

            // ASSERT
            assertEquals(mascotasDevueltasDisponibles.size(), mascotas.size());
        }

        @Test
        @DisplayName("Debería devolver una lista vacía si no hay mascotas disponibles")
        void escenario02() {

            // ARRANGE
            BDDMockito.given(repository.findAllByAdoptadaFalse()).willReturn(mascotas);

            // ACT
            var mascotasDevueltasDisponibles = service.buscarMascotasDisponibles();

            // ASSERT
            assertEquals(mascotasDevueltasDisponibles.size(), mascotas.size());
        }
    }

    @Nested
    @DisplayName("Casos para método registrarMascota")
    class RegistrarMascota {
        @Captor
        private ArgumentCaptor<Mascota> mascotaCaptor;

        @Test
        @DisplayName("Debería registrar una mascota con los datos informados")
        void escenario01() {
            // ARRANGE
            String nombreRefugio = "nombreRefugioTest";
            String telefonoRefugio = "1234567890";
            String emailRefugio = "testRefugio@email.com";

            var registroRefugioDTO = new RegistroRefugioDto(nombreRefugio, telefonoRefugio, emailRefugio);
            var refugio = new Refugio(registroRefugioDTO);

            final TipoMascota tipoMascota =  TipoMascota.PERRO;
            final String nombreMascota = "nombreMascotaTest";
            final String razaMascota = "razaTest";
            final Integer edadMascota = 12;
            final String colorMascota = "colorTest";
            final Float pesoMascota = 10.00F;

            var registroMascotaDTO = new RegistroMascotaDto(tipoMascota, nombreMascota, razaMascota, edadMascota, colorMascota, pesoMascota);
            var mascota = new Mascota(registroMascotaDTO, refugio);

            // ACT
            service.registrarMascota(refugio,registroMascotaDTO);

            // ASSERT
            BDDMockito.then(repository).should(times(1)).save(mascotaCaptor.capture());
            var mascotaEnviadaAGuardar = mascotaCaptor.getValue();
            var refugioEnviadoAGuardar = mascotaEnviadaAGuardar.getRefugio();

            Assertions.assertAll(
                    // refugio
                    () -> assertEquals(refugio.getNombre(), refugioEnviadoAGuardar.getNombre(), "El nombre del refugio no coincide"),
                    () -> assertEquals(refugio.getTelefono(), refugioEnviadoAGuardar.getTelefono(), "El teléfono del refugio no coincide"),
                    () -> assertEquals(refugio.getEmail(), refugioEnviadoAGuardar.getEmail(), "El email del refugio no coincide")
            );

            Assertions.assertAll(
                    // mascota
                    () -> assertEquals(mascota.getTipo(), mascotaEnviadaAGuardar.getTipo(), "El tipo de la mascota no coincide"),
                    () -> assertEquals(mascota.getNombre(), mascotaEnviadaAGuardar.getNombre(), "El nombre de la mascota no coincide"),
                    () -> assertEquals(mascota.getRaza(), mascotaEnviadaAGuardar.getRaza(), "La raza de la mascota no coincide"),
                    () -> assertEquals(mascota.getEdad(), mascotaEnviadaAGuardar.getEdad(), "La edad de la mascota no coincide"),
                    () -> assertEquals(mascota.getColor(), mascotaEnviadaAGuardar.getColor(), "El color de la mascota no coincide"),
                    () -> assertEquals(mascota.getPeso(), mascotaEnviadaAGuardar.getPeso(), "El peso de la mascota no coincide")
            );


        }
    }
}