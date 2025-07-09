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

    // Nombre del archivo JSON. Se buscará y guardará en el directorio de trabajo.
    private static final String ESTADISTICAS_FILE_NAME = "estadisticasJugadores.json";
    private final ObjectMapper objectMapper;
    private List<Jugador> jugadoresConEstadisticas; // Lista interna para mantener las estadísticas en memoria

    public GestorEstadisticas() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Para que el JSON se vea bonito
        this.jugadoresConEstadisticas = new ArrayList<>();
        cargarEstadisticas(); // Cargar al inicializar
    }

    /**
     * Carga las estadísticas de los jugadores desde el archivo JSON.
     * Intenta cargar desde el directorio de trabajo, o si no existe,
     * desde los recursos del JAR (si hay un archivo inicial ahí).
     */
    private void cargarEstadisticas() {
        // Ruta donde se espera que esté el archivo de estadísticas (directorio de trabajo)
        Path filePath = Paths.get(ESTADISTICAS_FILE_NAME);

        if (Files.exists(filePath)) { // Si el archivo existe en el directorio de trabajo, lo cargamos
            try (InputStream is = Files.newInputStream(filePath)) {
                this.jugadoresConEstadisticas = objectMapper.readValue(is, new TypeReference<List<Jugador>>() {});
                System.out.println("📊 Estadísticas cargadas exitosamente desde: " + filePath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("⚠️ Error al cargar el archivo de estadísticas desde el directorio de trabajo (" + ESTADISTICAS_FILE_NAME + "). Error: " + e.getMessage());
                this.jugadoresConEstadisticas = new ArrayList<>(); // Inicializar vacía si hay error
            }
        } else { // Si no existe en el directorio de trabajo, intentamos cargar desde los recursos (JAR)
            try (InputStream is = getClass().getResourceAsStream("/" + ESTADISTICAS_FILE_NAME)) { // Nota el '/' inicial
                if (is != null) {
                    this.jugadoresConEstadisticas = objectMapper.readValue(is, new TypeReference<List<Jugador>>() {});
                    System.out.println("📊 Estadísticas cargadas exitosamente desde los recursos (JAR): " + ESTADISTICAS_FILE_NAME);
                    // Opcional: Si lo cargas desde recursos y es la primera vez, podrías guardarlo al directorio de trabajo
                    // para futuras modificaciones.
                    guardarEstadisticas(); // Guarda la versión inicial en el directorio de trabajo
                } else {
                    System.out.println("⚠️ Archivo de estadísticas inicial '" + ESTADISTICAS_FILE_NAME + "' no encontrado ni en directorio de trabajo ni en recursos. Iniciando con lista vacía.");
                    this.jugadoresConEstadisticas = new ArrayList<>();
                }
            } catch (IOException e) {
                System.err.println("❌ Error al cargar el archivo de estadísticas desde recursos (" + ESTADISTICAS_FILE_NAME + "): " + e.getMessage());
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
        Path filePath = Paths.get(ESTADISTICAS_FILE_NAME); // Guarda en el directorio de trabajo
        try (OutputStream os = Files.newOutputStream(filePath)) {
            objectMapper.writeValue(os, this.jugadoresConEstadisticas);
            System.out.println("📊 Estadísticas guardadas exitosamente en: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ Error al guardar el archivo de estadísticas (" + ESTADISTICAS_FILE_NAME + "): " + e.getMessage());
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
                .filter(j -> j.getEmail().equals(jugador.getEmail())) // Asumimos que el email es único
                .findFirst();

        if (jugadorExistente.isPresent()) {
            // Reemplaza el jugador existente con el actualizado
            int index = jugadoresConEstadisticas.indexOf(jugadorExistente.get()); // Obtiene el índice del jugador existente
            if (index != -1) {
                jugadoresConEstadisticas.set(index, jugador); // Reemplaza el objeto completo en la lista
                System.out.println("Estadísticas de jugador " + jugador.getAlias() + " actualizadas en memoria.");
            }
        } else {
            // Si el jugador es nuevo, lo añade (normalmente no debería pasar si se carga de jugadores.json)
            // Solo se añade si realmente es un jugador que termina una partida y no estaba en el ranking previo
            jugadoresConEstadisticas.add(jugador);
            System.out.println("Jugador " + jugador.getAlias() + " añadido a las estadísticas en memoria.");
        }
        guardarEstadisticas(); // Guarda los cambios inmediatamente
    }

    /**
     * Obtiene la lista completa de jugadores con sus estadísticas.
     * @return Una lista de objetos Jugador (con sus estadísticas)
     */
    public List<Jugador> getRankingJugadores() {
        // Devolver una copia para evitar modificaciones externas directas de la lista interna
        List<Jugador> ranking = new ArrayList<>(this.jugadoresConEstadisticas);
        ranking.sort((j1, j2) -> {
            // Ejemplo de ordenación: más partidas ganadas primero
            int cmpGanadas = Integer.compare(j2.getEstadisticas().getPartidasGanadas(), j1.getEstadisticas().getPartidasGanadas());
            if (cmpGanadas != 0) return cmpGanadas;
            // Luego, por más preguntas correctas totales
            return Integer.compare(j2.getEstadisticas().getPreguntasCorrectasTotal(), j1.getEstadisticas().getPreguntasCorrectasTotal());
        });
        return ranking;
    }
}