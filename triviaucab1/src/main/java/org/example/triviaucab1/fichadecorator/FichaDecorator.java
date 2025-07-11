package org.example.triviaucab1.fichadecorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Decorador Abstracto: Mantiene una referencia a un objeto Ficha
 * y delega las peticiones a este. Es responsable de colorear un segmento específico.
 */
public abstract class FichaDecorator implements Ficha {
    /**
     * Referencia a la ficha decorada.
     */
    protected Ficha fichaDecorada;
    /**
     * Color para el segmento de este decorador.
     */
    protected Color color; // Color para el segmento de este decorador
    /**
     * Número de segmento (0-5) para este decorador.
     */
    protected int numeroSegmento; // Número de segmento (0-5) para este decorador

    /**
     * Constructor para FichaDecorator.
     *
     * @param fichaDecorada  La ficha a decorar.
     * @param color          El color con el que se pintará el segmento por este decorador.
     * @param numeroSegmento El índice del segmento a colorear (0 a 5) por este decorador.
     */
    public FichaDecorator(Ficha fichaDecorada, Color color, int numeroSegmento) {
        this.fichaDecorada = fichaDecorada;
        this.color = color;
        this.numeroSegmento = numeroSegmento;
    }

    /**
     * Dibuja la ficha decorada y colorea el segmento correspondiente a este decorador.
     *
     * @param gc     Contexto gráfico donde se dibuja la ficha.
     * @param x      Coordenada X del centro de la ficha.
     * @param y      Coordenada Y del centro de la ficha.
     * @param radius Radio de la ficha.
     */
    @Override
    public void dibujar(GraphicsContext gc, double x, double y, double radius) {
        fichaDecorada.dibujar(gc, x, y, radius);
        gc.setFill(this.color);
        double anguloInicio = numeroSegmento * 60.0;
        gc.fillArc(x - radius, y - radius, radius * 2, radius * 2, anguloInicio, 60, ArcType.ROUND);

        FichaBase.dibujarBordes(gc,x,y,radius);
    }
}