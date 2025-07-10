package org.example.triviaucab1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // Importar Initializable
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory; // Importar PropertyValueFactory
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node; // Importar Node para handleRegresar y handleSalir
import org.example.triviaucab1.module.GestorEstadisticas;
import org.example.triviaucab1.module.Jugador; // Importar la clase Jugador

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de estadísticas del juego.
 * Muestra el ranking y la información estadística de los jugadores.
 */
public class EstadisticasController implements Initializable {

    @FXML private TableView<Jugador> estadisticasTableView;
    @FXML private TableColumn<Jugador, String> aliasCol;
    @FXML private TableColumn<Jugador, Integer> partidasJugadasCol;
    @FXML private TableColumn<Jugador, Integer> partidasGanadasCol;
    @FXML private TableColumn<Jugador, Integer> partidasPerdidasCol;
    @FXML private TableColumn<Jugador, Integer> preguntasCorrectasColumn;
    @FXML private TableColumn<Jugador, Long> tiempoTotalRespuestaColumn;
    @FXML private TableColumn<Jugador, String> quesitosColumn;


    private GestorEstadisticas gestorEstadisticas;

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader después de que se carga el FXML.
     * Aquí se configuran las columnas y se cargan los datos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gestorEstadisticas = new GestorEstadisticas();

        aliasCol.setCellValueFactory(new PropertyValueFactory<>("alias"));
        partidasJugadasCol.setCellValueFactory(cellData -> cellData.getValue().getEstadisticas().partidasJugadasProperty().asObject());
        partidasGanadasCol.setCellValueFactory(cellData -> cellData.getValue().getEstadisticas().partidasGanadasProperty().asObject());
        partidasPerdidasCol.setCellValueFactory(cellData -> cellData.getValue().getEstadisticas().partidasPerdidasProperty().asObject());

        if (preguntasCorrectasColumn != null) {
            preguntasCorrectasColumn.setCellValueFactory(cellData -> cellData.getValue().getEstadisticas().preguntasCorrectasTotalProperty().asObject());
        }
        if (tiempoTotalRespuestaColumn != null) {
            tiempoTotalRespuestaColumn.setCellValueFactory(cellData -> cellData.getValue().getEstadisticas().tiempoTotalRespuestasCorrectasProperty().asObject());
        }
        if (quesitosColumn != null) {
            quesitosColumn.setCellValueFactory(cellData -> {
                List<String> quesitos = cellData.getValue().getQuesitosGanadosNombres();
                return new javafx.beans.property.SimpleStringProperty(String.join(", ", quesitos));
            });
        }
        cargarEstadisticasEnTabla();
    }

    /**
     * Carga la lista de jugadores con sus estadísticas desde el GestorEstadisticas
     * y las muestra en la TableView.
     */
    private void cargarEstadisticasEnTabla() {
        List<Jugador> ranking = gestorEstadisticas.getRankingJugadores();
        estadisticasTableView.getItems().setAll(ranking);
        System.out.println("📊 Estadísticas cargadas en la tabla. Total de jugadores: " + ranking.size());
    }

    /**
     * Maneja la acción cuando el botón "Regresar" es presionado.
     * Cierra la ventana actual de estadísticas.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleRegresar(ActionEvent event) {
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
