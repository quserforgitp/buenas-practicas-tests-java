package com.aluracursos.adopet.api.dto;

import com.aluracursos.adopet.api.model.TipoMascota;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroMascotaDto(
        @NotNull
        TipoMascota tipo,
        @NotBlank
        String nombre,
        @NotBlank
        String raza,
        @NotNull
        Integer edad,
        @NotBlank
        String color,
        @NotNull
        Float peso
) {
}
