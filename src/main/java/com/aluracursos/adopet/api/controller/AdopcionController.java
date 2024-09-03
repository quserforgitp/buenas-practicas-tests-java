package com.aluracursos.adopet.api.controller;

import com.aluracursos.adopet.api.dto.AprobacionAdopcionDTO;
import com.aluracursos.adopet.api.dto.ReprobacionAdopcionDTO;
import com.aluracursos.adopet.api.dto.SolicitudAdopcionDTO;
import com.aluracursos.adopet.api.exceptions.ValidacionException;
import com.aluracursos.adopet.api.service.AdopcionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adopciones")
public class AdopcionController {

    @Autowired
    private AdopcionService adopcionService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitudAdopcionDTO dto) {
        try {
            this.adopcionService.solicitar(dto);
            return ResponseEntity.ok("Solicitud enviada satisfactoriamente!");
        } catch (ValidacionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/aprobar")
    @Transactional
    public ResponseEntity<String> aprobar(@RequestBody @Valid AprobacionAdopcionDTO dto) {
        this.adopcionService.aprobar(dto);
        return ResponseEntity.ok("Solicitud aprobada satisfactoriamente!");
    }

    @PutMapping("/reprobar")
    @Transactional
    public ResponseEntity<String> reprobar(@RequestBody @Valid ReprobacionAdopcionDTO dto) {
        this.adopcionService.reprobar(dto);
        return ResponseEntity.ok("Solicitud reprobada.");
    }
}
