package org.example.triviaucab1.module;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GestorPreguntas {
    private Map<String, List<Pregunta>> preguntasPorCategoria;
    private List<String> categoriasDisponibles;

    public GestorPreguntas(String rutaArchivoJson) {
        preguntasPorCategoria = new HashMap<>();
        categoriasDisponibles = new ArrayList<>();
        cargarPreguntas(rutaArchivoJson);
    }

    private void cargarPreguntas(String rutaArchivoJson) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/" + rutaArchivoJson)) {
            if (is == null) {
                System.err.println("Error: Archivo JSON no encontrado en la ruta: " + rutaArchivoJson);
                return;
            }
            // Leer el JSON como un mapa donde las claves son las categorías
            Map<String, List<Pregunta>> tempMap = mapper.readValue(is, new TypeReference<Map<String, List<Pregunta>>>() {});

            // **MODIFICACIÓN CLAVE AQUÍ:**
            // Iterar sobre cada categoría y establecer el nombre de la categoría
            // para cada objeto Pregunta tan pronto como se cargan.
            for (Map.Entry<String, List<Pregunta>> entry : tempMap.entrySet()) {
                String categoriaActual = entry.getKey();
                List<Pregunta> preguntasDeEstaCategoria = entry.getValue();

                for (Pregunta pregunta : preguntasDeEstaCategoria) {
                    pregunta.setCategoria(categoriaActual); // Establece la categoría aquí
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

    public Pregunta getPreguntaAleatoria(String categoria) {
        List<Pregunta> listaPreguntas;
        String categoriaSeleccionada;

        if (categoria != null && preguntasPorCategoria.containsKey(categoria)) {
            listaPreguntas = preguntasPorCategoria.get(categoria);
            categoriaSeleccionada = categoria; // Usar la categoría solicitada
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

    public int getTotalCategorias() {
        return categoriasDisponibles.size();
    }
}