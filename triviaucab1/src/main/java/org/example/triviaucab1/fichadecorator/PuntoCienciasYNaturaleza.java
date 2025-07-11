package org.example.triviaucab1.fichadecorator;

import javafx.scene.paint.Color;

/**
 * Decorador Concreto para la categoría Ciencias y Naturaleza.
 * Colorea el segmento 3 de verde, delegando la lógica de dibujo al FichaDecorator.
 */
public class PuntoCienciasYNaturaleza extends FichaDecorator {
    /**
     * Índice del segmento correspondiente a Ciencias y Naturaleza.
     */
    private static final int SEGMENTO_CIENCIAS_NATURALEZA = 3;

    /**
     * Constructor para PuntoCienciasYNaturaleza.
     * @param fichaDecorada La ficha a decorar.
     */
    public PuntoCienciasYNaturaleza(Ficha fichaDecorada) {
        // Llama al constructor del decorador abstracto, pasando el color y el número de segmento.
        super(fichaDecorada, Color.GREEN, SEGMENTO_CIENCIAS_NATURALEZA);
    }
}