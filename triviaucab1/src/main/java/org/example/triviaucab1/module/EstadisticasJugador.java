package org.example.triviaucab1.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para manejar las estadísticas de un jugador.
 * Almacena y gestiona datos como el número de partidas jugadas, ganadas, perdidas,
 * preguntas correctas e incorrectas, preguntas correctas por categoría,
 * y el tiempo total dedicado a responder preguntas correctamente.
 * Esta clase es serializable para permitir el guardado y la carga de las estadísticas.
 */
public class EstadisticasJugador implements Serializable {
    /**
     * Identificador de versión para la serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Número total de partidas en las que el jugador ha participado.
     */
    private int partidasJugadas;
    /**
     * Número de partidas que el jugador ha ganado.
     */
    private int partidasGanadas;
    /**
     * Número de partidas que el jugador ha perdido.
     */
    private int partidasPerdidas;
    /**
     * Número total de preguntas que el jugador ha respondido correctamente.
     */
    private int preguntasCorrectasTotal;
    /**
     * Número total de preguntas que el jugador ha respondido incorrectamente.
     */
    private int preguntasIncorrectas;
    /**
     * Un mapa que almacena el conteo de preguntas correctas por cada categoría.
     * La clave es el nombre de la categoría (String) y el valor es el número de preguntas
     * correctas en esa categoría (Integer).
     */
    private Map<String, Integer> preguntasCorrectasPorCategoria;
    /**
     * El tiempo acumulado (en milisegundos) que el jugador ha tardado en responder
     * correctamente a las preguntas.
     */
    private long tiempoTotalRespuestasCorrectas;

    /**
     * Constructor por defecto para la clase EstadisticasJugador.
     * Inicializa todas las estadísticas a cero y el mapa de categorías como un nuevo HashMap vacío.
     */
    public EstadisticasJugador() {
        this.partidasJugadas = 0;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0;
        this.preguntasCorrectasTotal = 0;
        this.preguntasIncorrectas = 0;
        this.preguntasCorrectasPorCategoria = new HashMap<>();
        this.tiempoTotalRespuestasCorrectas = 0;
    }

    /**
     * Obtiene el número de partidas jugadas por el jugador.
     * @return El número de partidas jugadas.
     */
    public int getPartidasJugadas() { return partidasJugadas; }
    /**
     * Establece el número de partidas jugadas por el jugador.
     * @param partidasJugadas El nuevo número de partidas jugadas.
     */
    public void setPartidasJugadas(int partidasJugadas) { this.partidasJugadas = partidasJugadas; }

    /**
     * Obtiene el número de partidas ganadas por el jugador.
     * @return El número de partidas ganadas.
     */
    public int getPartidasGanadas() { return partidasGanadas; }
    /**
     * Establece el número de partidas ganadas por el jugador.
     * @param partidasGanadas El nuevo número de partidas ganadas.
     */
    public void setPartidasGanadas(int partidasGanadas) { this.partidasGanadas = partidasGanadas; }

    /**
     * Obtiene el número de partidas perdidas por el jugador.
     * @return El número de partidas perdidas.
     */
    public int getPartidasPerdidas() { return partidasPerdidas; }
    /**
     * Establece el número de partidas perdidas por el jugador.
     * @param partidasPerdidas El nuevo número de partidas perdidas.
     */
    public void setPartidasPerdidas(int partidasPerdidas) { this.partidasPerdidas = partidasPerdidas; }

    /**
     * Obtiene el número total de preguntas correctas respondidas por el jugador.
     * @return El número total de preguntas correctas.
     */
    public int getPreguntasCorrectasTotal() { return preguntasCorrectasTotal; }
    /**
     * Establece el número total de preguntas correctas respondidas por el jugador.
     * @param preguntasCorrectasTotal El nuevo número total de preguntas correctas.
     */
    public void setPreguntasCorrectasTotal(int preguntasCorrectasTotal) { this.preguntasCorrectasTotal = preguntasCorrectasTotal; }

    /**
     * Obtiene el número total de preguntas incorrectas respondidas por el jugador.
     * @return El número total de preguntas incorrectas.
     */
    public int getPreguntasIncorrectas() { return preguntasIncorrectas; }
    /**
     * Establece el número total de preguntas incorrectas respondidas por el jugador.
     * @param preguntasIncorrectas El nuevo número total de preguntas incorrectas.
     */
    public void setPreguntasIncorrectas(int preguntasIncorrectas) { this.preguntasIncorrectas = preguntasIncorrectas; }

    /**
     * Obtiene el mapa que contiene el conteo de preguntas correctas por categoría.
     * @return Un mapa donde la clave es la categoría (String) y el valor es el conteo (Integer).
     */
    public Map<String, Integer> getPreguntasCorrectasPorCategoria() { return preguntasCorrectasPorCategoria; }
    /**
     * Establece el mapa de preguntas correctas por categoría.
     * @param preguntasCorrectasPorCategoria El nuevo mapa de preguntas correctas por categoría.
     */
    public void setPreguntasCorrectasPorCategoria(Map<String, Integer> preguntasCorrectasPorCategoria) { this.preguntasCorrectasPorCategoria = preguntasCorrectasPorCategoria; }

    /**
     * Obtiene el tiempo total acumulado (en milisegundos) que el jugador ha tardado en responder correctamente.
     * @return El tiempo total de respuestas correctas en milisegundos.
     */
    public long getTiempoTotalRespuestasCorrectas() { return tiempoTotalRespuestasCorrectas; }
    /**
     * Establece el tiempo total acumulado (en milisegundos) que el jugador ha tardado en responder correctamente.
     * @param tiempoTotalRespuestasCorrectas El nuevo tiempo total de respuestas correctas en milisegundos.
     */
    public void setTiempoTotalRespuestasCorrectas(long tiempoTotalRespuestasCorrectas) { this.tiempoTotalRespuestasCorrectas = tiempoTotalRespuestasCorrectas; }


    /**
     * Incrementa en uno el contador de partidas jugadas.
     */
    public void incrementarPartidasJugadas() {
        this.partidasJugadas++;
    }

    /**
     * Incrementa en uno el contador de partidas ganadas.
     */
    public void incrementarPartidasGanadas() {
        this.partidasGanadas++;
    }

    /**
     * Incrementa en uno el contador de partidas perdidas.
     */
    public void incrementarPartidasPerdidas() {
        this.partidasPerdidas++;
    }

    /**
     * Incrementa en uno el contador total de preguntas correctas.
     */
    public void incrementarPreguntasCorrectasTotal() {
        this.preguntasCorrectasTotal++;
    }

    /**
     * Incrementa en uno el contador total de preguntas incorrectas.
     */
    public void incrementarPreguntasIncorrectas() {
        this.preguntasIncorrectas++;
    }

    /**
     * Incrementa la cuenta de preguntas correctas para una categoría específica.
     * Si la categoría no existe en el mapa, la inicializa en 1.
     * @param categoria La categoría de la pregunta que se respondió correctamente.
     */
    public void incrementarPreguntasCorrectasPorCategoria(String categoria) {
        this.preguntasCorrectasPorCategoria.put(categoria,
                this.preguntasCorrectasPorCategoria.getOrDefault(categoria, 0) + 1);
    }

    /**
     * Añade un valor de tiempo a la suma total de tiempo de respuestas correctas.
     * Este tiempo se espera que esté en milisegundos.
     * @param tiempoRespuesta El tiempo (en milisegundos) de la última respuesta correcta.
     */
    public void añadirTiempoRespuestaCorrecta(long tiempoRespuesta) {
        this.tiempoTotalRespuestasCorrectas += tiempoRespuesta;
    }
}
