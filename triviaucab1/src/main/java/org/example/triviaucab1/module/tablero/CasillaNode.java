package org.example.triviaucab1.module.tablero;

/**
 * Representa una casilla individual en el tablero del juego como un nodo en un grafo.
 * Cada casilla tiene un identificador único, coordenadas (x, y) en el espacio de la interfaz
 * gráfica, un tipo (por ejemplo, "normal", "pregunta", "central") y, si aplica,
 * una categoría asociada para preguntas.
 *
 * Esta clase es inmutable en cuanto a sus propiedades fundamentales (id, x, y),
 * pero permite la configuración de su tipo y categoría para mayor flexibilidad.
 */
public class CasillaNode {
    private final String id;
    private final double x;
    private final double y;
    private String tipoCasilla;
    private String categoriaAsociada;

    /**
     * Constructor para crear una nueva instancia de CasillaNode.
     *
     * @param id El identificador único de la casilla (ej. "1", "c", "30_path").
     * @param x La coordenada X central de la casilla en la interfaz gráfica.
     * @param y La coordenada Y central de la casilla en la interfaz gráfica.
     * @param tipoCasilla El tipo de casilla (ej. "normal", "pregunta", "central").
     * @param categoriaAsociada La categoría de pregunta asociada a esta casilla. Puede ser null
     * si la casilla no es de tipo "pregunta" o si la pregunta es aleatoria.
     */
    public CasillaNode(String id, double x, double y, String tipoCasilla, String categoriaAsociada) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tipoCasilla = tipoCasilla;
        this.categoriaAsociada = categoriaAsociada;
    }

    /**
     * Obtiene el identificador único de la casilla.
     *
     * @return El ID de la casilla como un String.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene la coordenada X central de la casilla.
     * Esta coordenada se utiliza para posicionar visualmente elementos en la interfaz.
     *
     * @return La coordenada X como un double.
     */
    public double getX() {
        return x;
    }

    /**
     * Obtiene la coordenada Y central de la casilla.
     * Esta coordenada se utiliza para posicionar visualmente elementos en la interfaz.
     *
     * @return La coordenada Y como un double.
     */
    public double getY() {
        return y;
    }

    /**
     * Obtiene el tipo de casilla.
     * Define el comportamiento o propósito de la casilla en el juego.
     *
     * @return El tipo de casilla como un String (ej. "normal", "pregunta", "central").
     */
    public String getTipoCasilla() {
        return tipoCasilla;
    }

    /**
     * Obtiene la categoría de pregunta asociada a esta casilla.
     * Si la casilla no es de tipo "pregunta" o si la pregunta no tiene una categoría específica,
     * este valor puede ser null.
     *
     * @return La categoría de pregunta como un String, o null si no aplica.
     */
    public String getCategoriaAsociada() {
        return categoriaAsociada;
    }

    /**
     * Compara este CasillaNode con otro objeto para determinar si son iguales.
     * Dos CasillaNode se consideran iguales si tienen el mismo {@code id}.
     *
     * @param o El objeto con el que comparar.
     * @return true si los objetos son iguales (tienen el mismo ID), false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CasillaNode that = (CasillaNode) o;
        return id.equals(that.id);
    }

    /**
     * Genera un valor de código hash para este CasillaNode.
     * El código hash se basa únicamente en el {@code id} de la casilla,
     * asegurando que sea consistente con la implementación de {@code equals}.
     *
     * @return El valor del código hash para esta casilla.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Devuelve una representación en cadena de este CasillaNode.
     * La cadena incluye el ID de la casilla, su tipo y la categoría asociada.
     *
     * @return Una representación en String de la casilla.
     */
    @Override
    public String toString() {
        return "CasillaNode{" +
                "id='" + id + '\'' +
                ", tipo='" + tipoCasilla + '\'' +
                ", categoria='" + categoriaAsociada + '\'' +
                '}';
    }
}