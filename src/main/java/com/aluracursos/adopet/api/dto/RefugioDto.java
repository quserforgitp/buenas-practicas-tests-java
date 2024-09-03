package com.aluracursos.adopet.api.dto;

import com.aluracursos.adopet.api.model.Refugio;

public record RefugioDto(Long id, String nombre) {

    public RefugioDto(Refugio refugio) {
        this(refugio.getId(), refugio.getNombre());
    }
}
