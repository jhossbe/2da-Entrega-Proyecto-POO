package org.example.triviaucab1.fichadecorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Componente Concreto: La ficha básica inicial, un círculo blanco con 6 segmentos.
 */
public class FichaBase implements Ficha {

    @Override
    public void dibujar(GraphicsContext gc, double x, double y, double radius) {
        // 1. Dibuja los 6 segmentos en blanco
        gc.setFill(Color.WHITE);
        for (int i = 0; i < 6; i++) {
            gc.fillArc(x - radius, y - radius, radius * 2, radius * 2, i * 60, 60, ArcType.ROUND);
        }

        // 2. Dibuja los bordes negros para separar los segmentos
        dibujarBordes(gc, x, y, radius);
    }

    /**
     * Método de utilidad para dibujar los bordes negros de la ficha.
     */
    public static void dibujarBordes(GraphicsContext gc, double x, double y, double radius) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);

        // Dibuja las 6 líneas divisorias desde el centro
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);
            double endX = x + radius * Math.cos(angle);
            double endY = y + radius * Math.sin(angle);
            gc.strokeLine(x, y, endX, endY);
        }

        // Dibuja el borde circular exterior
        gc.strokeArc(x - radius, y - radius, radius * 2, radius * 2, 0, 360, ArcType.OPEN);
    }
}
