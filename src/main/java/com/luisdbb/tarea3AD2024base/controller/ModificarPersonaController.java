package com.luisdbb.tarea3AD2024base.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.services.PersonaService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModificarPersonaController {

    @FXML private TextField idField;
    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private TextField nacionalidadField;
    @FXML private TextField apodoField;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private CheckBox seniorCheck;
    @FXML private DatePicker fechaSeniorPicker;

    @FXML private CheckBox acroCheck;
    @FXML private CheckBox humorCheck;
    @FXML private CheckBox magiaCheck;
    @FXML private CheckBox equilibrioCheck;
    @FXML private CheckBox malabaresCheck;

    @Autowired
    private PersonaService personaService;

    private Persona personaActual;
    
    @Autowired private StageManager stageManager;

    @FXML
    public void cargarPersona() {
        try {
            Long id = Long.parseLong(idField.getText());

            personaActual = personaService.buscarPorId(id);

            if (personaActual == null) {
                mostrarError("No existe ninguna persona con ese ID");
                return;
            }

            nombreField.setText(personaActual.getNombre());
            emailField.setText(personaActual.getEmail());
            nacionalidadField.setText(personaActual.getNacionalidad());

            if (personaActual.getCredenciales() != null) {
                usernameField.setText(personaActual.getCredenciales().getUsername());
            }
            passwordField.clear(); 

            // reset
            apodoField.clear();
            seniorCheck.setSelected(false);
            fechaSeniorPicker.setValue(null);

            acroCheck.setSelected(false);
            humorCheck.setSelected(false);
            magiaCheck.setSelected(false);
            equilibrioCheck.setSelected(false);
            malabaresCheck.setSelected(false);

            if (personaActual instanceof Artista artista) {

                apodoField.setText(artista.getApodo());

                if (artista.getEspecialidades() != null) {
                    for (Especialidad esp : artista.getEspecialidades()) {

                        switch (esp) {
                            case ACROBACIA -> acroCheck.setSelected(true);
                            case HUMOR -> humorCheck.setSelected(true);
                            case MAGIA -> magiaCheck.setSelected(true);
                            case EQUILIBRISMO -> equilibrioCheck.setSelected(true);
                            case MALABARISMO -> malabaresCheck.setSelected(true);
                        }
                    }
                }
            }

            if (personaActual instanceof Coordinacion coord) {

                seniorCheck.setSelected(coord.isSenior());

                if (coord.getFechaSenior() != null) {
                    fechaSeniorPicker.setValue(coord.getFechaSenior());
                }
            }

        } catch (NumberFormatException e) {
            mostrarError("ID inválido");
        }
    }

    @FXML
    public void guardarCambios() {

        if (personaActual == null) {
            mostrarError("Primero debes cargar una persona");
            return;
        }

        try {

            List<Especialidad> especialidades = new ArrayList<>();

            if (acroCheck.isSelected()) especialidades.add(Especialidad.ACROBACIA);
            if (humorCheck.isSelected()) especialidades.add(Especialidad.HUMOR);
            if (magiaCheck.isSelected()) especialidades.add(Especialidad.MAGIA);
            if (equilibrioCheck.isSelected()) especialidades.add(Especialidad.EQUILIBRISMO);
            if (malabaresCheck.isSelected()) especialidades.add(Especialidad.MALABARISMO);

            // 🔥 CREDENCIALES
            String username = usernameField.getText();
            String password = passwordField.getText();

            personaService.modificarPersona(
                    personaActual.getId(),
                    nombreField.getText(),
                    emailField.getText(),
                    nacionalidadField.getText(),
                    apodoField.getText(),
                    seniorCheck.isSelected(),
                    fechaSeniorPicker.getValue(),
                    especialidades,
                    username,
                    password
            );

            mostrarInfo("Persona modificada correctamente");

        } catch (Exception e) {
            mostrarError("Error al modificar: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OK");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    @FXML
    public void volver() {
        stageManager.switchScene(FxmlView.ADMIN);
    }
}