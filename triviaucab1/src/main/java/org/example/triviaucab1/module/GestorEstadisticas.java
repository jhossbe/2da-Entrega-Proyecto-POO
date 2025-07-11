package org.example.triviaucab1.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gestor para cargar y guardar las estadísticas de todos los jugadores desde/hacia un archivo JSON.
 */
public class GestorEstadisticas {

    private static final String ESTADISTICAS_FILE_NAME = "estadisticasJugadores.json";
    private final ObjectMapper objectMapper;
    private List<Jugador> jugadoresConEstadisticas;

    public GestorEstadisticas() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.jugadoresConEstadisticas = new ArrayList<>();
        cargarEstadisticas();
    }

    /**
     * Carga las estadísticas de los jugadores desde el archivo JSON.
     * Intenta cargar desde el directorio de trabajo, o si no existe,
     * desde los recursos del JAR (si hay un archivo inicial ahí).
     */
    private void cargarEstadisticas() {
        Path filePath = Paths.get(ESTADISTICAS_FILE_NAME);

        if (Files.exists(filePath)) {
            try (InputStream is = Files.newInputStream(filePath)) {
                this.jugadoresConEstadisticas = objectMapper.readValue(is, new TypeReference<List<Jugador>>() {});
                System.out.println("Estadísticas cargadas exitosamente desde: " + filePath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error al cargar el archivo de estadísticas desde el directorio de trabajo (" + ESTADISTICAS_FILE_NAME + "). Error: " + e.getMessage());
                this.jugadoresConEstadisticas = new ArrayList<>();
            }
        } else {
            try (InputStream is = getClass().getResourceAsStream("/" + ESTADISTICAS_FILE_NAME)) {
                if (is != null) {
                    this.jugadoresConEstadisticas = objectMapper.readValue(is, new TypeReference<List<Jugador>>() {});
                    System.out.println("Estadísticas cargadas exitosamente desde los recursos (JAR): " + ESTADISTICAS_FILE_NAME);
                    guardarEstadisticas();
                } else {
                    System.out.println("Archivo de estadísticas inicial '" + ESTADISTICAS_FILE_NAME + "' no encontrado ni en directorio de trabajo ni en recursos. Iniciando con lista vacía.");
                    this.jugadoresConEstadisticas = new ArrayList<>();
                }
            } catch (IOException e) {
                System.err.println("Error al cargar el archivo de estadísticas desde recursos (" + ESTADISTICAS_FILE_NAME + "): " + e.getMessage());
                e.printStackTrace();
                this.jugadoresConEstadisticas = new ArrayList<>();
            }
        }
    }

    /**
     * Guarda las estadísticas actuales de los jugadores en el archivo JSON
     * en el directorio de trabajo de la aplicación.
     */
    public void guardarEstadisticas() {
        Path filePath = Paths.get(ESTADISTICAS_FILE_NAME);
        try (OutputStream os = Files.newOutputStream(filePath)) {
            objectMapper.writeValue(os, this.jugadoresConEstadisticas);
            System.out.println("Estadísticas guardadas exitosamente en: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de estadísticas (" + ESTADISTICAS_FILE_NAME + "): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza las estadísticas de un jugador específico en la lista interna.
     * Si el jugador no existe, se añade.
     * @param jugador El objeto Jugador con las estadísticas actualizadas.
     */
    public void actualizarEstadisticasJugador(Jugador jugador) {
        Optional<Jugador> jugadorExistente = jugadoresConEstadisticas.stream()
                .filter(j -> j.getEmail().equals(jugador.getEmail()))
                .findFirst();

        if (jugadorExistente.isPresent()) {
            int index = jugadoresConEstadisticas.indexOf(jugadorExistente.get());
            if (index != -1) {
                jugadoresConEstadisticas.set(index, jugador);
                System.out.println("Estadísticas de jugador " + jugador.getAlias() + " actualizadas en memoria.");
            }
        } else {
            jugadoresConEstadisticas.add(jugador);
            System.out.println("Jugador " + jugador.getAlias() + " añadido a las estadísticas en memoria.");
        }
        guardarEstadisticas();
    }

    /**
     * Obtiene la lista completa de jugadores con sus estadísticas.
     * @return Una lista de objetos Jugador (con sus estadísticas)
     */
    public List<Jugador> getRankingJugadores() {
        List<Jugador> ranking = new ArrayList<>(this.jugadoresConEstadisticas);
        ranking.sort((j1, j2) -> {
            int cmpGanadas = Integer.compare(j2.getEstadisticas().getPartidasGanadas(), j1.getEstadisticas().getPartidasGanadas());
            if (cmpGanadas != 0) return cmpGanadas;
            return Integer.compare(j2.getEstadisticas().getPreguntasCorrectasTotal(), j1.getEstadisticas().getPreguntasCorrectasTotal());
        });
        return ranking;
    }
}
