package org.example.triviaucab1.fichadecorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Decorador Concreto: Añade un segmento de color específico a la ficha.
 * Este decorador permite colorear un segmento específico de la ficha
 * representando que el jugador ha ganado una categoría.
 */
public class SegmentoColorDecorator extends FichaDecorator {

    /**
     * Constructor para SegmentoColorDecorator.
     * @param fichaDecorada La ficha a decorar.
     * @param color El color del segmento a añadir.
     * @param numeroSegmento El número del segmento (0-5) a colorear.
     */
    public SegmentoColorDecorator(Ficha fichaDecorada, Color color, int numeroSegmento) {
        super(fichaDecorada, color, numeroSegmento);
    }

    @Override
    public void dibujar(GraphicsContext gc, double x, double y, double radius) {
        // Delega al decorador padre que maneja la lógica común
        super.dibujar(gc, x, y, radius);
    }

    /**
     * Obtiene el color del segmento.
     * @return El color del segmento.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Obtiene el número del segmento.
     * @return El número del segmento (0-5).
     */
    public int getNumeroSegmento() {
        return numeroSegmento;
    }
}
