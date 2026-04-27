package com.luisdbb.tarea3AD2024base.config;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import com.luisdbb.tarea3AD2024base.view.FxmlView;

@Component
public class StageManager {

    private Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    public StageManager(SpringFXMLLoader springFXMLLoader) {
        this.springFXMLLoader = springFXMLLoader;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchScene(FxmlView view) {
        try {
            Parent root = (Parent) springFXMLLoader.load(view.getFxml());

            Scene scene = new Scene(root, 600, 400);

            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();   
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}