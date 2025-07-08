package org.example.triviaucab1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

// Asume que tienes una clase Jugador o una clase específica para el modelo de estadísticas
// public class EstadisticasJugador {
//     private String alias;
//     private int partidasJugadas;
//     private int partidasGanadas;
//     private int partidasPerdidas;
//     // Más campos para categorías y tiempo
//     // Constructor, getters y setters
// }

/**
 * Controlador para la ventana de estadísticas del juego.
 * Muestra el ranking y la información estadística de los jugadores.
 */
public class EstadisticasController {

    @FXML
    private TableView<String> estadisticasTableView; // Cambiar String por tu clase de modelo de estadísticas
    @FXML
    private TableColumn<String, String> aliasCol; // Tipo de dato de la fila y tipo de dato de la columna
    @FXML
    private TableColumn<String, Integer> partidasJugadasCol;
    @FXML
    private TableColumn<String, Integer> partidasGanadasCol;
    @FXML
    private TableColumn<String, Integer> partidasPerdidasCol;

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader después de que se carga el FXML.
     * Aquí se cargan los datos de las estadísticas.
     */
    @FXML
    public void initialize() {
        // TODO: Enlazar las columnas de la tabla con las propiedades de tu clase de modelo (e.g., EstadisticasJugador).
        // aliasCol.setCellValueFactory(new PropertyValueFactory<>("alias"));
        // partidasJugadasCol.setCellValueFactory(new PropertyValueFactory<>("partidasJugadas"));
        // ...

        // TODO: Cargar los datos de las estadísticas desde el archivo JSON aquí.
        // ObservableList<EstadisticasJugador> data = FXCollections.observableArrayList();
        // data.add(new EstadisticasJugador("Alias1", 10, 5, 5));
        // estadisticasTableView.setItems(data);
        System.out.println("Cargando datos de estadísticas (simulado)...");
        ObservableList<String> ejemploDatos = FXCollections.observableArrayList(
                "JugadorA - 10 PJ, 5 PG, 5 PP",
                "JugadorB - 8 PJ, 6 PG, 2 PP",
                "JugadorC - 12 PJ, 3 PG, 9 PP"
        );
        // Esto es un placeholder. Deberías usar tu clase de modelo real y cargar los datos del JSON.
        // estadisticasTableView.setItems(ejemploDatos); // Esto fallaría sin el tipo de dato correcto en la tabla
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
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
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
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
