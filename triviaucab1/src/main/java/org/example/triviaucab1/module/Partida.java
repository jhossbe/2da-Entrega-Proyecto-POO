package org.example.triviaucab1.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import java.util.Random;

/**
 * Clase que representa el estado de una partida de trivia.
 * Contiene la información de los jugadores, el turno actual,
 * el tiempo de respuesta, y el estado del juego.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Partida {

    private List<Jugador> jugadores;
    @JsonIgnore
    private transient Tablero tablero;
    private int jugadorActualIndex;
    private Map<String, String> posiciones;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean partidaTerminada;
    private long tiempoTotalSegundos;
    @JsonIgnore
    private transient Random random;
    private long tiempoRespuesta;

    /**
     * Constructor por defecto de la clase Partida.
     * Inicializa las listas y mapas necesarios, y establece valores por defecto.
     * Jackson lo usará al deserializar el JSON.
     */
    public Partida() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero();
        this.posiciones = new HashMap<>();
        this.jugadorActualIndex = 0;
        this.fechaInicio = LocalDateTime.now();
        this.partidaTerminada = false;
        this.tiempoTotalSegundos = 0;
        this.random = new Random();
        this.tiempoRespuesta = 15;
    }

    /**
     * Inicializa una nueva partida con los jugadores seleccionados y el tiempo de respuesta.
     * Este método se usa para comenzar una partida desde cero.
     * @param jugadoresSeleccionados La lista de jugadores que participarán en la partida.
     * @param tiempoRespuesta El tiempo límite en segundos para responder cada pregunta.
     */
    public void iniciar(List<Jugador> jugadoresSeleccionados, long tiempoRespuesta) {
        if (jugadoresSeleccionados == null || jugadoresSeleccionados.isEmpty()) {
            throw new IllegalArgumentException("La lista de jugadores no puede ser nula o vacía.");
        }
        this.jugadores = new ArrayList<>(jugadoresSeleccionados);
        this.tiempoRespuesta = tiempoRespuesta;
        this.posiciones.clear();
        for (Jugador jugador : this.jugadores) {
            jugador.setQuesitosGanadosNombres(new ArrayList<>());
            jugador.setCasillaActualId("c");

            if (jugador.getEmail() != null && !jugador.getEmail().isEmpty()) {
                this.posiciones.put(jugador.getEmail(), "c");
            } else {
                System.err.println("Advertencia: El jugador " + jugador.getAlias() + " no tiene email. Usando alias como clave de posición.");
                this.posiciones.put(jugador.getAlias(), "c");
            }
        }
        establecerOrdenAleatorio();
        this.partidaTerminada = false;
        this.fechaInicio = LocalDateTime.now();
        this.tiempoTotalSegundos = 0;
        System.out.println("Partida iniciada con " + jugadores.size() + " jugadores.");
        System.out.println("Tiempo de respuesta configurado: " + tiempoRespuesta + " segundos.");
    }

    /**
     * Establece el orden aleatorio de los jugadores al inicio de la partida.
     */
    public void establecerOrdenAleatorio() {
        if (jugadores != null && !jugadores.isEmpty()) {
            Collections.shuffle(jugadores, random);
            this.jugadorActualIndex = 0;
            System.out.println("Orden de jugadores aleatorizado.");
        }
    }

    /**
     * Obtiene el jugador que tiene el turno actual.
     * @return El objeto Jugador en turno, o null si no hay jugadores.
     */
    @JsonIgnore // Ignorar este getter al serializar, ya que se deriva de jugadorActualIndex
    public Jugador getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty() || jugadorActualIndex < 0 || jugadorActualIndex >= jugadores.size()) {
            return null;
        }
        return jugadores.get(jugadorActualIndex);
    }

    /**
     * Avanza al siguiente turno, cambiando al siguiente jugador en la lista.
     */
    public void siguienteTurno() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida para avanzar el turno.");
            return;
        }
        this.jugadorActualIndex = (this.jugadorActualIndex + 1) % jugadores.size();
        System.out.println("Cambiando turno. Ahora es el turno de: " + getJugadorActual().getAlias());
    }


    /**
     * Marca la partida como terminada y calcula la duración total.
     */
    public void terminarPartida() {
        this.partidaTerminada = true;
        this.fechaFin = LocalDateTime.now();
        if (fechaInicio != null && fechaFin != null) {
            this.tiempoTotalSegundos = Duration.between(fechaInicio, fechaFin).getSeconds();
        }
    }

    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.posiciones.clear();
        if (jugadores != null) {
            for (Jugador jugador : jugadores) {
                if (jugador.getEmail() != null && !jugador.getEmail().isEmpty()) {
                    this.posiciones.put(jugador.getEmail(), jugador.getCasillaActualId());
                } else {
                    this.posiciones.put(jugador.getAlias(), jugador.getCasillaActualId());
                }
            }
        }
    }

    @JsonIgnore
    public Tablero getTablero() { return tablero; }
    @JsonIgnore
    public void setTablero(Tablero tablero) { this.tablero = tablero; }


    public long getTiempoRespuesta() { return tiempoRespuesta; }

    /**
     * Elimina un jugador de la partida.
     * Si el jugador eliminado era el jugador actual, el turno se ajusta para el siguiente jugador.
     * @param jugador El objeto Jugador a eliminar.
     */
    public void removeJugador(Jugador jugador) {
        if (jugador == null || jugadores == null || jugadores.isEmpty()) {
            return;
        }
        int indexToRemove = -1;
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getAlias().equals(jugador.getAlias())) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            jugadores.remove(indexToRemove);
            System.out.println("Jugador " + jugador.getAlias() + " eliminado de la partida.");
            if (jugadorActualIndex >= jugadores.size() && !jugadores.isEmpty()) {
                jugadorActualIndex = 0;
            } else if (indexToRemove < jugadorActualIndex) {
                jugadorActualIndex--;
            }
        } else {
            System.out.println("Jugador " + jugador.getAlias() + " no encontrado en la partida.");
        }
    }
}
