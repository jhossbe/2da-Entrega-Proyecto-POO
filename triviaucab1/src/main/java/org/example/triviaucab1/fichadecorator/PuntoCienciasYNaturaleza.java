package org.example.triviaucab1.fichadecorator;


import javafx.scene.paint.Color;

public class PuntoCienciasYNaturaleza extends FichaDecorator {
    // Asignamos el segmento 3 a Ciencias y Naturaleza
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