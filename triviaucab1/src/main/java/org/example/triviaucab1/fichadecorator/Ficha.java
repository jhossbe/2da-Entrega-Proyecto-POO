package org.example.triviaucab1.fichadecorator;

import javafx.scene.canvas.GraphicsContext;

/**
 * Componente: La interfaz para los objetos que pueden ser decorados.
 */
public interface Ficha {
    /**
     * Dibuja la ficha en el lienzo proporcionado.
     * @param gc El contexto gráfico del lienzo.
     * @param x La coordenada X del centro de la ficha.
     * @param y La coordenada Y del centro de la ficha.
     * @param radius El radio de la ficha.
     */
    void dibujar(GraphicsContext gc, double x, double y, double radius);
}
