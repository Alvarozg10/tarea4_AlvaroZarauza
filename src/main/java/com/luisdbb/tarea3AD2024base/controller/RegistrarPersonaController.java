package com.luisdbb.tarea3AD2024base.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import com.luisdbb.tarea3AD2024base.services.PersonaService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;
import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Especialidad;

@Component
public class RegistrarPersonaController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nacionalidadField;

    @FXML
    private ComboBox<String> tipoPersonaCombo;

    @FXML
    private TextField apodoField;

    @FXML
    private CheckBox seniorCheck;

    @FXML
    private DatePicker fechaSeniorPicker;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox especialidadesBox;

    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private StageManager stageManager;

    @FXML
    public void initialize() {
        tipoPersonaCombo.getItems().addAll(
                "COORDINADOR",
                "ARTISTA"
        );
    }

    private List<Especialidad> obtenerEspecialidadesSeleccionadas() {

        List<Especialidad> lista = new ArrayList<>();

        for (Node node : especialidadesBox.getChildren()) {
            CheckBox cb = (CheckBox) node;

            if (cb.isSelected()) {
                lista.add(Especialidad.valueOf(cb.getText()));
            }
        }

        return lista;
    }

    @FXML
    public void registrarPersona() {

        String nombre = nombreField.getText();
        String email = emailField.getText();
        String nacionalidad = nacionalidadField.getText();
        String tipo = tipoPersonaCombo.getValue();
        String apodo = apodoField.getText();
        boolean senior = seniorCheck.isSelected();
        LocalDate fechaSenior = fechaSeniorPicker.getValue();
        String username = usernameField.getText();
        String password = passwordField.getText();

        List<Especialidad> especialidades = obtenerEspecialidadesSeleccionadas();

        try {

            if (tipo.equals("COORDINADOR") && senior && fechaSenior == null) {
                throw new RuntimeException("Debes indicar la fecha si es senior");
            }

            personaService.registrarPersona(
                    nombre,
                    email,
                    nacionalidad,
                    tipo,
                    apodo,
                    senior,
                    fechaSenior,
                    username,
                    password,
                    especialidades
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro completado");
            alert.setHeaderText(null);
            alert.setContentText("Persona registrada correctamente");
            alert.showAndWait();

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en el registro");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    public void volver() {
        stageManager.switchScene(FxmlView.ADMIN);
    }
}