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

    private int partidasJugadas;
    private int partidasGanadas;
    private int partidasPerdidas;
    private int preguntasCorrectasTotal;
    private int preguntasIncorrectas;
    private Map<String, Integer> preguntasCorrectasPorCategoria; // Cambiado de ObservableMap a Map
    private long tiempoTotalRespuestasCorrectas;

    public EstadisticasJugador() {
        this.partidasJugadas = 0;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0;
        this.preguntasCorrectasTotal = 0;
        this.preguntasIncorrectas = 0;
        this.preguntasCorrectasPorCategoria = new HashMap<>(); // Inicializar como HashMap
        this.tiempoTotalRespuestasCorrectas = 0;
    }

    public int getPartidasJugadas() { return partidasJugadas; }
    public void setPartidasJugadas(int partidasJugadas) { this.partidasJugadas = partidasJugadas; }

    public int getPartidasGanadas() { return partidasGanadas; }
    public void setPartidasGanadas(int partidasGanadas) { this.partidasGanadas = partidasGanadas; }

    public int getPartidasPerdidas() { return partidasPerdidas; }
    public void setPartidasPerdidas(int partidasPerdidas) { this.partidasPerdidas = partidasPerdidas; }

    public int getPreguntasCorrectasTotal() { return preguntasCorrectasTotal; }
    public void setPreguntasCorrectasTotal(int preguntasCorrectasTotal) { this.preguntasCorrectasTotal = preguntasCorrectasTotal; }

    public int getPreguntasIncorrectas() { return preguntasIncorrectas; }
    public void setPreguntasIncorrectas(int preguntasIncorrectas) { this.preguntasIncorrectas = preguntasIncorrectas; }

    public Map<String, Integer> getPreguntasCorrectasPorCategoria() { return preguntasCorrectasPorCategoria; }
    public void setPreguntasCorrectasPorCategoria(Map<String, Integer> preguntasCorrectasPorCategoria) { this.preguntasCorrectasPorCategoria = preguntasCorrectasPorCategoria; }

    public long getTiempoTotalRespuestasCorrectas() { return tiempoTotalRespuestasCorrectas; }
    public void setTiempoTotalRespuestasCorrectas(long tiempoTotalRespuestasCorrectas) { this.tiempoTotalRespuestasCorrectas = tiempoTotalRespuestasCorrectas; }


    // --- Métodos para actualizar estadísticas ---

    public void incrementarPartidasJugadas() {
        this.partidasJugadas++;
    }

    public void incrementarPartidasGanadas() {
        this.partidasGanadas++;
    }

    public void incrementarPartidasPerdidas() {
        this.partidasPerdidas++;
    }

    public void incrementarPreguntasCorrectasTotal() {
        this.preguntasCorrectasTotal++;
    }

    public void incrementarPreguntasIncorrectas() {
        this.preguntasIncorrectas++;
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
        this.tiempoTotalRespuestasCorrectas += tiempoRespuesta;
    }
}