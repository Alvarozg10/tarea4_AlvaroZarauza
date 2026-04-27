package com.luisdbb.tarea3AD2024base.services;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.db4o.LogOperacion;
import com.luisdbb.tarea3AD2024base.modelo.db4o.TipoOperacion;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
public class LogService {

    private static final String RUTA = "ficheros/log.db4o";

    private ObjectContainer abrirDB() {
        new File("ficheros").mkdirs();
        return Db4oEmbedded.openFile(RUTA);
    }

    public void registrarOperacion(String usuario,
                                   TipoOperacion tipo,
                                   String resumen) {

        ObjectContainer db = abrirDB();

        try {
            LogOperacion log = new LogOperacion(
                    System.currentTimeMillis(),
                    LocalDateTime.now(),
                    usuario,
                    tipo,
                    resumen
            );

            db.store(log);
            db.commit();

        } finally {
            db.close();
        }
    }

    public List<LogOperacion> buscarLogs(
            String usuario,
            List<TipoOperacion> tipos,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        ObjectContainer db = abrirDB();

        try {
            var result = db.query(new Predicate<LogOperacion>() {

                @Override
                public boolean match(LogOperacion log) {

                    return
                        (usuario == null || log.getUsuario().equals(usuario)) &&

                        (tipos == null || tipos.isEmpty() || tipos.contains(log.getTipoOperacion())) &&

                        (fechaInicio == null || !log.getFechaHora().isBefore(fechaInicio)) &&

                        (fechaFin == null || !log.getFechaHora().isAfter(fechaFin));
                }
            });

            return new ArrayList<>(result);

        } finally {
            db.close();
        }
    }
}