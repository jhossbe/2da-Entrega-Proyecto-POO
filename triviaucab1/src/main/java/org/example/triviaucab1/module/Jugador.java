package org.example.triviaucab1.module;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String email; // Puedes mantenerlo si es para login, o quitarlo si no lo usas en el juego
    private String alias;
    // private int puntuacion; // <--- ¡ELIMINADO!
    private List<String> quesitos; // Lista de categorías de quesitos ganados
    private EstadisticasJugador estadisticas; // Objeto para las estadísticas


    // Constructor por defecto (NECESARIO para Jackson)
    public Jugador() {
        this.quesitos = new ArrayList<>(); // Inicializar la lista
        this.estadisticas = new EstadisticasJugador(); // Inicializar el objeto de estadísticas
    }

    public Jugador(String email, String alias) {
        this(); // Llama al constructor por defecto para inicializar quesitos y estadisticas
        this.email = email;
        this.alias = alias;
    }

    // Constructor adicional si solo usas alias (útil para jugadores por defecto)
    public Jugador(String alias) {
        this(); // Llama al constructor por defecto
        this.alias = alias;
    }

    // Getters y Setters existentes para email y alias
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

    // <--- GETTERS Y SETTERS para quesitos y estadisticas
    public List<String> getQuesitos() {
        return quesitos;
    }

    public void setQuesitos(List<String> quesitos) {
        this.quesitos = quesitos;
    }

    public EstadisticasJugador getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(EstadisticasJugador estadisticas) {
        this.estadisticas = estadisticas;
    }

    // Método para añadir un quesito (útil en la lógica del juego)
    public void addQuesito(String categoriaQuesito) {
        if (!this.quesitos.contains(categoriaQuesito)) { // Evitar duplicados
            this.quesitos.add(categoriaQuesito);
        }
    }

    // Método para verificar si ya tiene un quesito (útil en la lógica del juego)
    public boolean tieneQuesito(String categoriaQuesito) {
        return this.quesitos.contains(categoriaQuesito);
    }

    @Override
    public String toString() {
        return alias; // Para que se muestre el alias en ListViews, por ejemplo.
    }
}