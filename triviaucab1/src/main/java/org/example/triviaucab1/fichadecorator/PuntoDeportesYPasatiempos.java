package org.example.triviaucab1.fichadecorator;

import javafx.scene.paint.Color;

/**
 * Decorador Concreto para la categoría Deportes y Pasatiempos.
 * Colorea el segmento 2 de naranja, delegando la lógica de dibujo al FichaDecorator.
 */
public class PuntoDeportesYPasatiempos extends FichaDecorator {
    /**
     * Índice del segmento correspondiente a Deportes y Pasatiempos.
     */
    private static final int SEGMENTO_DEPORTES_PASATIEMPOS = 2;

    /**
     * Constructor para PuntoDeportesYPasatiempos.
     * @param fichaDecorada La ficha a decorar.
     */
    public PuntoDeportesYPasatiempos(Ficha fichaDecorada) {
        // Llama al constructor del decorador abstracto, pasando el color y el número de segmento.
        super(fichaDecorada, Color.ORANGE, SEGMENTO_DEPORTES_PASATIEMPOS);
    }
}
