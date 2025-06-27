package org.example.triviaucab1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la ventana de selección de jugadores para una nueva partida.
 * Permite al usuario seleccionar jugadores de una lista de disponibles y pasarlos a una lista de seleccionados.
 */
public class PartidaNuevaController {

    @FXML
    private ListView<String> jugadoresDisponiblesListView;

    @FXML
    private ListView<String> jugadoresSeleccionadosListView;

    private ObservableList<String> jugadoresDisponibles;
    private ObservableList<String> jugadoresSeleccionados;

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader después de que se carga el FXML.
     * Aquí se inicializan las listas de jugadores.
     */
    @FXML
    public void initialize() {
        jugadoresDisponibles = FXCollections.observableArrayList();
        jugadoresSeleccionados = FXCollections.observableArrayList();

        // TODO: En una implementación real, cargar jugadores desde jugadores.json aquí.
        // Por ahora, añadimos algunos jugadores de ejemplo.
        jugadoresDisponibles.addAll("Jugador 1", "Jugador 2", "Jugador 3", "Jugador 4", "Jugador 5", "Jugador 6", "Jugador 7", "Jugador 8", "Jugador 9", "Jugador 10");

        jugadoresDisponiblesListView.setItems(jugadoresDisponibles);
        jugadoresSeleccionadosListView.setItems(jugadoresSeleccionados);
    }

    /**
     * Añade el jugador seleccionado de la lista de disponibles a la lista de seleccionados.
     */
    @FXML
    private void handleAddPlayer() {
        String selectedPlayer = jugadoresDisponiblesListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null && !jugadoresSeleccionados.contains(selectedPlayer)) {
            jugadoresSeleccionados.add(selectedPlayer);
            // Si quieres que el jugador no esté disponible una vez seleccionado:
            // jugadoresDisponibles.remove(selectedPlayer);
        }
    }

    /**
     * Remueve el jugador seleccionado de la lista de seleccionados y lo devuelve a la lista de disponibles.
     */
    @FXML
    private void handleRemovePlayer() {
        String selectedPlayer = jugadoresSeleccionadosListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            jugadoresSeleccionados.remove(selectedPlayer);
            // Si quieres que el jugador vuelva a estar disponible:
            // if (!jugadoresDisponibles.contains(selectedPlayer)) {
            //     jugadoresDisponibles.add(selectedPlayer);
            // }
        }
    }

    /**
     * Maneja la acción cuando el botón "Jugar" es presionado.
     * Carga la ventana del juego (tablero).
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleJugar(ActionEvent event) {
        if (jugadoresSeleccionados.isEmpty()) {
            System.out.println("No se han seleccionado jugadores.");
            // TODO: Mostrar una alerta al usuario.
            return;
        }
        System.out.println("Botón 'Jugar' presionado. Cargando juego con jugadores: " + jugadoresSeleccionados);
        // Aquí pasarías la lista de jugadores seleccionados al modelo o al siguiente controlador.
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/triviaucab/vista/JuegoView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TRIVIA UCAB - El Juego");
            // stage.setFullScreen(true); // Opcional, para que el juego sea maximizado.
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la ventana del Juego: " + e.getMessage());
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