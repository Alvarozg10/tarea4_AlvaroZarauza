package com.luisdbb.tarea3AD2024base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.repositorios.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private CredencialesRepository credencialesRepository;

    public Persona buscarPorId(Long id) {
        return personaRepository.findById(id).orElse(null);
    }
    
    public List<Persona> obtenerTodas() {
        return personaRepository.findAll();
    }

    public void registrarPersona(
            String nombre,
            String email,
            String nacionalidad,
            String tipo,
            String apodo,
            boolean senior,
            LocalDate fechaSenior,
            String username,
            String password,
            List<Especialidad> especialidades) {

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        if (email == null || email.isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }

        if (username == null || username.isBlank()) {
            throw new RuntimeException("El username es obligatorio");
        }

        if (password == null || password.isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (!username.matches("^[a-zA-Z]+$")) {
            throw new RuntimeException("El username solo puede contener letras sin espacios");
        }

        if (username.length() <= 2) {
            throw new RuntimeException("El username debe tener más de 2 caracteres");
        }

        if (password.contains(" ") || password.length() <= 2) {
            throw new RuntimeException("La contraseña no puede tener espacios y debe tener más de 2 caracteres");
        }

        username = username.toLowerCase();

        if (credencialesRepository.findByUsername(username) != null) {
            throw new RuntimeException("El username ya existe");
        }

        if (personaRepository.findByEmail(email) != null) {
            throw new RuntimeException("El email ya existe");
        }

        Persona persona;

        if (tipo.equals("COORDINADOR")) {

            Coordinacion coord = new Coordinacion();
            coord.setNombre(nombre);
            coord.setEmail(email);
            coord.setNacionalidad(nacionalidad);
            coord.setSenior(senior);

            if (senior) {
                if (fechaSenior == null) {
                    throw new RuntimeException("Debe indicar la fecha de senior");
                }
                coord.setFechaSenior(fechaSenior);
            } else {
                coord.setFechaSenior(null);
            }

            persona = personaRepository.save(coord);

        } else if (tipo.equals("ARTISTA")) {

            Artista artista = new Artista();
            artista.setNombre(nombre);
            artista.setEmail(email);
            artista.setNacionalidad(nacionalidad);
            artista.setApodo(apodo);

            if (especialidades == null || especialidades.isEmpty()) {
                throw new RuntimeException("Debe seleccionar al menos una especialidad");
            }

            artista.setEspecialidades(especialidades);

            persona = personaRepository.save(artista);

        } else {
            throw new RuntimeException("Tipo de persona no válido");
        }

        Credenciales cred = new Credenciales();
        cred.setUsername(username);
        cred.setPassword(password);
        cred.setPersona(persona);

        if (tipo.equals("COORDINADOR")) {
            cred.setPerfil(Perfil.COORDINACION);
        } else {
            cred.setPerfil(Perfil.ARTISTA);
        }

        credencialesRepository.save(cred);
    }

    public void modificarPersona(
            Long id,
            String nombre,
            String email,
            String nacionalidad,
            String apodo,
            boolean senior,
            LocalDate fechaSenior,
            List<Especialidad> especialidades) {

        Persona persona = personaRepository.findById(id).orElse(null);

        if (persona == null) {
            throw new RuntimeException("Persona no encontrada");
        }

        persona.setNombre(nombre);
        persona.setEmail(email);
        persona.setNacionalidad(nacionalidad);

        if (persona instanceof Artista artista) {
            artista.setApodo(apodo);

            if (especialidades != null && !especialidades.isEmpty()) {
                artista.setEspecialidades(especialidades);
            }
        }

        if (persona instanceof Coordinacion coord) {
            coord.setSenior(senior);

            if (senior) {
                coord.setFechaSenior(fechaSenior);
            } else {
                coord.setFechaSenior(null);
            }
        }

        personaRepository.save(persona);
    }
}