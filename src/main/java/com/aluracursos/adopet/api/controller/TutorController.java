package com.aluracursos.adopet.api.controller;

import com.aluracursos.adopet.api.dto.ActualizacionTutorDto;
import com.aluracursos.adopet.api.dto.RegistroTutorDto;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> registrar(@RequestBody @Valid RegistroTutorDto dto) {
        try{
            service.registrar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacionException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> actualizar(@RequestBody @Valid ActualizacionTutorDto dto) {
        try{
            service.actualizar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacionException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

}
