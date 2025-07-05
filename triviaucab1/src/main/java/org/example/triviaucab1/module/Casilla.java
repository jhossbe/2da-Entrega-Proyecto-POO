package org.example.triviaucab1.module;

public class Casilla {
    private String categoria; // Ej: "Historia", "Ciencia", "Entretenimiento"
    private boolean esCentral; // Si es la casilla central
    private boolean esQuesito; // Si es una casilla de "quesito" por categoría

    public Casilla(String categoria, boolean esCentral, boolean esQuesito) {
        this.categoria = categoria;
        this.esCentral = esCentral;
        this.esQuesito = esQuesito;
    }

    // Getters
    public String getCategoria() {
        return categoria;
    }

    public boolean esCentral() {
        return esCentral;
    }

    public boolean esQuesito() {
        return esQuesito;
    }

    // Setters (si es necesario modificar la casilla durante el juego)
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setEsCentral(boolean esCentral) {
        this.esCentral = esCentral;
    }

    public void setEsQuesito(boolean esQuesito) {
        this.esQuesito = esQuesito;
    }
}