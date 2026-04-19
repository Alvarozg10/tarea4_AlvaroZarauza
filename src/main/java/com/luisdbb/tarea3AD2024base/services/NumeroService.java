package com.luisdbb.tarea3AD2024base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.repositorios.*;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class NumeroService {

	@Autowired
	private NumeroRepository numeroRepository;

    @Autowired
    private EspectaculoRepository espectaculoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public void crearNumero(String nombre, double duracion, int orden,
                            Long espectaculoId, List<Long> artistasIds) {

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        double decimal = duracion % 1;

        if (!(decimal == 0.0 || decimal == 0.5)) {
            throw new RuntimeException("Duración inválida (solo .0 o .5)");
        }

        if (orden < 1) {
            throw new RuntimeException("El orden debe ser mayor o igual a 1");
        }

        Espectaculo esp = espectaculoRepository.findById(espectaculoId).orElse(null);

        if (esp == null) {
            throw new RuntimeException("El espectáculo no existe");
        }

        if (numeroRepository.existsByEspectaculoAndOrden(esp, orden)) {
            throw new RuntimeException("Ya existe un número con ese orden");
        }

        if (artistasIds == null || artistasIds.isEmpty()) {
            throw new RuntimeException("Debes seleccionar al menos un artista");
        }

        List<Artista> artistas = artistasIds.stream()
                .map(id -> (Artista) personaRepository.findById(id).orElse(null))
                .toList();

        for (Artista a : artistas) {
            if (a == null) {
                throw new RuntimeException("Artista no válido");
            }
        }

        Numero numero = new Numero();
        numero.setNombre(nombre);
        numero.setDuracion(duracion);
        numero.setOrden(orden);
        numero.setEspectaculo(esp);
        numero.setArtistas(artistas);

        numeroRepository.save(numero);
    }
    
    public void modificarNumero(Long id, String nombre, double duracion, int orden, List<Long> artistasIds) {

        Numero numero = numeroRepository.findById(id).orElse(null);

        if (numero == null) {
            throw new RuntimeException("El número no existe");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        double decimal = duracion % 1;

        if (!(decimal == 0.0 || decimal == 0.5)) {
            throw new RuntimeException("Duración inválida (solo .0 o .5)");
        }

        if (orden < 1) {
            throw new RuntimeException("Orden inválido");
        }

        if (numeroRepository.existsByEspectaculoAndOrden(numero.getEspectaculo(), orden)
                && numero.getOrden() != orden) {
            throw new RuntimeException("Ya existe un número con ese orden");
        }

        if (artistasIds == null || artistasIds.isEmpty()) {
            throw new RuntimeException("Debes seleccionar al menos un artista");
        }

        List<Artista> artistas = artistasIds.stream()
                .map(idArt -> (Artista) personaRepository.findById(idArt).orElse(null))
                .toList();

        for (Artista a : artistas) {
            if (a == null) {
                throw new RuntimeException("Artista no válido");
            }
        }

        numero.setNombre(nombre);
        numero.setDuracion(duracion);
        numero.setOrden(orden);
        numero.setArtistas(artistas);

        numeroRepository.save(numero);
    }
    
    @Transactional
    public Numero buscarPorId(Long id) {
        Numero num = numeroRepository.findById(id).orElse(null);

        if (num != null) {
            num.getArtistas().size(); 
        }

        return num;
    }
    
    @Transactional
    public Espectaculo obtenerEspectaculoConNumeros(Long id) {

        Espectaculo esp = espectaculoRepository.findById(id).orElse(null);

        if (esp != null) {
            esp.getNumeros().size(); 
        }

        return esp;
    }
}