package org.example.triviaucab1.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.application.Platform;
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
    private PauseTransition temporizador;
    private int tiempoLimiteSegundos = 30; // valor por defecto
    private boolean respuestaCorrectaUsuario = false;
    private JuegoController juegoController;

    private long tiempoInicioRespuesta; // <--- NUEVO CAMPO: Para registrar el tiempo de inicio
    private long tiempoTranscurrido;     // <--- NUEVO CAMPO: Para guardar el tiempo final de la respuesta

    public void setJuegoController(JuegoController juegoController) {
        this.juegoController = juegoController;
    }

    public void setTiempoLimiteSegundos(int tiempoLimiteSegundos) {
        this.tiempoLimiteSegundos = tiempoLimiteSegundos;
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

        this.tiempoInicioRespuesta = System.currentTimeMillis();

        if (temporizador != null) {
            temporizador.stop();
        }
        // ... dentro de setPregunta(Pregunta pregunta) ...
        temporizador = new PauseTransition(javafx.util.Duration.seconds(tiempoLimiteSegundos));
        temporizador.setOnFinished(e -> {
            Platform.runLater(() -> {
                if (!respuestaTextField.isDisabled()) {
                    respuestaTextField.setDisable(true);
                    enviarRespuestaButton.setDisable(true);
                    resultadoLabel.setText("Se ha quedado sin tiempo para responder la pregunta");
                    resultadoLabel.setTextFill(Color.ORANGE);
                    resultadoLabel.setVisible(true);
                    continuarButton.setVisible(true);

                    // --- NUEVO: Notifica al controlador principal como respuesta incorrecta ---
                    respuestaCorrectaUsuario = false;
                    this.tiempoTranscurrido = tiempoLimiteSegundos * 1000; // El tiempo máximo permitido
                    if (juegoController != null) {
                        juegoController.notificarResultadoPregunta(false, preguntaActual.getCategoria(), this.tiempoTranscurrido);
                    }
                    // Cierra la ventana automáticamente después de un pequeño delay (opcional)
                    PauseTransition autoClose = new PauseTransition(javafx.util.Duration.seconds(1));
                    autoClose.setOnFinished(ev -> {
                        Stage stage = (Stage) continuarButton.getScene().getWindow();
                        stage.close();
                    });
                    autoClose.play();
                }
            });
        });
        temporizador.play();
    }

    /**
     * Inicializa el controlador, se llama automáticamente al cargar el FXML.
     * (Este comentario parece un remanente, el método initialize no está aquí, handleEnviarRespuesta se llama en acción del botón)
     */

    @FXML
    private void handleEnviarRespuesta(ActionEvent event) {
        if (temporizador != null) {
            temporizador.stop();
        }

        long tiempoFinRespuesta = System.currentTimeMillis();
        this.tiempoTranscurrido = tiempoFinRespuesta - this.tiempoInicioRespuesta;
        System.out.println("⏱️ Tiempo de respuesta: " + tiempoTranscurrido + " ms");

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