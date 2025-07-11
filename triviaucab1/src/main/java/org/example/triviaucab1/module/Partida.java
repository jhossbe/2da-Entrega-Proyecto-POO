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

    /**
     * Lista de jugadores que participan en la partida.
     */
    private List<Jugador> jugadores;
    /**
     * El tablero de juego asociado a la partida.
     * Este campo es transitorio y se ignora durante la serialización/deserialización JSON.
     */
    @JsonIgnore
    private transient Tablero tablero;
    /**
     * Índice del jugador actual en la lista de jugadores, indicando quién tiene el turno.
     */
    private int jugadorActualIndex;
    /**
     * Mapa que almacena las posiciones de los jugadores en el tablero,
     * donde la clave es el email (o alias si el email no está disponible) del jugador
     * y el valor es el ID de la casilla actual.
     */
    private Map<String, String> posiciones;
    /**
     * Fecha y hora en que la partida fue iniciada.
     */
    private LocalDateTime fechaInicio;
    /**
     * Fecha y hora en que la partida fue terminada.
     */
    private LocalDateTime fechaFin;
    /**
     * Bandera que indica si la partida ha terminado.
     */
    private boolean partidaTerminada;
    /**
     * Duración total de la partida en segundos.
     */
    private long tiempoTotalSegundos;
    /**
     * Objeto Random para generar números aleatorios, utilizado para el orden de los jugadores.
     * Este campo es transitorio y se ignora durante la serialización/deserialización JSON.
     */
    @JsonIgnore
    private transient Random random;
    /**
     * Tiempo límite en segundos para que un jugador responda una pregunta.
     */
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
     * @throws IllegalArgumentException Si la lista de jugadores es nula o vacía.
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
     * Este método es ignorado por Jackson al serializar, ya que se deriva de jugadorActualIndex.
     * @return El objeto Jugador en turno, o null si no hay jugadores o el índice es inválido.
     */
    @JsonIgnore
    public Jugador getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty() || jugadorActualIndex < 0 || jugadorActualIndex >= jugadores.size()) {
            return null;
        }
        return jugadores.get(jugadorActualIndex);
    }

    /**
     * Avanza al siguiente turno, cambiando al siguiente jugador en la lista.
     * Si no hay jugadores en la partida, no hace nada.
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

    /**
     * Obtiene la lista de jugadores en la partida.
     * @return La lista de objetos Jugador.
     */
    public List<Jugador> getJugadores() { return jugadores; }

    /**
     * Establece la lista de jugadores para la partida y actualiza sus posiciones.
     * @param jugadores La nueva lista de jugadores.
     */
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

    /**
     * Obtiene el objeto Tablero de la partida.
     * Este método es ignorado por Jackson.
     * @return El objeto Tablero.
     */
    @JsonIgnore
    public Tablero getTablero() { return tablero; }

    /**
     * Establece el objeto Tablero para la partida.
     * Este método es ignorado por Jackson.
     * @param tablero El nuevo objeto Tablero.
     */
    @JsonIgnore
    public void setTablero(Tablero tablero) { this.tablero = tablero; }

    /**
     * Obtiene el tiempo límite en segundos para responder una pregunta.
     * @return El tiempo de respuesta en segundos.
     */
    public long getTiempoRespuesta() { return tiempoRespuesta; }

    /**
     * Establece el tiempo límite en segundos para responder una pregunta.
     * @param tiempoRespuesta El nuevo tiempo de respuesta en segundos.
     */
    public void setTiempoRespuesta(long tiempoRespuesta) { this.tiempoRespuesta = tiempoRespuesta; }

    /**
     * Obtiene el índice del jugador actual en la lista de jugadores.
     * @return El índice del jugador actual.
     */
    public int getJugadorActualIndex() {
        return jugadorActualIndex;
    }

    /**
     * Establece el índice del jugador actual en la lista de jugadores.
     * @param jugadorActualIndex El nuevo índice del jugador actual.
     */
    public void setJugadorActualIndex(int jugadorActualIndex) {
        this.jugadorActualIndex = jugadorActualIndex;
    }

    /**
     * Obtiene el mapa de posiciones de los jugadores.
     * @return El mapa de posiciones.
     */
    public Map<String, String> getPosiciones() {
        return posiciones;
    }

    /**
     * Establece el mapa de posiciones de los jugadores.
     * @param posiciones El nuevo mapa de posiciones.
     */
    public void setPosiciones(Map<String, String> posiciones) {
        this.posiciones = posiciones;
    }

    /**
     * Obtiene la fecha y hora de inicio de la partida.
     * @return La fecha y hora de inicio.
     */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha y hora de inicio de la partida.
     * @param fechaInicio La nueva fecha y hora de inicio.
     */
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha y hora de finalización de la partida.
     * @return La fecha y hora de finalización.
     */
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha y hora de finalización de la partida.
     * @param fechaFin La nueva fecha y hora de finalización.
     */
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Verifica si la partida ha terminado.
     * @return true si la partida ha terminado, false en caso contrario.
     */
    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    /**
     * Establece el estado de terminación de la partida.
     * @param partidaTerminada true si la partida ha terminado, false en caso contrario.
     */
    public void setPartidaTerminada(boolean partidaTerminada) {
        this.partidaTerminada = partidaTerminada;
    }

    /**
     * Obtiene el tiempo total de la partida en segundos.
     * @return El tiempo total en segundos.
     */
    public long getTiempoTotalSegundos() {
        return tiempoTotalSegundos;
    }

    /**
     * Establece el tiempo total de la partida en segundos.
     * @param tiempoTotalSegundos El nuevo tiempo total en segundos.
     */
    public void setTiempoTotalSegundos(long tiempoTotalSegundos) {
        this.tiempoTotalSegundos = tiempoTotalSegundos;
    }

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
