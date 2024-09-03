package com.aluracursos.adopet.api.repository;

import com.aluracursos.adopet.api.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    boolean findByTelefonoOrEmail(String telefono, String email);

}
