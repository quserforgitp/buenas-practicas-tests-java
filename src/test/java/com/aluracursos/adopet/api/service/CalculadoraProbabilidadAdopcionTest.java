package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.RegistroMascotaDto;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.ProbabilidadAdopcion;
import com.aluracursos.adopet.api.model.Refugio;
import com.aluracursos.adopet.api.model.TipoMascota;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CalculadoraProbabilidadAdopcionTest {

    private CalculadoraProbabilidadAdopcion calculadora = new CalculadoraProbabilidadAdopcion();

    private Integer JOVEN = 9;
    private Integer ADULTO = 10;
    private Integer VIEJO = 15;
    private Float PERRO_SOBRE_PESO = 16.00f;
    private Float PERRO_BUEN_PESO = 15.00f;
    private Float GATO_BUEN_PESO = 10.00f;
    private Float GATO_SOBRE_PESO = 11.00f;

    @Nested
    @DisplayName("Casos para perro")
    class CasosPerro {

        @Test
        @DisplayName("Debería devolver ALTA cuando la mascota es un Perro sin sobrepeso y además joven")
        void escenario01() {

            var probabilidadCalculada = calcularProbabilidadDeAdopcion(TipoMascota.PERRO, PERRO_BUEN_PESO, JOVEN);

            assertThat(probabilidadCalculada).isEqualTo(ProbabilidadAdopcion.ALTA);
        }

        @Test
        @DisplayName("Debería devolver ALTA cuando la mascota es un Perro con sobrepeso pero joven")
        void escenario02() {

            var probabilidadCalculada = calcularProbabilidadDeAdopcion(TipoMascota.PERRO, PERRO_SOBRE_PESO, JOVEN);

            assertThat(probabilidadCalculada).isEqualTo(ProbabilidadAdopcion.ALTA);
        }

        @Test
        @DisplayName("Debería devolver MEDIA cuando la mascota es un Perro sin sobrepeso pero adulto")
        void escenario03() {

            var probabilidadCalculada = calcularProbabilidadDeAdopcion(TipoMascota.PERRO, PERRO_BUEN_PESO, ADULTO);

            assertThat(probabilidadCalculada).isEqualTo(ProbabilidadAdopcion.MEDIA);
        }

        @Test
        @DisplayName("Debería devolver MEDIA cuando la mascota es un Perro sin sobrepeso pero viejo")
        void escenario04() {

            var probabilidadCalculada = calcularProbabilidadDeAdopcion(TipoMascota.PERRO, PERRO_BUEN_PESO, VIEJO);

            assertThat(probabilidadCalculada).isEqualTo(ProbabilidadAdopcion.MEDIA);
        }

        @Test
        @DisplayName("Debería devolver BAJA cuando la mascota es un Perro con sobrepeso y además adulto")
        void escenario05() {

            var probabilidadCalculada = calcularProbabilidadDeAdopcion(TipoMascota.PERRO, PERRO_SOBRE_PESO, ADULTO);

            assertThat(probabilidadCalculada).isEqualTo(ProbabilidadAdopcion.BAJA);
        }

        @Test
        @DisplayName("Debería devolver BAJA cuando la mascota es un Perro con sobrepeso y además viejo")
        void escenario06() {

            var probabilidadCalculada = calcularProbabilidadDeAdopcion(TipoMascota.PERRO, PERRO_SOBRE_PESO, VIEJO);

            assertThat(probabilidadCalculada).isEqualTo(ProbabilidadAdopcion.BAJA);
        }
    }

    private Mascota obtenerMascota(TipoMascota tipoMascota, Float peso, Integer edad) {
        var mascotaDTO = new RegistroMascotaDto (
                tipoMascota,
                "nombreTest",
                "razaTest",
                edad,
                "colorTest",
                peso);

        var refugioAuxiliar = new Refugio();

        return new Mascota(mascotaDTO,refugioAuxiliar);
    }
    private ProbabilidadAdopcion calcularProbabilidadDeAdopcion(TipoMascota tipoMascota, Float peso, Integer edad) {
        var mascota = obtenerMascota(tipoMascota, peso, edad);
        return calculadora.calcular(mascota);
    }
}