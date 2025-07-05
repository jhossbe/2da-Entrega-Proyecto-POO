package org.example.triviaucab1.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class JsonService {

    private static final String JUGADORES_FILE = "jugadores.json";
    private static final String PARTIDA_FILE = "partida.json";
    private ObjectMapper objectMapper;

    public JsonService() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Para que el JSON se vea bonito
    }

    /**
     * Lee la lista de jugadores desde el archivo JSON.
     * @return Una lista de objetos Jugador.
     */
    public List<Jugador> cargarJugadores() {
        try {
            File file = new File(JUGADORES_FILE);
            if (!file.exists()) {
                System.out.println("Archivo de jugadores no encontrado, creando uno vacío.");
                // Si el archivo no existe, crea uno vacío o con un ejemplo
                guardarJugadores(new ArrayList<>());
                return new ArrayList<>();
            }
            // Lee el array de JSON y lo convierte a una lista de Jugador
            Jugador[] jugadoresArray = objectMapper.readValue(file, Jugador[].class);
            return new ArrayList<>(Arrays.asList(jugadoresArray));
        } catch (IOException e) {
            System.err.println("Error al cargar jugadores desde JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Guarda la lista de jugadores en el archivo JSON.
     * @param jugadores La lista de jugadores a guardar.
     */
    public void guardarJugadores(List<Jugador> jugadores) {
        try {
            objectMapper.writeValue(new File(JUGADORES_FILE), jugadores);
            System.out.println("Jugadores guardados en " + JUGADORES_FILE);
        } catch (IOException e) {
            System.err.println("Error al guardar jugadores en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga el estado de la última partida guardada.
     * @return El objeto Partida cargado.
     */
    public Partida cargarPartida() {
        try {
            File file = new File(PARTIDA_FILE);
            if (!file.exists()) {
                System.out.println("Archivo de partida no encontrado, retornando nueva partida.");
                return new Partida(); // Retorna una partida nueva si no hay archivo
            }
            return objectMapper.readValue(file, Partida.class);
        } catch (IOException e) {
            System.err.println("Error al cargar partida desde JSON: " + e.getMessage());
            e.printStackTrace();
            return new Partida();
        }
    }

    /**
     * Guarda el estado actual de la partida.
     * @param partida El objeto Partida a guardar.
     */
    public void guardarPartida(Partida partida) {
        try {
            objectMapper.writeValue(new File(PARTIDA_FILE), partida);
            System.out.println("Partida guardada en " + PARTIDA_FILE);
        } catch (IOException e) {
            System.err.println("Error al guardar partida en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}