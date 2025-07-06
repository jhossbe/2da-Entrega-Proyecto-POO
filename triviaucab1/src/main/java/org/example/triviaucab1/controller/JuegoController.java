package org.example.triviaucab1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.triviaucab1.module.Jugador;
import org.example.triviaucab1.module.Partida;
import org.example.triviaucab1.module.tablero.CasillaNode;
import org.example.triviaucab1.module.tablero.GrafoTablero;

import java.io.IOException;
import java.util.*;

public class JuegoController {

    @FXML private AnchorPane rootPane;
    @FXML private Label jugadorEnTurnoLabel;
    @FXML private VBox categoriasJugadorVBox;
    @FXML private Label tiempoRespuestaLabel;

    private GrafoTablero grafoTablero;
    private int ultimoValorDado = 0;
    private boolean puedeMover = false;
    private List<Canvas> fichasCanvas = new ArrayList<>();
    private List<CasillaNode> movimientosPosibles = new ArrayList<>();
    private Map<String, CasillaNode> mapaIDaNodo = new HashMap<>();
    private Map<CasillaNode, Rectangle> mapaNodoARect = new HashMap<>();
    private Partida partida;

    @FXML
    public void initialize() {
        grafoTablero = new GrafoTablero();

        // Crear nodos del grafo basados en las casillas del FXML
        for (Node node : rootPane.getChildren()) {
            if (node instanceof Rectangle rect && rect.getId() != null) {
                String id = rect.getId();
                // Solo procesar casillas válidas del juego (excluir leyenda)
                if (!id.equals("leyendaBackground") && (id.equals("c") || id.equals("30_path") || id.matches("\\d+"))) {
                    double x = rect.getLayoutX() + rect.getWidth() / 2;
                    double y = rect.getLayoutY() + rect.getHeight() / 2;
                    CasillaNode nodo = new CasillaNode(id, x, y);
                    grafoTablero.agregarNodo(nodo);
                    mapaIDaNodo.put(id, nodo);
                    mapaNodoARect.put(nodo, rect);
                }
            }
        }

        conectarCasillasCorrectamente();

        // Cargar el componente del dado
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/DadoView.fxml"));
            AnchorPane dadoPane = loader.load();
            DadoController dadoController = loader.getController();
            dadoController.setJuegoController(this);
            AnchorPane.setBottomAnchor(dadoPane, 30.0);
            AnchorPane.setRightAnchor(dadoPane, 30.0);
            rootPane.getChildren().add(dadoPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("🧩 Nodos en el grafo: " + grafoTablero.getIdsNodos().size());
        System.out.println("🧩 Nodos visuales encontrados: " + mapaIDaNodo.size());

        // Debug: verificar conexiones
        verificarConexiones();
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
        System.out.println("Partida recibida con " + partida.getJugadores().size() + " jugadores.");

        // Inicializar fichas de jugadores
        inicializarFichasJugadores();

        // Actualizar interfaz
        actualizarInterfazJugador();
    }

    /**
     * Inicializa las fichas visuales de todos los jugadores en el tablero.
     */
    private void inicializarFichasJugadores() {
        if (partida == null) return;

        // Limpiar fichas anteriores
        for (Canvas canvas : fichasCanvas) {
            rootPane.getChildren().remove(canvas);
        }
        fichasCanvas.clear();

        // Crear fichas para cada jugador
        for (int i = 0; i < partida.getJugadores().size(); i++) {
            Jugador jugador = partida.getJugadores().get(i);
            CasillaNode posicion = jugador.getPosicionActual();

            if (posicion == null) {
                // Si no tiene posición, colocar en el centro
                posicion = mapaIDaNodo.get("c");
                jugador.setPosicionActual(posicion);
            }

            // Crear canvas para la ficha
            Canvas fichaCanvas = new Canvas(30, 30);

            // Calcular posición con offset para múltiples jugadores
            double offsetX = (i % 2) * 15 - 7.5; // Alternar izquierda/derecha
            double offsetY = (i / 2) * 15 - 7.5; // Alternar arriba/abajo

            fichaCanvas.setLayoutX(posicion.getX() + offsetX - 15);
            fichaCanvas.setLayoutY(posicion.getY() + offsetY - 15);

            // Dibujar la ficha usando el patrón Decorator
            GraphicsContext gc = fichaCanvas.getGraphicsContext2D();
            jugador.getFicha().dibujar(gc, 15, 15, 12); // Centro del canvas, radio 12

            fichasCanvas.add(fichaCanvas);
            rootPane.getChildren().add(fichaCanvas);
        }
    }

    /**
     * Actualiza la interfaz para mostrar información del jugador actual.
     */
    private void actualizarInterfazJugador() {
        if (partida == null) return;

        Jugador jugadorActual = partida.getJugadorActual();
        if (jugadorActual != null) {
            jugadorEnTurnoLabel.setText("Jugador Actual: " + jugadorActual.getAlias());

            // Mostrar quesitos ganados
            StringBuilder quesitos = new StringBuilder("Quesitos: ");
            List<String> quesitosGanados = jugadorActual.getQuesitos();
            for (String quesito : quesitosGanados) {
                quesitos.append(quesito).append(" ✓ ");
            }

            // Actualizar algún label con los quesitos (si existe)
            // quesitosLabel.setText(quesitos.toString());
        }
    }

    /**
     * Actualiza la posición visual de un jugador específico.
     */
    private void actualizarPosicionJugador(Jugador jugador) {
        int indiceJugador = partida.getJugadores().indexOf(jugador);
        if (indiceJugador >= 0 && indiceJugador < fichasCanvas.size()) {
            Canvas fichaCanvas = fichasCanvas.get(indiceJugador);
            CasillaNode nuevaPosicion = jugador.getPosicionActual();

            // Calcular offset para múltiples jugadores
            double offsetX = (indiceJugador % 2) * 15 - 7.5;
            double offsetY = (indiceJugador / 2) * 15 - 7.5;

            fichaCanvas.setLayoutX(nuevaPosicion.getX() + offsetX - 15);
            fichaCanvas.setLayoutY(nuevaPosicion.getY() + offsetY - 15);

            // Redibujar la ficha (puede haber cambiado con nuevos segmentos)
            GraphicsContext gc = fichaCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, 30, 30);
            jugador.getFicha().dibujar(gc, 15, 15, 12);
        }
    }

    public void lanzarYMostrarMovimientos(int valorDado) {
        if (partida == null || partida.isPartidaTerminada()) {
            System.out.println("🚫 No hay partida activa");
            return;
        }

        Jugador jugadorActual = partida.getJugadorActual();
        if (jugadorActual == null) {
            System.out.println("🚫 No hay jugador activo");
            return;
        }

        this.ultimoValorDado = valorDado;
        this.puedeMover = true;

        // Limpiar resaltado anterior
        for (Rectangle rect : mapaNodoARect.values()) {
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
        }

        // Encontrar movimientos posibles desde la posición actual del jugador
        CasillaNode posicionActual = jugadorActual.getPosicionActual();
        movimientosPosibles = grafoTablero.encontrarDestinosConPasos(posicionActual, valorDado);

        System.out.println("🎯 Movimientos posibles para " + jugadorActual.getAlias() +
                " desde " + posicionActual.getId() + " con dado " + valorDado + ":");

        if (movimientosPosibles.isEmpty()) {
            System.out.println("   ❌ No hay movimientos posibles");
            puedeMover = false;
            return;
        }

        // Resaltar casillas de destino
        for (CasillaNode destino : movimientosPosibles) {
            Rectangle rect = mapaNodoARect.get(destino);
            if (rect != null) {
                rect.setStroke(Color.LIMEGREEN);
                rect.setStrokeWidth(4);
                System.out.println("   → " + destino.getId());
            } else {
                System.err.println("   ⚠️ No se encontró rectángulo para casilla: " + destino.getId());
            }
        }
    }

    @FXML
    private void onCasillaClick(MouseEvent event) {
        if (!puedeMover || partida == null || partida.isPartidaTerminada()) {
            System.out.println("🚫 No se puede mover en este momento");
            return;
        }

        Node node = event.getPickResult().getIntersectedNode();
        if (node instanceof Rectangle rect && rect.getId() != null) {
            String id = rect.getId();
            CasillaNode nodoDestino = mapaIDaNodo.get(id);

            if (nodoDestino != null && movimientosPosibles.contains(nodoDestino)) {
                Jugador jugadorActual = partida.getJugadorActual();

                // Realizar movimiento
                partida.moverJugador(jugadorActual, nodoDestino);
                actualizarPosicionJugador(jugadorActual);

                // Limpiar estado
                movimientosPosibles.clear();
                puedeMover = false;

                // Quitar resaltado
                for (Rectangle r : mapaNodoARect.values()) {
                    r.setStroke(Color.BLACK);
                    r.setStrokeWidth(1);
                }

                System.out.println("✅ " + jugadorActual.getAlias() + " se movió a: " + nodoDestino.getId());

                // Simular ganar quesito en casillas principales (para demostración)
                simularGanarQuesito(jugadorActual, nodoDestino);

                // Pasar turno automáticamente (en un juego real esto sería después de responder pregunta)
                partida.pasarTurno();
                actualizarInterfazJugador();

            } else {
                System.out.println("❌ Movimiento inválido a: " + id);
            }
        }
    }

    /**
     * Simula ganar un quesito cuando se llega a ciertas casillas (para demostración).
     */
    private void simularGanarQuesito(Jugador jugador, CasillaNode casilla) {
        // Mapeo de casillas principales a categorías (para demostración)
        Map<String, String> casillasACategorias = Map.of(
                "1", "Geografia",
                "2", "Historia",
                "3", "Deportes",
                "4", "Ciencias",
                "5", "Arte",
                "6", "Entretenimiento"
        );

        String categoria = casillasACategorias.get(casilla.getId());
        if (categoria != null && !jugador.tieneQuesito(categoria)) {
            // Simular respuesta correcta (50% de probabilidad)
            if (Math.random() > 0.5) {
                partida.otorgarCategoria(jugador, categoria);
                actualizarPosicionJugador(jugador); // Redibujar ficha con nuevo segmento

                if (jugador.haGanadoTodosLosQuesitos()) {
                    System.out.println("🎊 " + jugador.getAlias() + " ha ganado todos los quesitos! Debe llegar al centro para ganar.");
                }
            }
        }
    }

    @FXML
    private void handleFinalizarPartida(ActionEvent event) {
        if (partida != null) {
            partida.terminarPartida("Finalizada por el usuario");
        }
        System.out.println("Partida finalizada.");
    }

    @FXML
    private void handleRendicion(ActionEvent event) {
        if (partida != null && !partida.isPartidaTerminada()) {
            Jugador jugadorActual = partida.getJugadorActual();
            if (jugadorActual != null) {
                System.out.println("🏳️ " + jugadorActual.getAlias() + " se ha rendido.");
                partida.pasarTurno();
                actualizarInterfazJugador();
            }
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/triviaucab1/MenuPrincipalView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TRIVIA UCAB - Menú Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSalir(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void conectarCasillasCorrectamente() {
        // ===== ANILLO EXTERIOR (sentido horario) =====
        // Lado superior: 1 → 7 → 8 → 9 → 10 → 11 → 2
        grafoTablero.agregarArista("1", "7");
        grafoTablero.agregarArista("7", "8");
        grafoTablero.agregarArista("8", "9");
        grafoTablero.agregarArista("9", "10");
        grafoTablero.agregarArista("10", "11");
        grafoTablero.agregarArista("11", "2");

        // Lado derecho: 2 → 12 → 13 → 14 → 15 → 16 → 3
        grafoTablero.agregarArista("2", "12");
        grafoTablero.agregarArista("12", "13");
        grafoTablero.agregarArista("13", "14");
        grafoTablero.agregarArista("14", "15");
        grafoTablero.agregarArista("15", "16");
        grafoTablero.agregarArista("16", "3");

        // Lado inferior: 3 → 17 → 18 → 19 → 20 → 21 → 4
        grafoTablero.agregarArista("3", "17");
        grafoTablero.agregarArista("17", "18");
        grafoTablero.agregarArista("18", "19");
        grafoTablero.agregarArista("19", "20");
        grafoTablero.agregarArista("20", "21");
        grafoTablero.agregarArista("21", "4");

        // Lado inferior izquierdo: 4 → 22 → 23 → 24 → 25 → 26 → 5
        grafoTablero.agregarArista("4", "22");
        grafoTablero.agregarArista("22", "23");
        grafoTablero.agregarArista("23", "24");
        grafoTablero.agregarArista("24", "25");
        grafoTablero.agregarArista("25", "26");
        grafoTablero.agregarArista("26", "5");

        // Lado izquierdo: 5 → 27 → 28 → 29 → 30_path → 31 → 6
        grafoTablero.agregarArista("5", "27");
        grafoTablero.agregarArista("27", "28");
        grafoTablero.agregarArista("28", "29");
        grafoTablero.agregarArista("29", "30_path");
        grafoTablero.agregarArista("30_path", "31");
        grafoTablero.agregarArista("31", "6");

        // Lado superior izquierdo: 6 → 32 → 33 → 34 → 35 → 36 → 1
        grafoTablero.agregarArista("6", "32");
        grafoTablero.agregarArista("32", "33");
        grafoTablero.agregarArista("33", "34");
        grafoTablero.agregarArista("34", "35");
        grafoTablero.agregarArista("35", "36");
        grafoTablero.agregarArista("36", "1");

        // ===== CAMINOS RADIALES HACIA EL CENTRO =====

        // Desde casilla 1 (azul) hacia el centro
        grafoTablero.agregarArista("1", "37");
        grafoTablero.agregarArista("37", "38");
        grafoTablero.agregarArista("38", "39");
        grafoTablero.agregarArista("39", "40");
        grafoTablero.agregarArista("40", "41");
        grafoTablero.agregarArista("41", "c");

        // Desde casilla 2 (amarillo) hacia el centro
        grafoTablero.agregarArista("2", "47");
        grafoTablero.agregarArista("47", "48");
        grafoTablero.agregarArista("48", "49");
        grafoTablero.agregarArista("49", "50");
        grafoTablero.agregarArista("50", "51");
        grafoTablero.agregarArista("51", "c");

        // Desde casilla 3 (naranja) hacia el centro
        grafoTablero.agregarArista("3", "57");
        grafoTablero.agregarArista("57", "58");
        grafoTablero.agregarArista("58", "59");
        grafoTablero.agregarArista("59", "60");
        grafoTablero.agregarArista("60", "61");
        grafoTablero.agregarArista("61", "c");

        // Desde casilla 4 (verde) hacia el centro
        grafoTablero.agregarArista("4", "46");
        grafoTablero.agregarArista("46", "45");
        grafoTablero.agregarArista("45", "44");
        grafoTablero.agregarArista("44", "43");
        grafoTablero.agregarArista("43", "42");
        grafoTablero.agregarArista("42", "c");

        // Desde casilla 5 (morado) hacia el centro
        grafoTablero.agregarArista("5", "56");
        grafoTablero.agregarArista("56", "55");
        grafoTablero.agregarArista("55", "54");
        grafoTablero.agregarArista("54", "53");
        grafoTablero.agregarArista("53", "52");
        grafoTablero.agregarArista("52", "c");

        // Desde casilla 6 (rosa) hacia el centro
        grafoTablero.agregarArista("6", "66");
        grafoTablero.agregarArista("66", "65");
        grafoTablero.agregarArista("65", "64");
        grafoTablero.agregarArista("64", "63");
        grafoTablero.agregarArista("63", "62");
        grafoTablero.agregarArista("62", "c");

        System.out.println("✅ Conexiones del tablero configuradas correctamente");
    }

    /**
     * Método para verificar que las conexiones están correctas
     */
    private void verificarConexiones() {
        System.out.println("🔍 Verificando conexiones del tablero:");

        // Verificar conexiones desde el centro
        CasillaNode centro = mapaIDaNodo.get("c");
        if (centro != null) {
            List<CasillaNode> vecinosCentro = grafoTablero.getVecinos(centro);
            System.out.print("   Centro conectado a: ");
            for (CasillaNode vecino : vecinosCentro) {
                System.out.print(vecino.getId() + " ");
            }
            System.out.println();
        }

        // Verificar algunas conexiones clave del anillo exterior
        String[] secuenciaAnillo = {"1", "7", "8", "9", "10", "11", "2", "12", "13", "14", "15", "16", "3"};
        System.out.println("   Verificando secuencia del anillo exterior:");
        for (int i = 0; i < secuenciaAnillo.length - 1; i++) {
            CasillaNode actual = mapaIDaNodo.get(secuenciaAnillo[i]);
            CasillaNode siguiente = mapaIDaNodo.get(secuenciaAnillo[i + 1]);
            if (actual != null && siguiente != null) {
                List<CasillaNode> vecinos = grafoTablero.getVecinos(actual);
                boolean conectado = vecinos.contains(siguiente);
                System.out.println("     " + secuenciaAnillo[i] + " → " + secuenciaAnillo[i + 1] + ": " + (conectado ? "✅" : "❌"));
            }
        }

        // Verificar caminos radiales
        String[] casillasRadiales = {"1", "2", "3", "4", "5", "6"};
        System.out.println("   Verificando caminos radiales hacia el centro:");
        for (String casilla : casillasRadiales) {
            CasillaNode nodo = mapaIDaNodo.get(casilla);
            if (nodo != null) {
                List<CasillaNode> destinos = grafoTablero.encontrarDestinosConPasos(nodo, 6);
                boolean puedeAlcanzarCentro = destinos.stream().anyMatch(d -> d.getId().equals("c"));
                System.out.println("     Desde casilla " + casilla + " puede alcanzar centro en 6 pasos: " + (puedeAlcanzarCentro ? "✅" : "❌"));
            }
        }
    }

    /**
     * Método de utilidad para crear una partida de prueba.
     */
    public void crearPartidaPrueba() {
        partida = new Partida();

        // Crear jugadores de prueba
        CasillaNode centro = mapaIDaNodo.get("c");
        Jugador jugador1 = new Jugador("Jugador 1", Color.RED, centro);
        Jugador jugador2 = new Jugador("Jugador 2", Color.BLUE, centro);

        partida.agregarJugador(jugador1);
        partida.agregarJugador(jugador2);
        partida.iniciarPartida();

        // Configurar la partida en el controlador
        setPartida(partida);

        System.out.println("🎮 Partida de prueba creada con 2 jugadores");
    }
}


