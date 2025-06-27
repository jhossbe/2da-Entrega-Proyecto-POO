package org.example.triviaucab1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador para la ventana principal del juego (el tablero).
 * Maneja las interacciones durante el desarrollo de la partida.
 */
public class JuegoController {

    @FXML
    private Label jugadorEnTurnoLabel;
    @FXML
    private VBox categoriasJugadorVBox; // Para añadir las categorías dinámicamente
    @FXML
    private Label tiempoRespuestaLabel;

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader después de que se carga el FXML.
     * Aquí se inicializaría el estado del juego.
     */
    @FXML
    public void initialize() {
        System.out.println("Ventana de Juego inicializada.");
        // TODO: Cargar el estado inicial del juego, el jugador en turno, categorías, etc.
        // Esto vendría del modelo.
        jugadorEnTurnoLabel.setText("Jugador X"); // Ejemplo
        tiempoRespuestaLabel.setText("00:00"); // Ejemplo
    }

    /**
     * Maneja la acción de finalizar la partida.
     */
    @FXML
    private void handleFinalizarPartida(ActionEvent event) {
        System.out.println("Botón 'Finalizar Partida' presionado.");
        // TODO: Implementar lógica de fin de partida, guardar resultados, etc.
    }

    /**
     * Maneja la acción de rendición del jugador en turno.
     * Implementa las reglas de rendición especificadas en el documento.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleRendicion(ActionEvent event) {
        System.out.println("Botón 'Rendición' presionado.");
        // TODO: Implementar la lógica de rendición (ver reglas en el documento).
        // Podría necesitar confirmación del usuario.
    }

    /**
     * Maneja la acción cuando el botón "Regresar" es presionado.
     * Retorna a la ventana principal.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleRegresar(ActionEvent event) {
        System.out.println("Botón 'Regresar' presionado. Volviendo al menú principal.");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/triviaucab/vista/MenuPrincipalView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TRIVIA UCAB - Menú Principal");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la ventana del Menú Principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja la acción cuando el botón "Salir" es presionado.
     * Cierra la aplicación.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleSalir(ActionEvent event) {
        System.out.println("Botón 'Salir' presionado. Cerrando aplicación.");
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}