package org.example.triviaucab1.controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle; // Ahora solo Rectangle para las casillas
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;
import org.example.triviaucab1.module.JsonService; // Importar JsonService
import org.example.triviaucab1.module.GestorEstadisticas;
import org.example.triviaucab1.fichadecorator.Ficha;
import org.example.triviaucab1.module.GestorPreguntas;
import org.example.triviaucab1.module.Jugador;
import org.example.triviaucab1.module.Partida;
import org.example.triviaucab1.module.Pregunta;
import org.example.triviaucab1.module.tablero.CasillaNode;
import org.example.triviaucab1.module.tablero.GrafoTablero;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador principal del juego. Gestiona la lógica del tablero, los turnos de los jugadores,
 * las interacciones con las casillas, las preguntas y la integración con las estadísticas y guardado de partida.
 */
public class JuegoController {
    @FXML private AnchorPane rootPane;
    @FXML private Label jugadorEnTurnoLabel;
    @FXML private VBox categoriasJugadorVBox; // Contenedor para mostrar categorías de quesitos (no implementado en este fragmento)
    @FXML private Label tiempoRespuestaLabel; // Etiqueta para mostrar el tiempo de respuesta (no implementado en este fragmento)
    @FXML private Label tiempoTotalRespuestaCorrectaLabel; // NUEVA ETIQUETA
    @FXML private Label casillaActualLabel; // NUEVA ETIQUETA

    @FXML private Canvas fichaEnTableroCanvas; // Canvas para dibujar la ficha decorada del jugador en el tablero
    @FXML private Canvas fichaJugadorCanvas; // Canvas para dibujar la ficha decorada del jugador actual en el panel lateral

    private JsonService jsonService; // Servicio para guardar y cargar el estado de la partida
    private GestorEstadisticas gestorEstadisticas; // Gestor para actualizar y guardar estadísticas de jugadores
    private GrafoTablero grafoTablero; // Representación del tablero como un grafo
    private int ultimoValorDado = 0; // Último valor obtenido al lanzar el dado
    private boolean puedeMover = false; // Indica si el jugador actual puede mover su ficha
    private List<CasillaNode> movimientosPosibles = new ArrayList<>(); // Lista de casillas a las que el jugador puede moverse
    private Map<String, CasillaNode> mapaIDaNodo = new HashMap<>(); // Mapea IDs de casillas a sus nodos en el grafo
    private Map<CasillaNode, Rectangle> mapaNodoARect = new HashMap<>(); // Mapea nodos del grafo a sus rectángulos visuales
    private Partida partida; // Objeto que contiene el estado actual de la partida
    private DadoController dadoController; // Controlador del dado para lanzar y deshabilitar

    private GestorPreguntas gestorPreguntas; // Gestor para obtener preguntas
    private Map<String, String> casillaIdToCategory = new HashMap<>(); // Mapea IDs de casillas a categorías de preguntas
    private int totalCategoriasParaGanar; // Número total de quesitos necesarios para ganar la partida

    /**
     * Método de inicialización del controlador. Se llama automáticamente al cargar el FXML.
     * Configura el tablero, carga las preguntas, inicializa gestores y elementos de la UI.
     */
    @FXML
    public void initialize() {
        gestorPreguntas = new GestorPreguntas("preguntasJuegoTrivia_final.json");
        totalCategoriasParaGanar = gestorPreguntas.getTotalCategorias();
        System.out.println("Total de categorías de quesitos posibles para ganar: " + totalCategoriasParaGanar);
        gestorEstadisticas = new GestorEstadisticas();
        jsonService = new JsonService(); // Inicializa JsonService aquí
        grafoTablero = new GrafoTablero();

        // Estas son las definiciones de tus casillas y categorías
        addCategories(casillaIdToCategory, "Geografía", "1", "14", "17", "26", "44", "47", "53", "58", "62");
        addCategories(casillaIdToCategory, "Historia", "2", "19", "22", "31", "41", "45", "54", "57", "63");
        addCategories(casillaIdToCategory, "Deportes", "3", "24", "27", "36", "40", "46", "51", "55", "64");
        addCategories(casillaIdToCategory, "Ciencia", "4","11", "29", "32", "39", "50", "56", "61", "65");
        addCategories(casillaIdToCategory, "Arte y Literatura", "5","7", "16", "34", "38", "42", "49", "60", "66");
        addCategories(casillaIdToCategory, "Entretenimiento", "6","9", "12", "21", "37", "43", "48", "52", "59");
        Set<String> casillasBlancas = new HashSet<>(Arrays.asList("8","10", "13","15", "18", "20","23", "25", "28", "30_path","33", "35"));

        casillaIdToCategory.put("c", "Central");

        // Construcción del grafo del tablero a partir de los rectángulos FXML
        for (Node node : rootPane.getChildren()) {
            if (node instanceof Rectangle rect && rect.getId() != null) {
                String id = rect.getId();
                if (!id.equals("leyendaBackground")) {
                    double x = rect.getLayoutX() + rect.getWidth() / 2;
                    double y = rect.getLayoutY() + rect.getHeight() / 2;

                    String type;
                    String category = casillaIdToCategory.get(id);

                    if (id.equals("c")) {
                        type = "central";
                    } else if (casillasBlancas.contains(id)) {
                        type = "normal";
                        category = null;
                    } else {
                        type = "pregunta";
                        if (category == null) {
                            System.err.println("Advertencia: Casilla " + id + " marcada como pregunta pero sin categoría asignada.");
                        }
                    }

                    CasillaNode nodo = new CasillaNode(id, x, y, type, category);
                    grafoTablero.agregarNodo(nodo);
                    mapaIDaNodo.put(id, nodo);
                    mapaNodoARect.put(nodo, rect);
                }
            }
        }
        conectarCasillasCorrectamente(); // Establece las conexiones entre las casillas

        // Carga e inicialización del controlador del dado
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/DadoView.fxml"));
            AnchorPane dadoPane = loader.load();
            dadoController = loader.getController();
            dadoController.setJuegoController(this); // Pasa esta instancia al controlador del dado
            AnchorPane.setBottomAnchor(dadoPane, 30.0);
            AnchorPane.setRightAnchor(dadoPane, 30.0);
            rootPane.getChildren().add(dadoPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Nodos en el grafo: " + grafoTablero.getIdsNodos().size());
        verificarConexiones(); // Verifica que las conexiones del tablero sean correctas
    }

    /**
     * Método auxiliar para añadir múltiples IDs de casillas a una categoría en el mapa.
     * @param map El mapa de IDs de casillas a categorías.
     * @param category La categoría a la que pertenecen los IDs.
     * @param ids Un array de IDs de casillas.
     */
    private void addCategories(Map<String, String> map, String category, String... ids) {
        for (String id : ids) {
            map.put(id, category);
        }
    }

    /**
     * Establece la instancia de GestorEstadisticas para que el controlador pueda actualizar las estadísticas.
     * @param gestorEstadisticas La instancia de GestorEstadisticas.
     */
    public void setGestorEstadisticas(GestorEstadisticas gestorEstadisticas) {
        this.gestorEstadisticas = gestorEstadisticas;
    }

    /**
     * Establece la partida actual y configura el jugador inicial.
     * Este método DEBE SER LLAMADO por la clase que inicia la partida (ej. MenuPrincipalController o PartidaGuardadaController).
     * @param partida La instancia de la partida.
     */
    public void setPartida(Partida partida) {
        this.partida = partida;
        System.out.println("DEBUG (JuegoController): Partida recibida con " + partida.getJugadores().size() + " jugadores.");
        actualizarUIJugadorActual(); // Actualiza la UI para el jugador en turno
        if (dadoController != null) {
            dadoController.habilitarBotonLanzar(); // Habilita el botón del dado al inicio del turno
        }
    }

    /**
     * Actualiza toda la interfaz de usuario para reflejar al jugador que tiene el turno.
     * Esto incluye el Label del nombre, la ficha visual de quesitos, y la posición de la ficha en el tablero.
     */
    public void actualizarUIJugadorActual() {
        Jugador jugadorEnTurno = partida.getJugadorActual();

        if (jugadorEnTurno == null) {
            System.err.println("DEBUG (JuegoController): No hay jugador en turno para actualizar la UI. Posible fin de partida.");
            fichaEnTableroCanvas.setVisible(false); // Usa el Canvas
            jugadorEnTurnoLabel.setText("Partida Finalizada");
            tiempoTotalRespuestaCorrectaLabel.setText("Tiempo Correcto: -- s"); // Limpiar
            casillaActualLabel.setText("Casilla Actual: --"); // Limpiar
            tiempoRespuestaLabel.setText("Tiempo Restante: --"); // Limpiar
            if (dadoController != null) {
                dadoController.deshabilitarBotonLanzar();
            }
            return;
        }

        if (jugadorEnTurnoLabel != null) {
            jugadorEnTurnoLabel.setText("Jugador Actual: " + jugadorEnTurno.getAlias());
        }

        // --- DEBUGGING START ---
        System.out.println("DEBUG (JuegoController): Actualizando UI para jugador: " + jugadorEnTurno.getAlias());

        // ACTUALIZAR ETIQUETA DE TIEMPO CORRECTO
        if (tiempoTotalRespuestaCorrectaLabel != null) {
            long totalTiempo = 0;
            if (jugadorEnTurno.getEstadisticas() != null) {
                totalTiempo = jugadorEnTurno.getEstadisticas().getTiempoTotalRespuestasCorrectas() / 1000; // Convertir a segundos
                System.out.println("DEBUG (JuegoController): Valor de getTiempoTotalRespuestasCorrectas() (ms): " + jugadorEnTurno.getEstadisticas().getTiempoTotalRespuestasCorrectas());
            } else {
                System.out.println("DEBUG (JuegoController): Estadisticas del jugador es NULO.");
            }
            tiempoTotalRespuestaCorrectaLabel.setText("Tiempo Correcto: " + totalTiempo + " s");
            System.out.println("DEBUG (JuegoController): Etiqueta 'Tiempo Correcto' actualizada a: " + tiempoTotalRespuestaCorrectaLabel.getText());
        } else {
            System.out.println("DEBUG (JuegoController): La etiqueta tiempoTotalRespuestaCorrectaLabel es NULA. Verifica el FXML.");
        }

        // ACTUALIZAR ETIQUETA DE CASILLA ACTUAL
        if (casillaActualLabel != null) {
            String casillaId = jugadorEnTurno.getCasillaActualId();
            if (casillaId == null || casillaId.isEmpty()) {
                casillaId = "N/A"; // Si la casilla no está definida, muestra N/A
                System.out.println("DEBUG (JuegoController): Casilla Actual del jugador es NULA o VACÍA, mostrando 'N/A'.");
            }
            casillaActualLabel.setText("Casilla Actual: " + casillaId);
            System.out.println("DEBUG (JuegoController): Etiqueta 'Casilla Actual' actualizada a: " + casillaActualLabel.getText());
        } else {
            System.out.println("DEBUG (JuegoController): La etiqueta casillaActualLabel es NULA. Verifica el FXML.");
        }
        // --- DEBUGGING END ---

        // Esta etiqueta se mantiene como un marcador de posición general o para otro uso.
        tiempoRespuestaLabel.setText("Tiempo Restante: --");


        String idCasillaJugador = jugadorEnTurno.getCasillaActualId();
        if (idCasillaJugador == null || idCasillaJugador.isEmpty()) {
            // Si la casilla no está definida, se asume la casilla central 'c' como inicio
            idCasillaJugador = "c";
            jugadorEnTurno.setCasillaActualId("c");
            System.out.println("DEBUG (JuegoController): Casilla del jugador era nula o vacía, se estableció a 'c'.");
        }

        CasillaNode casillaDelJugadorEnGrafo = mapaIDaNodo.get(idCasillaJugador);

        if (casillaDelJugadorEnGrafo == null) {
            System.err.println("ERROR (JuegoController): La casilla '" + idCasillaJugador + "' del jugador no se encontró en el tablero. Reiniciando a 'c'.");
            // Intenta reubicar al jugador en la casilla central si la actual no se encuentra
            idCasillaJugador = "c";
            jugadorEnTurno.setCasillaActualId("c");
            casillaDelJugadorEnGrafo = mapaIDaNodo.get("c");

            if (casillaDelJugadorEnGrafo == null) {
                System.err.println("Error grave (JuegoController): Casilla central 'c' no encontrada en el tablero. No se puede continuar.");
                fichaEnTableroCanvas.setVisible(false);
                return;
            }
        }
        // Posiciona el Canvas de la ficha en el centro de la casilla
        fichaEnTableroCanvas.setLayoutX(casillaDelJugadorEnGrafo.getX() - fichaEnTableroCanvas.getWidth()/2);
        fichaEnTableroCanvas.setLayoutY(casillaDelJugadorEnGrafo.getY() - fichaEnTableroCanvas.getHeight()/2);
        fichaEnTableroCanvas.setVisible(true); // Usa el Canvas

        dibujarFichaEnTablero(jugadorEnTurno); // Dibuja la ficha con sus quesitos en el Canvas del tablero
        dibujarFichaDelJugador(jugadorEnTurno); // Dibuja la ficha en el canvas lateral

        System.out.println("DEBUG (JuegoController): UI actualizada para: " + jugadorEnTurno.getAlias() + " en casilla: " + idCasillaJugador);
        if (dadoController != null) {
            dadoController.habilitarBotonLanzar(); // Habilita el dado para el nuevo turno
        }
    }

    /**
     * Dibuja la ficha del jugador dado en el Canvas dedicado (panel lateral).
     * @param jugador El objeto Jugador cuya ficha visual se va a dibujar.
     */
    public void dibujarFichaDelJugador(Jugador jugador) {
        if (fichaJugadorCanvas != null && jugador != null && jugador.getFichaVisual() != null) {
            GraphicsContext gc = fichaJugadorCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, fichaJugadorCanvas.getWidth(), fichaJugadorCanvas.getHeight()); // Limpia el canvas

            Ficha fichaADibujar = jugador.getFichaVisual();

            double canvasWidth = fichaJugadorCanvas.getWidth();
            double canvasHeight = fichaJugadorCanvas.getHeight();
            double radius = Math.min(canvasWidth, canvasHeight) / 2.2; // Calcula el radio basado en el tamaño del canvas
            double centerX = canvasWidth / 2;
            double centerY = canvasHeight / 2;

            fichaADibujar.dibujar(gc, centerX, centerY, radius); // Dibuja la ficha usando el patrón Decorator
            System.out.println("DEBUG (JuegoController): Ficha visual dibujada para: " + jugador.getAlias());
        } else {
            System.err.println("ERROR (JuegoController): No se pudo dibujar la ficha. Canvas lateral no inicializado o Jugador/Ficha es nulo.");
        }
    }

    /**
     * Dibuja la ficha del jugador dado en el Canvas que se mueve sobre el tablero.
     * @param jugador El objeto Jugador cuya ficha visual se va a dibujar.
     */
    private void dibujarFichaEnTablero(Jugador jugador) {
        if (fichaEnTableroCanvas != null && jugador != null && jugador.getFichaVisual() != null) {
            GraphicsContext gc = fichaEnTableroCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, fichaEnTableroCanvas.getWidth(), fichaEnTableroCanvas.getHeight()); // Limpia el canvas
            Ficha fichaADibujar = jugador.getFichaVisual();
            double canvasWidth = fichaEnTableroCanvas.getWidth();
            double canvasHeight = fichaEnTableroCanvas.getHeight();
            double radius = Math.min(canvasWidth, canvasHeight) / 2.2; // Calcula el radio basado en el tamaño del canvas
            double centerX = canvasWidth / 2;
            double centerY = canvasHeight / 2;
            fichaADibujar.dibujar(gc, centerX, centerY, radius); // Dibuja la ficha decorada
            System.out.println("DEBUG (JuegoController): Ficha en tablero dibujada para: " + jugador.getAlias());
        } else {
            System.err.println("ERROR (JuegoController): No se pudo dibujar la ficha en el tablero. Canvas de tablero no inicializado o Jugador/Ficha es nulo.");
        }
    }

    /**
     * Maneja el lanzamiento del dado y muestra los movimientos posibles al jugador.
     * @param valorDado El valor obtenido del dado.
     */
    public void lanzarYMostrarMovimientos(int valorDado) {
        this.ultimoValorDado = valorDado;
        this.puedeMover = true; // El jugador ahora puede seleccionar una casilla

        Jugador jugadorEnTurno = partida.getJugadorActual();
        if (jugadorEnTurno == null) {
            System.err.println("DEBUG (JuegoController): No hay jugador en turno para lanzar el dado.");
            return;
        }

        // Limpiar resaltado de casillas anteriores
        for (Rectangle rect : mapaNodoARect.values()) {
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
        }

        CasillaNode casillaActualDelJugador = mapaIDaNodo.get(jugadorEnTurno.getCasillaActualId());
        if (casillaActualDelJugador == null) {
            System.err.println("ERROR (JuegoController): Posición de casilla actual del jugador no válida o no encontrada en el mapa. Reiniciando a 'c'.");
            casillaActualDelJugador = mapaIDaNodo.get("c");
            jugadorEnTurno.setCasillaActualId("c"); // Asegura que el jugador tenga una posición válida

            if (casillaActualDelJugador == null) {
                System.err.println("Error grave (JuegoController): Casilla central 'c' no encontrada en el tablero. No se puede continuar.");
                return;
            }
            // Posiciona el Canvas de la ficha en el centro de la casilla
            fichaEnTableroCanvas.setLayoutX(casillaActualDelJugador.getX() - fichaEnTableroCanvas.getWidth()/2);
            fichaEnTableroCanvas.setLayoutY(casillaActualDelJugador.getY() - fichaEnTableroCanvas.getHeight()/2);
            dibujarFichaEnTablero(jugadorEnTurno); // Dibuja la ficha en la nueva posición
        }

        // Obtener todos los destinos posibles sin filtrar aún
        List<CasillaNode> posiblesSinFiltrar = grafoTablero.encontrarDestinosConPasos(casillaActualDelJugador, valorDado);
        movimientosPosibles.clear(); // Limpiar la lista para añadir solo los movimientos válidos

        // Determinar si el jugador tiene todos los quesitos para ir a la casilla central
        boolean jugadorTieneTodosLosQuesitos = jugadorEnTurno.tieneTodosLosQuesitos(totalCategoriasParaGanar);

        // Filtrar movimientos: la casilla 'c' solo es un destino válido si el jugador cumple la condición
        for (CasillaNode destino : posiblesSinFiltrar) {
            if (destino.getId().equals("c")) {
                if (jugadorTieneTodosLosQuesitos) {
                    movimientosPosibles.add(destino); // Añadir 'c' solo si el jugador cumple la condición
                } else {
                    System.out.println("DEBUG (JuegoController): Jugador " + jugadorEnTurno.getAlias() + " no tiene todos los quesitos. La casilla central ('c') no es un destino válido.");
                }
            } else {
                movimientosPosibles.add(destino); // Todas las demás casillas son siempre destinos válidos
            }
        }

        System.out.println("DEBUG (JuegoController): Movimientos posibles desde " + casillaActualDelJugador.getId() + " con dado " + valorDado + ":");

        if (movimientosPosibles.isEmpty()) {
            System.out.println("DEBUG (JuegoController): No hay movimientos posibles (o todos fueron filtrados). Pasando el turno.");
            puedeMover = false;
            partida.siguienteTurno();
            actualizarUIJugadorActual();
            return;
        }
        // Resalta las casillas posibles de movimiento
        for (CasillaNode destino : movimientosPosibles) {
            Rectangle rect = mapaNodoARect.get(destino);
            if (rect != null) {
                rect.setStroke(Color.RED);
                rect.setStrokeWidth(4);
                System.out.println("DEBUG (JuegoController):    → " + destino.getId());
            } else {
                System.err.println("DEBUG (JuegoController): No se encontró rectángulo para casilla: " + destino.getId());
            }
        }
        if (dadoController != null) {
            dadoController.deshabilitarBotonLanzar(); // Deshabilita el dado hasta que el jugador se mueva
        }
    }

    /**
     * Maneja el evento de clic en una casilla del tablero.
     * Permite al jugador mover su ficha a una casilla válida.
     * @param event El evento de ratón.
     */
    @FXML
    private void onCasillaClick(MouseEvent event) {
        if (!puedeMover) {
            System.out.println("DEBUG (JuegoController): No se puede mover en este momento (esperando lanzamiento de dado o acción).");
            return;
        }

        Node node = event.getPickResult().getIntersectedNode();
        if (node instanceof Rectangle rect && rect.getId() != null) {
            String idDestino = rect.getId();
            CasillaNode nodoDestino = mapaIDaNodo.get(idDestino);

            if (nodoDestino != null && movimientosPosibles.contains(nodoDestino)) {
                Jugador jugadorEnTurno = partida.getJugadorActual();
                if (jugadorEnTurno == null) {
                    System.err.println("ERROR (JuegoController): No hay jugador en turno al intentar mover.");
                    return;
                }
                jugadorEnTurno.setCasillaActualId(idDestino); // Actualiza la posición del jugador
                // Posiciona el Canvas de la ficha en el centro de la nueva casilla
                fichaEnTableroCanvas.setLayoutX(nodoDestino.getX() - fichaEnTableroCanvas.getWidth()/2);
                fichaEnTableroCanvas.setLayoutY(nodoDestino.getY() - fichaEnTableroCanvas.getHeight()/2);
                dibujarFichaEnTablero(jugadorEnTurno); // Dibuja la ficha decorada sobre el tablero después de mover

                movimientosPosibles.clear(); // Limpia los movimientos posibles
                // Restaura el color de los bordes de todas las casillas
                for (Rectangle r : mapaNodoARect.values()) {
                    r.setStroke(Color.BLACK);
                    r.setStrokeWidth(1);
                }

                System.out.println("DEBUG (JuegoController): Movimiento de " + jugadorEnTurno.getAlias() + " realizado a: " + idDestino);
                puedeMover = false; // El jugador ya no puede mover hasta el siguiente turno

                // Pequeña pausa para que el movimiento visual se asiente antes de la acción
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(e -> {
                    manejarAccionDespuesDeMovimiento(jugadorEnTurno, nodoDestino);
                });
                pause.play();

            } else {
                System.out.println("DEBUG (JuegoController): Movimiento inválido a: " + idDestino + ". No es un destino posible.");
            }
        } else {
            System.out.println("DEBUG (JuegoController): No se hizo clic en una casilla válida.");
        }
    }

    /**
     * Maneja la acción que ocurre después de que un jugador se ha movido a una casilla.
     * Esto puede ser mostrar una pregunta, verificar victoria, o dar un turno extra.
     * @param jugador El jugador que acaba de mover.
     * @param casillaDestino El nodo de la casilla a la que se movió el jugador.
     */
    private void manejarAccionDespuesDeMovimiento(Jugador jugador, CasillaNode casillaDestino) {
        String tipoCasilla = casillaDestino.getTipoCasilla();
        String categoriaCasilla = casillaDestino.getCategoriaAsociada();

        if ("pregunta".equals(tipoCasilla)) {
            Pregunta pregunta;
            if (categoriaCasilla != null) {
                pregunta = gestorPreguntas.getPreguntaAleatoria(categoriaCasilla);
                System.out.println("DEBUG (JuegoController): Pregunta de categoría específica: " + categoriaCasilla);
            } else {
                pregunta = gestorPreguntas.getPreguntaAleatoria(null);
                System.out.println("DEBUG (JuegoController): Advertencia: Casilla de pregunta sin categoría. Usando categoría aleatoria.");
            }

            if (pregunta != null) {
                final Pregunta finalPregunta = pregunta;

                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/PreguntaView.fxml"));
                        Parent root = loader.load();
                        PreguntaController preguntaController = loader.getController();

                        preguntaController.setJuegoController(this);
                        // ¡IMPORTANTE! Primero establece el tiempo límite, luego la pregunta.
                        preguntaController.setTiempoLimiteSegundos(partida.getTiempoRespuesta());
                        System.out.println("DEBUG (JuegoController): Pasando tiempo de respuesta a PreguntaController: " + partida.getTiempoRespuesta() + " segundos.");
                        preguntaController.setPregunta(finalPregunta);


                        Stage preguntaStage = new Stage();
                        preguntaStage.setScene(new Scene(root));
                        preguntaStage.setTitle("Pregunta de " + finalPregunta.getCategoria());
                        preguntaStage.initModality(Modality.APPLICATION_MODAL);
                        preguntaStage.setResizable(false);
                        preguntaStage.showAndWait();

                    } catch (IOException e) {
                        System.err.println("Error al cargar la vista de pregunta: " + e.getMessage());
                        e.printStackTrace();
                        Platform.runLater(() -> {
                            partida.siguienteTurno();
                            actualizarUIJugadorActual();
                        });
                    }
                });

            } else {
                System.out.println("No hay preguntas disponibles para la categoría " + (categoriaCasilla != null ? categoriaCasilla : "aleatoria") + ". Pasando turno.");
                partida.siguienteTurno();
                actualizarUIJugadorActual();
            }
        } else if ("central".equals(tipoCasilla)) {
            System.out.println("Jugador en la casilla central.");
            System.out.println("¡VICTORIA! " + jugador.getAlias() + " ha ganado el juego!");
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("¡FIN DEL JUEGO!");
                alert.setHeaderText("¡Felicidades, " + jugador.getAlias() + "!");
                alert.setContentText("¡Has llegado a la casilla central con todos los quesitos y has ganado el juego!\nRegresando al menú principal.");
                alert.showAndWait();
                jugador.getEstadisticas().incrementarPartidasJugadas();
                jugador.getEstadisticas().incrementarPartidasGanadas();
                gestorEstadisticas.actualizarEstadisticasJugador(jugador);
                guardarPartidaActual();
                if(jsonService != null) {
                    jsonService.eliminarPartidaGuardada();
                }
                if (dadoController != null) {
                    dadoController.deshabilitarBotonLanzar();
                }
                Stage currentStage = (Stage) rootPane.getScene().getWindow();
                navigateToMenuPrincipal(currentStage);
            });
        } else {
            System.out.println("Jugador en casilla especial. ¡Repites el turno!");
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("¡Casilla Especial!");
                alert.setHeaderText(null);
                alert.setContentText("¡Has caído en una casilla especial! Tienes otro turno para lanzar el dado.");
                alert.showAndWait();
                actualizarUIJugadorActual();
            });
        }
    }

    /**
     * Notifica el resultado de una pregunta al controlador del juego.
     * Actualiza las estadísticas del jugador y decide si el turno pasa o no.
     * @param respuestaCorrecta Verdadero si la respuesta fue correcta, falso en caso contrario.
     * @param categoriaPregunta La categoría de la pregunta respondida.
     * @param tiempoRespuesta El tiempo que tardó el jugador en responder (en milisegundos).
     */
    public void notificarResultadoPregunta(boolean respuestaCorrecta, String categoriaPregunta,long tiempoRespuesta) {
        Jugador jugadorEnTurnoAntes = partida.getJugadorActual();
        System.out.println("DEBUG (JuegoController): Notificación de resultado. Jugador actual ANTES de la lógica del turno: " + (jugadorEnTurnoAntes != null ? jugadorEnTurnoAntes.getAlias() : "NULO"));

        if (jugadorEnTurnoAntes == null) {
            System.err.println("ERROR (JuegoController): No hay jugador en turno al notificar resultado de pregunta.");
            return;
        }

        if (respuestaCorrecta) {
            System.out.println("DEBUG (JuegoController): ¡Respuesta Correcta! " + jugadorEnTurnoAntes.getAlias() + " ha ganado un quesito de " + categoriaPregunta);
            jugadorEnTurnoAntes.addQuesito(categoriaPregunta); // Añade el quesito al jugador
            dibujarFichaDelJugador(jugadorEnTurnoAntes); // Redibuja la ficha con el nuevo quesito

            // Actualiza estadísticas del jugador
            jugadorEnTurnoAntes.getEstadisticas().incrementarPreguntasCorrectasTotal();
            jugadorEnTurnoAntes.getEstadisticas().incrementarPreguntasCorrectasPorCategoria(categoriaPregunta);
            jugadorEnTurnoAntes.getEstadisticas().añadirTiempoRespuestaCorrecta(tiempoRespuesta);
            gestorEstadisticas.actualizarEstadisticasJugador(jugadorEnTurnoAntes); // Guarda las estadísticas actualizadas

            System.out.println("DEBUG (JuegoController): Respuesta CORRECTA. NO se pasa el turno.");
        } else {
            System.out.println("DEBUG (JuegoController): Respuesta Incorrecta. " + jugadorEnTurnoAntes.getAlias() + " no gana quesito. El turno pasa.");

            // Actualiza estadísticas del jugador
            jugadorEnTurnoAntes.getEstadisticas().incrementarPreguntasIncorrectas();
            gestorEstadisticas.actualizarEstadisticasJugador(jugadorEnTurnoAntes);

            partida.siguienteTurno(); // Pasa al siguiente turno
            System.out.println("DEBUG (JuegoController): Respuesta INCORRECTA. Se pasa el turno.");
        }

        guardarPartidaActual();

        Jugador jugadorEnTurnoDespues = partida.getJugadorActual();
        System.out.println("DEBUG (JuegoController): Jugador actual DESPUÉS de la lógica del turno (antes de actualizar UI): " + (jugadorEnTurnoDespues != null ? jugadorEnTurnoDespues.getAlias() : "NULO"));

        actualizarUIJugadorActual(); // Actualiza la UI para el siguiente jugador o el mismo si repite turno

        Jugador jugadorEnTurnoFinal = partida.getJugadorActual();
        System.out.println("DEBUG (JuegoController): Jugador actual FINAL (después de actualizar UI): " + (jugadorEnTurnoFinal != null ? jugadorEnTurnoFinal.getAlias() : "NULO"));
    }

    /**
     * Maneja la acción del botón "Finalizar Partida".
     * Actualiza las estadísticas de todos los jugadores al final de la partida y regresa al menú principal.
     * @param event El evento de acción.
     */
    @FXML
    private void handleFinalizarPartida(ActionEvent event) {
        System.out.println("DEBUG (JuegoController): Partida finalizada.");
        for (Jugador j : partida.getJugadores()) {
            // Solo incrementa partidas perdidas si no ganó y no se rindió explícitamente
            if (!j.tieneTodosLosQuesitos(totalCategoriasParaGanar)) {
                j.getEstadisticas().incrementarPartidasJugadas();
                j.getEstadisticas().incrementarPartidasPerdidas();
                gestorEstadisticas.actualizarEstadisticasJugador(j);
            } else {
                // Si el jugador tiene todos los quesitos, solo incrementa partidas jugadas (ganadas ya se manejó en casilla central)
                j.getEstadisticas().incrementarPartidasJugadas();
                gestorEstadisticas.actualizarEstadisticasJugador(j);
            }
        }
        partida.terminarPartida(); // Marca la partida como terminada
        // Eliminar la partida guardada al finalizar
        if (jsonService != null) {
            jsonService.eliminarPartidaGuardada();
        }
        handleRegresar(event); // Regresa al menú principal
    }

    /**
     * Maneja la acción de rendición de un jugador.
     * Actualiza las estadísticas del jugador rendido y pasa el turno o finaliza la partida.
     * @param event El evento de acción.
     */
    @FXML
    private void handleRendicion(ActionEvent event) {
        Jugador jugadorRendido = partida.getJugadorActual();
        if (jugadorRendido == null) {
            System.err.println("DEBUG (JuegoController): No hay jugador en turno para rendirse.");
            return;
        }

        List<Jugador> otrosJugadoresActivos = partida.getJugadores().stream()
                .filter(j -> !j.equals(jugadorRendido))
                .collect(Collectors.toList());

        boolean jugadorRendidoGanaPorQuesitos = true;
        if (!otrosJugadoresActivos.isEmpty()) {
            for (Jugador otroJugador : otrosJugadoresActivos) {
                if (jugadorRendido.getQuesitosGanadosNombres().size() <= otroJugador.getQuesitosGanadosNombres().size()) {
                    jugadorRendidoGanaPorQuesitos = false;
                    break;
                }
            }
        } else {
            // Si no hay otros jugadores activos, el jugador actual no puede ganar por tener "más" quesitos
            jugadorRendidoGanaPorQuesitos = false;
        }

        if (jugadorRendidoGanaPorQuesitos) {
            System.out.println("DEBUG (JuegoController): ¡" + jugadorRendido.getAlias() + " se ha rendido y ha ganado por tener más quesitos!");
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("¡Victoria por Rendición!");
                alert.setHeaderText("¡Felicidades, " + jugadorRendido.getAlias() + "!");
                alert.setContentText("Te has rendido, pero has ganado la partida por tener más quesitos que los demás jugadores activos.");
                alert.showAndWait();
            });

            jugadorRendido.getEstadisticas().incrementarPartidasJugadas();
            jugadorRendido.getEstadisticas().incrementarPartidasGanadas();
            gestorEstadisticas.actualizarEstadisticasJugador(jugadorRendido);

            partida.removeJugador(jugadorRendido); // Elimina al jugador de la partida
            guardarPartidaActual(); // Guarda el estado actual sin el jugador rendido

            // La partida continúa si hay jugadores restantes
            if (partida.getJugadores().isEmpty()) {
                finalizarPartidaPorRendicion(); // Si no quedan jugadores, la partida termina
            } else {
                partida.siguienteTurno(); // Pasa el turno al siguiente jugador
                actualizarUIJugadorActual();
            }

        } else {
            System.out.println("DEBUG (JuegoController): " + jugadorRendido.getAlias() + " se ha rendido.");

            jugadorRendido.getEstadisticas().incrementarPartidasJugadas();
            jugadorRendido.getEstadisticas().incrementarPartidasPerdidas();
            gestorEstadisticas.actualizarEstadisticasJugador(jugadorRendido);

            partida.removeJugador(jugadorRendido); // Elimina al jugador de la partida
            guardarPartidaActual(); // Guarda el estado actual sin el jugador rendido

            // La partida continúa si hay jugadores restantes
            if (partida.getJugadores().isEmpty()) {
                finalizarPartidaPorRendicion(); // Si no quedan jugadores, la partida termina
            } else {
                partida.siguienteTurno(); // Pasa el turno al siguiente jugador
                actualizarUIJugadorActual();
            }
        }
    }

    /**
     * Lógica para finalizar la partida cuando no quedan jugadores después de una rendición.
     */
    private void finalizarPartidaPorRendicion() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Fin de Partida por Rendición");
            alert.setHeaderText(null);

            if (partida.getJugadores().isEmpty()) {
                alert.setContentText("Todos los jugadores se han rendido. Fin de la partida.");
            } else {
                // Esto no debería ocurrir si este método solo se llama cuando no quedan jugadores
                System.err.println("ERROR (JuegoController): finalizarPartidaPorRendicion llamada con jugadores restantes.");
                alert.setContentText("La partida ha terminado.");
            }
            alert.showAndWait();

            partida.terminarPartida(); // Marca la partida como terminada
            if (jsonService != null) {
                jsonService.eliminarPartidaGuardada(); // Elimina la partida guardada
            }
            if (dadoController != null) {
                dadoController.deshabilitarBotonLanzar();
            }
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            navigateToMenuPrincipal(currentStage);
        });
    }


    /**
     * Método para guardar el estado actual de la partida en un archivo JSON.
     * Se llama automáticamente en puntos clave del juego.
     */
    private void guardarPartidaActual() {
        if (partida != null && jsonService != null) {
            jsonService.guardarPartida(partida);
            System.out.println("DEBUG (JuegoController): Partida guardada automáticamente.");
        } else {
            System.err.println("ERROR (JuegoController): No se pudo guardar la partida. Objeto partida o jsonService es nulo.");
        }
    }

    /**
     * Maneja la acción del botón "Regresar".
     * Navega de vuelta a la pantalla del menú principal.
     * @param event El evento de acción.
     */
    @FXML
    private void handleRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        navigateToMenuPrincipal(stage);
    }

    /**
     * Maneja la acción del botón "Salir".
     * Cierra la aplicación.
     * @param event El evento de acción.
     */
    @FXML
    private void handleSalir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Establece las conexiones entre las casillas del tablero para formar el grafo.
     * Este método define la estructura de movimiento en el tablero.
     */
    private void conectarCasillasCorrectamente() {
        grafoTablero.agregarArista("1", "7");
        grafoTablero.agregarArista("7", "8");
        grafoTablero.agregarArista("8", "9");
        grafoTablero.agregarArista("9", "10");
        grafoTablero.agregarArista("10", "11");
        grafoTablero.agregarArista("11", "2");
        grafoTablero.agregarArista("2", "12");
        grafoTablero.agregarArista("12", "13");
        grafoTablero.agregarArista("13", "14");
        grafoTablero.agregarArista("14", "15");
        grafoTablero.agregarArista("15", "16");
        grafoTablero.agregarArista("16", "3");
        grafoTablero.agregarArista("3", "17");
        grafoTablero.agregarArista("17", "18");
        grafoTablero.agregarArista("18", "19");
        grafoTablero.agregarArista("19", "20");
        grafoTablero.agregarArista("20", "21");
        grafoTablero.agregarArista("21", "4");
        grafoTablero.agregarArista("4", "22");
        grafoTablero.agregarArista("22", "23");
        grafoTablero.agregarArista("23", "24");
        grafoTablero.agregarArista("24", "25");
        grafoTablero.agregarArista("25", "26");
        grafoTablero.agregarArista("26", "5");
        grafoTablero.agregarArista("5", "27");
        grafoTablero.agregarArista("27", "28");
        grafoTablero.agregarArista("28", "29");
        grafoTablero.agregarArista("29", "30_path");
        grafoTablero.agregarArista("30_path", "31");
        grafoTablero.agregarArista("31", "6");
        grafoTablero.agregarArista("6", "32");
        grafoTablero.agregarArista("32", "33");
        grafoTablero.agregarArista("33", "34");
        grafoTablero.agregarArista("34", "35");
        grafoTablero.agregarArista("35", "36");
        grafoTablero.agregarArista("36", "1");
        grafoTablero.agregarArista("1", "37");
        grafoTablero.agregarArista("37", "38");
        grafoTablero.agregarArista("38", "39");
        grafoTablero.agregarArista("39", "40");
        grafoTablero.agregarArista("40", "41");
        grafoTablero.agregarArista("41", "c");
        grafoTablero.agregarArista("2", "47");
        grafoTablero.agregarArista("47", "48");
        grafoTablero.agregarArista("48", "49");
        grafoTablero.agregarArista("49", "50");
        grafoTablero.agregarArista("50", "51");
        grafoTablero.agregarArista("51", "c");
        grafoTablero.agregarArista("3", "57");
        grafoTablero.agregarArista("57", "58");
        grafoTablero.agregarArista("58", "59");
        grafoTablero.agregarArista("59", "60");
        grafoTablero.agregarArista("60", "61");
        grafoTablero.agregarArista("61", "c");
        grafoTablero.agregarArista("4", "46");
        grafoTablero.agregarArista("46", "45");
        grafoTablero.agregarArista("45", "44");
        grafoTablero.agregarArista("44", "43");
        grafoTablero.agregarArista("43", "42");
        grafoTablero.agregarArista("42", "c");
        grafoTablero.agregarArista("5", "56");
        grafoTablero.agregarArista("56", "55");
        grafoTablero.agregarArista("55", "54");
        grafoTablero.agregarArista("54", "53");
        grafoTablero.agregarArista("53", "52");
        grafoTablero.agregarArista("52", "c");
        grafoTablero.agregarArista("6", "66");
        grafoTablero.agregarArista("66", "65");
        grafoTablero.agregarArista("65", "64");
        grafoTablero.agregarArista("64", "63");
        grafoTablero.agregarArista("63", "62");
        grafoTablero.agregarArista("62", "c");
        grafoTablero.agregarArista("c", "41");
        grafoTablero.agregarArista("c", "51");
        grafoTablero.agregarArista("c", "61");
        grafoTablero.agregarArista("c", "42");
        grafoTablero.agregarArista("c", "52");
        grafoTablero.agregarArista("c", "62");
    }

    /**
     * Verifica las conexiones del grafo del tablero imprimiéndolas en la consola.
     * Útil para depuración.
     */
    private void verificarConexiones() {
        System.out.println("DEBUG (JuegoController): Conexiones del grafo:");
        for (String id : grafoTablero.getIdsNodos()) {
            CasillaNode nodo = mapaIDaNodo.get(id);
            if (nodo != null) {
                System.out.print("  " + id + " -> ");
                for (CasillaNode vecino : grafoTablero.getVecinos(nodo)) {
                    System.out.print(vecino.getId() + " ");
                }
                System.out.println();
            }
        }
    }

    /**
     * Método para navegar de vuelta a la pantalla del menú principal.
     * @param currentStage El Stage actual de la aplicación.
     */
    private void navigateToMenuPrincipal(Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/MenuPrincipalView.fxml"));
            Scene scene = new Scene(loader.load());
            currentStage.setScene(scene);
            currentStage.setTitle("Trivia UCAB - Menú Principal");
            currentStage.setMaximized(true);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR (JuegoController): Error al cargar la vista del menú principal: " + e.getMessage());
        }
    }
}
