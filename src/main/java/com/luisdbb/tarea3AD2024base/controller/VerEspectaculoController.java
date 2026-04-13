package com.luisdbb.tarea3AD2024base.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.services.EspectaculoService;

import java.time.LocalDate;
import java.util.List;

@Component
public class VerEspectaculoController {
	
    @FXML private TableView<Espectaculo> tablaEspectaculos;
    @FXML private TableColumn<Espectaculo, Long> colEspId;
    @FXML private TableColumn<Espectaculo, String> colEspNombre;
    @FXML private TableColumn<Espectaculo, LocalDate> colEspInicio;
    @FXML private TableColumn<Espectaculo, LocalDate> colEspFin;

    @FXML private Label nombreLabel;
    @FXML private Label fechasLabel;
    @FXML private Label coordLabel;

    @FXML private TableView<Numero> tablaNumeros;
    @FXML private TableColumn<Numero, Long> colNumId;
    @FXML private TableColumn<Numero, String> colNumNombre;
    @FXML private TableColumn<Numero, Double> colNumDuracion;

    @FXML private TableView<Artista> tablaArtistas;
    @FXML private TableColumn<Artista, String> colArtNombre;
    @FXML private TableColumn<Artista, String> colArtNac;
    @FXML private TableColumn<Artista, String> colArtApodo;

    @Autowired
    private EspectaculoService espectaculoService;

    @FXML
    public void initialize() {

        colEspId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEspNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colEspFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));

        colNumId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNumNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNumDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        colArtNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colArtNac.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colArtApodo.setCellValueFactory(new PropertyValueFactory<>("apodo"));

        tablaEspectaculos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaNumeros.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaArtistas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        List<Espectaculo> lista = espectaculoService.obtenerTodos();
        tablaEspectaculos.setItems(FXCollections.observableArrayList(lista));

        tablaEspectaculos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, espSel) -> {

            if (espSel != null) {

                Espectaculo esp = espectaculoService.obtenerEspectaculoCompleto(espSel.getId());

                tablaNumeros.setItems(FXCollections.observableArrayList(esp.getNumeros()));

                tablaArtistas.getItems().clear();
            }
        });

        tablaNumeros.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, numSel) -> {

            if (numSel != null) {
                tablaArtistas.setItems(FXCollections.observableArrayList(numSel.getArtistas()));
            }
        });
        
        tablaEspectaculos.setPlaceholder(
                new Label("No hay espectáculos disponibles")
            );

            tablaNumeros.setPlaceholder(
                new Label("No hay números")
            );

            tablaArtistas.setPlaceholder(
                new Label("No hay artistas")
            );
        }
    }