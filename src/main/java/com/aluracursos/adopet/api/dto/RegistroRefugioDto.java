package com.aluracursos.adopet.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistroRefugioDto(
        @NotBlank
        String nombre,
        @NotBlank
        @Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}")
        String telefono,
        @NotBlank
        @Email
        String email
) {

}
