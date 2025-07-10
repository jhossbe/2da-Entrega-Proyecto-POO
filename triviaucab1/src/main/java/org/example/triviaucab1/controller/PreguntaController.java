package org.example.triviaucab1.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.triviaucab1.module.Pregunta;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de preguntas del juego.
 * Gestiona la visualización de la pregunta, la entrada de respuesta del jugador y el temporizador.
 */
public class PreguntaController implements Initializable {

    @FXML private Label categoriaLabel;
    @FXML private Label preguntaLabel;
    @FXML private TextField respuestaTextField;
    @FXML private Button enviarRespuestaButton;
    @FXML private Label resultadoLabel;
    @FXML private Label tiempoRespuestaLabel;

    private Pregunta preguntaActual;
    private JuegoController juegoController;
    private Timeline timeline;
    private long tiempoLimiteSegundos;
    private long tiempoRestante;
    private boolean respuestaEnviada = false;

    /**
     * Constructor por defecto de PreguntaController.
     */
    public PreguntaController() {
        System.out.println("DEBUG (PreguntaController): Constructor por defecto llamado.");
    }

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader.
     * Configura los elementos de la UI.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DEBUG (PreguntaController): Inicializando controlador de pregunta.");
        tiempoRespuestaLabel.setText("Tiempo: --");
        resultadoLabel.setVisible(false);
        if (enviarRespuestaButton != null) {
            enviarRespuestaButton.setOnAction(this::handleEnviarRespuesta);
        }
        if (respuestaTextField != null) {
            respuestaTextField.setOnAction(this::handleEnviarRespuesta);
        }
    }

    /**
     * Establece la referencia al JuegoController.
     * @param juegoController La instancia de JuegoController.
     */
    public void setJuegoController(JuegoController juegoController) {
        this.juegoController = juegoController;
        System.out.println("DEBUG (PreguntaController): JuegoController establecido.");
    }

    /**
     * Establece la pregunta actual y la muestra en la interfaz.
     * También inicia el temporizador.
     * @param pregunta La pregunta a mostrar.
     */
    public void setPregunta(Pregunta pregunta) {
        this.preguntaActual = pregunta;
        categoriaLabel.setText("Categoría: " + pregunta.getCategoria());
        preguntaLabel.setText(pregunta.getTextoPregunta());
        respuestaTextField.clear();
        respuestaTextField.setDisable(false);
        enviarRespuestaButton.setDisable(false);
        resultadoLabel.setVisible(false);
        respuestaEnviada = false;

        startCountdown();
        System.out.println("DEBUG (PreguntaController): Pregunta establecida y temporizador iniciado.");
    }

    /**
     * Establece el tiempo límite para responder la pregunta en segundos.
     * Este método también inicializa el tiempo restante del temporizador.
     * @param segundos El tiempo límite en segundos.
     */
    public void setTiempoLimiteSegundos(long segundos) {
        this.tiempoLimiteSegundos = segundos;
        this.tiempoRestante = segundos;
        tiempoRespuestaLabel.setText(String.valueOf(tiempoRestante));
        System.out.println("DEBUG (PreguntaController): Tiempo límite de respuesta establecido en: " + tiempoLimiteSegundos + " segundos.");
    }

    /**
     * Inicia el temporizador de cuenta atrás.
     */
    private void startCountdown() {
        if (timeline != null) {
            timeline.stop();
        }

        if (tiempoLimiteSegundos <= 0) {
            System.out.println("DEBUG (PreguntaController): Temporizador se inicializa a 0 segundos (tiempoLimiteSegundos es <= 0).");
            tiempoRespuestaLabel.setText("0");
            handleTiempoAgotado();
            return;
        }

        System.out.println("DEBUG (PreguntaController): Temporizador de cuenta atrás iniciado.");
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    tiempoRestante--;
                    tiempoRespuestaLabel.setText(String.valueOf(tiempoRestante));
                    System.out.println("DEBUG (PreguntaController): Tiempo restante: " + tiempoRestante + " segundos.");
                    if (tiempoRestante <= 0) {
                        timeline.stop();
                        if (!respuestaEnviada) {
                            handleTiempoAgotado();
                        }
                    }
                })
        );
        timeline.setCycleCount((int) tiempoLimiteSegundos);
        timeline.play();
    }

    /**
     * Maneja la acción cuando el tiempo para responder se agota.
     */
    private void handleTiempoAgotado() {
        System.out.println("DEBUG (PreguntaController): ¡Tiempo agotado! Preparando para notificar y cerrar.");
        respuestaEnviada = true;
        respuestaTextField.setDisable(true);
        enviarRespuestaButton.setDisable(true);

        javafx.application.Platform.runLater(() -> {
            System.out.println("DEBUG (PreguntaController): Ejecutando lógica de tiempo agotado en Platform.runLater.");
            showAlert(Alert.AlertType.WARNING, "¡Tiempo Agotado!", "Se acabó el tiempo. La respuesta es incorrecta.");
            if (juegoController != null) {
                juegoController.notificarResultadoPregunta(false, preguntaActual.getCategoria(), 0);
            }
            Stage stage = (Stage) tiempoRespuestaLabel.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Maneja el evento de clic en el botón "Enviar Respuesta" o Enter en el TextField.
     * @param event El evento de acción.
     */
    @FXML
    private void handleEnviarRespuesta(ActionEvent event) {
        if (respuestaEnviada) {
            return;
        }
        if (timeline != null) {
            timeline.stop();
        }
        respuestaEnviada = true;

        String respuestaJugador = respuestaTextField.getText().trim();
        boolean respuestaCorrecta = preguntaActual.esRespuestaCorrecta(respuestaJugador);
        long tiempoTomado = tiempoLimiteSegundos - tiempoRestante;

        respuestaTextField.setDisable(true);
        enviarRespuestaButton.setDisable(true);

        if (respuestaCorrecta) {
            resultadoLabel.setText("¡Correcto!");
            resultadoLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            showAlert(Alert.AlertType.INFORMATION, "¡Correcto!", "¡Respuesta correcta!");
        } else {
            resultadoLabel.setText("Incorrecto.");
            resultadoLabel.setTextFill(javafx.scene.paint.Color.RED);
            showAlert(Alert.AlertType.ERROR, "Incorrecto", "Respuesta incorrecta. " + "Intenta de nuevo.");
        }
        resultadoLabel.setVisible(true);

        if (juegoController != null) {
            juegoController.notificarResultadoPregunta(respuestaCorrecta, preguntaActual.getCategoria(), tiempoTomado * 1000);
        }
        Stage stage = (Stage) enviarRespuestaButton.getScene().getWindow();
        stage.close();
        System.out.println("DEBUG (PreguntaController): Respuesta enviada. Cerrando ventana.");
    }

    /**
     * Muestra una ventana de alerta.
     * @param alertType El tipo de alerta.
     * @param title El título de la alerta.
     * @param message El mensaje de la alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
