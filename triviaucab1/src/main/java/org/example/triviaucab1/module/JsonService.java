package org.example.triviaucab1.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Servicio para guardar y cargar objetos de partida y estadísticas de jugadores en formato JSON.
 * Utiliza la librería Jackson para la serialización y deserialización.
 */
public class JsonService {
    private final ObjectMapper objectMapper;
    private static final String PARTIDAS_FILE = "partidas_guardadas.json";
    private static final String JUGADORES_FILE = "jugadores.json";
    private static final String ESTADISTICAS_FILE = "estadisticasJugadores.json";

    public JsonService() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Guarda el estado actual de la partida en un archivo JSON.
     *
     * @param partida El objeto Partida a guardar.
     */
    public void guardarPartida(Partida partida) {
        try {
            File file = new File(PARTIDAS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writeValue(file, partida);
            System.out.println("Partida guardada exitosamente en " + PARTIDAS_FILE);
        } catch (IOException e) {
            System.err.println("Error al guardar partida en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga el estado de una partida desde un archivo JSON.
     *
     * @return El objeto Partida cargado, o null si no se pudo cargar.
     */
    public Partida cargarPartida() {
        File file = new File(PARTIDAS_FILE);
        if (file.exists() && file.length() > 0) {
            try {
                return objectMapper.readValue(file, Partida.class);
            } catch (IOException e) {
                System.err.println("Error al cargar partida desde JSON: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Carga la lista de jugadores desde un archivo JSON.
     *
     * @return Una lista de objetos Jugador.
     */
    public List<Jugador> cargarJugadores() {
        File file = new File(JUGADORES_FILE);
        if (file.exists() && file.length() > 0) {
            try {
                return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Jugador.class));
            } catch (IOException e) {
                System.err.println("Error al cargar jugadores desde JSON: " + e.getMessage());
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    /**
     * Guarda la lista de jugadores en un archivo JSON.
     *
     * @param jugadores La lista de objetos Jugador a guardar.
     */
    public void guardarJugadores(List<Jugador> jugadores) {
        try {
            File file = new File(JUGADORES_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writeValue(file, jugadores);
            System.out.println("Jugadores guardados exitosamente en " + JUGADORES_FILE);
        } catch (IOException e) {
            System.err.println("Error al guardar jugadores en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Elimina el archivo de la partida guardada.
     * Se llama cuando una partida finaliza (victoria, rendición, etc.) para que no se pueda cargar de nuevo.
     */
    public void eliminarPartidaGuardada() {
        try {
            File file = new File(PARTIDAS_FILE);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Archivo de partida '" + PARTIDAS_FILE + "' eliminado exitosamente.");
                } else {
                    System.err.println("Error: No se pudo eliminar el archivo de partida '" + PARTIDAS_FILE + "'.");
                }
            } else {
                System.out.println("Advertencia: No se encontró el archivo de partida '" + PARTIDAS_FILE + "' para eliminar.");
            }
        } catch (SecurityException e) {
            System.err.println("Error de seguridad al intentar eliminar el archivo de partida: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
