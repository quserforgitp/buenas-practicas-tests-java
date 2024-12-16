package com.aluracursos.adopet.api.controller;

import com.aluracursos.adopet.api.dto.ActualizacionTutorDto;
import com.aluracursos.adopet.api.dto.RegistroTutorDto;
import com.aluracursos.adopet.api.service.TutorService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TutorControllerTest {

    private final String ENDPOINT_BASE = "/tutores";

    @MockBean
    private TutorService service;

    @Autowired
    private JacksonTester<RegistroTutorDto> jsonRegistroTutorDTO;
    @Autowired
    private JacksonTester<ActualizacionTutorDto> jsonActualizacionTutorDTO;
    @Autowired
    private MockMvc mvc;

    @Nested
    @DisplayName("Casos para el método registrar")
    class Registrar {
        @Test
        @DisplayName("Debería devolver 200 para solicitudes sin errores")
        void escenario1() throws Exception {
            // ARRANGE
            var registroTutorDTO = new RegistroTutorDto("nombreTutorTest","1234567890","test@email.com");
            var jsonValido = jsonRegistroTutorDTO.write(registroTutorDTO).getJson();


            // ACT
            var response = mvc.perform(
                    post(ENDPOINT_BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonValido)
            ).andReturn().getResponse();

            // ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        }

        @Test
        @DisplayName("Debería devolver 400 para solicitudes com errores")
        void escenario2() throws Exception {
            // ARRANGE
            var jsonInvalido = "{}";


            // ACT
            var response = mvc.perform(
                    post(ENDPOINT_BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonInvalido)
            ).andReturn().getResponse();

            // ASSERT
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        }

    }

    @Nested
    @DisplayName("Casos para el método actualizar")
    class Actualizar {

        @Test
        @DisplayName("Debería devolver 200 para solicitudes sin errores")
        void escenario1() throws Exception {
            // ARRANGE
            var actualizacionTutorDTO = new ActualizacionTutorDto(10L,"nombreTutorTest","1234567890","test@email.com");
            var jsonValido = jsonActualizacionTutorDTO.write(actualizacionTutorDTO).getJson();


            // ACT
            var response = mvc.perform(
                    put(ENDPOINT_BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonValido)
            ).andReturn().getResponse();

            // ASSERT
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        }

        @Test
        @DisplayName("Debería devolver 400 para solicitudes com errores")
        void escenario2() throws Exception {
            // ARRANGE
            var jsonInvalido = "{}";


            // ACT
            var response = mvc.perform(
                    put(ENDPOINT_BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonInvalido)
            ).andReturn().getResponse();

            // ASSERT
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        }

    }
}