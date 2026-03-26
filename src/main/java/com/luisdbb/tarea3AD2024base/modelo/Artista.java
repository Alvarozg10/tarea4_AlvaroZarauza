package com.luisdbb.tarea3AD2024base.modelo;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("ARTISTA")
public class Artista extends Persona {

    private String apodo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "artista_especialidad",
        joinColumns = @JoinColumn(name = "artista_id")
    )
    @Column(name = "especialidad")
    @Enumerated(EnumType.STRING)
    private List<Especialidad> especialidades;

    public Artista() {
        this.especialidades = new ArrayList<>();
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public List<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Especialidad> especialidades) {
        this.especialidades = especialidades;
    }
}