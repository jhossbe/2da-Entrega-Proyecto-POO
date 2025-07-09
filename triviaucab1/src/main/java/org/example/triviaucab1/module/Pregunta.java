package org.example.triviaucab1.module;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pregunta {
    private String categoria;
    private String pregunta;
    private String respuesta;

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
        return this.respuesta.trim().equalsIgnoreCase(respuestaUsuario.trim());
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
}