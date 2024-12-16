package com.aluracursos.adopet.api.controller;

import com.aluracursos.adopet.api.dto.MascotaDto;
import com.aluracursos.adopet.api.dto.RegistroMascotaDto;
import com.aluracursos.adopet.api.dto.RegistroRefugioDto;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.Refugio;
import com.aluracursos.adopet.api.model.TipoMascota;
import com.aluracursos.adopet.api.service.MascotaService;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MascotaControllerTest {

    @MockBean
    private MascotaService service;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<List<MascotaDto>> jsonListaMascotaDTO;

    private final String ENDPOINT_BASE = "/mascotas";

    @Nested
    @DisplayName("Casos para método listarTodasDisponibles")
    class ListarTodasDisponibles {
        private final List<MascotaDto> mascotas = new ArrayList<>();

        @Test
        @DisplayName("Debería devolver 200 para solicitud sin errores")
        void escenario1() throws Exception {

            // ARRANGE
            // ACT
            var response = lanzarGetYObtenerResponse(ENDPOINT_BASE);

            // ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        }

        @Test
        @DisplayName("Debería devolver una lista de mascotas para solicitud sin errores")
        void escenario2() throws Exception {

            // ARRANGE
            var mascota1 = obtenerMascota("1","1");
            var mascota2 = obtenerMascota("2", "1");

            var mascotaDto1 = new MascotaDto(mascota1);
            var mascotaDto2 = new MascotaDto(mascota2);

            this.mascotas.add(mascotaDto1);
            this.mascotas.add(mascotaDto2);

            BDDMockito.given(service.buscarMascotasDisponibles()).willReturn(mascotas);

            var jsonEsperado = jsonListaMascotaDTO
                    .write(mascotas)
                    .getJson();

            // ACT
            var response = lanzarGetYObtenerResponse(ENDPOINT_BASE);

            //ASSERT
            Assertions.assertEquals(jsonEsperado,response.getContentAsString());
        }

        @Test
        @DisplayName("Debería devolver 405 para solicitud con método no permitido")
        void escenario3() throws Exception {

            // ARRANGE
            // ACT
            var response = mvc.perform(post(ENDPOINT_BASE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andReturn()
                    .getResponse();

            //ASSERT
            Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
        }

        // auxiliar
        private MockHttpServletResponse lanzarGetYObtenerResponse(String endpoint) throws Exception {
            return mvc
                    .perform(get(endpoint))
                    .andReturn()
                    .getResponse();
        }
        private Mascota obtenerMascota(String sufijoMascota, String sufijoRefugio) {

            var registroMascotaDTO = new RegistroMascotaDto(
                    TipoMascota.PERRO,
                    "nombreMascotaTest" + sufijoMascota,
                    "razaMascotaTest" + sufijoMascota,
                    10,
                    "colorMascotaTest"  + sufijoMascota,
                    1.00F
            );

            var registroRefugioDTO = new RegistroRefugioDto(
                    "nombreRefugioTest" + sufijoRefugio,
                    "123456789",
                    "test@email.com"
            );

            var refugio = new Refugio(registroRefugioDTO);

            return new Mascota(registroMascotaDTO,refugio);
        }
    }
}