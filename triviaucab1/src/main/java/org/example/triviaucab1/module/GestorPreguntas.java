package org.example.triviaucab1.module;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Clase para gestionar la carga y el acceso a preguntas desde un archivo JSON.
 * Organiza las preguntas por categoría y permite obtener preguntas aleatorias.
 */
public class GestorPreguntas {
    /**
     * Un mapa que almacena las preguntas, organizadas por categoría.
     * La clave es el nombre de la categoría (String) y el valor es una lista de objetos Pregunta.
     */
    private Map<String, List<Pregunta>> preguntasPorCategoria;
    /**
     * Una lista que contiene los nombres de todas las categorías de preguntas disponibles.
     */
    private List<String> categoriasDisponibles;

    /**
     * Constructor para la clase GestorPreguntas.
     * Inicializa el gestor y carga las preguntas desde el archivo JSON especificado.
     * @param rutaArchivoJson La ruta del archivo JSON que contiene las preguntas.
     */
    public GestorPreguntas(String rutaArchivoJson) {
        preguntasPorCategoria = new HashMap<>();
        categoriasDisponibles = new ArrayList<>();
        cargarPreguntas(rutaArchivoJson);
    }

    /**
     * Carga las preguntas desde un archivo JSON.
     * El archivo JSON debe estar en el classpath y tener un formato de mapa donde las claves son las categorías
     * y los valores son listas de preguntas.
     * Establece la categoría para cada objeto Pregunta después de cargarlo.
     * @param rutaArchivoJson La ruta del archivo JSON a cargar.
     */
    private void cargarPreguntas(String rutaArchivoJson) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/" + rutaArchivoJson)) {
            if (is == null) {
                System.err.println("Error: Archivo JSON no encontrado en la ruta: " + rutaArchivoJson);
                return;
            }
            Map<String, List<Pregunta>> tempMap = mapper.readValue(is, new TypeReference<Map<String, List<Pregunta>>>() {});

            for (Map.Entry<String, List<Pregunta>> entry : tempMap.entrySet()) {
                String categoriaActual = entry.getKey();
                List<Pregunta> preguntasDeEstaCategoria = entry.getValue();

                for (Pregunta pregunta : preguntasDeEstaCategoria) {
                    pregunta.setCategoria(categoriaActual);
                }
                preguntasPorCategoria.put(categoriaActual, preguntasDeEstaCategoria);
            }
            categoriasDisponibles.addAll(preguntasPorCategoria.keySet());
            System.out.println("Preguntas cargadas exitosamente desde " + rutaArchivoJson);
            System.out.println("Categorías cargadas: " + categoriasDisponibles);
        } catch (IOException e) {
            System.err.println("Error al cargar preguntas desde JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una pregunta aleatoria de una categoría específica.
     * Si la categoría es nula o no existe, selecciona una categoría aleatoria de las disponibles.
     * @param categoria La categoría de la cual se desea obtener una pregunta. Puede ser nulo para una categoría aleatoria.
     * @return Un objeto Pregunta aleatorio de la categoría especificada o de una categoría aleatoria,
     * o null si no hay preguntas disponibles para la categoría o en general.
     */
    public Pregunta getPreguntaAleatoria(String categoria) {
        List<Pregunta> listaPreguntas;
        String categoriaSeleccionada;

        if (categoria != null && preguntasPorCategoria.containsKey(categoria)) {
            listaPreguntas = preguntasPorCategoria.get(categoria);
            categoriaSeleccionada = categoria;
        } else {
            if (categoriasDisponibles.isEmpty()) {
                System.err.println("No hay categorías disponibles para obtener preguntas.");
                return null;
            }
            Random rand = new Random();
            categoriaSeleccionada = categoriasDisponibles.get(rand.nextInt(categoriasDisponibles.size()));
            listaPreguntas = preguntasPorCategoria.get(categoriaSeleccionada);
            System.out.println("Seleccionada categoría aleatoria: " + categoriaSeleccionada);
        }

        if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
            Random rand = new Random();
            Pregunta preguntaSeleccionada = listaPreguntas.get(rand.nextInt(listaPreguntas.size()));
            return preguntaSeleccionada;
        } else {
            System.err.println("No hay preguntas disponibles para la categoría: " + categoriaSeleccionada);
            return null;
        }
    }

    /**
     * Obtiene el número total de categorías de preguntas disponibles.
     * @return El número de categorías disponibles.
     */
    public int getTotalCategorias() {
        return categoriasDisponibles.size();
    }
}