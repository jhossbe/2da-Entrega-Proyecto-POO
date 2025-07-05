package org.example.triviaucab1.module;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.Duration;

public class Partida {
    private List<Jugador> jugadores;
    private Tablero tablero; // Asumiendo que Tablero es serializable o no se guarda directamente
    private int jugadorEnTurnoIndex;
    private Map<Jugador, Integer> posiciones; // <--- Esto puede dar problemas al serializar con Jackson
    private LocalDateTime fechaInicio; // <--- Esto puede dar problemas al serializar con Jackson
    private LocalDateTime fechaFin;
    private boolean partidaTerminada;
    private long tiempoTotalSegundos;
    // private List<Pregunta> bancoDePreguntas; // Lo añadiremos cuando tengamos la clase Pregunta

    public Partida() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero(); // Asegúrate de que Tablero tenga un constructor por defecto
        this.posiciones = new HashMap<>();
        // this.quesitosObtenidos = new HashMap<>(); // ELIMINADO
        this.jugadorEnTurnoIndex = 0;
        this.fechaInicio = LocalDateTime.now(); // Se inicializa al crear la partida
        this.partidaTerminada = false;
        this.tiempoTotalSegundos = 0;
    }

    public void iniciar(List<Jugador> jugadoresSeleccionados) {
        this.jugadores = jugadoresSeleccionados;
        for (Jugador jugador : jugadoresSeleccionados) {
            this.posiciones.put(jugador, 0);
            // No necesitamos inicializar quesitos aquí, ya se inicializan en el constructor de Jugador
        }
        this.jugadorEnTurnoIndex = 0;
        this.partidaTerminada = false;
        this.fechaInicio = LocalDateTime.now();
        this.tiempoTotalSegundos = 0;
        System.out.println("Partida iniciada con " + jugadores.size() + " jugadores.");
    }

    public Jugador getJugadorEnTurno() {
        if (jugadores.isEmpty()) {
            return null;
        }
        return jugadores.get(jugadorEnTurnoIndex);
    }

    public void siguienteTurno() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida para avanzar el turno.");
            return;
        }
        this.jugadorEnTurnoIndex = (this.jugadorEnTurnoIndex + 1) % jugadores.size();
        System.out.println("Es el turno de: " + getJugadorEnTurno().getAlias());
    }

    public void moverJugador(Jugador jugador, int pasos) {
        if (!posiciones.containsKey(jugador)) {
            System.out.println("Error: El jugador " + jugador.getAlias() + " no está en esta partida.");
            return;
        }
        int currentPos = posiciones.getOrDefault(jugador, 0);
        // Asegúrate de que tablero.getRutaPrincipalCasillaIds() no sea null/vacío
        int newPos = (currentPos + pasos) % tablero.getRutaPrincipalCasillaIds().length;
        posiciones.put(jugador, newPos);
        System.out.println(jugador.getAlias() + " se movió de la posición " + currentPos + " a la posición " + newPos);
    }

    /**
     * Añade un quesito a un jugador (ahora usa el método del Jugador).
     * @param jugador El jugador que gana el quesito.
     * @param categoria La categoría del quesito ganado.
     */
    public void añadirQuesito(Jugador jugador, String categoria) {
        if (jugador != null) {
            jugador.addQuesito(categoria); // <--- USA EL MÉTODO DEL JUGADOR
            System.out.println(jugador.getAlias() + " ganó el quesito de " + categoria);
        }
    }

    /**
     * Verifica si un jugador ha ganado la partida (ahora usa los quesitos del Jugador).
     * @param jugador El jugador a verificar.
     * @return true si el jugador ha ganado, false en caso contrario.
     */
    public boolean verificarGanador(Jugador jugador) {
        int quesitosNecesarios = 6;
        return jugador != null && jugador.getQuesitos().size() >= quesitosNecesarios;
    }

    public void terminarPartida() {
        this.partidaTerminada = true;
        this.fechaFin = LocalDateTime.now();
        this.tiempoTotalSegundos = Duration.between(fechaInicio, fechaFin).getSeconds();
    }

    // Getters y Setters (mantenerlos para la serialización básica, aunque algunos den problemas)
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }
    public Tablero getTablero() { return tablero; }
    public void setTablero(Tablero tablero) { this.tablero = tablero; }
    public int getJugadorEnTurnoIndex() { return jugadorEnTurnoIndex; }
    public void setJugadorEnTurnoIndex(int jugadorEnTurnoIndex) { this.jugadorEnTurnoIndex = jugadorEnTurnoIndex; }
    public Map<Jugador, Integer> getPosiciones() { return posiciones; }
    public void setPosiciones(Map<Jugador, Integer> posiciones) { this.posiciones = posiciones; }
    // public Map<Jugador, List<String>> getQuesitosObtenidos() { return quesitosObtenidos; } // ELIMINADO
    // public void setQuesitosObtenidos(Map<Jugador, List<String>> quesitosObtenidos) { this.quesitosObtenidos = quesitosObtenidos; } // ELIMINADO
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public boolean isPartidaTerminada() { return partidaTerminada; }
    public void setPartidaTerminada(boolean partidaTerminada) { this.partidaTerminada = partidaTerminada; }
    public long getTiempoTotalSegundos() { return tiempoTotalSegundos; }
    public void setTiempoTotalSegundos(long tiempoTotalSegundos) { this.tiempoTotalSegundos = tiempoTotalSegundos; }
}