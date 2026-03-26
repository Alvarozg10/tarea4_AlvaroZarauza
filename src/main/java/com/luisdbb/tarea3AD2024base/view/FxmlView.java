package com.luisdbb.tarea3AD2024base.view;

public enum FxmlView {

    LOGIN("/fxml/login.fxml"),
    ADMIN("/fxml/menuAdmin.fxml"),
    REGISTRAR_PERSONA("/fxml/registrarPersona.fxml"),
    MODIFICAR_PERSONA("/fxml/modificarPersona.fxml"),
    CREAR_ESPECTACULO("/fxml/crearEspectaculo.fxml"),
    MODIFICAR_ESPECTACULO("/fxml/modificarEspectaculo.fxml"),
    COORDINADOR("/fxml/MenuCoordinador.fxml"),
    ARTISTA("/fxml/menuArtista.fxml"),
    INVITADO("/fxml/menuInvitado.fxml");

    private final String fxml;

    FxmlView(String fxml) {
        this.fxml = fxml;
    }

    public String getFxml() {
        return fxml;
    }
}