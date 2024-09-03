package com.aluracursos.adopet.api.model;

import com.aluracursos.adopet.api.dto.ActualizacionTutorDto;
import com.aluracursos.adopet.api.dto.RegistroTutorDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tutores")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String telefono;

    private String email;

    @OneToMany(mappedBy = "tutor")
    private List<Adopcion> adopciones = new ArrayList<>();

    public Tutor() {
    }

    public Tutor(RegistroTutorDto dto) {
        this.nombre = dto.nombre();
        this.telefono = dto.telefono();
        this.email = dto.email();
    }

    public void actualizarDatos(ActualizacionTutorDto dto) {
        this.nombre = dto.nombre();
        this.email = dto.email();
        this.telefono = dto.telefono();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tutor tutor = (Tutor) o;
        return Objects.equals(id, tutor.id);
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

    public List<Adopcion> getAdopciones() {
        return adopciones;
    }
}
