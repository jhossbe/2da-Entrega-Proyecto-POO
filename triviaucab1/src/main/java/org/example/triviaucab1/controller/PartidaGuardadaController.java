package org.example.triviaucab1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.stage.Stage;

import org.example.triviaucab1.module.JsonService;
import org.example.triviaucab1.module.Partida;
import org.example.triviaucab1.module.GestorEstadisticas;

import java.io.IOException;

/**
 * Controlador para la ventana de carga de partida guardada.
 * Permite al usuario seleccionar y cargar una partida previamente guardada.
 */
public class PartidaGuardadaController {

    private JsonService jsonService = new JsonService();
    private GestorEstadisticas gestorEstadisticas = new GestorEstadisticas();

    /**
     * Maneja la acción cuando el botón "Cargar Partida" es presionado.
     * Intenta cargar una partida desde el JSON.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleCargarPartida(ActionEvent event) {
        System.out.println("Botón 'Cargar Partida' presionado. Intentando cargar partida guardada...");
        Partida partidaGuardada = jsonService.cargarPartida();

        if (partidaGuardada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/JuegoView.fxml"));
                Parent juegoRoot = loader.load();
                JuegoController juegoController = loader.getController();
                if (juegoController != null) {
                    juegoController.setPartida(partidaGuardada);
                    juegoController.setGestorEstadisticas(gestorEstadisticas);
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(juegoRoot));
                stage.setTitle("TRIVIA UCAB - Partida Guardada");
                stage.setFullScreen(true);
                stage.show();
                System.out.println("Partida guardada cargada exitosamente.");

            } catch (IOException e) {
                new Alert(AlertType.ERROR, "Error al cargar la vista del juego: " + e.getMessage()).showAndWait();
                System.err.println("Error al cargar la vista del juego para partida guardada: " + e.getMessage());
                e.printStackTrace();
            }
        } else {

            new Alert(AlertType.INFORMATION, "No hay partida guardada para cargar.").showAndWait();
            System.out.println("No se encontró partida guardada para cargar.");
        }
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/MenuPrincipalView.fxml"));
            Parent menuPrincipalRoot = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(menuPrincipalRoot));
            stage.setTitle("TRIVIA UCAB - Menú Principal");
            stage.setFullScreen(true);
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}