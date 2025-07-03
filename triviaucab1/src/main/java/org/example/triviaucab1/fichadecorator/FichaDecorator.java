package org.example.triviaucab1.fichadecorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Decorador Abstracto: Mantiene una referencia a un objeto Ficha
 * y delega las peticiones a este. Ahora también maneja la lógica
 * de colorear un segmento específico.
 */
public abstract class FichaDecorator implements Ficha {
    protected Ficha fichaDecorada;
    protected Color color; // Color para el segmento de este decorador
    protected int numeroSegmento; // Número de segmento (0-5) para este decorador

    /**
     * Constructor para FichaDecorator.
     * @param fichaDecorada La ficha a decorar.
     * @param color El color con el que se pintará el segmento por este decorador.
     * @param numeroSegmento El índice del segmento a colorear (0 a 5) por este decorador.
     */
    public FichaDecorator(Ficha fichaDecorada, Color color, int numeroSegmento) {
        this.fichaDecorada = fichaDecorada;
        this.color = color;
        this.numeroSegmento = numeroSegmento;
    }

    @Override
    public void dibujar(GraphicsContext gc, double x, double y, double radius) {
        // 1. Dibuja la ficha subyacente (lo que ya estaba pintado)
        fichaDecorada.dibujar(gc, x, y, radius);

        // 2. Añade la nueva decoración: pinta el segmento correspondiente con el color de este decorador
        gc.setFill(this.color);
        double anguloInicio = numeroSegmento * 60.0;
        gc.fillArc(x - radius, y - radius, radius * 2, radius * 2, anguloInicio, 60, ArcType.ROUND);

        // 3. Vuelve a dibujar los bordes para que queden por encima del color
        FichaBase.dibujarBordes(gc, x, y, radius);
    }




}
