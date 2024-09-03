package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.MascotaDto;
import com.aluracursos.adopet.api.dto.RegistroMascotaDto;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.Refugio;
import com.aluracursos.adopet.api.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    public List<MascotaDto> buscarMascotasDisponibles() {
        return mascotaRepository
                .findAllByAdoptadaFalse()
                .stream()
                .map(MascotaDto::new)
                .toList();
    }

    public void registrarMascota(Refugio refugio, RegistroMascotaDto dto) {
        mascotaRepository.save(new Mascota(dto, refugio));
    }
}
