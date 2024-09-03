package com.aluracursos.adopet.api.service;

import com.aluracursos.adopet.api.dto.AprobacionAdopcionDTO;
import com.aluracursos.adopet.api.dto.ReprobacionAdopcionDTO;
import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.model.Adopcion;
import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.Tutor;
import com.aluracursos.adopet.api.repository.AdopcionRepository;
import com.aluracursos.adopet.api.repository.MascotaRepository;
import com.aluracursos.adopet.api.repository.TutorRepository;
import com.aluracursos.adopet.api.validations.ValidacionesSolicitudAdopcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdopcionService {

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private List<ValidacionesSolicitudAdopcion> validaciones;

    public void solicitar(SolicitudAdopcionDTO dto) {
        Mascota mascota = mascotaRepository.getReferenceById(dto.idMascota());
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

        validaciones.forEach(v -> v.validar(dto));

        Adopcion adopcion = new Adopcion(tutor, mascota, dto.motivo());

        adopcionRepository.save(adopcion);

        emailService.enviarEmail(
                adopcion.getMascota().getRefugio().getEmail(),
                "Solicitación de adopción",
                "Hola " + adopcion.getMascota().getRefugio().getNombre() +"!\n\nUna solicitud de adopción fue registrada hoy para la mascota: " + adopcion.getMascota().getNombre() +". \nPor favor, evaluarla para aprobación o reprobación."
        );
    }

    public void aprobar(AprobacionAdopcionDTO dto) {
        Adopcion adopcion = adopcionRepository.getReferenceById(dto.idAdopcion());
        adopcion.marcarComoAprobada();

        emailService.enviarEmail(
                adopcion.getTutor().getEmail(),
                "Adopción aprobada",
                "Felicitaciones " + adopcion.getTutor().getNombre() +"!\n\nSu adopción de la mascota " + adopcion.getMascota().getNombre() +", solicitada el dia " + adopcion.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", fue aprobada.\nPor favor, entrar en contacto con el refugio " + adopcion.getMascota().getRefugio().getNombre() +" para ir a buscar a su mascota."
        );
    }

    public void reprobar(ReprobacionAdopcionDTO dto) {
        Adopcion adopcion = adopcionRepository.getReferenceById(dto.idAdopcion());
        adopcion.marcarComoReprobada(dto.justificacion());

        emailService.enviarEmail(
                adopcion.getTutor().getEmail(),
                "Adopción reprobada",
                "Hola " + adopcion.getTutor().getNombre() +"!\n\nInfelizmente su adopción de la mascota " + adopcion.getMascota().getNombre() +", solicitada el dia " + adopcion.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", fue reprobada por el refugio " + adopcion.getMascota().getRefugio().getNombre() +" con la seguiente justificativa: " + adopcion.getJustificacionStatus()
        );
    }
}
