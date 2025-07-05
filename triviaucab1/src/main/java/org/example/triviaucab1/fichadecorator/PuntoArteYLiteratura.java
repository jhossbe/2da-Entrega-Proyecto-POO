package org.example.triviaucab1.fichadecorator;

import javafx.scene.paint.Color;

/**
 * Decorador Concreto para la categoría Arte y Literatura.
 * Colorea el segmento 4 de morado, delegando la lógica de dibujo al FichaDecorator.
 */
public class PuntoArteYLiteratura extends FichaDecorator {
    // Asignamos el segmento 4 a Arte y Literatura
    private static final int SEGMENTO_ARTE_LITERATURA = 4;

    /**
     * Constructor para PuntoArteYLiteratura.
     * @param fichaDecorada La ficha a decorar.
     */
    public PuntoArteYLiteratura(Ficha fichaDecorada) {
        // Llama al constructor del decorador abstracto, pasando el color y el número de segmento.
        super(fichaDecorada, Color.PURPLE, SEGMENTO_ARTE_LITERATURA);
    }
}

