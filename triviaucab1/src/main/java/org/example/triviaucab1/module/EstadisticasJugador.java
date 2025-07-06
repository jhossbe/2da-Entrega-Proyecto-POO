package org.example.triviaucab1.module;

/**
 * Clase para manejar las estadísticas de un jugador.
 */
public class EstadisticasJugador {
    private int partidasJugadas;
    private int partidasGanadas;
    private int preguntasCorrectas;
    private int preguntasIncorrectas;
    private long tiempoPromedioRespuesta;

    public EstadisticasJugador() {
        this.partidasJugadas = 0;
        this.partidasGanadas = 0;
        this.preguntasCorrectas = 0;
        this.preguntasIncorrectas = 0;
        this.tiempoPromedioRespuesta = 0;
    }

    // Getters y Setters
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

    public int getPreguntasCorrectas() {
        return preguntasCorrectas;
    }

    public void setPreguntasCorrectas(int preguntasCorrectas) {
        this.preguntasCorrectas = preguntasCorrectas;
    }

    public int getPreguntasIncorrectas() {
        return preguntasIncorrectas;
    }

    public void setPreguntasIncorrectas(int preguntasIncorrectas) {
        this.preguntasIncorrectas = preguntasIncorrectas;
    }

    public long getTiempoPromedioRespuesta() {
        return tiempoPromedioRespuesta;
    }

    public void setTiempoPromedioRespuesta(long tiempoPromedioRespuesta) {
        this.tiempoPromedioRespuesta = tiempoPromedioRespuesta;
    }

    public void incrementarPartidasJugadas() {
        this.partidasJugadas++;
    }

    public void incrementarPartidasGanadas() {
        this.partidasGanadas++;
    }

    public void incrementarPreguntasCorrectas() {
        this.preguntasCorrectas++;
    }

    public void incrementarPreguntasIncorrectas() {
        this.preguntasIncorrectas++;
    }
}
