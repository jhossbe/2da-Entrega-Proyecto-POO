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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;
import org.example.triviaucab1.fichadecorator.Ficha;
import org.example.triviaucab1.module.GestorPreguntas;
import org.example.triviaucab1.module.Jugador;
import org.example.triviaucab1.module.Partida;
import org.example.triviaucab1.module.Pregunta;
import org.example.triviaucab1.module.tablero.CasillaNode;
import org.example.triviaucab1.module.tablero.GrafoTablero;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert; // NUEVO: Importar Alert
import javafx.scene.control.Alert.AlertType; // NUEVO: Importar AlertType

import java.io.IOException;
import java.util.*;

public class JuegoController {

    @FXML private AnchorPane rootPane;
    @FXML private Label jugadorEnTurnoLabel;
    @FXML private VBox categoriasJugadorVBox; // No usado en este ejemplo, pero se mantiene
    @FXML private Label tiempoRespuestaLabel; // No usado en este ejemplo, pero se mantiene

    @FXML private Canvas fichaJugadorCanvas;

    private GrafoTablero grafoTablero;
    private int ultimoValorDado = 0;
    private boolean puedeMover = false;
    private Circle fichaEnTablero;
    private List<CasillaNode> movimientosPosibles = new ArrayList<>();
    private Map<String, CasillaNode> mapaIDaNodo = new HashMap<>();
    private Map<CasillaNode, Rectangle> mapaNodoARect = new HashMap<>();
    private Partida partida;
    private DadoController dadoController;

    private GestorPreguntas gestorPreguntas;
    private Map<String, String> casillaIdToCategory = new HashMap<>();
    private int totalCategoriasParaGanar;


    @FXML
    public void initialize() {
        gestorPreguntas = new GestorPreguntas("preguntasJuegoTrivia_final.json");
        totalCategoriasParaGanar = gestorPreguntas.getTotalCategorias();
        System.out.println("Total de categorías de quesitos posibles para ganar: " + totalCategoriasParaGanar);

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
        conectarCasillasCorrectamente();
        fichaEnTablero = new Circle(17, Color.WHITE);
        fichaEnTablero.setStroke(Color.BLACK);
        fichaEnTablero.setStrokeWidth(2);
        fichaEnTablero.setMouseTransparent(true);
        fichaEnTablero.setVisible(false);
        rootPane.getChildren().add(fichaEnTablero);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/DadoView.fxml"));
            AnchorPane dadoPane = loader.load();
            dadoController = loader.getController();
            dadoController.setJuegoController(this);
            AnchorPane.setBottomAnchor(dadoPane, 30.0);
            AnchorPane.setRightAnchor(dadoPane, 30.0);
            rootPane.getChildren().add(dadoPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Nodos en el grafo: " + grafoTablero.getIdsNodos().size());
        //System.out.println("🧩 Nodos visuales encontrados: " + mapaIDaNodo.size());
        verificarConexiones();
    }

    /**
     * Método auxiliar para añadir múltiples IDs a una categoría en el mapa.
     */
    private void addCategories(Map<String, String> map, String category, String... ids) {
        for (String id : ids) {
            map.put(id, category);
        }
    }


    /**
     * Establece la partida actual y configura el jugador inicial.
     * Este método DEBE SER LLAMADO por la clase que inicia la partida (ej. MainPrincipal).
     * @param partida La instancia de la partida.
     */
    public void setPartida(Partida partida) {
        this.partida = partida;
        System.out.println("Partida recibida con " + partida.getJugadores().size() + " jugadores.");
        actualizarUIJugadorActual();
        if (dadoController != null) {
            dadoController.habilitarBotonLanzar();
        }
    }

    /**
     * Actualiza toda la interfaz de usuario para reflejar al jugador que tiene el turno.
     * Esto incluye el Label del nombre, la ficha visual de quesitos, y la posición de la ficha en el tablero.
     */
    public void actualizarUIJugadorActual() {
        Jugador jugadorEnTurno = partida.getJugadorActual();

        if (jugadorEnTurno == null) {
            System.err.println("Error: No hay jugador en turno para actualizar la UI.");
            fichaEnTablero.setVisible(false);
            return;
        }

        if (jugadorEnTurnoLabel != null) {
            jugadorEnTurnoLabel.setText("Jugador Actual: " + jugadorEnTurno.getAlias());
        }

        String idCasillaJugador = jugadorEnTurno.getCasillaActualId();
        if (idCasillaJugador == null || idCasillaJugador.isEmpty()) {

            idCasillaJugador = "c";
            jugadorEnTurno.setCasillaActualId("c"); // Establece la casilla inicial si no tiene
        }

        CasillaNode casillaDelJugadorEnGrafo = mapaIDaNodo.get(idCasillaJugador);

        if (casillaDelJugadorEnGrafo == null) {
            System.err.println("❌ ERROR: La casilla '" + idCasillaJugador + "' del jugador no se encontró en el tablero.");
            fichaEnTablero.setVisible(false);
            return;
        }
        fichaEnTablero.setLayoutX(casillaDelJugadorEnGrafo.getX());
        fichaEnTablero.setLayoutY(casillaDelJugadorEnGrafo.getY());
        fichaEnTablero.setVisible(true);
        dibujarFichaDelJugador(jugadorEnTurno);

        System.out.println("🔄 UI actualizada para: " + jugadorEnTurno.getAlias() + " en casilla: " + idCasillaJugador);
        // Habilitar dado al comienzo del turno
        if (dadoController != null) {
            dadoController.habilitarBotonLanzar();
        }
    }

    /**
     * Dibuja la ficha del jugador dado en el Canvas dedicado.
     * Ahora recibe el jugador como parámetro.
     */
    public void dibujarFichaDelJugador(Jugador jugador) {
        if (fichaJugadorCanvas != null && jugador != null && jugador.getFichaVisual() != null) {
            GraphicsContext gc = fichaJugadorCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, fichaJugadorCanvas.getWidth(), fichaJugadorCanvas.getHeight());

            Ficha fichaADibujar = jugador.getFichaVisual();

            double canvasWidth = fichaJugadorCanvas.getWidth();
            double canvasHeight = fichaJugadorCanvas.getHeight();
            double radius = Math.min(canvasWidth, canvasHeight) / 2.2;
            double centerX = canvasWidth / 2;
            double centerY = canvasHeight / 2;

            fichaADibujar.dibujar(gc, centerX, centerY, radius);
            System.out.println("Ficha visual dibujada para: " + jugador.getAlias());
        } else {
            System.err.println("Error: No se pudo dibujar la ficha. Canvas no inicializado o Jugador/Ficha es nulo.");
        }
    }

    public void lanzarYMostrarMovimientos(int valorDado) {
        this.ultimoValorDado = valorDado;
        this.puedeMover = true;

        Jugador jugadorEnTurno = partida.getJugadorActual();
        if (jugadorEnTurno == null) {
            System.err.println("No hay jugador en turno para lanzar el dado.");
            return;
        }

        // Limpiar resaltado de casillas anteriores
        for (Rectangle rect : mapaNodoARect.values()) {
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
        }

        CasillaNode casillaActualDelJugador = mapaIDaNodo.get(jugadorEnTurno.getCasillaActualId());
        if (casillaActualDelJugador == null) {
            System.err.println("Error: Posición de casilla actual del jugador no válida o no encontrada en el mapa.");
            casillaActualDelJugador = mapaIDaNodo.get("c");
            jugadorEnTurno.setCasillaActualId("c");

            if (casillaActualDelJugador == null) {
                System.err.println("Error grave: Casilla central 'c' no encontrada en el tablero. No se puede continuar.");
                return;
            }
            fichaEnTablero.setLayoutX(casillaActualDelJugador.getX());
            fichaEnTablero.setLayoutY(casillaActualDelJugador.getY());
        }

        // Obtener todos los destinos posibles sin filtrar aún
        List<CasillaNode> posiblesSinFiltrar = grafoTablero.encontrarDestinosConPasos(casillaActualDelJugador, valorDado);
        movimientosPosibles.clear(); // Limpiar la lista para añadir solo los movimientos válidos

        // Determinar si el jugador tiene todos los quesitos para ir a la casilla central
        boolean jugadorTieneTodosLosQuesitos = jugadorEnTurno.tieneTodosLosQuesitos(totalCategoriasParaGanar);

        // Filtrar movimientos: la casilla 'c' solo es un destino válido si el jugador tiene todos los quesitos
        for (CasillaNode destino : posiblesSinFiltrar) {
            if (destino.getId().equals("c")) {
                if (jugadorTieneTodosLosQuesitos) {
                    movimientosPosibles.add(destino); // Añadir 'c' solo si el jugador cumple la condición
                } else {
                    System.out.println("Jugador " + jugadorEnTurno.getAlias() + " no tiene todos los quesitos. La casilla central ('c') no es un destino válido.");
                }
            } else {
                movimientosPosibles.add(destino); // Todas las demás casillas son siempre destinos válidos
            }
        }

        System.out.println("🎯 Movimientos posibles desde " + casillaActualDelJugador.getId() + " con dado " + valorDado + ":");

        if (movimientosPosibles.isEmpty()) {
            System.out.println("   ❌ No hay movimientos posibles (o todos fueron filtrados). Pasando el turno.");
            puedeMover = false;
            partida.siguienteTurno();
            actualizarUIJugadorActual();
            return;
        }
        for (CasillaNode destino : movimientosPosibles) {
            Rectangle rect = mapaNodoARect.get(destino);
            if (rect != null) {
                rect.setStroke(Color.RED);
                rect.setStrokeWidth(4);
                System.out.println("   → " + destino.getId());
            } else {
                System.err.println("   ⚠️ No se encontró rectángulo para casilla: " + destino.getId());
            }
        }
        if (dadoController != null) {
            dadoController.deshabilitarBotonLanzar();
        }
    }

    @FXML
    private void onCasillaClick(MouseEvent event) {
        if (!puedeMover) {
            System.out.println("🚫 No se puede mover en este momento (esperando lanzamiento de dado o acción).");
            return;
        }

        Node node = event.getPickResult().getIntersectedNode();
        if (node instanceof Rectangle rect && rect.getId() != null) {
            String idDestino = rect.getId();
            CasillaNode nodoDestino = mapaIDaNodo.get(idDestino);

            if (nodoDestino != null && movimientosPosibles.contains(nodoDestino)) {
                Jugador jugadorEnTurno = partida.getJugadorActual();
                if (jugadorEnTurno == null) {
                    System.err.println("Error: No hay jugador en turno al intentar mover.");
                    return;
                }
                jugadorEnTurno.setCasillaActualId(idDestino);
                fichaEnTablero.setLayoutX(nodoDestino.getX());
                fichaEnTablero.setLayoutY(nodoDestino.getY());
                movimientosPosibles.clear();
                for (Rectangle r : mapaNodoARect.values()) {
                    r.setStroke(Color.BLACK);
                    r.setStrokeWidth(1);
                }

                System.out.println("✅ Movimiento de " + jugadorEnTurno.getAlias() + " realizado a: " + idDestino);
                puedeMover = false;
                // Pequeña pausa para que el movimiento visual se asiente antes de la acción
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // Reducido a 0.5 segundos
                pause.setOnFinished(e -> {
                    manejarAccionDespuesDeMovimiento(jugadorEnTurno, nodoDestino);
                });
                pause.play();

            } else {
                System.out.println("❌ Movimiento inválido a: " + idDestino + ". No es un destino posible.");
            }
        } else {
            System.out.println("No se hizo clic en una casilla válida.");
        }
    }


    // MÉTODO PARA MANEJAR LA ACCIÓN DESPUÉS DE QUE EL JUGADOR SE MUEVE
    private void manejarAccionDespuesDeMovimiento(Jugador jugador, CasillaNode casillaDestino) {
        String tipoCasilla = casillaDestino.getTipoCasilla();
        String categoriaCasilla = casillaDestino.getCategoriaAsociada();

        if ("pregunta".equals(tipoCasilla)) {
            Pregunta pregunta;
            if (categoriaCasilla != null) {
                pregunta = gestorPreguntas.getPreguntaAleatoria(categoriaCasilla);
                System.out.println("Pregunta de categoría específica: " + categoriaCasilla);
            } else {
                pregunta = gestorPreguntas.getPreguntaAleatoria(null);
                System.out.println("Advertencia: Casilla de pregunta sin categoría. Usando categoría aleatoria.");
            }

            if (pregunta != null) {
                final Pregunta finalPregunta = pregunta;

                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/PreguntaView.fxml"));
                        Parent root = loader.load();
                        PreguntaController preguntaController = loader.getController();

                        preguntaController.setJuegoController(this);
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
            // Un jugador solo puede llegar a la casilla 'c' si tiene todos los quesitos (filtrado en lanzarYMostrarMovimientos).
            // Por lo tanto, al caer aquí, significa que gana.
            System.out.println("Jugador en la casilla central.");
            System.out.println("¡VICTORIA! " + jugador.getAlias() + " ha ganado el juego!");
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("¡FIN DEL JUEGO!");
                alert.setHeaderText("¡Felicidades, " + jugador.getAlias() + "!");
                alert.setContentText("¡Has llegado a la casilla central con todos los quesitos y has ganado el juego!");
                alert.showAndWait();
                // Deshabilitar el botón de dado para indicar que el juego ha terminado
                if (dadoController != null) {
                    dadoController.deshabilitarBotonLanzar();
                }
                Stage currentStage = (Stage) rootPane.getScene().getWindow();
                navigateToMenuPrincipal(currentStage);
            });
        } else { // Este es el caso para casillas "normal" (ahora tratadas como especiales)
            System.out.println("Jugador en casilla especial. ¡Repites el turno!");
            // No se llama a partida.siguienteTurno() aquí. El turno se mantiene.
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("¡Casilla Especial!");
                alert.setHeaderText(null);
                alert.setContentText("¡Has caído en una casilla especial! Tienes otro turno para lanzar el dado.");
                alert.showAndWait();
                actualizarUIJugadorActual(); // Asegurarse de que la UI se actualice después del alert
            });
        }
    }


    public void notificarResultadoPregunta(boolean respuestaCorrecta, String categoriaPregunta) {
        Jugador jugadorEnTurnoAntes = partida.getJugadorActual();
        System.out.println("DEBUG: Notificación de resultado. Jugador actual ANTES de la lógica del turno: " + (jugadorEnTurnoAntes != null ? jugadorEnTurnoAntes.getAlias() : "NULO"));


        if (jugadorEnTurnoAntes == null) {
            System.err.println("Error: No hay jugador en turno al notificar resultado de pregunta.");
            return;
        }

        if (respuestaCorrecta) {
            System.out.println("¡Respuesta Correcta! " + jugadorEnTurnoAntes.getAlias() + " ha ganado un quesito de " + categoriaPregunta);
            jugadorEnTurnoAntes.addQuesito(categoriaPregunta);
            dibujarFichaDelJugador(jugadorEnTurnoAntes); // Actualiza la ficha de quesitos visualmente
            // NO se llama a partida.siguienteTurno() aquí. El turno se mantiene para el mismo jugador.
            System.out.println("DEBUG: Respuesta CORRECTA. NO se pasa el turno.");
        } else {
            System.out.println("Respuesta Incorrecta. " + jugadorEnTurnoAntes.getAlias() + " no gana quesito. El turno pasa.");
            partida.siguienteTurno(); // <-- ¡SOLO AQUÍ SE PASA EL TURNO!
            System.out.println("DEBUG: Respuesta INCORRECTA. Se pasa el turno.");
        }

        // Siempre se actualiza la UI para reflejar el estado del juego (ya sea el mismo jugador o el siguiente)
        Jugador jugadorEnTurnoDespues = partida.getJugadorActual();
        System.out.println("DEBUG: Jugador actual DESPUÉS de la lógica del turno (antes de actualizar UI): " + (jugadorEnTurnoDespues != null ? jugadorEnTurnoDespues.getAlias() : "NULO"));

        actualizarUIJugadorActual();

        Jugador jugadorEnTurnoFinal = partida.getJugadorActual();
        System.out.println("DEBUG: Jugador actual FINAL (después de actualizar UI): " + (jugadorEnTurnoFinal != null ? jugadorEnTurnoFinal.getAlias() : "NULO"));
    }

    @FXML
    private void handleFinalizarPartida(ActionEvent event) {
        System.out.println("Partida finalizada.");
        partida.terminarPartida();
        handleRegresar(event);
    }

    @FXML
    private void handleRendicion(ActionEvent event) {
        Jugador jugadorRendido = partida.getJugadorActual();
        if (jugadorRendido != null) {
            System.out.println(jugadorRendido.getAlias() + " se ha rendido.");
            if (partida.getJugadores().size() <= 1) {
                handleFinalizarPartida(event);
            } else {
                partida.siguienteTurno();
                actualizarUIJugadorActual();
            }
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/MenuPrincipalView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSalir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

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

    private void verificarConexiones() {
        //System.out.println("Conexiones del grafo:");
        for (String id : grafoTablero.getIdsNodos()) {
            CasillaNode nodo = mapaIDaNodo.get(id);
            if (nodo != null) {
                //System.out.print("  " + id + " -> ");
                for (CasillaNode vecino : grafoTablero.getVecinos(nodo)) {
                    System.out.print(vecino.getId() + " ");
                }
                //System.out.println();
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
            System.err.println("Error al cargar la vista del menú principal: " + e.getMessage());
        }
    }

}