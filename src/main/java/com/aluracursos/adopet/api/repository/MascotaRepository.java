package com.aluracursos.adopet.api.repository;

import com.aluracursos.adopet.api.model.Mascota;
import com.aluracursos.adopet.api.model.Refugio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findAllByAdoptadaFalse();

    List<Mascota> findByRefugio(Refugio refugio);
}
