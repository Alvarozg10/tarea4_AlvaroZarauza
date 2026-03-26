package com.luisdbb.tarea3AD2024base.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.services.*;

import java.time.LocalDate;
import java.util.List;

@Component
public class CrearEspectaculoController {

    @FXML
    private TextField nombreField;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    private ComboBox<Persona> coordinadorCombo;

    @Autowired
    private EspectaculoService espectaculoService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private Sesion sesion;

    @FXML
    public void initialize() {

        Platform.runLater(() -> {

            Persona usuario = sesion.getUsuario();

            if (usuario == null) return;

            Credenciales credUsuario = usuario.getCredenciales();

            if (credUsuario != null && credUsuario.getPerfil() == Perfil.COORDINACION) {

                coordinadorCombo.setVisible(false);
                coordinadorCombo.setManaged(false);

            } else {
                coordinadorCombo.setVisible(true);
                coordinadorCombo.setManaged(true);

                coordinadorCombo.getItems().clear();

                List<Persona> personas = personaService.obtenerTodas();

                for (Persona p : personas) {

                    Credenciales cred = p.getCredenciales();

                    if (cred != null && cred.getPerfil() == Perfil.COORDINACION) {
                        coordinadorCombo.getItems().add(p);
                    }
                }

                coordinadorCombo.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(Persona item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getNombre());
                    }
                });

                coordinadorCombo.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(Persona item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getNombre());
                    }
                });
            }
        });
    }

    @FXML
    public void crearEspectaculo() {

        try {
            String nombre = nombreField.getText();
            LocalDate inicio = fechaInicioPicker.getValue();
            LocalDate fin = fechaFinPicker.getValue();

            Persona usuario = sesion.getUsuario();
            Credenciales credUsuario = usuario.getCredenciales();

            Coordinacion coordinador;

            if (credUsuario != null && credUsuario.getPerfil() == Perfil.COORDINACION) {

                coordinador = (Coordinacion) usuario;

            } else {
                Persona seleccionado = coordinadorCombo.getValue();

                if (seleccionado == null) {
                    throw new RuntimeException("Debes seleccionar un coordinador");
                }

                coordinador = (Coordinacion) seleccionado;
            }

            espectaculoService.crearEspectaculo(
                    nombre,
                    inicio,
                    fin,
                    coordinador.getId()
            );

            mostrarInfo("Espectáculo creado correctamente");

        } catch (Exception e) {
            mostrarError(e.getMessage());
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