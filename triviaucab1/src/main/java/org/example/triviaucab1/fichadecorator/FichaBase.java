package org.example.triviaucab1.fichadecorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Componente Concreto: La ficha básica inicial.
 * Dibuja el círculo central de la ficha y los bordes de los segmentos.
 * No rellena los segmentos individuales; eso es responsabilidad de los decoradores.
 */
public class FichaBase implements Ficha {

    private Color baseColor;

    public FichaBase() {
        this.baseColor = Color.WHITE;
    }

    public FichaBase(Color color) {
        this.baseColor = color;
    }

    @Override
    public void dibujar(GraphicsContext gc, double x, double y, double radius) {
        // Dibuja el círculo base blanco
        gc.setFill(baseColor);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        // Dibuja los bordes de los segmentos (la "torta")
        dibujarBordes(gc, x, y, radius);
    }

    public static void dibujarBordes(GraphicsContext gc, double x, double y, double radius) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);
            double endX = x + radius * Math.cos(angle);
            double endY = y + radius * Math.sin(angle);
            gc.strokeLine(x, y, endX, endY);
        }

        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
