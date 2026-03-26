package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;

public interface EspectaculoRepository extends JpaRepository<Espectaculo, Long> {

    Espectaculo findByNombre(String nombre);
}