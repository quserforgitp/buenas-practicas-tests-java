package com.aluracursos.adopet.api.model;

import com.aluracursos.adopet.api.dto.RegistroMascotaDto;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoMascota tipo;

    private String nombre;

    private String raza;

    private Integer edad;

    private String color;

    private Float peso;

    private Boolean adoptada;

    @ManyToOne(fetch = FetchType.LAZY)
    private Refugio refugio;

    @OneToOne(mappedBy = "mascota", fetch = FetchType.LAZY)
    private Adopcion adopcion;

    public Mascota() {
    }

    public Mascota(RegistroMascotaDto dto, Refugio refugio) {
        this.tipo = dto.tipo();
        this.nombre = dto.nombre();
        this.raza = dto.raza();
        this.edad = dto.edad();
        this.color = dto.color();
        this.peso = dto.peso();
        this.refugio = refugio;
        this.adoptada = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mascota mascota = (Mascota) o;
        return Objects.equals(id, mascota.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public TipoMascota getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getColor() {
        return color;
    }

    public Float getPeso() {
        return peso;
    }

    public Boolean getAdoptada() {
        return adoptada;
    }

    public Refugio getRefugio() {
        return refugio;
    }

    public Adopcion getAdopcion() {
        return adopcion;
    }
}
