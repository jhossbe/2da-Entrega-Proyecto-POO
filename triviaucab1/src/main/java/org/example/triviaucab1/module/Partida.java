package org.example.triviaucab1.module;

import org.example.triviaucab1.module.tablero.CasillaNode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.Duration;

public class Partida {
    private List<Jugador> jugadores;
    private Tablero tablero;
    private int jugadorEnTurnoIndex;
    private Map<Jugador, Integer> posiciones; // Posiciones en el tablero lineal
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean partidaTerminada;
    private long tiempoTotalSegundos;
    private int numeroRonda;

    public Partida() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero();
        this.posiciones = new HashMap<>();
        this.jugadorEnTurnoIndex = 0;
        this.fechaInicio = LocalDateTime.now();
        this.partidaTerminada = false;
        this.tiempoTotalSegundos = 0;
        this.numeroRonda = 1;
    }

    public void iniciar(List<Jugador> jugadoresSeleccionados) {
        this.jugadores = jugadoresSeleccionados;
        for (Jugador jugador : jugadoresSeleccionados) {
            this.posiciones.put(jugador, 0);
        }
        this.jugadorEnTurnoIndex = 0;
        this.partidaTerminada = false;
        this.fechaInicio = LocalDateTime.now();
        this.tiempoTotalSegundos = 0;

        // Activar el primer jugador
        if (!jugadores.isEmpty()) {
            jugadores.get(0).setTurnoActivo(true);
        }

        System.out.println("Partida iniciada con " + jugadores.size() + " jugadores.");
    }

    /**
     * Inicia la partida estableciendo el primer jugador como activo.
     */
    public void iniciarPartida() {
        if (!jugadores.isEmpty()) {
            jugadores.get(0).setTurnoActivo(true);
            System.out.println("🎮 Partida iniciada con " + jugadores.size() + " jugadores");
            System.out.println("🎯 Turno de: " + getJugadorActual().getAlias());
        }
    }

    /**
     * Añade un jugador a la partida.
     */
    public void agregarJugador(Jugador jugador) {
        if (!partidaTerminada) {
            jugadores.add(jugador);
            posiciones.put(jugador, 0);
            System.out.println("➕ Jugador agregado: " + jugador.getAlias());
        }
    }

    public Jugador getJugadorEnTurno() {
        if (jugadores.isEmpty()) {
            return null;
        }
        return jugadores.get(jugadorEnTurnoIndex);
    }

    /**
     * Obtiene el jugador que tiene el turno actual (alias para compatibilidad).
     */
    public Jugador getJugadorActual() {
        return getJugadorEnTurno();
    }

    public void siguienteTurno() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida para avanzar el turno.");
            return;
        }

        // Desactivar turno del jugador actual
        jugadores.get(jugadorEnTurnoIndex).setTurnoActivo(false);

        // Pasar al siguiente jugador
        this.jugadorEnTurnoIndex = (this.jugadorEnTurnoIndex + 1) % jugadores.size();

        // Si volvemos al primer jugador, incrementar ronda
        if (jugadorEnTurnoIndex == 0) {
            numeroRonda++;
            System.out.println("🔄 Ronda " + numeroRonda);
        }

        // Activar turno del nuevo jugador actual
        jugadores.get(jugadorEnTurnoIndex).setTurnoActivo(true);

        System.out.println("🎯 Turno de: " + getJugadorActual().getAlias());
    }

    /**
     * Pasa el turno al siguiente jugador (alias para compatibilidad).
     */
    public void pasarTurno() {
        siguienteTurno();
    }

    public void moverJugador(Jugador jugador, int pasos) {
        if (!posiciones.containsKey(jugador)) {
            System.out.println("Error: El jugador " + jugador.getAlias() + " no está en esta partida.");
            return;
        }
        int currentPos = posiciones.getOrDefault(jugador, 0);
        int newPos = (currentPos + pasos) % tablero.getRutaPrincipalCasillaIds().length;
        posiciones.put(jugador, newPos);
        System.out.println(jugador.getAlias() + " se movió de la posición " + currentPos + " a la posición " + newPos);
    }

    /**
     * Mueve un jugador a una nueva posición específica (para el sistema visual).
     */
    public void moverJugador(Jugador jugador, CasillaNode nuevaPosicion) {
        jugador.moverA(nuevaPosicion);

        // Verificar si el jugador ha ganado después del movimiento
        verificarCondicionVictoria(jugador);
    }

    /**
     * Añade un quesito a un jugador.
     */
    public void añadirQuesito(Jugador jugador, String categoria) {
        if (jugador != null) {
            jugador.addQuesito(categoria);
            System.out.println(jugador.getAlias() + " ganó el quesito de " + categoria);
        }
    }

    /**
     * Otorga una categoría a un jugador (alias para compatibilidad).
     */
    public void otorgarCategoria(Jugador jugador, String categoria) {
        añadirQuesito(jugador, categoria);
        verificarCondicionVictoria(jugador);
    }

    /**
     * Verifica si un jugador ha ganado la partida.
     */
    public boolean verificarGanador(Jugador jugador) {
        return jugador != null && jugador.haGanadoTodosLosQuesitos();
    }

    /**
     * Verifica si un jugador ha cumplido las condiciones de victoria.
     */
    private void verificarCondicionVictoria(Jugador jugador) {
        if (jugador.haGanadoTodosLosQuesitos() &&
                jugador.getPosicionActual() != null &&
                jugador.getPosicionActual().getId().equals("c")) {

            partidaTerminada = true;
            System.out.println("🏆 ¡" + jugador.getAlias() + " ha ganado la partida!");
            terminarPartida();
        }
    }

    public void terminarPartida() {
        this.partidaTerminada = true;
        this.fechaFin = LocalDateTime.now();
        this.tiempoTotalSegundos = Duration.between(fechaInicio, fechaFin).getSeconds();
    }

    /**
     * Termina la partida forzosamente con un motivo.
     */
    public void terminarPartida(String motivo) {
        terminarPartida();
        System.out.println("🛑 Partida terminada: " + motivo);
    }

    /**
     * Obtiene estadísticas de la partida.
     */
    public String getEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("📊 Estadísticas de la Partida:\n");
        stats.append("Ronda: ").append(numeroRonda).append("\n");
        stats.append("Jugadores: ").append(jugadores.size()).append("\n");

        for (Jugador jugador : jugadores) {
            stats.append("- ").append(jugador.getAlias())
                    .append(": ").append(jugador.getNumeroQuesitosGanados())
                    .append("/6 quesitos")
                    .append(jugador.isTurnoActivo() ? " (TURNO ACTUAL)" : "")
                    .append("\n");
        }

        return stats.toString();
    }

    // Getters y Setters existentes
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public int getJugadorEnTurnoIndex() {
        return jugadorEnTurnoIndex;
    }

    public void setJugadorEnTurnoIndex(int jugadorEnTurnoIndex) {
        this.jugadorEnTurnoIndex = jugadorEnTurnoIndex;
    }

    public Map<Jugador, Integer> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(Map<Jugador, Integer> posiciones) {
        this.posiciones = posiciones;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    public void setPartidaTerminada(boolean partidaTerminada) {
        this.partidaTerminada = partidaTerminada;
    }

    public long getTiempoTotalSegundos() {
        return tiempoTotalSegundos;
    }

    public void setTiempoTotalSegundos(long tiempoTotalSegundos) {
        this.tiempoTotalSegundos = tiempoTotalSegundos;
    }

    public int getNumeroRonda() {
        return numeroRonda;
    }

    public void setNumeroRonda(int numeroRonda) {
        this.numeroRonda = numeroRonda;
    }

    /**
     * Obtiene el ganador de la partida.
     */
    public Jugador getGanador() {
        if (partidaTerminada) {
            for (Jugador jugador : jugadores) {
                if (verificarGanador(jugador)) {
                    return jugador;
                }
            }
        }
        return null;
    }
}