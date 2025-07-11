package org.example.triviaucab1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.triviaucab1.module.Jugador;
import org.example.triviaucab1.module.JsonService;
import org.example.triviaucab1.module.Partida;
import org.example.triviaucab1.module.GestorEstadisticas;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controlador para la vista de creación de nueva partida y selección de jugadores.
 * Permite añadir nuevos jugadores, seleccionar jugadores existentes y configurar el tiempo de respuesta.
 */
public class PartidaNuevaController implements Initializable {

    @FXML private TextField newPlayerEmailField;
    @FXML private TextField newPlayerAliasField;
    @FXML private ListView<Jugador> jugadoresDisponiblesListView;
    @FXML private ListView<Jugador> jugadoresSeleccionadosListView;
    @FXML private TextField tiempoRespuestaField;

    private ObservableList<Jugador> jugadoresDisponiblesObservable;
    private ObservableList<Jugador> jugadoresSeleccionados;
    private JsonService jsonService;
    private GestorEstadisticas gestorEstadisticas;

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader después de que se carga el FXML.
     * Aquí se inicializan las listas de jugadores, se cargan desde el JSON y se configura el campo de tiempo.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jsonService = new JsonService();
        gestorEstadisticas = new GestorEstadisticas();
        jugadoresDisponiblesObservable = FXCollections.observableArrayList();
        jugadoresSeleccionados = FXCollections.observableArrayList();
        jugadoresDisponiblesListView.setItems(jugadoresDisponiblesObservable);
        jugadoresSeleccionadosListView.setItems(jugadoresSeleccionados);
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
        tiempoRespuestaField.setText("10");
        cargarJugadoresYFusionarEstadisticas();
    }

    /**
     * Carga los jugadores desde el archivo JSON y los fusiona con sus estadísticas globales.
     */
    private void cargarJugadoresYFusionarEstadisticas() {
        List<Jugador> jugadoresBase = jsonService.cargarJugadores();
        System.out.println("DEBUG (PartidaNuevaController): Jugadores base cargados: " + jugadoresBase.size());
        List<Jugador> jugadoresConEstadisticasGlobales = gestorEstadisticas.getRankingJugadores();
        System.out.println("DEBUG (PartidaNuevaController): Estadísticas globales cargadas para: " + jugadoresConEstadisticasGlobales.size() + " jugadores.");
        jugadoresDisponiblesObservable.clear();
        for (Jugador jugadorBase : jugadoresBase) {
            Optional<Jugador> jugadorGlobalConStats = jugadoresConEstadisticasGlobales.stream()
                    .filter(j -> j.getEmail() != null && jugadorBase.getEmail() != null && j.getEmail().equals(jugadorBase.getEmail()))
                    .findFirst();

            if (jugadorGlobalConStats.isPresent()) {
                jugadorBase.setEstadisticas(jugadorGlobalConStats.get().getEstadisticas());
                jugadorBase.setQuesitosGanadosNombres(jugadorGlobalConStats.get().getQuesitosGanadosNombres());
                System.out.println("DEBUG (PartidaNuevaController): Estadísticas fusionadas para: " + jugadorBase.getAlias());
            }
            jugadoresDisponiblesObservable.add(jugadorBase);
        }
        System.out.println("DEBUG (PartidaNuevaController): Jugadores disponibles en ListView: " + jugadoresDisponiblesObservable.size());
    }

    /**
     * Maneja la acción de añadir un nuevo jugador.
     * Valida los campos y añade el jugador a la lista de disponibles.
     * @param event El evento de acción.
     */
    @FXML
    private void handleAddPlayer(ActionEvent event) {
        String email = newPlayerEmailField.getText().trim();
        String alias = newPlayerAliasField.getText().trim();

        if (email.isEmpty() || alias.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor, ingrese el email y el alias del jugador.");
            return;
        }

        boolean emailExists = jugadoresDisponiblesObservable.stream().anyMatch(j -> j.getEmail().equalsIgnoreCase(email)) ||
                jugadoresSeleccionados.stream().anyMatch(j -> j.getEmail().equalsIgnoreCase(email));
        if (emailExists) {
            showAlert(Alert.AlertType.WARNING, "Email Existente", "Ya existe un jugador con este email. Por favor, use otro.");
            return;
        }
        Jugador nuevoJugador = new Jugador(email, alias);
        List<Jugador> todosLosJugadoresActualizados = new ArrayList<>(jugadoresDisponiblesObservable);
        todosLosJugadoresActualizados.addAll(jugadoresSeleccionados);
        todosLosJugadoresActualizados.add(nuevoJugador);
        jsonService.guardarJugadores(todosLosJugadoresActualizados.stream().distinct().collect(Collectors.toList()));

        jugadoresDisponiblesObservable.add(nuevoJugador);
        newPlayerEmailField.clear();
        newPlayerAliasField.clear();
        showAlert(Alert.AlertType.INFORMATION, "Jugador Añadido", "Jugador '" + alias + "' añadido exitosamente.");
        System.out.println("DEBUG (PartidaNuevaController): Jugador añadido: " + alias);
    }

    /**
     * Maneja la acción de seleccionar un jugador de la lista de disponibles a la lista de seleccionados.
     * @param event El evento de acción.
     */
    @FXML
    private void handleSelectPlayer(ActionEvent event) {
        Jugador selectedPlayer = jugadoresDisponiblesListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            if (jugadoresSeleccionados.size() >= 6) {
                showAlert(Alert.AlertType.WARNING, "Límite de Jugadores", "No puedes seleccionar más de 6 jugadores.");
                return;
            }
            if (!jugadoresSeleccionados.contains(selectedPlayer)) {
                jugadoresSeleccionados.add(selectedPlayer);
                jugadoresDisponiblesObservable.remove(selectedPlayer);
                System.out.println("DEBUG (PartidaNuevaController): Jugador seleccionado: " + selectedPlayer.getAlias());
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Jugador Ya Seleccionado", "Este jugador ya ha sido seleccionado.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Ningún Jugador Seleccionado", "Por favor, seleccione un jugador de la lista de disponibles.");
        }
    }

    /**
     * Maneja la acción de deseleccionar un jugador de la lista de seleccionados.
     * @param event El evento de acción.
     */
    @FXML
    private void handleDeselectPlayer(ActionEvent event) {
        Jugador deselectedPlayer = jugadoresSeleccionadosListView.getSelectionModel().getSelectedItem();
        if (deselectedPlayer != null) {
            jugadoresSeleccionados.remove(deselectedPlayer);
            jugadoresDisponiblesObservable.add(deselectedPlayer);
            System.out.println("DEBUG (PartidaNuevaController): Jugador deseleccionado: " + deselectedPlayer.getAlias());
        } else {
            showAlert(Alert.AlertType.WARNING, "Ningún Jugador Deseleccionado", "Por favor, seleccione un jugador de la lista de seleccionados.");
        }
    }

    /**
     * Maneja la acción de iniciar la partida con los jugadores seleccionados.
     * @param event El evento de acción.
     */
    @FXML
    private void handleIniciarPartida(ActionEvent event) {
        List<Jugador> jugadoresParaPartida = new ArrayList<>(jugadoresSeleccionados);

        System.out.println("DEBUG (PartidaNuevaController): Jugadores seleccionados ANTES de crear Partida: " + jugadoresParaPartida.size());

        if (jugadoresParaPartida.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No hay Jugadores", "Por favor, seleccione al menos un jugador para iniciar la partida.");
            return;
        }

        long tiempoRespuesta;
        try {
            tiempoRespuesta = Long.parseLong(tiempoRespuestaField.getText());
            if (tiempoRespuesta <= 0) {
                showAlert(Alert.AlertType.WARNING, "Tiempo Inválido", "El tiempo de respuesta debe ser un número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Formato de Tiempo Inválido", "Por favor, ingrese un número válido para el tiempo de respuesta.");
            return;
        }
        jugadoresParaPartida.forEach(jugador -> {
            jugador.setQuesitosGanadosNombres(jugador.getQuesitosGanadosNombres());
        });
        Partida nuevaPartida = new Partida();
        nuevaPartida.iniciar(jugadoresParaPartida, tiempoRespuesta);

        System.out.println("DEBUG (PartidaNuevaController): Partida creada con jugadores: " + nuevaPartida.getJugadores().size());

        jsonService.guardarPartida(nuevaPartida);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/JuegoView.fxml"));
            Parent juegoRoot = loader.load();
            JuegoController juegoController = loader.getController();
            juegoController.setPartida(nuevaPartida);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(juegoRoot);
            stage.setScene(scene);
            stage.setTitle("Trivia UCAB - Juego");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al Cargar Juego", "No se pudo cargar la vista del juego: " + e.getMessage());
        }
    }

    /**
     * Maneja la acción de cancelar la selección de jugadores y regresar al menú principal.
     * @param event El evento de acción.
     */
    @FXML
    private void handleCancelar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/MenuPrincipalView.fxml"));
            Parent menuPrincipalRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(menuPrincipalRoot);
            stage.setScene(scene);
            stage.setTitle("Trivia UCAB - Menú Principal");
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al Cargar Menú", "No se pudo cargar la vista del menú principal: " + e.getMessage());
        }
    }

    /**
     * Muestra una ventana de alerta con el tipo, título y mensaje especificados.
     * @param alertType El tipo de alerta (INFORMATION, WARNING, ERROR, etc.).
     * @param title El título de la ventana de alerta.
     * @param message El mensaje a mostrar en la alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

