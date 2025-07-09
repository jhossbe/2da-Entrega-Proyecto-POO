package org.example.triviaucab1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.triviaucab1.module.Jugador;
import org.example.triviaucab1.module.JsonService;
import org.example.triviaucab1.module.Partida;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de selección de jugadores para una nueva partida.
 * Permite al usuario seleccionar jugadores de una lista de disponibles y pasarlos a una lista de seleccionados.
 */
public class PartidaNuevaController implements Initializable {

    @FXML
    private ListView<Jugador> jugadoresDisponiblesListView;
    @FXML
    private ListView<Jugador> jugadoresSeleccionadosListView;

    private ObservableList<Jugador> jugadoresDisponibles;
    private ObservableList<Jugador> jugadoresSeleccionados;
    private JsonService jsonService;

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader después de que se carga el FXML.
     * Aquí se inicializan las listas de jugadores y se cargan desde el JSON.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jsonService = new JsonService();
        jugadoresDisponibles = FXCollections.observableArrayList();
        jugadoresSeleccionados = FXCollections.observableArrayList();

        jugadoresDisponiblesListView.setCellFactory(lv -> new ListCell<Jugador>() {
            @Override
            protected void updateItem(Jugador jugador, boolean empty) {
                super.updateItem(jugador, empty);
                setText(empty ? null : jugador.getAlias());
            }
        });
        jugadoresSeleccionadosListView.setCellFactory(lv -> new ListCell<Jugador>() {
            @Override
            protected void updateItem(Jugador jugador, boolean empty) {
                super.updateItem(jugador, empty);
                setText(empty ? null : jugador.getAlias());
            }
        });

        cargarJugadoresDesdeJson();
    }

    @FXML
    private void handleAddPlayer() {
        Jugador selectedPlayer = jugadoresDisponiblesListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            if (!jugadoresSeleccionados.contains(selectedPlayer)) {
                jugadoresSeleccionados.add(selectedPlayer);
                jugadoresSeleccionadosListView.setItems(jugadoresSeleccionados);
                jugadoresDisponibles.remove(selectedPlayer);
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Jugador Duplicado", "Este jugador ya ha sido añadido a la lista de seleccionados.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Vacía", "Por favor, selecciona un jugador de la lista de disponibles.");
        }
    }

    /**
     * Carga los jugadores desde el archivo JSON y los añade a la lista de disponibles.
     */
    private void cargarJugadoresDesdeJson() {
        List<Jugador> loadedPlayers = jsonService.cargarJugadores();
        jugadoresDisponibles.addAll(loadedPlayers);
        jugadoresDisponiblesListView.setItems(jugadoresDisponibles);
    }

    /**
     * Añade el jugador seleccionado de la lista de disponibles a la lista de seleccionados.
     */
    @FXML
    private void handleJugar(ActionEvent event) {
        if (jugadoresSeleccionados.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin Jugadores", "Debes seleccionar al menos un jugador para iniciar la partida.");
            return;
        }

        Partida nuevaPartida = new Partida();
        nuevaPartida.iniciar(new ArrayList<>(jugadoresSeleccionados));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/JuegoView.fxml"));
            Parent juegoRoot = loader.load();

            JuegoController juegoController = loader.getController();
            if (juegoController != null) {
                juegoController.setPartida(nuevaPartida);
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(juegoRoot));
            stage.setTitle("TRIVIA UCAB - El Juego");
            stage.setFullScreen(true);
            stage.show();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Cargar Juego", "No se pudo cargar la vista del juego.", "Detalles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Remueve el jugador seleccionado de la lista de seleccionados.
     */
    @FXML
    private void handleRemovePlayer() {
        Jugador selectedPlayer = jugadoresSeleccionadosListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            jugadoresSeleccionados.remove(selectedPlayer);
            jugadoresSeleccionadosListView.setItems(jugadoresSeleccionados);
            if (!jugadoresDisponibles.contains(selectedPlayer)) {
                jugadoresDisponibles.add(selectedPlayer);
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Vacía", "Por favor, selecciona un jugador de la lista de seleccionados para quitar.");
        }
    }

    /**
     * Maneja la acción cuando el botón "Regresar" es presionado.
     * Retorna a la ventana principal (Menú Principal).
     *
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleRegresar(ActionEvent event) {
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle("Confirmar Regreso");
       alert.setHeaderText("¿Estás seguro que deseas regresar al menú principal?");
       alert.setContentText("Se perderá la selección actual de jugadores.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
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
                new Alert(Alert.AlertType.ERROR, "Error interno: No se pudo cargar la vista del menú principal. Revisa la ruta del FXML en el código. Detalles: " + e.getMessage()).showAndWait();
            }
        }
    }

    /**
     * Maneja la acción cuando el botón "Salir" es presionado.
     * Cierra la aplicación con una confirmación.
     *
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleSalir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Salida");
        alert.setHeaderText("¿Estás seguro que deseas salir de la aplicación?");
        alert.setContentText("Se cerrará la ventana actual.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();

        }
    }

    /**
     * Muestra una alerta al usuario.
     *
     * @param tipo      Tipo de alerta (INFORMATION, WARNING, ERROR, CONFIRMATION).
     * @param titulo    Título de la ventana de alerta.
     * @param encabezado Texto del encabezado de la alerta.
     * @param contenido Contenido principal de la alerta.
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
}