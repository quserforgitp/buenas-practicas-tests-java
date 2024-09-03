package com.aluracursos.adopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReprobacionAdopcionDTO(
        @NotNull Long idAdopcion,
        @NotBlank String justificacion
) {
}
