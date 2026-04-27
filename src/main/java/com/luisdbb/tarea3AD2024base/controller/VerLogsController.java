package com.luisdbb.tarea3AD2024base.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.modelo.db4o.*;
import com.luisdbb.tarea3AD2024base.services.LogService;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.modelo.Perfil;
import com.luisdbb.tarea3AD2024base.view.FxmlView;
import com.luisdbb.tarea3AD2024base.config.StageManager;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class VerLogsController {

    @FXML private TextField usuarioField;

    @FXML private CheckBox nuevoCheck;
    @FXML private CheckBox actCheck;
    @FXML private CheckBox borradoCheck;

    @FXML private DatePicker fechaInicioPicker;
    @FXML private DatePicker fechaFinPicker;

    @FXML private TableView<LogOperacion> tablaLogs;
    @FXML private TableColumn<LogOperacion, String> colId;
    @FXML private TableColumn<LogOperacion, String> colFecha;
    @FXML private TableColumn<LogOperacion, String> colUsuario;
    @FXML private TableColumn<LogOperacion, String> colTipo;
    @FXML private TableColumn<LogOperacion, String> colResumen;

    @Autowired
    private LogService logService;

    @Autowired
    private Sesion sesion;

    @Autowired
    private StageManager stageManager;

    @FXML
    public void initialize() {

        if (sesion.getUsuario() == null ||
            sesion.getUsuario().getCredenciales() == null ||
            sesion.getUsuario().getCredenciales().getPerfil() != Perfil.ADMIN) {

            throw new RuntimeException("Acceso denegado");
        }

        colId.setCellValueFactory(data ->
            new SimpleStringProperty(String.valueOf(data.getValue().getId()))
        );

        colFecha.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getFechaHora().toString())
        );

        colUsuario.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getUsuario())
        );

        colTipo.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getTipoOperacion().name())
        );

        colResumen.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getResumen())
        );

        tablaLogs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void buscar() {

        String usuario = usuarioField.getText().isBlank()
                ? null
                : usuarioField.getText();

        List<TipoOperacion> tipos = new ArrayList<>();

        if (nuevoCheck.isSelected()) tipos.add(TipoOperacion.NUEVO);
        if (actCheck.isSelected()) tipos.add(TipoOperacion.ACTUALIZACION);
        if (borradoCheck.isSelected()) tipos.add(TipoOperacion.BORRADO);

        LocalDateTime inicio = fechaInicioPicker.getValue() != null
                ? fechaInicioPicker.getValue().atStartOfDay()
                : null;

        LocalDateTime fin = fechaFinPicker.getValue() != null
                ? fechaFinPicker.getValue().atTime(23, 59)
                : null;

        List<LogOperacion> logs = logService.buscarLogs(usuario, tipos, inicio, fin);

        tablaLogs.setItems(FXCollections.observableArrayList(logs));
    }

    @FXML
    public void volver() {
        stageManager.switchScene(FxmlView.ADMIN);
    }
}