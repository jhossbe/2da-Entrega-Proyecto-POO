package org.example.triviaucab1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controlador para la ventana del menú principal del juego TRIVIA-UCAB.
 * Maneja las interacciones del usuario con los botones de la pantalla inicial.
 */
public class MenuPrincipalController {

    // No necesitamos @FXML para los botones si solo los usamos en el ActionEvent
    // @FXML private Button nuevaPartidaButton;

    /**
     * Maneja la acción cuando el botón "Partida nueva" es presionado.
     * Carga la ventana de selección de jugadores.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleNuevaPartida(ActionEvent event) {
        System.out.println("Botón 'Partida nueva' presionado. Cargando selección de jugadores.");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/PartidaNuevaView.fxml"));
            Scene scene = new Scene(fxmlLoader.load()); // Tamaño predeterminado o especificado
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow(); // Obtiene el Stage actual
            stage.setScene(scene);
            stage.setTitle("TRIVIA UCAB - Selección de Jugadores");
            // stage.setFullScreen(true); // Si quieres que la ventana de selección de jugadores sea maximizada
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la ventana de Partida Nueva: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja la acción cuando el botón "Partida guardada" es presionado.
     * Esto debería cargar la última partida guardada desde un archivo JSON.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handlePartidaGuardada(ActionEvent event) {
        System.out.println("Botón 'Partida guardada' presionado. Cargando partida.");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/triviaucab/vista/PartidaGuardadaView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TRIVIA UCAB - Partida Guardada");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la ventana de Partida Guardada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja la acción cuando el botón "Estadísticas" es presionado.
     * Esto debería mostrar el ranking y las estadísticas de los jugadores.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleEstadisticas(ActionEvent event) {
        System.out.println("Botón 'Estadísticas' presionado. Cargando estadísticas.");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/triviaucab/vista/EstadisticasView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TRIVIA UCAB - Estadísticas");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la ventana de Estadísticas: " + e.getMessage());
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