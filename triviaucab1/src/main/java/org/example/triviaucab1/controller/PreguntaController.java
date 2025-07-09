package org.example.triviaucab1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.example.triviaucab1.module.Pregunta;

public class PreguntaController {

    @FXML private Label categoriaLabel;
    @FXML private Label preguntaLabel;
    @FXML private TextField respuestaTextField;
    @FXML private Button enviarRespuestaButton;
    @FXML private Label resultadoLabel;
    @FXML private Button continuarButton;

    private Pregunta preguntaActual;
    private boolean respuestaCorrectaUsuario = false;
    private JuegoController juegoController;

    public void setJuegoController(JuegoController juegoController) {
        this.juegoController = juegoController;
    }

    public void setPregunta(Pregunta pregunta) {
        this.preguntaActual = pregunta;
        categoriaLabel.setText("Categoría: " + pregunta.getCategoria());
        preguntaLabel.setText(pregunta.getTextoPregunta());

        respuestaTextField.clear();
        respuestaTextField.setDisable(false);
        enviarRespuestaButton.setDisable(false);

        resultadoLabel.setVisible(false);
        continuarButton.setVisible(false);
    }

    /**
     * Inicializa el controlador, se llama automáticamente al cargar el FXML.
     */
    @FXML
    private void handleEnviarRespuesta(ActionEvent event) {
        String respuestaUsuario = respuestaTextField.getText();
        boolean esCorrecta = preguntaActual.esRespuestaCorrecta(respuestaUsuario);
        respuestaCorrectaUsuario = esCorrecta;

        respuestaTextField.setDisable(true);
        enviarRespuestaButton.setDisable(true);

        resultadoLabel.setText(esCorrecta ? "¡Correcto!" : "¡Incorrecto!");
        resultadoLabel.setTextFill(esCorrecta ? Color.LIMEGREEN : Color.RED);
        resultadoLabel.setVisible(true);

        continuarButton.setVisible(true);
    }

    @FXML
    private void handleContinuar() {
        if (juegoController != null) {
            juegoController.notificarResultadoPregunta(respuestaCorrectaUsuario, preguntaActual.getCategoria());
        }
        Stage stage = (Stage) continuarButton.getScene().getWindow();
        stage.close();
    }
}