package com.luisdbb.objectdb.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;

@Entity
public class ResolucionIncidencia {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime fechaHoraResolucion;

    private String accionesRealizadas;

    private Long idPersonaResuelve;

    @ManyToOne
    private Incidencia incidencia;

    public ResolucionIncidencia() {
        this.fechaHoraResolucion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaHoraResolucion() {
        return fechaHoraResolucion;
    }

    public String getAccionesRealizadas() {
        return accionesRealizadas;
    }

    public void setAccionesRealizadas(String accionesRealizadas) {
        this.accionesRealizadas = accionesRealizadas;
    }

    public Long getIdPersonaResuelve() {
        return idPersonaResuelve;
    }

    public void setIdPersonaResuelve(Long idPersonaResuelve) {
        this.idPersonaResuelve = idPersonaResuelve;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }
}