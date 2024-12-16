package com.aluracursos.adopet.api.controller;

import com.aluracursos.adopet.api.dto.MascotaDto;
import com.aluracursos.adopet.api.dto.RefugioDto;
import com.aluracursos.adopet.api.dto.RegistroMascotaDto;
import com.aluracursos.adopet.api.dto.RegistroRefugioDto;
import com.aluracursos.adopet.api.model.TipoMascota;
import com.aluracursos.adopet.api.service.MascotaService;
import com.aluracursos.adopet.api.service.RefugioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class RefugioControllerTest {

    @Autowired
    private JacksonTester<List<RefugioDto>> jsonRefugiosDTO;

    @Autowired
    private JacksonTester<List<MascotaDto>> jsonMascotasDTO;
    @Autowired
    private JacksonTester<RegistroMascotaDto> jsonRegistroMascotaDTO;

    @MockBean
    private RefugioService refugioService;
    @MockBean
    private MascotaService mascotaService;

    @Autowired
    private MockMvc mvc;

    private final String ENDPOINT_BASE = "/refugios";
    private final String ENDPOINT_MASCOTAS = "/mascotas";

    @Nested
    @DisplayName("Casos para método listar")
    class Listar {

        private final List<RefugioDto> refugios = new ArrayList<>();

        @Test
        @DisplayName("Deberia devolver 200 para petitción sin errores")
        void escenario1() throws Exception {

            // ARRANGE
            // ACT
            var response = mvc.perform(get(ENDPOINT_BASE))
                    .andReturn().
                    getResponse();
            // ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        }

        @Test
        @DisplayName("Deberia devolver una lista de refugios para petitción sin errores")
        void escenario2() throws Exception {

            // ARRANGE
            var refugio1DTO = new RefugioDto(1L,"nombreRefugioTest1");
            var refugio2DTO = new RefugioDto(2L,"nombreRefugioTest2");

            this.refugios.add(refugio1DTO);
            this.refugios.add(refugio2DTO);

            BDDMockito.given(refugioService.listar()).willReturn(refugios);

            var jsonEsperado = jsonRefugiosDTO.write(refugios).getJson();

            // ACT
            var response = mvc.perform(get(ENDPOINT_BASE))
                    .andReturn().
                    getResponse();
            // ASSERT
            Assertions.assertEquals(jsonEsperado, response.getContentAsString());
        }
    }

    @Nested
    @DisplayName("Casos para método registrar")
    class Registrar {
        @Test
        @DisplayName("Debería devolver 200 para petición sin errores")
        void escenario1() throws Exception {
            // ARRANGE
            var goodJSON = """
                        {
                            "nombre": "nombreRefugioTest",
                            "telefono": "1234567890",
                            "email": "refugioTest@email.com"
                        }
                    """;

            // ACT
            var response = mvc.perform(
                    post(ENDPOINT_BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(goodJSON)
                    ).andReturn().getResponse();

            // ASSERT
            System.out.println(response.getContentAsString());
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        }

        @Test
        @DisplayName("Debería devolver 400 para petición con errores")
        void escenario2() throws Exception {
            // ARRANGE
            var badJSON = "{}";

            // ACT
            var response = mvc.perform(
                    post(ENDPOINT_BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(badJSON)
            ).andReturn().getResponse();

            // ASSERT
            System.out.println(response.getContentAsString());
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }
    }

    @Nested
    @DisplayName("Casos para método listarMascotas")
    class ListarMascotas {

        private final List<MascotaDto> mascotas = new ArrayList<>();

        @Test
        @DisplayName("Deberia devolver 200 para solicitud sin errores")
        void escenario1 () throws Exception {
            // ARRANGE
            String idRefugio = "10";

            final String ENDPOINT = ENDPOINT_BASE + "/" + idRefugio + ENDPOINT_MASCOTAS;

            // ACT
            var response = mvc.perform(get(ENDPOINT)).andReturn().getResponse();

            // ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        }

        @Test
        @DisplayName("Deberia devolver una lista de mascotas para solicitud sin errores")
        void escenario2() throws Exception {
            // ARRANGE
            String idRefugio = "10";

            final String ENDPOINT = ENDPOINT_BASE + "/" + idRefugio + ENDPOINT_MASCOTAS;

            var mascota1DTO = new MascotaDto(1L, TipoMascota.PERRO,"nombreMascotaTest1","razaTest1",1);
            var mascota2DTO = new MascotaDto(2L, TipoMascota.GATO,"nombreMascotaTest2","razaTest2",2);

            mascotas.add(mascota1DTO);
            mascotas.add(mascota2DTO);

            BDDMockito.given(refugioService.listarMascotasDelRefugio(anyString())).willReturn(mascotas);

            var jsonEsperado =  jsonMascotasDTO.write(mascotas).getJson();

            // ACT
            var response = mvc.perform(get(ENDPOINT)).andReturn().getResponse();

            // ASSERT
            Assertions.assertEquals(jsonEsperado, response.getContentAsString());
        }
    }

    @Nested
    @DisplayName("Casos para método registrarMascota")
    class RegistrarMascota {

        @Test
        @DisplayName("Debería devolver codigo 200 para peticion sin errores")
        void escenario1() throws Exception {

            // ARRANGE
            var registroMascotaDTO = new RegistroMascotaDto(TipoMascota.PERRO, "nombreMascotaTest","razaTest",10,"colorTest",12.00F);
            var goodJSON = jsonRegistroMascotaDTO.write(registroMascotaDTO).getJson();
            String idRefugio = "10";
            var endpointURL = ENDPOINT_BASE + "/" + idRefugio + ENDPOINT_MASCOTAS;

            // ACT
            var response = mvc.perform(
                    post(endpointURL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(goodJSON)
            ).andReturn().getResponse();

            // ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        }

        @Test
        @DisplayName("Debería devolver codigo 400 para peticion con errores")
        void escenario2() throws Exception {

            // ARRANGE
            var badJSON = "{}";

            String idRefugio = "10";
            var endpointURL = ENDPOINT_BASE + "/" + idRefugio + ENDPOINT_MASCOTAS;

            // ACT
            var response = mvc.perform(
                    post(endpointURL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(badJSON)
            ).andReturn().getResponse();

            // ASSERT
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }


    }

}