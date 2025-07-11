package org.example.triviaucab1.fichadecorator;

import javafx.scene.paint.Color;

/**
 * Decorador Concreto para la categoría Historia.
 * Colorea el segmento 1 de amarillo, delegando la lógica de dibujo al FichaDecorator.
 */
public class PuntoHistoria extends FichaDecorator {
    /**
     * Índice del segmento correspondiente a Historia.
     */
    private static final int SEGMENTO_HISTORIA = 1;

    /**
     * Constructor para PuntoHistoria.
     * @param fichaDecorada La ficha a decorar.
     */
    public PuntoHistoria(Ficha fichaDecorada) {
        // Llama al constructor del decorador abstracto, pasando el color y el número de segmento.
        super(fichaDecorada, Color.YELLOW, SEGMENTO_HISTORIA);
    }
}
