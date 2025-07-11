package org.example.triviaucab1.module;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Pregunta {
    private String categoria;
    private String pregunta;
    private String respuesta;

    // Lista de conectores/preposiciones/artículos a ignorar
    private static final List<String> CONECTORES = Arrays.asList(
            "el", "la", "los", "las", "un", "una", "unos", "unas",
            "al", "del", "lo", "este", "esta", "estos", "estas",
            "ese", "esa", "esos", "esas", "aquel", "aquella", "aquellos", "aquellas",
            "en", "de", "a", "por", "para", "con", "sin", "sobre", "bajo", "entre", "hacia"
    );

    @JsonCreator
    public Pregunta(
            @JsonProperty("pregunta") String textoPregunta,
            @JsonProperty("respuesta") String respuestaCorrecta) {
        this.pregunta = textoPregunta;
        this.respuesta = respuestaCorrecta;
    }

    public Pregunta() {
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTextoPregunta() {
        return this.pregunta;
    }

    public boolean esRespuestaCorrecta(String respuestaUsuario) {
        String respuestaNormalizada = normalizarTexto(this.respuesta);
        String usuarioNormalizado = normalizarTexto(respuestaUsuario);

        return respuestaNormalizada.equals(usuarioNormalizado);
    }

    public String getRespuestaCorrecta() {
        return this.respuesta;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Categoría: " + categoria + ", Pregunta: " + pregunta + ", Respuesta: " + respuesta;
    }

    // Método para normalizar el texto (quitar acentos, espacios, conectores, etc.)
    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        // Convertir a minúsculas
        String normalizado = texto.toLowerCase();

        // Quitar acentos y caracteres especiales
        normalizado = quitarAcentos(normalizado);

        // Quitar signos de puntuación
        normalizado = normalizado.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Quitar conectores comunes
        normalizado = quitarConectores(normalizado);

        // Quitar espacios extras y trim
        normalizado = normalizado.replaceAll("\\s+", " ").trim();

        return normalizado;
    }

    // Método para quitar acentos
    private String quitarAcentos(String texto) {
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    // Método para quitar conectores comunes
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