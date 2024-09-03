package com.aluracursos.adopet.api.validations;

import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.Adopcion;
import com.aluracursos.adopet.api.model.StatusAdopcion;
import com.aluracursos.adopet.api.model.Tutor;
import com.aluracursos.adopet.api.repository.AdopcionRepository;
import com.aluracursos.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacionTutorConAdopcionEnAndamiento implements ValidacionesSolicitudAdopcion {

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private TutorRepository tutorRepository;

    public void validar(SolicitudAdopcionDTO dto) {
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());
        List<Adopcion> adopciones = adopcionRepository.findAll();
        for (Adopcion a : adopciones) {
            if (a.getTutor() == tutor && a.getStatus() == StatusAdopcion.ESPERANDO_EVALUACION) {
                throw new ValidacionException("Tutor ya tiene otra adopción esperando evaluación!");
            }
        }
    }
}
