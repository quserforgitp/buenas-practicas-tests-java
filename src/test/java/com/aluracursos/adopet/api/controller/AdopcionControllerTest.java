package com.aluracursos.adopet.api.controller;

import com.aluracursos.adopet.api.dto.AprobacionAdopcionDTO;
import com.aluracursos.adopet.api.dto.ReprobacionAdopcionDTO;
import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.service.AdopcionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AdopcionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdopcionService adopcionService;

    @Autowired
    private JacksonTester<SolicitudAdopcionDTO> jsonSolicitudAdopcionDTO;

    @Autowired
    private JacksonTester<AprobacionAdopcionDTO> jsonAprobacionAdopcionDTO;

    @Autowired
    private JacksonTester<ReprobacionAdopcionDTO> jsonReprobacionAdopcionDTO;

    private final String ENDPOINT_BASE = "/adopciones";
    private final String ENDPOINT_SOLICITAR = ENDPOINT_BASE;
    private final String ENDPOINT_APROBAR = ENDPOINT_BASE + "/aprobar";
    private final String ENDPOINT_REPROBAR = ENDPOINT_BASE + "/reprobar";

    @Nested
    @DisplayName("Casos para método solicitar")
    class Solicitar {
        @Test
        @DisplayName("Deberia de devolver codigo 400 para solicitud con errores")
        void escenario1() throws Exception {

            // ARRANGE
            var badJSON = obtenerJSONSolicitudDTO(null, null, null);

            // ACT
            var response = lanzarPostYObtenerResponse(badJSON, ENDPOINT_SOLICITAR);

            //ASSERT
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }

        @Test
        @DisplayName("Deberia de devolver codigo 200 para solicitud sin errores")
        void escenario2() throws Exception {

            // ARRANGE
            var goodJSON = obtenerJSONSolicitudDTO(10L, 10L, "motivoTest");

            // ACT
            var response = lanzarPostYObtenerResponse(goodJSON, ENDPOINT_SOLICITAR);

            //ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        }

        // auxiliares
        private SolicitudAdopcionDTO obtenerSolicitudDTO(Long idTutor, Long idMascota, String motivo) {
            return new SolicitudAdopcionDTO(idTutor, idMascota, motivo);
        }
        private String obtenerJSONSolicitudDTO(Long idTutor, Long idMascota, String motivo) throws IOException {

            boolean noVieneNingunValor = idTutor == null && idMascota == null && motivo == null;

            if (noVieneNingunValor)
                return "{}";

            return jsonSolicitudAdopcionDTO
                    .write(
                            obtenerSolicitudDTO(idTutor,idMascota, motivo)
                    ).getJson();
        }
    }

    @Nested
    @DisplayName("Casos para método aprobar")
    class Aprobar {

        @Test
        @DisplayName("Debería devolver 400 para solicitud con errores  ")
        void escenario1() throws Exception {
            // ARRANGE
            var badJSON = obtenerJSONAprobacionDTO(null);

            // ACT
            var response = lanzarPutYObtenerResponse(badJSON, ENDPOINT_APROBAR);

            //ASSERT
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        }

        @Test
        @DisplayName("Deberia de devolver codigo 200 para solicitud sin errores")
        void escenario2() throws Exception {
            // ARRANGE
            var goodJSON = obtenerJSONAprobacionDTO(10L);

            // ACT
            var response = lanzarPutYObtenerResponse(goodJSON, ENDPOINT_APROBAR);

            //ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        }

        // auxiliares
        private AprobacionAdopcionDTO obtenerAprobacionDTO(Long idAdopcion) {
            return new AprobacionAdopcionDTO(idAdopcion);
        }
        private String obtenerJSONAprobacionDTO(Long idAdopcion) throws IOException {

            boolean noVieneNingunValor = idAdopcion == null;

            if (noVieneNingunValor)
                return "{}";

            return jsonAprobacionAdopcionDTO
                    .write(
                            obtenerAprobacionDTO(idAdopcion)
                    ).getJson();
        }

    }

    @Nested
    @DisplayName("Casos para método reprobar")
    class Reprobar {
        @Test
        @DisplayName("Debería devolver 400 para solicitud con errores")
        void escenario1() throws Exception {
            // ARRANGE
            var badJSON = obtenerJSONReprobacionDTO(null, null);

            // ACT
            var response = lanzarPutYObtenerResponse(badJSON, ENDPOINT_REPROBAR);

            //ASSERT
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        }

        @Test
        @DisplayName("Deberia de devolver codigo 200 para solicitud sin errores")
        void escenario2() throws Exception {
            // ARRANGE
            var goodJSON = obtenerJSONReprobacionDTO(1L,"testJustificacion");

            // ACT
            var response = lanzarPutYObtenerResponse(goodJSON, ENDPOINT_APROBAR);

            //ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        }

        // auxiliares
        private ReprobacionAdopcionDTO obtenerReprobacionDTO(Long idAdopcion, String justificacion) {
            return new ReprobacionAdopcionDTO(idAdopcion, justificacion);
        }
        private String obtenerJSONReprobacionDTO(Long idAdopcion, String justificacion) throws IOException {

            boolean noVieneNingunValor = idAdopcion == null && justificacion == null;

            if (noVieneNingunValor)
                return "{}";

            return jsonReprobacionAdopcionDTO
                    .write(
                            obtenerReprobacionDTO(idAdopcion,justificacion)
                    ).getJson();
        }
    }

    // auxiliares
    private MockHttpServletResponse lanzarPostYObtenerResponse(String content, String endpoint) throws Exception {
        return mvc.perform(
                post(endpoint)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }
    private MockHttpServletResponse lanzarPutYObtenerResponse(String content, String endpoint) throws Exception {
        return mvc.perform(
                put(endpoint)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }

}