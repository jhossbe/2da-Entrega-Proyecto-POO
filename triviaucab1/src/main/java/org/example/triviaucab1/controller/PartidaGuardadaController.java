package org.example.triviaucab1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador para la ventana de carga de partida guardada.
 * Permite al usuario seleccionar y cargar una partida previamente guardada.
 */
public class PartidaGuardadaController {

    /**
     * Maneja la acción cuando el botón "Cargar Partida" es presionado.
     * En una implementación real, esto leería un archivo JSON y cargaría el estado del juego.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleCargarPartida(ActionEvent event) {
        System.out.println("Botón 'Cargar Partida' presionado.");
        // TODO: Implementar la lógica para cargar la partida desde un JSON.
        System.out.println("Lógica de carga de partida aún no implementada.");
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
            stage.setMaximized(true);
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