package com.aluracursos.adopet.api.validations;

import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.model.StatusAdopcion;
import com.aluracursos.adopet.api.repository.AdopcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacionMascotaConAdopcionEnAndamiento implements ValidacionesSolicitudAdopcion{

    @Autowired
    private AdopcionRepository adopcionRepository;

    public void validar(SolicitudAdopcionDTO dto) {
        boolean existeMascotaConAdopcionEsperandoEvaluacion =
                adopcionRepository.existsByMascotaIdAndStatus(dto.idMascota(), StatusAdopcion.ESPERANDO_EVALUACION);
        if (existeMascotaConAdopcionEsperandoEvaluacion) {
            throw new ValidacionException("Mascota ya esta esperando evaluación para ser adoptada!");
        }
    }
}
