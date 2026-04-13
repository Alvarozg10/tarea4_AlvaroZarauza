package com.luisdbb.tarea3AD2024base.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.services.AuthService;
import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

@Controller
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisible;

    @FXML
    private CheckBox mostrarPassword;

    @Autowired
    private AuthService authService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private Sesion sesion;

    @FXML
    public void login() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (sesion.getUsuario() != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ya hay una sesión iniciada");
            alert.showAndWait();
            return;
        }

        if (username.isBlank() || password.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Debes rellenar usuario y contraseña");
            alert.showAndWait();
            return;
        }

        Credenciales cred = authService.login(username, password);

        if (cred == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();

        } else {

            sesion.setUsuario(cred.getPersona());

            switch (cred.getPerfil()) {

                case ADMIN:
                    stageManager.switchScene(FxmlView.ADMIN);
                    break;

                case COORDINACION:
                    stageManager.switchScene(FxmlView.COORDINADOR);
                    break;

                case ARTISTA:
                    stageManager.switchScene(FxmlView.ARTISTA);
                    break;
            }
        }
    }
        
    
    @FXML
    private void mostrarPassword() {

        if (mostrarPassword.isSelected()) {
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);
            passwordVisible.setVisible(false);
        }

    }

    @FXML
    private void recuperarPassword() {

        String username = usernameField.getText();

        Credenciales cred = authService.buscarPorUsername(username);

        if (cred == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Usuario no encontrado");
            alert.show();

        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Tu contraseña es: " + cred.getPassword());
            alert.show();
        }
    }
    
    @FXML
    public void entrarInvitado() {

        sesion.setUsuario(null);

        stageManager.switchScene(FxmlView.VER_ESPECTACULO);
    }
}