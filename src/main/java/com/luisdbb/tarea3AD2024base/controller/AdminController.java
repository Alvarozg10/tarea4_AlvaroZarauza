package com.luisdbb.tarea3AD2024base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

@Component
public class AdminController {

    @Autowired
    private StageManager stageManager;
    
    @Autowired
    private Sesion sesion;

    public void registrarPersona(ActionEvent event) {
        stageManager.switchScene(FxmlView.REGISTRAR_PERSONA);
    }
    
    @FXML
    public void modificarPersona() {
        stageManager.switchScene(FxmlView.MODIFICAR_PERSONA);
    }
    
    @FXML
    public void crearEspectaculo() {
        stageManager.switchScene(FxmlView.CREAR_ESPECTACULO);
    }
    
    @FXML
    public void modificarEspectaculo() {
        stageManager.switchScene(FxmlView.MODIFICAR_ESPECTACULO);
    }
    
    @FXML
    public void modificarNumero() {
        stageManager.switchScene(FxmlView.MODIFICAR_NUMERO);
    }
    
    @FXML
    public void verEspectaculos() {
        stageManager.switchScene(FxmlView.VER_ESPECTACULO);
    }
    
    public void verLogs() {
        stageManager.switchScene(FxmlView.VER_LOGS);
    }
    
    @FXML
    public void cerrarSesion() {

        sesion.setUsuario(null); 

        stageManager.switchScene(FxmlView.LOGIN); 
    }
}