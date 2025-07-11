package org.example.triviaucab1.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty; // Mantener por si se usa en otros constructores o setters que no vemos

import org.example.triviaucab1.fichadecorator.Ficha;
import org.example.triviaucab1.fichadecorator.FichaBase;
import org.example.triviaucab1.fichadecorator.PuntoArteYLiteratura;
import org.example.triviaucab1.fichadecorator.PuntoCienciasYNaturaleza;
import org.example.triviaucab1.fichadecorator.PuntoDeportesYPasatiempos;
import org.example.triviaucab1.fichadecorator.PuntoEntretenimiento;
import org.example.triviaucab1.fichadecorator.PuntoGeografia;
import org.example.triviaucab1.fichadecorator.PuntoHistoria;

import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class Jugador {
    private String email;
    private String alias;
    private Set<String> quesitosGanados;
    private EstadisticasJugador estadisticas;
    private String casillaActualId;
    @JsonIgnore // Asegura que Jackson no intente serializar/deserializar este campo directamente
    private Ficha fichaVisual;

    @JsonIgnore
    private int tiempoLimiteRespuesta; // <-- ¡NUEVO! Añadido para que Jackson no falle si lo encuentra en el JSON

    /**
     * Constructor por defecto. Jackson lo usará al deserializar el JSON.
     * Es crucial que aquí se inicialicen TODOS los campos que no vienen del JSON
     * o que Jackson no puede inicializar por sí mismo (como los Sets y objetos complejos).
     */
    public Jugador() {
        System.out.println("DEBUG: Constructor por defecto de Jugador llamado."); // Mensaje de depuración
        this.quesitosGanados = new HashSet<>();
        this.estadisticas = new EstadisticasJugador(); // Asegura que EstadisticasJugador siempre esté inicializado
        this.casillaActualId = "c";
        this.fichaVisual = new FichaBase(Color.WHITE); // Inicializa la ficha visual
        this.tiempoLimiteRespuesta = 0; // Inicializa el campo ignorado
    }

    /**
     * Constructor para crear un Jugador con email y alias.
     * Llama al constructor por defecto para inicializar los otros campos.
     */
    public Jugador(String email, String alias) {
        this(); // Llama al constructor por defecto para inicializar quesitosGanados, estadisticas, etc.
        this.email = email;
        this.alias = alias;
    }


    // --- Getters y Setters ---
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public EstadisticasJugador getEstadisticas() {
        if (this.estadisticas == null) {
            this.estadisticas = new EstadisticasJugador();
        }
        return estadisticas;
    }

    public void setEstadisticas(EstadisticasJugador estadisticas) {
        this.estadisticas = estadisticas;
    }

    public String getCasillaActualId() { return casillaActualId; }
    public void setCasillaActualId(String casillaActualId) { this.casillaActualId = casillaActualId; }

    @JsonIgnore
    public Ficha getFichaVisual() {
        if (this.fichaVisual == null) {
            reconstruirFichaVisual();
        }
        return fichaVisual;
    }

    public void setFichaVisual(Ficha fichaVisual) { this.fichaVisual = fichaVisual; }

    // Getter para Jackson al guardar en JSON o para lógica de juego
    public List<String> getQuesitosGanadosNombres() {
        if (this.quesitosGanados != null) {
            return new ArrayList<>(this.quesitosGanados);
        }
        return Collections.emptyList();
    }

    // Setter para Jackson al cargar del JSON. Dispara la reconstrucción de la ficha.
    public void setQuesitosGanadosNombres(List<String> quesitosGanadosNombres) {
        if (this.quesitosGanados == null) {
            this.quesitosGanados = new HashSet<>();
        }
        this.quesitosGanados.clear();
        if (quesitosGanadosNombres != null) {
            this.quesitosGanados.addAll(quesitosGanadosNombres);
        }
        reconstruirFichaVisual();
    }


    // --- Métodos de Lógica de Juego ---
    public void addQuesito(String categoria) {
        if (this.quesitosGanados.add(categoria)) {
            System.out.println(getAlias() + " ha ganado el quesito de " + categoria + ". Total de quesitos únicos: " + quesitosGanados.size());
            reconstruirFichaVisual();
        } else {
            System.out.println(getAlias() + " ya tenía el quesito de " + categoria + ". No se añade de nuevo.");
        }
    }

    public boolean tieneTodosLosQuesitos(int totalCategoriasRequeridas) {
        if (this.quesitosGanados == null) {
            return false;
        }
        return this.quesitosGanados.size() >= totalCategoriasRequeridas;
    }

    private void reconstruirFichaVisual() {
        this.fichaVisual = new FichaBase(Color.WHITE);

        if (quesitosGanados != null) {
            for (String categoria : quesitosGanados) {
                this.fichaVisual = aplicarDecoradorQuesito(this.fichaVisual, categoria);
            }
        }
    }

    private Ficha aplicarDecoradorQuesito(Ficha fichaActual, String categoria) {
        switch (categoria) {
            case "Geografía": return new PuntoGeografia(fichaActual);
            case "Historia": return new PuntoHistoria(fichaActual);
            case "Deportes": return new PuntoDeportesYPasatiempos(fichaActual);
            case "Ciencia": return new PuntoCienciasYNaturaleza(fichaActual);
            case "Arte y Literatura": return new PuntoArteYLiteratura(fichaActual);
            case "Entretenimiento": return new PuntoEntretenimiento(fichaActual);
            default:
                System.err.println("Advertencia: Categoría de quesito desconocida para el decorador: '" + categoria + "'");
                return fichaActual;
        }
    }

    // --- Métodos de Utilidad ---
    @Override
    public String toString() {
        return alias; // Esto es lo que se mostrará en el ListView
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(email, jugador.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
