package com.luisdbb.tarea3AD2024base.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.services.objectdb.IncidenciaService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;
import com.luisdbb.objectdb.modelo.TipoIncidencia;
import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;

@Component
public class RegistrarIncidenciaController {

    @FXML
    private ComboBox<TipoIncidencia> tipoCombo;

    @FXML
    private TextArea descripcionField;

    @FXML
    private TextField espectaculoField;

    @FXML
    private TextField numeroField;

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private Sesion sesion;
    
    @Autowired
    private StageManager stageManager;

    @FXML
    public void initialize() {
        tipoCombo.getItems().setAll(TipoIncidencia.values());
    }

    @FXML
    public void registrarIncidencia() {

        try {
            TipoIncidencia tipo = tipoCombo.getValue();
            String descripcion = descripcionField.getText();

            if (tipo == null) {
                throw new RuntimeException("Selecciona un tipo");
            }

            if (descripcion == null || descripcion.isBlank()) {
                throw new RuntimeException("Descripción obligatoria");
            }

            Long idEspectaculo = null;
            Long idNumero = null;

            if (!espectaculoField.getText().isBlank()) {
                idEspectaculo = Long.parseLong(espectaculoField.getText());
            }

            if (!numeroField.getText().isBlank()) {
                idNumero = Long.parseLong(numeroField.getText());
            }

            incidenciaService.registrarIncidencia(
                    tipo,
                    descripcion,
                    sesion.getUsuario().getId(), 
                    idEspectaculo,
                    idNumero
            );

            mostrarInfo("Incidencia registrada correctamente");

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
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }
}