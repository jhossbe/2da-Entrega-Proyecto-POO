package org.example.triviaucab1.module;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Jugador {
    private String email;
    private String alias;
    // Aquí podrías añadir atributos para estadísticas de juego como partidas jugadas/ganadas/perdidas
    // o categorías obtenidas, para ser persistidos.

    public Jugador(String email, String alias) {
        this.email = email;
        this.alias = alias;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return alias; // Para que se muestre el alias en ListViews, por ejemplo.
    }
}