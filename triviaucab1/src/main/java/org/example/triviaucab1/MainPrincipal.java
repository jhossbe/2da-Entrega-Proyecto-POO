package org.example.triviaucab1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPrincipal extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuPrincipalView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setTitle("TRIVIA UCAB - Menú Principal");
        stage.setScene(scene);
        // stage.setMaximized(true);
        stage.show();
    }

    /**
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPrincipal.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Tablero");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    **/
    public static void main(String[] args) {
        launch();
    }
}
