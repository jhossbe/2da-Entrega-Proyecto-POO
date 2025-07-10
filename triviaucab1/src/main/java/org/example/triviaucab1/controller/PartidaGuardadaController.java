package org.example.triviaucab1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    private JsonService jsonService;
    private GestorEstadisticas gestorEstadisticas;

    public PartidaGuardadaController() {
        this.jsonService = new JsonService();
        this.gestorEstadisticas = new GestorEstadisticas();
    }

    /**
     * Maneja la acción cuando el botón "Cargar Partida" es presionado.
     * En una implementación real, esto leería un archivo JSON y cargaría el estado del juego.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleCargarPartida(ActionEvent event) {
        System.out.println("Botón 'Cargar Partida' presionado.");
        
        try {
            // Cargar la partida desde JSON
            Partida partidaCargada = jsonService.cargarPartida();
            
            if (partidaCargada == null || partidaCargada.getJugadores().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Sin Partida", 
                    "No se encontró ninguna partida guardada para cargar.");
                return;
            }
            
            System.out.println("Partida cargada con " + partidaCargada.getJugadores().size() + 
                " jugadores y tiempo de respuesta: " + partidaCargada.getTiempoRespuestaSegundos() + " segundos");
            
            // Cargar la vista del juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/JuegoView.fxml"));
            Parent juegoRoot = loader.load();

            JuegoController juegoController = loader.getController();
            if (juegoController != null) {
                juegoController.setPartida(partidaCargada);
                juegoController.setGestorEstadisticas(gestorEstadisticas);
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(juegoRoot));
            stage.setTitle("TRIVIA UCAB - El Juego (Partida Cargada)");
            stage.setFullScreen(true);
            stage.show();
            
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Cargar", 
                "No se pudo cargar la partida guardada.", "Detalles: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", 
                "Ocurrió un error inesperado al cargar la partida.", "Detalles: " + e.getMessage());
            e.printStackTrace();
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
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
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
     * Muestra una alerta al usuario.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado) {
        mostrarAlerta(tipo, titulo, encabezado, null);
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