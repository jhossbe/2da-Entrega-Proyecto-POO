package org.example.triviaucab1.module;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para manejar las estadísticas de un jugador.
 */
public class EstadisticasJugador {
    private int partidasJugadas;
    private int partidasGanadas;
    private int partidasPerdidas; // <--- Nuevo campo para partidas perdidas
    private int preguntasCorrectasTotal; // <--- Cambiado a total, para diferenciar de por categoría
    private int preguntasIncorrectas;
    private Map<String, Integer> preguntasCorrectasPorCategoria; // <--- Nuevo campo: preguntas correctas por categoría
    private long tiempoTotalRespuestasCorrectas; // <--- Nuevo campo para el tiempo total, no promedio

    public EstadisticasJugador() {
        this.partidasJugadas = 0;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0; // Inicializar en 0
        this.preguntasCorrectasTotal = 0;
        this.preguntasIncorrectas = 0;
        this.preguntasCorrectasPorCategoria = new HashMap<>(); // Inicializar el mapa
        this.tiempoTotalRespuestasCorrectas = 0; // Inicializar en 0
    }

    // --- Getters y Setters Actualizados o Nuevos ---

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    public void setPartidasGanadas(int partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }

    // Nuevo Getter y Setter para partidasPerdidas
    public int getPartidasPerdidas() {
        return partidasPerdidas;
    }

    public void setPartidasPerdidas(int partidasPerdidas) {
        this.partidasPerdidas = partidasPerdidas;
    }

    // Getter y Setter para preguntasCorrectasTotal
    public int getPreguntasCorrectasTotal() {
        return preguntasCorrectasTotal;
    }

    public void setPreguntasCorrectasTotal(int preguntasCorrectasTotal) {
        this.preguntasCorrectasTotal = preguntasCorrectasTotal;
    }

    public int getPreguntasIncorrectas() {
        return preguntasIncorrectas;
    }

    public void setPreguntasIncorrectas(int preguntasIncorrectas) {
        this.preguntasIncorrectas = preguntasIncorrectas;
    }

    // Nuevo Getter y Setter para preguntasCorrectasPorCategoria
    public Map<String, Integer> getPreguntasCorrectasPorCategoria() {
        return preguntasCorrectasPorCategoria;
    }

    public void setPreguntasCorrectasPorCategoria(Map<String, Integer> preguntasCorrectasPorCategoria) {
        this.preguntasCorrectasPorCategoria = preguntasCorrectasPorCategoria;
    }

    // Nuevo Getter y Setter para tiempoTotalRespuestasCorrectas
    public long getTiempoTotalRespuestasCorrectas() {
        return tiempoTotalRespuestasCorrectas;
    }

    public void setTiempoTotalRespuestasCorrectas(long tiempoTotalRespuestasCorrectas) {
        this.tiempoTotalRespuestasCorrectas = tiempoTotalRespuestasCorrectas;
    }

    // --- Métodos de Incremento Actualizados o Nuevos ---

    public void incrementarPartidasJugadas() {
        this.partidasJugadas++;
    }

    public void incrementarPartidasGanadas() {
        this.partidasGanadas++;
    }

    public void incrementarPartidasPerdidas() { // <--- Nuevo método
        this.partidasPerdidas++;
    }

    public void incrementarPreguntasCorrectasTotal() { // <--- Método actualizado
        this.preguntasCorrectasTotal++;
    }

    public void incrementarPreguntasIncorrectas() {
        this.preguntasIncorrectas++;
    }

    /**
     * Incrementa la cuenta de preguntas correctas para una categoría específica.
     * @param categoria La categoría de la pregunta.
     */
    public void incrementarPreguntasCorrectasPorCategoria(String categoria) { // <--- Nuevo método
        this.preguntasCorrectasPorCategoria.put(categoria,
                this.preguntasCorrectasPorCategoria.getOrDefault(categoria, 0) + 1);
    }

    /**
     * Añade tiempo a la suma total de tiempo de respuestas correctas.
     * @param tiempoRespuesta El tiempo (en milisegundos, por ejemplo) de la última respuesta correcta.
     */
    public void añadirTiempoRespuestaCorrecta(long tiempoRespuesta) { // <--- Nuevo método
        this.tiempoTotalRespuestasCorrectas += tiempoRespuesta;
    }
}