package com.luisdbb.tarea3AD2024base.view;

public enum FxmlView {

    LOGIN("/fxml/login.fxml"),
    RECUPERAR_PASSWORD("/fxml/recuperarPassword.fxml"),
    ADMIN("/fxml/menuAdmin.fxml"),
    REGISTRAR_PERSONA("/fxml/registrarPersona.fxml"),
    MODIFICAR_PERSONA("/fxml/modificarPersona.fxml"),
    CREAR_ESPECTACULO("/fxml/crearEspectaculo.fxml"),
    MODIFICAR_ESPECTACULO("/fxml/modificarEspectaculo.fxml"),
    CREAR_NUMERO("/fxml/crearNumero.fxml"),
    MODIFICAR_NUMERO("/fxml/modificarNumero.fxml"),
    FICHA_ARTISTA("/fxml/fichaArtista.fxml"),
    VER_ESPECTACULO("/fxml/verEspectaculo.fxml"),
    COORDINADOR("/fxml/MenuCoordinador.fxml"),
    ARTISTA("/fxml/MenuArtista.fxml"),
    INVITADO("/fxml/menuInvitado.fxml"),
    VER_LOGS("/fxml/verLogs.fxml"),
    REGISTRAR_INCIDENCIA("/fxml/registrarIncidencia.fxml"),
    RESOLVER_INCIDENCIA("/fxml/resolverIncidencia.fxml"),
    COSNULTAR_INCIDENCIAS("/fxml/consultarIncidencias.fxml");

    private final String fxml;

    FxmlView(String fxml) {
        this.fxml = fxml;
    }

    public String getFxml() {
        return fxml;
    }
}