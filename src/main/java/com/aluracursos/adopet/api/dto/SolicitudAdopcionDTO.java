package com.aluracursos.adopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitudAdopcionDTO(
        @NotNull Long idTutor,
        @NotNull Long idMascota,
        @NotBlank String motivo
) {
}
