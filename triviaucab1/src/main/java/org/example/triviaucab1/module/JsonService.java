package org.example.triviaucab1.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JsonService {

    // Define la ruta del archivo de jugadores para lectura de recursos (dentro del JAR)
    private static final String JUGADORES_RESOURCE_PATH = "/jugadores.json";
    private static final String JUGADORES_FILE_NAME = "jugadores.json"; // Para guardar
    private static final String PARTIDA_FILE_NAME = "partida.json"; // Para guardar/cargar partida

    private ObjectMapper objectMapper;

    public JsonService() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Support for Java 8 time types
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Write dates as ISO strings
    }

    /**
     * Lee la lista de jugadores desde el archivo JSON de recursos.
     * Asume que jugadores.json está en src/main/resources/ y solo contiene email y alias.
     * Los campos como EstadisticasJugador se inicializarán por el constructor de Jugador.
     * @return Una lista de objetos Jugador.
     */
    public List<Jugador> cargarJugadores() {
        List<Jugador> jugadores = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(JUGADORES_RESOURCE_PATH)) {
            if (is == null) {
                System.err.println("Error: Archivo de jugadores '" + JUGADORES_RESOURCE_PATH + "' no encontrado en los recursos. Retornando lista vacía.");
                // Si el recurso no se encuentra (ej. en un entorno de desarrollo donde no está copiado),
                // podrías considerar una ruta alternativa para desarrollo o simplemente retornar vacío.
                return new ArrayList<>();
            }

            // Usar TypeReference es la forma robusta de deserializar listas de objetos complejos con Jackson
            jugadores = objectMapper.readValue(is, new TypeReference<List<Jugador>>() {});
            System.out.println("Jugadores cargados exitosamente desde " + JUGADORES_RESOURCE_PATH);
        } catch (IOException e) {
            System.err.println("Error al cargar jugadores desde JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return jugadores;
    }

    /**
     * Guarda la lista de jugadores en el archivo JSON.
     * Este método guardará el archivo en el directorio donde se ejecute el JAR o en la raíz del proyecto.
     * Ten en cuenta que si el `jugadores.json` de recursos es estático, este método solo
     * se usaría si los jugadores pueden ser creados/modificados y guardados por el usuario.
     * @param jugadores La lista de jugadores a guardar.
     */
    public void guardarJugadores(List<Jugador> jugadores) {
        try (OutputStream os = Files.newOutputStream(Paths.get(JUGADORES_FILE_NAME))) {
            objectMapper.writeValue(os, jugadores);
            System.out.println("Jugadores guardados en " + JUGADORES_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error al guardar jugadores en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Los métodos cargarPartida() y guardarPartida() pueden permanecer como están
    // ya que probablemente Partida.json se guarda y carga en el directorio de trabajo.

    /**
     * Carga el estado de la última partida guardada.
     * @return El objeto Partida cargado.
     */
    public Partida cargarPartida() {
        try {
            File file = new File(PARTIDA_FILE_NAME);
            if (!file.exists()) {
                System.out.println("Archivo de partida no encontrado, retornando nueva partida.");
                return new Partida();
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
            objectMapper.writeValue(new File(PARTIDA_FILE_NAME), partida);
            System.out.println("Partida guardada en " + PARTIDA_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error al guardar partida en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}