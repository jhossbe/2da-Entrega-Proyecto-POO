package org.example.triviaucab1.module;

/**
 * Representa una casilla en el tablero de juego, definiendo sus propiedades principales.
 * Una casilla puede tener una categoría asociada (para preguntas), ser la casilla central,
 * o ser una casilla de tipo "quesito" donde se puede ganar un quesito.
 */
public class Casilla {
    /**
     * La categoría de la casilla. Puede ser una categoría de pregunta (ej. "Geografía", "Historia")
     * o null si la casilla no está asociada a una categoría específica.
     */
    private String categoria;
    /**
     * Indica si esta casilla es la casilla central del tablero.
     * La casilla central es el objetivo final para ganar el juego.
     */
    private boolean esCentral;
    /**
     * Indica si esta casilla es una casilla de "quesito".
     * En estas casillas, los jugadores pueden ganar un quesito de la categoría asociada
     * si responden correctamente una pregunta.
     */
    private boolean esQuesito;

    /**
     * Constructor para crear una nueva instancia de Casilla.
     *
     * @param categoria La categoría de la casilla. Puede ser null si no aplica.
     * @param esCentral Verdadero si la casilla es la central del tablero, falso en caso contrario.
     * @param esQuesito Verdadero si la casilla es una casilla de quesito, falso en caso contrario.
     */
    public Casilla(String categoria, boolean esCentral, boolean esQuesito) {
        this.categoria = categoria;
        this.esCentral = esCentral;
        this.esQuesito = esQuesito;
    }

    /**
     * Obtiene la categoría de la casilla.
     *
     * @return La categoría de la casilla como un String, o null si no tiene una.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría de la casilla.
     *
     * @param categoria La nueva categoría para la casilla.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Verifica si la casilla es la central del tablero.
     *
     * @return Verdadero si la casilla es central, falso en caso contrario.
     */
    public boolean isEsCentral() {
        return esCentral;
    }

    /**
     * Establece si la casilla es la central del tablero.
     *
     * @param esCentral Verdadero para marcarla como central, falso para lo contrario.
     */
    public void setEsCentral(boolean esCentral) {
        this.esCentral = esCentral;
    }

    /**
     * Verifica si la casilla es una casilla de quesito.
     *
     * @return Verdadero si la casilla es de quesito, falso en caso contrario.
     */
    public boolean isEsQuesito() {
        return esQuesito;
    }

    /**
     * Establece si la casilla es una casilla de quesito.
     *
     * @param esQuesito Verdadero para marcarla como casilla de quesito, falso para lo contrario.
     */
    public void setEsQuesito(boolean esQuesito) {
        this.esQuesito = esQuesito;
    }
}