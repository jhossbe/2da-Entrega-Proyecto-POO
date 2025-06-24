package org.example.triviaucab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EstadisticaController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}