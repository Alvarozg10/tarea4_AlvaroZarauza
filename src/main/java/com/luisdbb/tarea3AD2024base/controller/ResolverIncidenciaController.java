package com.luisdbb.tarea3AD2024base.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.modelo.Perfil;
import com.luisdbb.tarea3AD2024base.services.objectdb.IncidenciaService;
import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

@Component
public class ResolverIncidenciaController {

    @FXML
    private TextField idField;

    @FXML
    private TextArea accionesField;

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private Sesion sesion;

    @Autowired
    private StageManager stageManager;

    @FXML
    public void resolver() {

        try {
            Perfil perfil = sesion.getUsuario().getCredenciales().getPerfil();

            if (perfil != Perfil.ADMIN && perfil != Perfil.COORDINACION) {
                throw new RuntimeException("No tienes permisos para resolver incidencias");
            }

            Long id = Long.parseLong(idField.getText());
            String acciones = accionesField.getText();

            if (acciones == null || acciones.isBlank()) {
                throw new RuntimeException("Debes describir las acciones realizadas");
            }

            incidenciaService.resolverIncidencia(
                    id,
                    acciones,
                    sesion.getUsuario().getId()
            );

            mostrarInfo("Incidencia resuelta correctamente");

            idField.clear();
            accionesField.clear();

        } catch (NumberFormatException e) {
            mostrarError("El ID debe ser un número válido");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void volver() {

        switch (sesion.getUsuario().getCredenciales().getPerfil()) {

            case ADMIN ->
                stageManager.switchScene(FxmlView.ADMIN);

            case COORDINACION ->
                stageManager.switchScene(FxmlView.COORDINADOR);

            case ARTISTA ->
                stageManager.switchScene(FxmlView.ARTISTA);
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}