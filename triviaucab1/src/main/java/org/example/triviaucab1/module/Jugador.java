package org.example.triviaucab1.module;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

/**
 * Representa a un jugador en el juego, incluyendo sus datos personales,
 * estadísticas de juego, progreso en el tablero (casilla actual),
 * y los "quesitos" de categoría que ha ganado.
 * También gestiona la representación visual de su ficha en el tablero.
 */
public class Jugador {
    private String email;
    private String alias;
    private Set<String> quesitosGanados;
    private EstadisticasJugador estadisticas;
    private String casillaActualId;
    /**
     * Representación visual de la ficha del jugador en el tablero,
     * que se decora con los quesitos ganados.
     * Este campo es ignorado por Jackson para la serialización/deserialización.
     */
    @JsonIgnore
    private Ficha fichaVisual;

    /**
     * Tiempo límite para la respuesta de una pregunta.
     * Este campo es ignorado por Jackson para la serialización/deserialización.
     */
    @JsonIgnore
    private int tiempoLimiteRespuesta;

    /**
     * Constructor por defecto. Jackson lo usará al deserializar el JSON.
     * Es crucial que aquí se inicialicen TODOS los campos que no vienen del JSON
     * o que Jackson no puede inicializar por sí mismo (como los Sets y objetos complejos).
     */
    public Jugador() {
        System.out.println("DEBUG: Constructor por defecto de Jugador llamado.");
        this.quesitosGanados = new HashSet<>();
        this.estadisticas = new EstadisticasJugador();
        this.casillaActualId = "c";
        this.fichaVisual = new FichaBase(Color.WHITE);
        this.tiempoLimiteRespuesta = 0;
    }

    /**
     * Constructor para crear un Jugador con email y alias.
     * Llama al constructor por defecto para inicializar los otros campos.
     * @param email El correo electrónico del jugador, usado como identificador único.
     * @param alias El alias o nombre de usuario del jugador.
     */
    public Jugador(String email, String alias) {
        this();
        this.email = email;
        this.alias = alias;
    }

    /**
     * Obtiene el correo electrónico del jugador.
     * @return El correo electrónico del jugador.
     */
    public String getEmail() { return email; }

    /**
     * Establece el correo electrónico del jugador.
     * @param email El nuevo correo electrónico del jugador.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtiene el alias del jugador.
     * @return El alias del jugador.
     */
    public String getAlias() { return alias; }

    /**
     * Establece el alias del jugador.
     * @param alias El nuevo alias del jugador.
     */
    public void setAlias(String alias) { this.alias = alias; }

    /**
     * Obtiene las estadísticas de juego del jugador.
     * Si las estadísticas no están inicializadas, crea una nueva instancia.
     * @return Las estadísticas de juego del jugador.
     */
    public EstadisticasJugador getEstadisticas() {
        if (this.estadisticas == null) {
            this.estadisticas = new EstadisticasJugador();
        }
        return estadisticas;
    }

    /**
     * Establece las estadísticas de juego del jugador.
     * @param estadisticas Las nuevas estadísticas de juego.
     */
    public void setEstadisticas(EstadisticasJugador estadisticas) {
        this.estadisticas = estadisticas;
    }

    /**
     * Obtiene el ID de la casilla actual del jugador en el tablero.
     * @return El ID de la casilla actual.
     */
    public String getCasillaActualId() { return casillaActualId; }

    /**
     * Establece el ID de la casilla actual del jugador en el tablero.
     * @param casillaActualId El nuevo ID de la casilla actual.
     */
    public void setCasillaActualId(String casillaActualId) { this.casillaActualId = casillaActualId; }

    /**
     * Obtiene la ficha visual del jugador, que representa su progreso con los quesitos.
     * Este método es ignorado por Jackson.
     * @return La ficha visual del jugador.
     */
    @JsonIgnore
    public Ficha getFichaVisual() {
        if (this.fichaVisual == null) {
            reconstruirFichaVisual();
        }
        return fichaVisual;
    }

    /**
     * Obtiene una lista de los nombres de las categorías de los quesitos ganados por el jugador.
     * Este método es utilizado por Jackson para serializar el conjunto de quesitos.
     * @return Una lista de Strings con los nombres de las categorías de los quesitos ganados.
     */
    public List<String> getQuesitosGanadosNombres() {
        if (this.quesitosGanados != null) {
            return new ArrayList<>(this.quesitosGanados);
        }
        return Collections.emptyList();
    }

    /**
     * Establece los quesitos ganados por el jugador a partir de una lista de nombres de categorías.
     * Este método es utilizado por Jackson al deserializar el JSON y dispara la reconstrucción de la ficha visual.
     * @param quesitosGanadosNombres Una lista de Strings con los nombres de las categorías de los quesitos ganados.
     */
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

    /**
     * Añade un quesito de una categoría específica a la colección del jugador.
     * Si el quesito ya ha sido ganado, no se añade de nuevo.
     * Después de añadir un quesito, la ficha visual se reconstruye.
     * @param categoria La categoría del quesito a añadir.
     */
    public void addQuesito(String categoria) {
        if (this.quesitosGanados.add(categoria)) {
            System.out.println(getAlias() + " ha ganado el quesito de " + categoria + ". Total de quesitos únicos: " + quesitosGanados.size());
            reconstruirFichaVisual();
        } else {
            System.out.println(getAlias() + " ya tenía el quesito de " + categoria + ". No se añade de nuevo.");
        }
    }

    /**
     * Verifica si el jugador ha ganado todos los quesitos requeridos.
     * @param totalCategoriasRequeridas El número total de categorías de quesitos que se deben ganar para completar el juego.
     * @return true si el jugador tiene un número de quesitos ganados igual o mayor al total requerido, false en caso contrario.
     */
    public boolean tieneTodosLosQuesitos(int totalCategoriasRequeridas) {
        if (this.quesitosGanados == null) {
            return false;
        }
        return this.quesitosGanados.size() >= totalCategoriasRequeridas;
    }

    /**
     * Reconstruye la ficha visual del jugador aplicando los decoradores
     * correspondientes a cada quesito ganado.
     * Se inicializa con una FichaBase y luego se le añaden los puntos de quesito.
     */
    private void reconstruirFichaVisual() {
        this.fichaVisual = new FichaBase(Color.WHITE);

        if (quesitosGanados != null) {
            for (String categoria : quesitosGanados) {
                this.fichaVisual = aplicarDecoradorQuesito(this.fichaVisual, categoria);
            }
        }
    }

    /**
     * Aplica el decorador de quesito correspondiente a la ficha actual.
     * @param fichaActual La ficha sobre la cual se aplicará el decorador.
     * @param categoria La categoría del quesito que se va a aplicar como decorador.
     * @return La ficha decorada con el nuevo quesito.
     */
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

    /**
     * Retorna una representación en cadena del objeto Jugador, que es su alias.
     * Esto es útil para mostrar el jugador en componentes de UI como un ListView.
     * @return El alias del jugador.
     */
    @Override
    public String toString() {
        return alias;
    }

    /**
     * Compara este objeto Jugador con otro objeto para verificar si son iguales.
     * La igualdad se basa en el correo electrónico del jugador.
     * @param o El objeto a comparar.
     * @return true si los objetos son iguales (tienen el mismo correo electrónico), false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(email, jugador.email);
    }

    /**
     * Calcula el valor hash para este objeto Jugador.
     * El valor hash se basa en el correo electrónico del jugador.
     * @return El valor hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
