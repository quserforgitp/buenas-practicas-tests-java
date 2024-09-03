package com.aluracursos.adopet.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "adopciones")
public class Adopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tutor tutor;

    @OneToOne(fetch = FetchType.LAZY)
    private Mascota mascota;

    private String motivo;

    @Enumerated(EnumType.STRING)
    private StatusAdopcion status;

    private String justificacionStatus;

    public Adopcion(Tutor tutor, Mascota mascota, String motivo) {
        this.tutor = tutor;
        this.mascota = mascota;
        this.motivo = motivo;
        this.fecha = LocalDateTime.now();
        this.status = StatusAdopcion.ESPERANDO_EVALUACION;
    }

    public Adopcion() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopcion adopcion = (Adopcion) o;
        return Objects.equals(id, adopcion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public String getMotivo() {
        return motivo;
    }

    public StatusAdopcion getStatus() {
        return status;
    }
    public String getJustificacionStatus() {
        return justificacionStatus;
    }

    public void marcarComoAprobada() {
        this.status = StatusAdopcion.APROBADO;
    }

    public void marcarComoReprobada(@NotBlank String justificacion) {
        this.status = StatusAdopcion.REPROBADO;
        this.justificacionStatus = justificacion;
    }
}
