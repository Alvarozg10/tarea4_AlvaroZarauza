package com.luisdbb.tarea3AD2024base.services;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.db4o.LogOperacion;
import com.luisdbb.tarea3AD2024base.modelo.db4o.TipoOperacion;

import java.io.File;
import java.time.LocalDateTime;

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
}