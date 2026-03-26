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
public class ModificarEspectaculoController {

    @FXML
    private TextField idField;

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

    private Espectaculo espectaculoActual;

    @FXML
    public void initialize() {

        Platform.runLater(() -> {

            Persona usuario = sesion.getUsuario();
            Credenciales cred = usuario.getCredenciales();

            if (cred.getPerfil() == Perfil.COORDINACION) {
                coordinadorCombo.setVisible(false);
                coordinadorCombo.setManaged(false);
            } else {

                List<Persona> personas = personaService.obtenerTodas();

                for (Persona p : personas) {
                    Credenciales c = p.getCredenciales();

                    if (c != null && c.getPerfil() == Perfil.COORDINACION) {
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
    public void cargarEspectaculo() {

        try {
            Long id = Long.parseLong(idField.getText());

            espectaculoActual = espectaculoService.buscarPorId(id);

            if (espectaculoActual == null) {
                mostrarError("No existe ese espectáculo");
                return;
            }

            nombreField.setText(espectaculoActual.getNombre());
            fechaInicioPicker.setValue(espectaculoActual.getFechaInicio());
            fechaFinPicker.setValue(espectaculoActual.getFechaFin());

            if (coordinadorCombo.isVisible()) {
            	coordinadorCombo.getSelectionModel().select(espectaculoActual.getCoordinador());
            }

        } catch (Exception e) {
            mostrarError("ID inválido");
        }
    }

    @FXML
    public void guardarCambios() {

        if (espectaculoActual == null) {
            mostrarError("Primero debes cargar un espectáculo");
            return;
        }

        try {
            String nombre = nombreField.getText();
            LocalDate inicio = fechaInicioPicker.getValue();
            LocalDate fin = fechaFinPicker.getValue();

            Persona usuario = sesion.getUsuario();
            Credenciales cred = usuario.getCredenciales();

            Long coordinadorId = espectaculoActual.getCoordinador().getId();

            if (cred.getPerfil() == Perfil.ADMIN) {

                Persona seleccionado = coordinadorCombo.getValue();

                if (seleccionado == null) {
                    throw new RuntimeException("Debes seleccionar coordinador");
                }

                coordinadorId = seleccionado.getId();
            }

            espectaculoService.modificarEspectaculo(
                    espectaculoActual.getId(),
                    nombre,
                    inicio,
                    fin,
                    coordinadorId
            );

            mostrarInfo("Espectáculo modificado correctamente");

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