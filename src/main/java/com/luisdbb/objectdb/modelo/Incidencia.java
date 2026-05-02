package com.luisdbb.objectdb.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Column;
import java.time.LocalDateTime;

@Entity
public class Incidencia {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private TipoIncidencia tipo;

    @Column(length = 1000)
    private String descripcion;

    private boolean resuelta;

    private Long idPersonaReporta;
    private Long idEspectaculo;
    private Long idNumero;

    public Incidencia() {
        this.fechaHora = LocalDateTime.now();
        this.resuelta = false;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public TipoIncidencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoIncidencia tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isResuelta() {
        return resuelta;
    }

    public void setResuelta(boolean resuelta) {
        this.resuelta = resuelta;
    }

    public Long getIdPersonaReporta() {
        return idPersonaReporta;
    }

    public void setIdPersonaReporta(Long idPersonaReporta) {
        this.idPersonaReporta = idPersonaReporta;
    }

    public Long getIdEspectaculo() {
        return idEspectaculo;
    }

    public void setIdEspectaculo(Long idEspectaculo) {
        this.idEspectaculo = idEspectaculo;
    }

    public Long getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(Long idNumero) {
        this.idNumero = idNumero;
    }
}