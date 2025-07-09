package org.example.triviaucab1.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.triviaucab1.fichadecorator.Ficha;
import org.example.triviaucab1.fichadecorator.FichaBase;
// Importa todos tus decoradores de Punto...
import org.example.triviaucab1.fichadecorator.PuntoArteYLiteratura;
import org.example.triviaucab1.fichadecorator.PuntoCienciasYNaturaleza;
import org.example.triviaucab1.fichadecorator.PuntoDeportesYPasatiempos;
import org.example.triviaucab1.fichadecorator.PuntoEntretenimiento;
import org.example.triviaucab1.fichadecorator.PuntoGeografia;
import org.example.triviaucab1.fichadecorator.PuntoHistoria;

import javafx.scene.paint.Color; // Importar Color para FichaBase

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class Jugador {
    private String email;
    private String alias;
    private Set<String> quesitosGanados; // Campo interno para los nombres de los quesitos
    private EstadisticasJugador estadisticas;
    private String casillaActualId;
    private Ficha fichaVisual; // Campo para la ficha visual del jugador


    public Jugador() {
        this.quesitosGanados = new HashSet<>();
        this.estadisticas = new EstadisticasJugador(); // Asumiendo que EstadisticasJugador tiene un constructor por defecto
        this.casillaActualId = "c";
        this.fichaVisual = new FichaBase(Color.WHITE); // Deja este color para la prueba
    }

    public Jugador(String email, String alias) {
        this();
        this.email = email;
        this.alias = alias;
    }

    // Constructor que podría ser usado al cargar jugadores de JSON
    // Asegúrate de que este constructor es el que Jackson está utilizando si cargas Jugadores directamente.
    // Si Jackson usa el constructor por defecto y luego setters, entonces el setQuesitosGanadosNombres es clave.
    public Jugador(
            @JsonProperty("email") String email,
            @JsonProperty("alias") String alias,
            @JsonProperty("quesitosGanadosNombres") List<String> quesitosIniciales,
            @JsonProperty("estadisticas") EstadisticasJugador estadisticas,
            @JsonProperty("casillaActualId") String casillaActualId) {
        this(); // Llama al constructor por defecto para inicializar campos
        this.email = email;
        this.alias = alias;
        this.estadisticas = estadisticas;
        this.casillaActualId = casillaActualId;
        // Llama al setter para asegurar que se reconstruya la ficha visual al cargar
        setQuesitosGanadosNombres(quesitosIniciales);
    }


    public String getEmail() { return email; }
    public String getAlias() { return alias; }
    public EstadisticasJugador getEstadisticas() { return estadisticas; }
    public String getCasillaActualId() { return casillaActualId; }
    public Ficha getFichaVisual() { return fichaVisual; } // Getter para la ficha visual

    public void setEstadisticas(EstadisticasJugador estadisticas) { this.estadisticas = estadisticas; }
    public void setCasillaActualId(String casillaActualId) { this.casillaActualId = casillaActualId; }
    public void setFichaVisual(Ficha fichaVisual) { this.fichaVisual = fichaVisual; } // Setter para la ficha visual

    // Getter para Jackson al guardar en JSON
    public List<String> getQuesitosGanadosNombres() {
        if (this.quesitosGanados != null) {
            return new ArrayList<>(this.quesitosGanados);
        }
        return Collections.emptyList();
    }

    // Setter para Jackson al cargar del JSON. Dispara la reconstrucción de la ficha.
    public void setQuesitosGanadosNombres(List<String> quesitosGanadosNombres) {
        //System.out.println("DEBUG (Jugador): setQuesitosGanadosNombres llamado con: " + quesitosGanadosNombres);
        if (this.quesitosGanados == null) {
            this.quesitosGanados = new HashSet<>();
        }
        this.quesitosGanados.clear();
        if (quesitosGanadosNombres != null) {
            this.quesitosGanados.addAll(quesitosGanadosNombres);
        }
        //System.out.println("DEBUG (Jugador): Quesitos internos después de setear: " + this.quesitosGanados);
        reconstruirFichaVisual();
    }


    // Método para añadir un quesito (usado cuando el jugador lo gana)
    public void addQuesito(String categoria) {
        if (this.quesitosGanados.add(categoria)) { // Si se añade un nuevo quesito (Set.add() retorna true)
            System.out.println(getAlias() + " ha ganado el quesito de " + categoria + ". Total de quesitos únicos: " + quesitosGanados.size());
            reconstruirFichaVisual(); // Reconstruye la ficha visual para mostrar el nuevo quesito
        } else {
            System.out.println(getAlias() + " ya tenía el quesito de " + categoria + ". No se añade de nuevo.");
        }
    }

    // Método para verificar si el jugador tiene todos los quesitos necesarios
    public boolean tieneTodosLosQuesitos(int totalCategoriasRequeridas) {
        if (this.quesitosGanados == null) {
            return false;
        }
        //System.out.println("DEBUG (Jugador): Quesitos ganados (" + quesitosGanados.size() + ") vs Requeridos (" + totalCategoriasRequeridas + ")");
        return quesitosGanados.size() >= totalCategoriasRequeridas;
    }

    // Este método es crucial para construir la representación visual de la ficha
    private void reconstruirFichaVisual() {
        //System.out.println("DEBUG (Jugador): Reconstruyendo ficha visual para " + getAlias());
        // Siempre empieza con una FichaBase limpia
        this.fichaVisual = new FichaBase(Color.WHITE); // Usa el color de prueba para la base

        // Aplica un decorador por cada quesito ganado
        if (quesitosGanados != null) {
            for (String categoria : quesitosGanados) {
                //System.out.println("DEBUG (Jugador): Aplicando decorador para categoría: " + categoria);
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

    @Override
    public String toString() {
        return alias;
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
