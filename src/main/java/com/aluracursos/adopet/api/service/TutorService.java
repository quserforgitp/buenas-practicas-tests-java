package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.ActualizacionTutorDto;
import com.aluracursos.adopet.api.dto.RegistroTutorDto;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.Tutor;
import com.aluracursos.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    @Autowired
    public TutorRepository repository;

    public void registrar(RegistroTutorDto dto) {
        boolean yaRegistrado = repository.findByTelefonoOrEmail(dto.telefono(), dto.email());

        if (yaRegistrado){
            throw new ValidacionException("Datos ya registrados por otro tutor!");
        }

        repository.save(new Tutor(dto));
    }

    public void actualizar(ActualizacionTutorDto dto) {
        Tutor tutor = repository.getReferenceById(dto.id());
        tutor.actualizarDatos(dto);
    }
}
