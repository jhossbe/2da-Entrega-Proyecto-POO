package org.example.triviaucab1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.application.Platform;
import org.example.triviaucab1.module.Pregunta;

public class PreguntaController {

    @FXML private Label categoriaLabel;
    @FXML private Label preguntaLabel;
    @FXML private TextField respuestaTextField;
    @FXML private Button enviarRespuestaButton;
    @FXML private Label resultadoLabel;
    @FXML private Button continuarButton;
    @FXML private Label contadorTiempoLabel; // Nuevo: label para mostrar el contador

    private Pregunta preguntaActual;
    private boolean respuestaCorrectaUsuario = false;
    private JuegoController juegoController;
    
    private int tiempoRespuestaSegundos = 30; // Tiempo límite configurable
    private Timeline timeline; // Timeline para el contador
    private int tiempoRestanteSegundos; // Tiempo restante en segundos

    private long tiempoInicioRespuesta; // <--- NUEVO CAMPO: Para registrar el tiempo de inicio
    private long tiempoTranscurrido;     // <--- NUEVO CAMPO: Para guardar el tiempo final de la respuesta

    public void setJuegoController(JuegoController juegoController) {
        this.juegoController = juegoController;
    }

    public void setTiempoRespuestaSegundos(int tiempoRespuestaSegundos) {
        this.tiempoRespuestaSegundos = tiempoRespuestaSegundos;
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
        
        // Inicializar y comenzar el contador de tiempo
        iniciarContadorTiempo();
        // --- FIN DE CAMBIO ---
    }

    /**
     * Inicia el contador de tiempo visual y funcional.
     */
    private void iniciarContadorTiempo() {
        // Detener timeline anterior si existe
        if (timeline != null) {
            timeline.stop();
        }
        
        tiempoRestanteSegundos = tiempoRespuestaSegundos;
        
        // Actualizar el label inicial
        if (contadorTiempoLabel != null) {
            contadorTiempoLabel.setText("Tiempo: " + tiempoRestanteSegundos + "s");
            contadorTiempoLabel.setTextFill(Color.BLACK);
        }
        
        // Crear timeline que se ejecuta cada segundo
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tiempoRestanteSegundos--;
            
            if (contadorTiempoLabel != null) {
                contadorTiempoLabel.setText("Tiempo: " + tiempoRestanteSegundos + "s");
                
                // Cambiar color cuando queden pocos segundos
                if (tiempoRestanteSegundos <= 10) {
                    contadorTiempoLabel.setTextFill(Color.RED);
                } else if (tiempoRestanteSegundos <= 20) {
                    contadorTiempoLabel.setTextFill(Color.ORANGE);
                }
            }
            
            // Si se acaba el tiempo, procesar como respuesta incorrecta
            if (tiempoRestanteSegundos <= 0) {
                Platform.runLater(this::manejarTiempoAgotado);
            }
        }));
        
        timeline.setCycleCount(tiempoRespuestaSegundos);
        timeline.play();
    }

    /**
     * Maneja cuando se agota el tiempo de respuesta.
     */
    private void manejarTiempoAgotado() {
        if (timeline != null) {
            timeline.stop();
        }
        
        // Calcular tiempo transcurrido (tiempo límite completo)
        long tiempoFinRespuesta = System.currentTimeMillis();
        this.tiempoTranscurrido = tiempoFinRespuesta - this.tiempoInicioRespuesta;
        
        // Marcar como respuesta incorrecta
        respuestaCorrectaUsuario = false;
        
        // Deshabilitar controles
        respuestaTextField.setDisable(true);
        enviarRespuestaButton.setDisable(true);
        
        // Mostrar resultado
        resultadoLabel.setText("¡Tiempo Agotado!");
        resultadoLabel.setTextFill(Color.RED);
        resultadoLabel.setVisible(true);
        
        // Mostrar botón continuar
        continuarButton.setVisible(true);
        
        System.out.println("⏱️ Tiempo agotado para la pregunta.");
    }

    /**
     * Inicializa el controlador, se llama automáticamente al cargar el FXML.
     * (Este comentario parece un remanente, el método initialize no está aquí, handleEnviarRespuesta se llama en acción del botón)
     */
    @FXML
    private void handleEnviarRespuesta(ActionEvent event) {
        // Detener el timer si está corriendo
        if (timeline != null) {
            timeline.stop();
        }
        
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