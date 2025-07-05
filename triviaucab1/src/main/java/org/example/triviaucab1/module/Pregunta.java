package org.example.triviaucab1.module;

import java.util.List;

public class Pregunta {
    private String categoria;
    private String textoPregunta;
    private List<String> opciones;
    private String respuestaCorrecta;

    public Pregunta(String categoria, String textoPregunta, List<String> opciones, String respuestaCorrecta) {
        this.categoria = categoria;
        this.textoPregunta = textoPregunta;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    // Getters y Setters
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public boolean esRespuestaCorrecta(String respuestaUsuario) {
        return this.respuestaCorrecta.equalsIgnoreCase(respuestaUsuario);
    }
}