package org.example.triviaucab1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import org.example.triviaucab1.module.Partida;

/**
 * Controlador para la ventana principal del juego (el tablero).
 * Maneja las interacciones durante el desarrollo de la partida.
 */
public class JuegoController {
    private Partida partidaActual;

    @FXML
    private Label jugadorEnTurnoLabel;
    @FXML
    private VBox categoriasJugadorVBox; // Para añadir las categorías dinámicamente
    @FXML
    private Label tiempoRespuestaLabel;


    /**
     * Método para recibir la instancia de la Partida desde PartidaNuevaController.
     * ESTE ES EL MÉTODO QUE NECESITAS AÑADIR/VERIFICAR.
     * @param partida La instancia de la Partida que se va a jugar.
     */
    public void setPartida(Partida partida) {
        this.partidaActual = partida;
        System.out.println("Partida recibida en JuegoController. Jugadores: " + partida.getJugadores().size());

    }

    /**
     * Método de inicialización llamado automáticamente por FXMLLoader después de que se carga el FXML.
     * Aquí se inicializaría el estado del juego.
     */
    @FXML
    public void initialize() {
        // Es buena práctica verificar si las Labels no son null antes de usarlas
        // Aunque si todo está bien en el FXML y la declaración @FXML, no deberían serlo.
        if (jugadorEnTurnoLabel != null) {
            jugadorEnTurnoLabel.setText("Cargando juego...");
        }
        if (tiempoRespuestaLabel != null) {
            tiempoRespuestaLabel.setText("Tiempo: --");
        }
        System.out.println("JuegoController inicializado.");
    }

    /**
     * Maneja la acción de finalizar la partida.
     */
    @FXML
    private void handleFinalizarPartida(ActionEvent event) {
        System.out.println("Botón 'Finalizar Partida' presionado.");
        // TODO: Implementar lógica de fin de partida, guardar resultados, etc.
    }

    /**
     * Maneja la acción de rendición del jugador en turno.
     * Implementa las reglas de rendición especificadas en el documento.
     * @param event El evento de acción que disparó este método.
     */
    @FXML
    private void handleRendicion(ActionEvent event) {
        System.out.println("Botón 'Rendición' presionado.");
        // TODO: Implementar la lógica de rendición (ver reglas en el documento).
        // Podría necesitar confirmación del usuario.
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
            // Asumiendo que MenuPrincipalView.fxml está en el mismo paquete 'controller'
            // o si lo mueves a un paquete 'view' dentro de 'org.example.triviaucab1'
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/MenuPrincipalView.fxml")); // O "/org/example/triviaucab1/view/MenuPrincipalView.fxml" si creas esa carpeta
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