package com.luisdbb.tarea3AD2024base.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.services.*;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrearNumeroController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField duracionField;

    @FXML
    private TextField ordenField;

    @FXML
    private ListView<Persona> artistasList;

    @Autowired
    private NumeroService numeroService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private Sesion sesion;
    
    @Autowired
    private StageManager stageManager;

    private Long espectaculoId;
    
    @Autowired
    private EspectaculoService espectaculoService;
    
    private Espectaculo espectaculoActual;

    @FXML
    public void initialize() {

        espectaculoId = sesion.getEspectaculoId();

        // 🔥 CARGAR ESPECTÁCULO (CLAVE)
        espectaculoActual = espectaculoService.obtenerEspectaculoCompleto(espectaculoId);

        artistasList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<Persona> personas = personaService.obtenerTodas();

        for (Persona p : personas) {
            if (p instanceof Artista) {
                artistasList.getItems().add(p);
            }
        }

        artistasList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Persona item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
    }

    @FXML
    public void crearNumero() {

        try {
            String nombre = nombreField.getText();
            double duracion = Double.parseDouble(duracionField.getText());
            int orden = Integer.parseInt(ordenField.getText());

            List<Persona> seleccionados = artistasList.getSelectionModel().getSelectedItems();

            List<Long> artistasIds = new ArrayList<>();

            for (Persona p : seleccionados) {
                artistasIds.add(p.getId());
            }

            if (espectaculoId == null) {
                throw new RuntimeException("No hay espectáculo seleccionado");
            }

            numeroService.crearNumero(
                    nombre,
                    duracion,
                    orden,
                    espectaculoId,
                    artistasIds
            );

            mostrarInfo("Número creado correctamente");

        } catch (NumberFormatException e) {
            mostrarError("Duración y orden deben ser números válidos");
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
    
    @FXML
    public void volver() {

        espectaculoActual = espectaculoService.obtenerEspectaculoCompleto(espectaculoId);

        int totalNumeros = espectaculoActual.getNumeros().size();

        if (totalNumeros < 3) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Mínimo de números");
            alert.setHeaderText(null);
            alert.setContentText("Debes crear al menos 3 números antes de salir");

            alert.showAndWait();
            return;
        }

        stageManager.switchScene(FxmlView.ADMIN);
    }
}