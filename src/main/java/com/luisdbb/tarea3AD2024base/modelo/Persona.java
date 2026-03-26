package com.luisdbb.tarea3AD2024base.modelo;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String nacionalidad;

    @OneToOne(mappedBy = "persona")
    private Credenciales credenciales;

    public Persona() {}

    public Persona(Long id, String nombre, String email, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.nacionalidad = nacionalidad;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}