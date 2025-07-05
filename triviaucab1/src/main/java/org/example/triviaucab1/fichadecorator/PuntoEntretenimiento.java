package org.example.triviaucab1.fichadecorator;

import javafx.scene.paint.Color;

/**
 * Decorador Concreto para la categoría Entretenimiento.
 * Colorea el segmento 5 de rosado, delegando la lógica de dibujo al FichaDecorator.
 */
public class PuntoEntretenimiento extends FichaDecorator {
    // Asignamos el segmento 5 a Entretenimiento
    private static final int SEGMENTO_ENTRETENIMIENTO = 5;

    /**
     * Constructor para PuntoEntretenimiento.
     * @param fichaDecorada La ficha a decorar.
     */
    public PuntoEntretenimiento(Ficha fichaDecorada) {
        // Llama al constructor del decorador abstracto, pasando el color y el número de segmento.
        super(fichaDecorada, Color.PINK, SEGMENTO_ENTRETENIMIENTO);
    }
}

