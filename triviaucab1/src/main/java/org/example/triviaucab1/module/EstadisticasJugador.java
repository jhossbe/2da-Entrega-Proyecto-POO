package org.example.triviaucab1.module;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para manejar las estadísticas de un jugador.
 */
public class EstadisticasJugador {
    private IntegerProperty partidasJugadas;
    private IntegerProperty partidasGanadas;
    private IntegerProperty partidasPerdidas;
    private IntegerProperty preguntasCorrectasTotal;
    private IntegerProperty preguntasIncorrectas;
    private ObservableMap<String, Integer> preguntasCorrectasPorCategoria;
    private LongProperty tiempoTotalRespuestasCorrectas;

    public EstadisticasJugador() {
        this.partidasJugadas = new SimpleIntegerProperty(0);
        this.partidasGanadas = new SimpleIntegerProperty(0);
        this.partidasPerdidas = new SimpleIntegerProperty(0);
        this.preguntasCorrectasTotal = new SimpleIntegerProperty(0);
        this.preguntasIncorrectas = new SimpleIntegerProperty(0);
        this.preguntasCorrectasPorCategoria = FXCollections.observableHashMap();
        this.tiempoTotalRespuestasCorrectas = new SimpleLongProperty(0);
    }

    // --- Getters, Setters y los métodos Property() para cada campo ---

    public int getPartidasJugadas() { return partidasJugadas.get(); }
    public void setPartidasJugadas(int partidasJugadas) { this.partidasJugadas.set(partidasJugadas); }
    public IntegerProperty partidasJugadasProperty() { return partidasJugadas; } // Método Property

    public int getPartidasGanadas() { return partidasGanadas.get(); }
    public void setPartidasGanadas(int partidasGanadas) { this.partidasGanadas.set(partidasGanadas); }
    public IntegerProperty partidasGanadasProperty() { return partidasGanadas; } // Método Property

    public int getPartidasPerdidas() { return partidasPerdidas.get(); }
    public void setPartidasPerdidas(int partidasPerdidas) { this.partidasPerdidas.set(partidasPerdidas); }
    public IntegerProperty partidasPerdidasProperty() { return partidasPerdidas; } // Método Property

    public int getPreguntasCorrectasTotal() { return preguntasCorrectasTotal.get(); }
    public void setPreguntasCorrectasTotal(int preguntasCorrectasTotal) { this.preguntasCorrectasTotal.set(preguntasCorrectasTotal); }
    public IntegerProperty preguntasCorrectasTotalProperty() { return preguntasCorrectasTotal; } // Método Property

    public int getPreguntasIncorrectas() { return preguntasIncorrectas.get(); }
    public void setPreguntasIncorrectas(int preguntasIncorrectas) { this.preguntasIncorrectas.set(preguntasIncorrectas); }
    public IntegerProperty preguntasIncorrectasProperty() { return preguntasIncorrectas; } // Método Property

    public Map<String, Integer> getPreguntasCorrectasPorCategoria() {
        return new HashMap<>(preguntasCorrectasPorCategoria);
    }
    public void setPreguntasCorrectasPorCategoria(Map<String, Integer> preguntasCorrectasPorCategoria) {
        this.preguntasCorrectasPorCategoria.clear();
        if (preguntasCorrectasPorCategoria != null) {
            this.preguntasCorrectasPorCategoria.putAll(preguntasCorrectasPorCategoria);
        }
    }

    public ObservableMap<String, Integer> preguntasCorrectasPorCategoriaProperty() {
        return preguntasCorrectasPorCategoria;
    }

    public long getTiempoTotalRespuestasCorrectas() { return tiempoTotalRespuestasCorrectas.get(); }
    public void setTiempoTotalRespuestasCorrectas(long tiempoTotalRespuestasCorrectas) { this.tiempoTotalRespuestasCorrectas.set(tiempoTotalRespuestasCorrectas); }
    public LongProperty tiempoTotalRespuestasCorrectasProperty() { return tiempoTotalRespuestasCorrectas; } // Método Property

    public void incrementarPartidasJugadas() {
        this.partidasJugadas.set(this.partidasJugadas.get() + 1);
    }

    public void incrementarPartidasGanadas() {
        this.partidasGanadas.set(this.partidasGanadas.get() + 1);
    }

    public void incrementarPartidasPerdidas() {
        this.partidasPerdidas.set(this.partidasPerdidas.get() + 1);
    }

    public void incrementarPreguntasCorrectasTotal() {
        this.preguntasCorrectasTotal.set(this.preguntasCorrectasTotal.get() + 1);
    }

    public void incrementarPreguntasIncorrectas() {
        this.preguntasIncorrectas.set(this.preguntasIncorrectas.get() + 1);
    }

    /**
     * Incrementa la cuenta de preguntas correctas para una categoría específica.
     * @param categoria La categoría de la pregunta.
     */
    public void incrementarPreguntasCorrectasPorCategoria(String categoria) {
        this.preguntasCorrectasPorCategoria.put(categoria,
                this.preguntasCorrectasPorCategoria.getOrDefault(categoria, 0) + 1);
    }

    /**
     * Añade tiempo a la suma total de tiempo de respuestas correctas.
     * @param tiempoRespuesta El tiempo (en milisegundos, por ejemplo) de la última respuesta correcta.
     */
    public void añadirTiempoRespuestaCorrecta(long tiempoRespuesta) {
        this.tiempoTotalRespuestasCorrectas.set(this.tiempoTotalRespuestasCorrectas.get() + tiempoRespuesta);
    }
}