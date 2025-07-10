package org.example.triviaucab1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.triviaucab1.module.Jugador;
import org.example.triviaucab1.module.JsonService;
import org.example.triviaucab1.module.Partida;
import org.example.triviaucab1.module.GestorEstadisticas; // Importa el gestor
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Asegúrate de que solo haya un Optional importado
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
    @FXML
    private TextField tiempoRespuestaField;
    private ObservableList<Jugador> jugadoresDisponiblesObservable; // Renombrado para evitar conflicto con el método
    private ObservableList<Jugador> jugadoresSeleccionados;
    private JsonService jsonService;
    private GestorEstadisticas gestorEstadisticas; // Instancia del gestor de estadísticas

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader después de que se carga el FXML.
     * Aquí se inicializan las listas de jugadores y se cargan desde el JSON.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jsonService = new JsonService();
        gestorEstadisticas = new GestorEstadisticas(); // Inicializa el gestor de estadísticas

        jugadoresDisponiblesObservable = FXCollections.observableArrayList();
        jugadoresSeleccionados = FXCollections.observableArrayList();

        // Configuración del CellFactory para mostrar solo el alias
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

        cargarJugadoresYFusionarEstadisticas(); // Nuevo método para cargar y fusionar
    }

    /**
     * Carga los jugadores desde el archivo JSON y los fusiona con sus estadísticas globales.
     */
    private void cargarJugadoresYFusionarEstadisticas() {
        // 1. Carga los jugadores básicos (solo email y alias) del jugadores.json
        List<Jugador> jugadoresBase = jsonService.cargarJugadores();

        // 2. Carga todas las estadísticas acumuladas de todos los jugadores desde estadisticasJugadores.json
        List<Jugador> jugadoresConEstadisticasGlobales = gestorEstadisticas.getRankingJugadores();

        // 3. Fusiona las estadísticas globales en los jugadores base
        for (Jugador jugadorBase : jugadoresBase) {
            Optional<Jugador> jugadorGlobalConStats = jugadoresConEstadisticasGlobales.stream()
                    .filter(j -> j.getEmail().equals(jugadorBase.getEmail()))
                    .findFirst();

            if (jugadorGlobalConStats.isPresent()) {
                // Si encontramos estadísticas globales, las asignamos al jugador para la partida.
                jugadorBase.setEstadisticas(jugadorGlobalConStats.get().getEstadisticas());
                jugadorBase.setQuesitosGanadosNombres(jugadorGlobalConStats.get().getQuesitosGanadosNombres()); // También quesitos
                System.out.println("DEBUG: Estadísticas y quesitos globales cargados para: " + jugadorBase.getAlias() +
                        " - Preguntas Correctas: " + jugadorBase.getEstadisticas().getPreguntasCorrectasTotal() +
                        ", Quesitos: " + jugadorBase.getQuesitosGanadosNombres());
            } else {
                // Si el jugador no existe en estadisticasJugadores.json (es un jugador nuevo),
                // sus estadísticas y quesitos permanecerán en cero/vacío, como se inicializaron en el constructor de Jugador.
                System.out.println("DEBUG: No se encontraron estadísticas globales para: " + jugadorBase.getAlias() + ". Se usarán estadísticas por defecto (cero/vacío).");
            }
        }

        // Añade los jugadores fusionados a la lista de disponibles
        jugadoresDisponiblesObservable.addAll(jugadoresBase);
        jugadoresDisponiblesListView.setItems(jugadoresDisponiblesObservable);
    }

    /**
     * Añade el jugador seleccionado de la lista de disponibles a la lista de seleccionados.
     */
    @FXML
    private void handleAddPlayer() {
        // **INICIO DE LA MODIFICACIÓN**
        // Comprueba si ya se ha alcanzado el número máximo de jugadores.
        if (jugadoresSeleccionados.size() >= 6) {
            mostrarAlerta(Alert.AlertType.WARNING, "Límite de Jugadores", "No se pueden añadir más de 6 jugadores a la partida.");
            return; // Detiene la ejecución del método para no añadir más jugadores.
        }
        // **FIN DE LA MODIFICACIÓN**

        Jugador selectedPlayer = jugadoresDisponiblesListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            if (!jugadoresSeleccionados.contains(selectedPlayer)) {
                jugadoresSeleccionados.add(selectedPlayer);
                jugadoresSeleccionadosListView.setItems(jugadoresSeleccionados);
                jugadoresDisponiblesObservable.remove(selectedPlayer);
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Jugador Duplicado", "Este jugador ya ha sido añadido a la lista de seleccionados.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Vacía", "Por favor, selecciona un jugador de la lista de disponibles.");
        }
    }

    /**
     * Remueve el jugador seleccionado de la lista de seleccionados y lo devuelve a la de disponibles.
     */
    @FXML
    private void handleRemovePlayer() {
        Jugador selectedPlayer = jugadoresSeleccionadosListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            jugadoresSeleccionados.remove(selectedPlayer);
            // Add back to available list if not already there
            if (!jugadoresDisponiblesObservable.contains(selectedPlayer)) {
                jugadoresDisponiblesObservable.add(selectedPlayer);
                // Sort the available list by alias to keep it tidy
                FXCollections.sort(jugadoresDisponiblesObservable, (j1, j2) -> j1.getAlias().compareToIgnoreCase(j2.getAlias()));
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Vacía", "Por favor, selecciona un jugador de la lista de seleccionados para quitar.");
        }
    }

    /**
     * Maneja la acción cuando el botón "Jugar" es presionado.
     * Inicia una nueva partida con los jugadores seleccionados.
     */

    @FXML
    private void handleJugar(ActionEvent event) {
        if (jugadoresSeleccionados.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin Jugadores", "Debes seleccionar al menos un jugador para iniciar la partida.");
            return;
        }

        // Leer y validar el tiempo de respuesta ingresado
        String textoTiempo = tiempoRespuestaField.getText().trim();
        int tiempoRespuesta;
        try {
            tiempoRespuesta = Integer.parseInt(textoTiempo);
            if (tiempoRespuesta <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Valor inválido", "El tiempo debe ser un número mayor a 0.");
                tiempoRespuestaField.requestFocus();
                return; // NO avanza, espera que el usuario arregle el valor
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Valor inválido", "Debes ingresar un número válido para el tiempo de respuesta.");
            tiempoRespuestaField.requestFocus();
            return; // NO avanza, espera que el usuario arregle el valor
        }

        // Si llegaste aquí, el valor es válido
        // Crear una nueva partida y pasar los jugadores seleccionados (¡que ya tienen sus estadísticas fusionadas!)
        Partida nuevaPartida = new Partida();
        nuevaPartida.iniciar(new ArrayList<>(jugadoresSeleccionados)); // Pasa la lista de jugadores
        nuevaPartida.setTiempoRespuesta(tiempoRespuesta);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/JuegoView.fxml"));
            Parent juegoRoot = loader.load();

            JuegoController juegoController = loader.getController();
            if (juegoController != null) {
                juegoController.setPartida(nuevaPartida);
                juegoController.setGestorEstadisticas(gestorEstadisticas);
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