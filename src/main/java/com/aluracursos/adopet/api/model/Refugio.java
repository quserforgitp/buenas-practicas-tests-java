package com.aluracursos.adopet.api.model;

import com.aluracursos.adopet.api.dto.RegistroRefugioDto;
import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.util.Objects;

@Entity
@Table(name = "refugios")
public class Refugio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String telefono;

    private String email;

    public Refugio() {
    }

    public Refugio(@Valid RegistroRefugioDto dto) {
        this.nombre = dto.nombre();
        this.telefono = dto.telefono();
        this.email = dto.email();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Refugio refugio = (Refugio) o;
        return Objects.equals(id, refugio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }
}
