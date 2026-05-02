package com.luisdbb.tarea3AD2024base.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.view.FxmlView;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.objectdb.modelo.Incidencia;
import com.luisdbb.objectdb.modelo.TipoIncidencia;
import com.luisdbb.tarea3AD2024base.services.objectdb.IncidenciaService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConsultarIncidenciasController {

    @FXML
    private ComboBox<TipoIncidencia> tipoCombo;

    @FXML
    private ComboBox<String> estadoCombo;

    @FXML
    private TextField espectaculoField;

    @FXML
    private TextField numeroField;

    @FXML
    private DatePicker desdePicker;

    @FXML
    private DatePicker hastaPicker;

    // 🔥 TABLEVIEW
    @FXML
    private TableView<Incidencia> tabla;

    @FXML
    private TableColumn<Incidencia, Long> colId;

    @FXML
    private TableColumn<Incidencia, TipoIncidencia> colTipo;

    @FXML
    private TableColumn<Incidencia, Boolean> colResuelta;

    @FXML
    private TableColumn<Incidencia, LocalDateTime> colFecha;

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private Sesion sesion;

    @FXML
    public void initialize() {
    	
    	tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tipoCombo.getItems().add(null);
        tipoCombo.getItems().addAll(TipoIncidencia.values());

        estadoCombo.getItems().addAll("TODAS", "RESUELTAS", "NO_RESUELTAS");
        estadoCombo.setValue("TODAS");

        colId.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getId()));

        colTipo.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getTipo()));

        colResuelta.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().isResuelta()));

        colFecha.setCellValueFactory(data ->
        new SimpleObjectProperty<>(data.getValue().getFechaHora())
    );

    colFecha.setCellFactory(tc -> new TableCell<>() {

        private final java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        @Override
        protected void updateItem(LocalDateTime item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText("");
            } else {
                setText(item.format(formatter));
            }
        }
    });
        
        colResuelta.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : (item ? "Sí" : "No"));
            }
        });
    }

    @FXML
    public void buscar() {

        try {
            TipoIncidencia tipo = tipoCombo.getValue();

            Boolean resuelta = null;
            if ("RESUELTAS".equals(estadoCombo.getValue())) {
                resuelta = true;
            } else if ("NO_RESUELTAS".equals(estadoCombo.getValue())) {
                resuelta = false;
            }

            Long idEspectaculo = null;
            Long idNumero = null;

            if (!espectaculoField.getText().isBlank()) {
                idEspectaculo = Long.parseLong(espectaculoField.getText());
            }

            if (!numeroField.getText().isBlank()) {
                idNumero = Long.parseLong(numeroField.getText());
            }

            LocalDateTime desde = desdePicker.getValue() != null
                    ? desdePicker.getValue().atStartOfDay()
                    : null;

            LocalDateTime hasta = hastaPicker.getValue() != null
                    ? hastaPicker.getValue().atTime(23, 59)
                    : null;

            List<Incidencia> lista = incidenciaService.consultarIncidencias(
                    tipo,
                    resuelta,
                    idEspectaculo,
                    idNumero,
                    desde,
                    hasta
            );

            tabla.getItems().setAll(lista);

        } catch (NumberFormatException e) {
            mostrarError("Los IDs deben ser numéricos");
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
}