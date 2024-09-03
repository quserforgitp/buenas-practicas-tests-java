package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.MascotaDto;
import com.aluracursos.adopet.api.dto.RefugioDto;
import com.aluracursos.adopet.api.dto.RegistroRefugioDto;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.Refugio;
import com.aluracursos.adopet.api.repository.MascotaRepository;
import com.aluracursos.adopet.api.repository.RefugioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RefugioService {

    @Autowired
    private RefugioRepository refugioRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    public List<RefugioDto> listar() {
        return refugioRepository
                .findAll()
                .stream()
                .map(RefugioDto::new)
                .toList();
    }

    public void registrar(@Valid RegistroRefugioDto dto) {
        boolean yaRegistrado = refugioRepository.existsByNombreOrTelefonoOrEmail(dto.nombre(), dto.telefono(), dto.email());
        if(yaRegistrado) {
            throw new ValidacionException("Datos ya registrados en otro refugio!");
        }
        refugioRepository.save(new Refugio(dto));
    }

    public List<MascotaDto> listarMascotasDelRefugio(String idONombre) {
        Refugio refugio = cargarRefugio(idONombre);
        return mascotaRepository
                .findByRefugio(refugio)
                .stream()
                .map(MascotaDto::new)
                .toList();
    }

    public Refugio cargarRefugio(String idONombre) {
        Optional<Refugio> optional;
        try{
            Long id = Long.parseLong(idONombre);
            optional = refugioRepository.findById(id);
        } catch (NumberFormatException exception){
            optional = refugioRepository.findByNombre(idONombre);
        }
        return optional.orElseThrow(() -> new ValidacionException("Refugio no encontrado"));
    }
}
