package org.example.triviaucab1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Random;

/**
 * Controlador del dado del juego.
 * Se encarga de lanzar el dado y notificar al controlador principal del juego (JuegoController)
 * para mostrar las casillas a las que puede avanzar el jugador.
 */
public class DadoController {

    @FXML
    private Button diceButton;

    @FXML
    private Label diceResultLabel;

    private JuegoController juegoController;

    /**
     * Conecta este controlador con el controlador principal del juego.
     * @param juegoController referencia al JuegoController
     */
    public void setJuegoController(JuegoController juegoController) {
        this.juegoController = juegoController;
    }

    /**
     * Lanza el dado al presionar el botón y muestra los movimientos posibles en el tablero.
     */
    @FXML
    protected void rollDice() {

        deshabilitarBotonLanzar();

        int resultado = new Random().nextInt(6) + 1;
        diceResultLabel.setText(String.valueOf(resultado));
        //System.out.println(" El dado lanzó: " + resultado);

        if (juegoController != null) {
            juegoController.lanzarYMostrarMovimientos(resultado);
        } else {
            System.err.println("ERROR: JuegoController no está conectado a DadoController.");
            habilitarBotonLanzar();
        }
    }

    /**
     * Habilita el botón de lanzar el dado.
     */
    public void habilitarBotonLanzar() {
        if (diceButton != null) {
            diceButton.setDisable(false);
            System.out.println("Botón de dado HABILITADO.");
        }
    }

    /**
     * Deshabilita el botón de lanzar el dado.
     */
    public void deshabilitarBotonLanzar() {
        if (diceButton != null) {
            diceButton.setDisable(true);
            System.out.println(" Botón de dado DESHABILITADO.");
        }
    }
}