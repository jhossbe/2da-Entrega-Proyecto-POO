package org.example.triviaucab1.module;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Clase que representa una pregunta de trivia, incluyendo su categoría,
 * el texto de la pregunta y la respuesta correcta.
 * Proporciona métodos para normalizar y comparar respuestas.
 */
public class Pregunta {
    /**
     * La categoría a la que pertenece la pregunta (ej. "Geografía", "Historia").
     */
    private String categoria;
    /**
     * El texto completo de la pregunta.
     */
    private String pregunta;
    /**
     * La respuesta correcta a la pregunta.
     */
    private String respuesta;
    /**
     * Lista estática de conectores comunes en español que se ignoran al normalizar las respuestas.
     */
    private static final List<String> CONECTORES = Arrays.asList(
            "el", "la", "los", "las", "un", "una", "unos", "unas",
            "al", "del", "lo", "este", "esta", "estos", "estas",
            "ese", "esa", "esos", "esas", "aquel", "aquella", "aquellos", "aquellas",
            "en", "de", "a", "por", "para", "con", "sin", "sobre", "bajo", "entre", "hacia"
    );

    /**
     * Constructor utilizado por Jackson para deserializar objetos Pregunta desde JSON.
     * @param textoPregunta El texto de la pregunta.
     * @param respuestaCorrecta La respuesta correcta a la pregunta.
     */
    @JsonCreator
    public Pregunta(
            @JsonProperty("pregunta") String textoPregunta,
            @JsonProperty("respuesta") String respuestaCorrecta) {
        this.pregunta = textoPregunta;
        this.respuesta = respuestaCorrecta;
    }

    /**
     * Constructor por defecto de la clase Pregunta.
     */
    public Pregunta() {
    }

    /**
     * Obtiene la categoría de la pregunta.
     * @return La categoría de la pregunta.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Obtiene el texto de la pregunta.
     * @return El texto de la pregunta.
     */
    public String getTextoPregunta() {
        return this.pregunta;
    }

    /**
     * Verifica si la respuesta proporcionada por el usuario es correcta,
     * realizando una normalización de ambos textos para una comparación flexible.
     * La normalización incluye quitar acentos, caracteres no alfanuméricos y conectores.
     * @param respuestaUsuario La respuesta ingresada por el usuario.
     * @return true si la respuesta del usuario es considerada correcta, false en caso contrario.
     */
    public boolean esRespuestaCorrecta(String respuestaUsuario) {
        String respuestaNormalizada = normalizarTexto(this.respuesta);
        String usuarioNormalizado = normalizarTexto(respuestaUsuario);

        return respuestaNormalizada.equals(usuarioNormalizado);
    }

    /**
     * Establece la categoría de la pregunta.
     * @param categoria La nueva categoría de la pregunta.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Retorna una representación en cadena del objeto Pregunta.
     * @return Una cadena que incluye la categoría, pregunta y respuesta.
     */
    @Override
    public String toString() {
        return "Categoría: " + categoria + ", Pregunta: " + pregunta + ", Respuesta: " + respuesta;
    }

    /**
     * Normaliza un texto para facilitar comparaciones, eliminando acentos,
     * caracteres especiales, conectores y espacios extra.
     * @param texto El texto a normalizar.
     * @return El texto normalizado.
     */
    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        String normalizado = texto.toLowerCase();
        normalizado = quitarAcentos(normalizado);
        normalizado = normalizado.replaceAll("[^a-zA-Z0-9\\s]", "");
        normalizado = quitarConectores(normalizado);
        normalizado = normalizado.replaceAll("\\s+", " ").trim();

        return normalizado;
    }

    /**
     * Elimina los acentos de un texto.
     * @param texto El texto del cual se eliminarán los acentos.
     * @return El texto sin acentos.
     */
    private String quitarAcentos(String texto) {
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    /**
     * Elimina los conectores comunes definidos en {@code CONECTORES} de un texto.
     * @param texto El texto del cual se eliminarán los conectores.
     * @return El texto sin conectores.
     */
    private String quitarConectores(String texto) {
        String[] palabras = texto.split("\\s+");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (!CONECTORES.contains(palabra)) {
                if (resultado.length() > 0) {
                    resultado.append(" ");
                }
                resultado.append(palabra);
            }
        }

        return resultado.toString();
    }
}
