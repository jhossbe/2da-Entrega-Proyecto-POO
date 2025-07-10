package org.example.triviaucab1.module;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.Duration;
import java.util.Collections; // <--- AÑADE ESTA IMPORTACIÓN
import java.util.Random;      // <--- AÑADE ESTA IMPORTACIÓN

public class Partida {
    private List<Jugador> jugadores;
    private Tablero tablero; // Asumiendo que Tablero es serializable o no se guarda directamente
    private int jugadorActualIndex; // Nombre consistente: jugadorActualIndex
    // Recomendación: Cambiado a Map<String, Integer> para una serialización/deserialización robusta
    private Map<String, Integer> posiciones; // Map<ID Único del Jugador (ej. email), Posición>
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean partidaTerminada;
    private int tiempoRespuesta = 30; // Valor por defecto
    private long tiempoTotalSegundos;
    private Random random; // <--- AÑADE ESTE CAMPO

    public Partida() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero();
        // Inicializa el mapa de posiciones con clave String
        this.posiciones = new HashMap<>();
        this.jugadorActualIndex = 0;
        this.fechaInicio = LocalDateTime.now();
        this.partidaTerminada = false;
        this.tiempoTotalSegundos = 0;
        this.random = new Random(); // <--- INICIALIZA Random
    }

    public int getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void setTiempoRespuesta(int tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    // El método `iniciar` debería encargarse de las posiciones iniciales y la configuración
    public void iniciar(List<Jugador> jugadoresSeleccionados) {
        this.jugadores = jugadoresSeleccionados;
        // Llena el mapa de posiciones usando el ID único del jugador (ej. email o alias)
        this.posiciones.clear(); // Limpia cualquier posición anterior
        for (Jugador jugador : jugadoresSeleccionados) {
            // Usando el email como clave única para el seguimiento de la posición
            if (jugador.getEmail() != null && !jugador.getEmail().isEmpty()) {
                this.posiciones.put(jugador.getEmail(), 0); // Todos los jugadores empiezan en la posición 0 (o equivalente a 'c')
            } else {
                System.err.println("Advertencia: El jugador " + jugador.getAlias() + " no tiene email para el seguimiento de posición.");
                // Alternativa: usa el alias si el email no está disponible, pero asegúrate de que el alias sea único.
                this.posiciones.put(jugador.getAlias(), 0);
            }
        }
        // Importante: Aleatoriza el orden aquí o en el método que llama (JuegoController.setPartida)
        establecerOrdenAleatorio(); // <--- Llama para aleatorizar el orden
        this.partidaTerminada = false;
        this.fechaInicio = LocalDateTime.now();
        this.tiempoTotalSegundos = 0;
        System.out.println("Partida iniciada con " + jugadores.size() + " jugadores.");
    }

    // Nuevo: Establecer el orden aleatorio de los jugadores al inicio de la partida
    public void establecerOrdenAleatorio() {
        if (jugadores != null && !jugadores.isEmpty()) {
            java.util.Collections.shuffle(jugadores, random);
            this.jugadorActualIndex = 0; // Reiniciar al primer jugador del nuevo orden
            System.out.println("Orden de jugadores aleatorizado.");
        }
    }

    public Jugador getJugadorActual() { // Renombrado de getJugadorEnTurno para coincidir con sugerencias anteriores
        if (jugadores.isEmpty()) {
            return null;
        }
        return jugadores.get(jugadorActualIndex);
    }

    public void siguienteTurno() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida para avanzar el turno.");
            return;
        }
        this.jugadorActualIndex = (this.jugadorActualIndex + 1) % jugadores.size();
        System.out.println("Cambiando turno. Ahora es el turno de: " + getJugadorActual().getAlias());
        // Aquí podrías añadir lógica para verificar el ganador después de cada turno
        // if (verificarGanador(getJugadorActual())) {
        //     System.out.println("¡" + getJugadorActual().getAlias() + " ha ganado la partida!");
        //     // Lógica para finalizar el juego
        // }
    }

    // Debes asegurarte de que `jugador.getEmail()` sea realmente la clave correcta a usar.
    // Si planeas usar `alias` como clave, asegúrate de que `alias` sea único entre los jugadores.
    public void moverJugador(Jugador jugador, int pasos) {
        String playerKey = jugador.getEmail(); // O jugador.getAlias()
        if (playerKey == null || !posiciones.containsKey(playerKey)) {
            System.out.println("Error: El jugador " + jugador.getAlias() + " no está en esta partida o no tiene clave de posición.");
            return;
        }
        int currentPos = posiciones.getOrDefault(playerKey, 0);
        // Asegúrate de que tablero.getRutaPrincipalCasillaIds() esté correctamente inicializado y sea accesible
        // (Necesitarás una clase Tablero que tenga este método y un array de IDs de casillas válidas)
        int newPos = (currentPos + pasos) % tablero.getRutaPrincipalCasillaIds().length; // Esto asume un camino lineal
        posiciones.put(playerKey, newPos);
        System.out.println(jugador.getAlias() + " se movió de la posición " + currentPos + " a la posición " + newPos);
    }

    /**
     * Añade un quesito a un jugador (ahora usa el método del Jugador).
     *
     * @param jugador   El jugador que gana el quesito.
     * @param categoria La categoría del quesito ganado.
     */
    public void añadirQuesito(Jugador jugador, String categoria) {
        if (jugador != null) {
            jugador.addQuesito(categoria);
            System.out.println(jugador.getAlias() + " ganó el quesito de " + categoria);
        }
    }

    /**
     * Verifica si un jugador ha ganado la partida (ahora usa los quesitos del Jugador).
     *
     * @param jugador El jugador a verificar.
     * @return true si el jugador ha ganado, false en caso contrario.
     */
    public boolean verificarGanador(Jugador jugador) {
        int quesitosNecesarios = 6;
        return jugador != null && jugador.getQuesitosGanadosNombres().size() >= quesitosNecesarios;
    }

    public void terminarPartida() {
        this.partidaTerminada = true;
        this.fechaFin = LocalDateTime.now();
        this.tiempoTotalSegundos = Duration.between(fechaInicio, fechaFin).getSeconds();
    }

    // Getters y Setters
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        // Al establecer los jugadores externamente, es posible que quieras reinicializar posiciones y orden
        iniciar(jugadores); // Reinicializa las posiciones y aleatoriza el orden si se establece desde fuera
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    // Nombre consistente: getJugadorActualIndex / setJugadorActualIndex
    public int getJugadorActualIndex() {
        return jugadorActualIndex;
    }

    public void setJugadorActualIndex(int jugadorActualIndex) {
        this.jugadorActualIndex = jugadorActualIndex;
    }

    // El mapa de posiciones debería basarse en una clave String para una serialización adecuada
    public Map<String, Integer> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(Map<String, Integer> posiciones) {
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

    // También podrías necesitar métodos para obtener un jugador por su clave (email/alias)
    public Jugador getJugadorByEmail(String email) {
        for (Jugador jugador : jugadores) {
            if (jugador.getEmail() != null && jugador.getEmail().equals(email)) {
                return jugador;
            }
        }
        return null;
    }
}