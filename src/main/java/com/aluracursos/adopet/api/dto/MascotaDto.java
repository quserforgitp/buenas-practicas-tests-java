package com.aluracursos.adopet.api.dto;

import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.TipoMascota;

public record MascotaDto(
        Long id,
        TipoMascota tipo,
        String nombre,
        String raza,
        Integer edad) {

    public MascotaDto(Mascota mascota){
        this(
                mascota.getId(),
                mascota.getTipo(),
                mascota.getNombre(),
                mascota.getRaza(),
                mascota.getEdad()
        );
    }
}
