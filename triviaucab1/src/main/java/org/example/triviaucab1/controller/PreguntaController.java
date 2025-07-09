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

    private long tiempoInicioRespuesta; // <--- NUEVO CAMPO: Para registrar el tiempo de inicio
    private long tiempoTranscurrido;     // <--- NUEVO CAMPO: Para guardar el tiempo final de la respuesta

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

        // --- INICIO DE CAMBIO ---
        // Registrar el tiempo de inicio cuando la pregunta se muestra
        this.tiempoInicioRespuesta = System.currentTimeMillis();
        System.out.println("⏱️ Temporizador de pregunta iniciado.");
        // --- FIN DE CAMBIO ---
    }

    /**
     * Inicializa el controlador, se llama automáticamente al cargar el FXML.
     * (Este comentario parece un remanente, el método initialize no está aquí, handleEnviarRespuesta se llama en acción del botón)
     */
    @FXML
    private void handleEnviarRespuesta(ActionEvent event) {
        // --- INICIO DE CAMBIO ---
        // Calcular el tiempo transcurrido cuando el usuario envía la respuesta
        long tiempoFinRespuesta = System.currentTimeMillis();
        this.tiempoTranscurrido = tiempoFinRespuesta - this.tiempoInicioRespuesta;
        System.out.println("⏱️ Tiempo de respuesta: " + tiempoTranscurrido + " ms");
        // --- FIN DE CAMBIO ---

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
            juegoController.notificarResultadoPregunta(respuestaCorrectaUsuario, preguntaActual.getCategoria(), tiempoTranscurrido);
        }
        Stage stage = (Stage) continuarButton.getScene().getWindow();
        stage.close();
    }
}