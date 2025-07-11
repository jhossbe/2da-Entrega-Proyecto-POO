package org.example.triviaucab1.fichadecorator;

import javafx.scene.paint.Color;

/**
 * Decorador Concreto para la categoría Geografía.
 * Colorea el segmento 0 de azul, delegando la lógica de dibujo al FichaDecorator.
 */
public class PuntoGeografia extends FichaDecorator {
    /**
     * Índice del segmento correspondiente a Geografía.
     */
    private static final int SEGMENTO_GEOGRAFIA = 0;

    /**
     * Constructor para PuntoGeografia.
     * @param fichaDecorada La ficha a decorar.
     */
    public PuntoGeografia(Ficha fichaDecorada) {
        // Llama al constructor del decorador abstracto, pasando el color y el número de segmento.
        super(fichaDecorada, Color.BLUE, SEGMENTO_GEOGRAFIA);
    }
}