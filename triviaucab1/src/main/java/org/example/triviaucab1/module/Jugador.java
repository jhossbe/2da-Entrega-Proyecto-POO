package org.example.triviaucab1.module;

import javafx.scene.paint.Color;
import org.example.triviaucab1.fichadecorator.Ficha;
import org.example.triviaucab1.fichadecorator.FichaBase;
import org.example.triviaucab1.fichadecorator.SegmentoColorDecorator;
import org.example.triviaucab1.module.tablero.CasillaNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jugador {
    private String email;
    private String alias;
    private List<String> quesitos; // Lista de categorías de quesitos ganados
    private EstadisticasJugador estadisticas;

    // Nuevos campos para el patrón Decorator y visualización
    private Color colorFicha;
    private CasillaNode posicionActual;
    private Ficha ficha;
    private boolean turnoActivo;

    // Mapeo de categorías a números de segmento y colores
    private static final Map<String, Integer> CATEGORIA_A_SEGMENTO = new HashMap<>();
    private static final Map<String, Color> CATEGORIA_A_COLOR = new HashMap<>();

    static {
        // Configuración de categorías según el tablero de Trivial Pursuit
        CATEGORIA_A_SEGMENTO.put("Geografia", 0);
        CATEGORIA_A_SEGMENTO.put("Historia", 1);
        CATEGORIA_A_SEGMENTO.put("Deportes", 2);
        CATEGORIA_A_SEGMENTO.put("Ciencias", 3);
        CATEGORIA_A_SEGMENTO.put("Arte", 4);
        CATEGORIA_A_SEGMENTO.put("Entretenimiento", 5);

        CATEGORIA_A_COLOR.put("Geografia", Color.BLUE);
        CATEGORIA_A_COLOR.put("Historia", Color.YELLOW);
        CATEGORIA_A_COLOR.put("Deportes", Color.ORANGE);
        CATEGORIA_A_COLOR.put("Ciencias", Color.GREEN);
        CATEGORIA_A_COLOR.put("Arte", Color.PURPLE);
        CATEGORIA_A_COLOR.put("Entretenimiento", Color.PINK);
    }

    // Constructor por defecto (NECESARIO para Jackson)
    public Jugador() {
        this.quesitos = new ArrayList<>();
        this.estadisticas = new EstadisticasJugador();
        this.ficha = new FichaBase(); // Comienza con una ficha base
        this.turnoActivo = false;
        this.colorFicha = Color.DODGERBLUE; // Color por defecto
    }

    public Jugador(String email, String alias) {
        this();
        this.email = email;
        this.alias = alias;
    }

    // Constructor adicional si solo usas alias
    public Jugador(String alias) {
        this();
        this.alias = alias;
    }

    // Constructor completo para el juego visual
    public Jugador(String alias, Color colorFicha, CasillaNode posicionInicial) {
        this();
        this.alias = alias;
        this.colorFicha = colorFicha;
        this.posicionActual = posicionInicial;
    }

    /**
     * Añade un quesito y actualiza la ficha con el decorador correspondiente.
     */
    public void addQuesito(String categoriaQuesito) {
        if (!this.quesitos.contains(categoriaQuesito)) {
            this.quesitos.add(categoriaQuesito);

            // Aplicar el decorador para colorear el segmento correspondiente
            if (CATEGORIA_A_SEGMENTO.containsKey(categoriaQuesito)) {
                int numeroSegmento = CATEGORIA_A_SEGMENTO.get(categoriaQuesito);
                Color colorSegmento = CATEGORIA_A_COLOR.get(categoriaQuesito);

                ficha = new SegmentoColorDecorator(ficha, colorSegmento, numeroSegmento);

                System.out.println("🎉 " + alias + " ha ganado el quesito de: " + categoriaQuesito);
            }
        }
    }

    /**
     * Verifica si el jugador ha ganado todos los quesitos.
     */
    public boolean haGanadoTodosLosQuesitos() {
        return quesitos.size() >= 6;
    }

    /**
     * Verifica si ya tiene un quesito específico.
     */
    public boolean tieneQuesito(String categoriaQuesito) {
        return this.quesitos.contains(categoriaQuesito);
    }

    /**
     * Mueve el jugador a una nueva posición.
     */
    public void moverA(CasillaNode nuevaPosicion) {
        this.posicionActual = nuevaPosicion;
        System.out.println("📍 " + alias + " se ha movido a la casilla: " + nuevaPosicion.getId());
    }

    /**
     * Obtiene el número de quesitos ganados.
     */
    public int getNumeroQuesitosGanados() {
        return quesitos.size();
    }

    // Getters y Setters existentes
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<String> getQuesitos() {
        return quesitos;
    }

    public void setQuesitos(List<String> quesitos) {
        this.quesitos = quesitos;
        // Reconstruir la ficha basada en los quesitos
        reconstruirFicha();
    }

    public EstadisticasJugador getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(EstadisticasJugador estadisticas) {
        this.estadisticas = estadisticas;
    }

    // Nuevos getters y setters para el patrón Decorator
    public Color getColorFicha() {
        return colorFicha;
    }

    public void setColorFicha(Color colorFicha) {
        this.colorFicha = colorFicha;
    }

    public CasillaNode getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(CasillaNode posicionActual) {
        this.posicionActual = posicionActual;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public boolean isTurnoActivo() {
        return turnoActivo;
    }

    public void setTurnoActivo(boolean turnoActivo) {
        this.turnoActivo = turnoActivo;
    }

    /**
     * Reconstruye la ficha aplicando todos los decoradores basados en los quesitos ganados.
     */
    private void reconstruirFicha() {
        this.ficha = new FichaBase();
        for (String quesito : quesitos) {
            if (CATEGORIA_A_SEGMENTO.containsKey(quesito)) {
                int numeroSegmento = CATEGORIA_A_SEGMENTO.get(quesito);
                Color colorSegmento = CATEGORIA_A_COLOR.get(quesito);
                ficha = new SegmentoColorDecorator(ficha, colorSegmento, numeroSegmento);
            }
        }
    }

    /**
     * Obtiene el mapa de categorías a colores.
     */
    public static Map<String, Color> getCategoriaAColor() {
        return new HashMap<>(CATEGORIA_A_COLOR);
    }

    /**
     * Obtiene el mapa de categorías a números de segmento.
     */
    public static Map<String, Integer> getCategoriaASegmento() {
        return new HashMap<>(CATEGORIA_A_SEGMENTO);
    }

    @Override
    public String toString() {
        return alias;
    }
}