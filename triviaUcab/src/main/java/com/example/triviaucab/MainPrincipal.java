package com.example.triviaucab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPrincipal extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Título del menú
        Text title = new Text("Menú Principal");
        title.setFont(Font.font("Arial", 24));

        // Botones del menú
        Button btnPartidaNueva = new Button("Partida nueva");
        Button btnPartidaGuardada = new Button("Partida guardada");
        Button btnEstadisticas = new Button("Estadísticas");
        Button btnSalir = new Button("Salir");

        // Acción de botón salir
        btnSalir.setOnAction(e -> {
            System.out.println("Saliendo de la aplicación...");
            primaryStage.close();
        });

        // Layout vertical
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(title, btnPartidaNueva, btnPartidaGuardada, btnEstadisticas, btnSalir);
        vbox.setStyle("-fx-padding: 30; -fx-alignment: center;");

        // Escena
        Scene scene = new Scene(vbox, 400, 300);

        // Configuración del Stage (ventana)
        primaryStage.setTitle("Juego Trivia UCAB");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
