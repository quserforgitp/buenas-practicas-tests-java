package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.MascotaDto;
import com.aluracursos.adopet.api.dto.RefugioDto;
import com.aluracursos.adopet.api.dto.RegistroMascotaDto;
import com.aluracursos.adopet.api.dto.RegistroRefugioDto;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.Refugio;
import com.aluracursos.adopet.api.model.TipoMascota;
import com.aluracursos.adopet.api.repository.MascotaRepository;
import com.aluracursos.adopet.api.repository.RefugioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RefugioServiceTest {

    @Mock
    private RefugioRepository refugioRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @Captor
    private ArgumentCaptor<Refugio> refugioCaptor;

    @InjectMocks
    @Spy
    private RefugioService service;

    private final List<Refugio> refugios = new ArrayList<>();
    private List<RefugioDto> refugiosDTO = new ArrayList<>();
    private final List<Mascota> mascotas = new ArrayList<>();
    private List<MascotaDto> mascotasDTO = new ArrayList<>();

    @Nested
    @DisplayName("Casos para método listar")
    class Listar {

        @Test
        @DisplayName("Debería devolver una lista con todos los refugios registrados")
        void escenario01() {
            // ARRANGE
            var registroRefugioDTO1 = new RegistroRefugioDto("nombreRefugioTest1","1234567890","refugioTest@email.com");
            var registroRefugioDTO2 = new RegistroRefugioDto("nombreRefugioTest2","1234567890","refugioTest@email.com");
            var registroRefugioDTO3 = new RegistroRefugioDto("nombreRefugioTest3","1234567890","refugioTest@email.com");

            var refugio1 = new Refugio(registroRefugioDTO1);
            var refugio2 = new Refugio(registroRefugioDTO2);
            var refugio3 = new Refugio(registroRefugioDTO3);

            refugios.add(refugio1);
            refugios.add(refugio2);
            refugios.add(refugio3);

            BDDMockito.given(refugioRepository.findAll()).willReturn(refugios);

            refugiosDTO = refugios
                    .stream()
                    .map(RefugioDto::new)
                    .toList();

            // ACT
            var refugiosDTODevueltos = service.listar();

            // ASSERT
            Assertions.assertAll(
                    () -> assertEquals(refugiosDTO.get(0).nombre(), refugiosDTODevueltos.get(0).nombre(), "El nombre del primer refugio no coincide"),
                    () -> assertEquals(refugiosDTO.get(1).nombre(), refugiosDTODevueltos.get(1).nombre(), "El nombre del segundo refugio no coincide"),
                    () -> assertEquals(refugiosDTO.get(2).nombre(), refugiosDTODevueltos.get(2).nombre(), "El nombre del tercer refugio no coincide")
            );
        }
    }

    @Nested
    @DisplayName("Casos para método registrar")
    class Registrar {

        @Test
        @DisplayName("Debería guardar el refugio si aun no ha sido registrado otro con los mismos datos")
        void escenario01() {
            // ARRANGE
            var dto = new RegistroRefugioDto("nombreRefugioTest1","1234567890","refugioTest@email.com");
            var refugio = new Refugio(dto);

            BDDMockito.given(refugioRepository.existsByNombreOrTelefonoOrEmail(dto.nombre(), dto.telefono(), dto.email())).willReturn(false);

            // ACT
            service.registrar(dto);

            // ASSERT
            BDDMockito.then(refugioRepository).should(times(1)).save(refugioCaptor.capture());
            var refugioEnviadoAguardar = refugioCaptor.getValue();

            Assertions.assertAll(
                    () -> assertEquals(refugio.getNombre(), refugioEnviadoAguardar.getNombre(), "El nombre del refugio no coincide"),
                    () -> assertEquals(refugio.getTelefono(), refugioEnviadoAguardar.getTelefono(), "El telefono del refugio no coincide"),
                    () -> assertEquals(refugio.getEmail(), refugioEnviadoAguardar.getEmail(), "El email del refugio no coincide")
            );
        }

        @Test
        @DisplayName("Debería lanzar excepción si ya existe un refugio registrado los los datos informados")
        void escenario02() {
            // ARRANGE
            var dto = new RegistroRefugioDto("nombreRefugioTest1","1234567890","refugioTest@email.com");

            BDDMockito.given(refugioRepository.existsByNombreOrTelefonoOrEmail(dto.nombre(), dto.telefono(), dto.email())).willReturn(true);

            // ACT
            // ASSERT
            assertThatThrownBy(() -> service.registrar(dto))
                    .isInstanceOf(ValidacionException.class)
                    .hasMessage("Datos ya registrados en otro refugio!");
        }
    }

    @Nested
    @DisplayName("Casos para método listarMascotasDelRefugio")
    class ListarMascotasDelRefugio {

        @Test
        @DisplayName("Debería devolver una lista de mascotas cuando el refugio informado existe")
        void escenario01() {
            // ARRANGE
            String idONombre = "1";
            var refugio = new Refugio();

            var registroMascota1DTO = new RegistroMascotaDto(TipoMascota.PERRO, "nombreMascotaTest1", "razaTest1", 1, "colorTest1", 1.00F);
            var mascota1 = new Mascota(registroMascota1DTO, refugio);

            var registroMascota2DTO = new RegistroMascotaDto(TipoMascota.PERRO, "nombreMascotaTest2", "razaTest2", 2, "colorTest2", 2.00F);
            var mascota2 = new Mascota(registroMascota2DTO, refugio);

            mascotas.add(mascota1);
            mascotas.add(mascota2);

            Mockito.doReturn(refugio).when(service).cargarRefugio(idONombre);
            BDDMockito.given(mascotaRepository.findByRefugio(refugio)).willReturn(mascotas);

            mascotasDTO = mascotas
                    .stream()
                    .map(MascotaDto::new)
                    .toList();

            // ACT
            var mascotasDevueltas = service.listarMascotasDelRefugio(idONombre);

            // ASSERT
            Assertions.assertAll(
                    // mascota 1
                    () -> assertEquals(mascotasDTO.get(0).tipo(), mascotasDevueltas.get(0).tipo(), "El tipo de mascota de la primer mascota no coincide"),
                    () -> assertEquals(mascotasDTO.get(0).nombre(), mascotasDevueltas.get(0).nombre(), "El nombre de la primer mascota no coincide"),
                    () -> assertEquals(mascotasDTO.get(0).raza(), mascotasDevueltas.get(0).raza(), "La raza de la primer mascota no coincide"),
                    () -> assertEquals(mascotasDTO.get(0).edad(), mascotasDevueltas.get(0).edad(), "La edad de la primer mascota no coincide")
            );

            Assertions.assertAll(
                    // mascota 2
                    () -> assertEquals(mascotasDTO.get(1).tipo(), mascotasDevueltas.get(1).tipo(), "El tipo de mascota de la segunda mascota no coincide"),
                    () -> assertEquals(mascotasDTO.get(1).nombre(), mascotasDevueltas.get(1).nombre(), "El nombre de la segunda mascota no coincide"),
                    () -> assertEquals(mascotasDTO.get(1).raza(), mascotasDevueltas.get(1).raza(), "La raza de la segunda mascota no coincide"),
                    () -> assertEquals(mascotasDTO.get(1).edad(), mascotasDevueltas.get(1).edad(), "La edad de la segunda mascota no coincide")
            );

        }
    }

    @Nested
    @DisplayName("Casos para método cargarRefugio")
    class CargarRefugio {

        @Test
        @DisplayName("Debería devolver un refugio cuando lo encuentra por ID")
        void escenario01() {
            // ARRANGE
            String id = "1";
            var registroRefugioDTO = new RegistroRefugioDto("nombreRefugioTest","1234567890","refugioTest@email.com");

            var refugio = new Refugio(registroRefugioDTO);

            var optional = Optional.of(refugio);

            BDDMockito.given(refugioRepository.findById(anyLong())).willReturn(optional);

            // ACT
            var refugioDevuelto = service.cargarRefugio(id);

            // ASSERT
            Assertions.assertAll(
                    () -> assertEquals(refugio.getNombre(), refugioDevuelto.getNombre(), "El nombre del refugio no coincide"),
                    () -> assertEquals(refugio.getTelefono(), refugioDevuelto.getTelefono(), "El telefono del refugio no coincide"),
                    () -> assertEquals(refugio.getEmail(), refugioDevuelto.getEmail(), "El email del refugio no coincide")

            );
            assertThatNoException().isThrownBy(() -> service.cargarRefugio(id));
        }

        @Test
        @DisplayName("Debería devolver un refugio cuando lo encuentra por nombre")
        void escenario02() {
            // ARRANGE
            String nombreRefugio = "nombreRefugioTest";
            var registroRefugioDTO = new RegistroRefugioDto("nombreRefugioTest","1234567890","refugioTest@email.com");

            var refugio = new Refugio(registroRefugioDTO);

            var optional = Optional.of(refugio);

            BDDMockito.given(refugioRepository.findByNombre(anyString())).willReturn(optional);

            // ACT
            var refugioDevuelto = service.cargarRefugio(nombreRefugio);

            // ASSERT
            Assertions.assertAll(
                    () -> assertEquals(refugio.getNombre(), refugioDevuelto.getNombre(), "El nombre del refugio no coincide"),
                    () -> assertEquals(refugio.getTelefono(), refugioDevuelto.getTelefono(), "El telefono del refugio no coincide"),
                    () -> assertEquals(refugio.getEmail(), refugioDevuelto.getEmail(), "El email del refugio no coincide")

            );
            assertThatNoException().isThrownBy(() -> service.cargarRefugio(nombreRefugio));
        }

        @Test
        @DisplayName("Debería lanzar una excepcion cuando no encuentra el refugio por nombre")
        void escenario03() {
            // ARRANGE
            String nombreRefugio = "nombreRefugioTest";

            Optional<Refugio> refugioOptional = Optional.empty();

            BDDMockito.given(refugioRepository.findByNombre(anyString())).willReturn(refugioOptional);

            // ACT
            // ASSERT
            assertThatThrownBy(() -> service.cargarRefugio(nombreRefugio))
                    .isInstanceOf(ValidacionException.class)
                    .hasMessage("Refugio no encontrado");
        }

        @Test
        @DisplayName("Debería lanzar una excepcion cuando no encuentra el refugio por id")
        void escenario04() {
            // ARRANGE
            String id = "1";

            Optional<Refugio> refugioOptional = Optional.empty();

            BDDMockito.given(refugioRepository.findById(anyLong())).willReturn(refugioOptional);

            // ACT
            // ASSERT
            assertThatThrownBy(() -> service.cargarRefugio(id))
                    .isInstanceOf(ValidacionException.class)
                    .hasMessage("Refugio no encontrado");
        }
    }
}