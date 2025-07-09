package org.example.triviaucab1.module.tablero;

public class CasillaNode {
    private final String id;
    private final double x;
    private final double y;
    private String tipoCasilla;
    private String categoriaAsociada;

    public CasillaNode(String id, double x, double y) {
        this(id, x, y, "normal", null);
    }

    public CasillaNode(String id, double x, double y, String tipoCasilla, String categoriaAsociada) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tipoCasilla = tipoCasilla;
        this.categoriaAsociada = categoriaAsociada;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getTipoCasilla() {
        return tipoCasilla;
    }

    public String getCategoriaAsociada() {
        return categoriaAsociada;
    }

    public void setTipoCasilla(String tipoCasilla) {
        this.tipoCasilla = tipoCasilla;
    }

    public void setCategoriaAsociada(String categoriaAsociada) {
        this.categoriaAsociada = categoriaAsociada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CasillaNode that = (CasillaNode) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CasillaNode{" +
                "id='" + id + '\'' +
                ", tipo='" + tipoCasilla + '\'' +
                ", categoria='" + categoriaAsociada + '\'' +
                '}';
    }
}