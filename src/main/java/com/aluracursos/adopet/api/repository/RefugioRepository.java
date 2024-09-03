package com.aluracursos.adopet.api.repository;

import com.aluracursos.adopet.api.model.Refugio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefugioRepository extends JpaRepository<Refugio, Long> {

    Optional<Refugio> findByNombre(String nombre);

    boolean existsByNombreOrTelefonoOrEmail(String nombre, String telefono, String email);
}
