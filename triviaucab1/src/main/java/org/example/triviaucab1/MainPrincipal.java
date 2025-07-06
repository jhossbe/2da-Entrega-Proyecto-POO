package org.example.triviaucab1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.triviaucab1.controller.JuegoController;

import java.io.IOException;

public class MainPrincipal extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Para demostración, cargar directamente el juego con una partida de prueba
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("JuegoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Obtener el controlador y crear una partida de prueba
        JuegoController controller = fxmlLoader.getController();
        controller.crearPartidaPrueba();

        stage.setTitle("TRIVIA UCAB - Juego con Patrón Decorator");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}