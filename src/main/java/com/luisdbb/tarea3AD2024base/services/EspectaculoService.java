package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.modelo.db4o.TipoOperacion;
import com.luisdbb.tarea3AD2024base.repositorios.*;

import jakarta.transaction.Transactional;

@Service
public class EspectaculoService {

    @Autowired
    private EspectaculoRepository espectaculoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private NumeroRepository numeroRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private Sesion sesion;

    public Espectaculo crearEspectaculo(
            String nombre,
            LocalDate inicio,
            LocalDate fin,
            Long coordinadorId) {

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("Nombre obligatorio");
        }

        if (nombre.length() > 25) {
            throw new RuntimeException("Máximo 25 caracteres");
        }

        if (espectaculoRepository.findByNombre(nombre) != null) {
            throw new RuntimeException("El nombre ya existe");
        }

        if (inicio == null || fin == null) {
            throw new RuntimeException("Fechas obligatorias");
        }

        if (fin.isBefore(inicio)) {
            throw new RuntimeException("Fecha fin inválida");
        }

        if (inicio.plusYears(1).isBefore(fin)) {
            throw new RuntimeException("No puede durar más de 1 año");
        }

        Persona persona = personaRepository.findById(coordinadorId).orElse(null);

        if (!(persona instanceof Coordinacion coord)) {
            throw new RuntimeException("Debe ser un coordinador");
        }

        Espectaculo esp = new Espectaculo();
        esp.setNombre(nombre);
        esp.setFechaInicio(inicio);
        esp.setFechaFin(fin);
        esp.setCoordinador(coord);

        esp = espectaculoRepository.save(esp);

        String usuario = sesion.getUsuario().getNombre();

        logService.registrarOperacion(
                usuario,
                TipoOperacion.NUEVO,
                "Se ha insertado un Espectáculo con id " + esp.getId()
        );

        return esp;
    }

    public void modificarEspectaculo(Long id, String nombre,
            LocalDate inicio, LocalDate fin,
            Long coordinadorId) {

        Espectaculo esp = espectaculoRepository.findById(id).orElse(null);

        if (esp == null) {
            throw new RuntimeException("El espectáculo no existe");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        if (nombre.length() > 25) {
            throw new RuntimeException("El nombre no puede superar los 25 caracteres");
        }

        Espectaculo existente = espectaculoRepository.findByNombre(nombre);

        if (existente != null && !existente.getId().equals(id)) {
            throw new RuntimeException("Ya existe otro espectáculo con ese nombre");
        }

        if (inicio == null || fin == null) {
            throw new RuntimeException("Las fechas son obligatorias");
        }

        if (fin.isBefore(inicio)) {
            throw new RuntimeException("La fecha fin no puede ser anterior a la de inicio");
        }

        if (inicio.plusYears(1).isBefore(fin)) {
            throw new RuntimeException("El espectáculo no puede durar más de 1 año");
        }

        Persona persona = personaRepository.findById(coordinadorId).orElse(null);

        if (persona == null) {
            throw new RuntimeException("El coordinador no existe");
        }

        if (!(persona instanceof Coordinacion coord)) {
            throw new RuntimeException("La persona no es un coordinador");
        }

        esp.setNombre(nombre);
        esp.setFechaInicio(inicio);
        esp.setFechaFin(fin);
        esp.setCoordinador(coord);

        espectaculoRepository.save(esp);

        String usuario = sesion.getUsuario().getNombre();

        logService.registrarOperacion(
                usuario,
                TipoOperacion.ACTUALIZACION,
                "Se ha actualizado Espectáculo con id " + esp.getId()
        );
    }

    public Espectaculo buscarPorId(Long id) {
        return espectaculoRepository.findById(id).orElse(null);
    }

    public void validarMinimoNumeros(Long espectaculoId) {

        Espectaculo esp = espectaculoRepository.findById(espectaculoId).orElse(null);

        if (esp == null) {
            throw new RuntimeException("Espectáculo no existe");
        }

        long total = numeroRepository.countByEspectaculo(esp);

        if (total < 3) {
            throw new RuntimeException("El espectáculo debe tener al menos 3 números");
        }
    }

    @Transactional
    public Espectaculo obtenerEspectaculoCompleto(Long id) {

        Espectaculo esp = espectaculoRepository.findById(id).orElse(null);

        if (esp != null) {
            esp.getNumeros().size();

            for (Numero n : esp.getNumeros()) {
                n.getArtistas().size();
            }

            esp.getCoordinador().getNombre();
        }

        return esp;
    }

    public List<Espectaculo> obtenerTodos() {
        return espectaculoRepository.findAll();
    }
}