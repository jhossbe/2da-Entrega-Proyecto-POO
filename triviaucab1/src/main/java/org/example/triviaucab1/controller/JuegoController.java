package org.example.triviaucab1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
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
    private Circle fichaJugador;
    private List<CasillaNode> movimientosPosibles = new ArrayList<>();
    private Map<String, CasillaNode> mapaIDaNodo = new HashMap<>();
    private Map<CasillaNode, Rectangle> mapaNodoARect = new HashMap<>();
    private CasillaNode casillaActual;
    private Partida partida;

    @FXML
    public void initialize() {
        grafoTablero = new GrafoTablero();

        for (Node node : rootPane.getChildren()) {
            if (node instanceof Rectangle rect && rect.getId() != null) {
                String id = rect.getId();
                if (id.equals("c") || id.equals("30_path") || id.matches("\\d+")) {
                    double x = rect.getLayoutX() + rect.getWidth() / 2;
                    double y = rect.getLayoutY() + rect.getHeight() / 2;
                    CasillaNode nodo = new CasillaNode(id, x, y);
                    grafoTablero.agregarNodo(nodo);
                    mapaIDaNodo.put(id, nodo);
                    mapaNodoARect.put(nodo, rect);
                }
            }
        }

        conectarCasillas();

        casillaActual = mapaIDaNodo.get("c");
        fichaJugador = new Circle(10, Color.DODGERBLUE);
        fichaJugador.setStroke(Color.WHITE);
        fichaJugador.setStrokeWidth(2);
        fichaJugador.setLayoutX(casillaActual.getX());
        fichaJugador.setLayoutY(casillaActual.getY());
        rootPane.getChildren().add(fichaJugador);

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
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
        System.out.println("Partida recibida con " + partida.getJugadores().size() + " jugadores.");
    }

    public void lanzarYMostrarMovimientos(int valorDado) {
        this.ultimoValorDado = valorDado;
        this.puedeMover = true; // se habilita el movimiento

        movimientosPosibles = grafoTablero.encontrarDestinosConDFS(casillaActual, valorDado);

        for (Rectangle rect : mapaNodoARect.values()) {
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
        }

        System.out.println("🎯 Movimientos posibles desde " + casillaActual.getId() + " con dado " + valorDado + ":");
        for (CasillaNode destino : movimientosPosibles) {
            Rectangle rect = mapaNodoARect.get(destino);
            if (rect != null) {
                rect.setStroke(Color.LIMEGREEN);
                rect.setStrokeWidth(4);
                System.out.println(" → " + destino.getId());
            }
        }
    }

    @FXML
    private void onCasillaClick(MouseEvent event) {
        if (!puedeMover) return; // solo puede moverse si ha tirado el dado

        Node node = event.getPickResult().getIntersectedNode();
        if (node instanceof Rectangle rect && rect.getId() != null) {
            String id = rect.getId();
            CasillaNode nodoDestino = mapaIDaNodo.get(id);
            if (nodoDestino != null && movimientosPosibles.contains(nodoDestino)) {
                casillaActual = nodoDestino;
                fichaJugador.setLayoutX(nodoDestino.getX());
                fichaJugador.setLayoutY(nodoDestino.getY());

                movimientosPosibles.clear();
                puedeMover = false; // ya se movió, debe volver a tirar dado

                // Limpiar resaltado
                for (Rectangle r : mapaNodoARect.values()) {
                    r.setStroke(Color.BLACK);
                    r.setStrokeWidth(1);
                }

                System.out.println("✅ Movimiento realizado a: " + nodoDestino.getId());
            }
        }
    }

    @FXML
    private void handleFinalizarPartida(ActionEvent event) {
        System.out.println("Botón 'Finalizar Partida' presionado.");
    }

    @FXML
    private void handleRendicion(ActionEvent event) {
        System.out.println("Botón 'Rendición' presionado.");
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

    private void conectarCasillas() {
        grafoTablero.agregarArista("1", "36");
        grafoTablero.agregarArista("1", "37");
        grafoTablero.agregarArista("1", "7");

        grafoTablero.agregarArista("2", "11");
        grafoTablero.agregarArista("2", "12");

        grafoTablero.agregarArista("3", "15");
        grafoTablero.agregarArista("3", "16");

        grafoTablero.agregarArista("4", "21");
        grafoTablero.agregarArista("4", "22");

        grafoTablero.agregarArista("5", "26");
        grafoTablero.agregarArista("5", "27");

        grafoTablero.agregarArista("6", "31");
        grafoTablero.agregarArista("6", "32");

        grafoTablero.agregarArista("7", "1");
        grafoTablero.agregarArista("7", "36");

        grafoTablero.agregarArista("8", "7");
        grafoTablero.agregarArista("8", "9");

        grafoTablero.agregarArista("9", "8");
        grafoTablero.agregarArista("9", "10");

        grafoTablero.agregarArista("10", "9");
        grafoTablero.agregarArista("10", "11");

        grafoTablero.agregarArista("11", "2");
        grafoTablero.agregarArista("11", "10");

        grafoTablero.agregarArista("12", "2");
        grafoTablero.agregarArista("12", "13");

        grafoTablero.agregarArista("13", "12");
        grafoTablero.agregarArista("13", "14");

        grafoTablero.agregarArista("14", "13");
        grafoTablero.agregarArista("14", "15");

        grafoTablero.agregarArista("15", "14");
        grafoTablero.agregarArista("15", "16");

        grafoTablero.agregarArista("16", "15");
        grafoTablero.agregarArista("16", "17");

        grafoTablero.agregarArista("17", "16");
        grafoTablero.agregarArista("17", "18");

        grafoTablero.agregarArista("18", "17");
        grafoTablero.agregarArista("18", "19");

        grafoTablero.agregarArista("19", "18");
        grafoTablero.agregarArista("19", "20");

        grafoTablero.agregarArista("20", "19");
        grafoTablero.agregarArista("20", "21");

        grafoTablero.agregarArista("21", "20");
        grafoTablero.agregarArista("21", "4");

        grafoTablero.agregarArista("22", "4");
        grafoTablero.agregarArista("22", "23");

        grafoTablero.agregarArista("23", "22");
        grafoTablero.agregarArista("23", "24");

        grafoTablero.agregarArista("24", "23");
        grafoTablero.agregarArista("24", "25");

        grafoTablero.agregarArista("25", "24");
        grafoTablero.agregarArista("25", "26");

        grafoTablero.agregarArista("26", "25");
        grafoTablero.agregarArista("26", "5");

        grafoTablero.agregarArista("27", "5");
        grafoTablero.agregarArista("27", "28");

        grafoTablero.agregarArista("28", "27");
        grafoTablero.agregarArista("28", "29");

        grafoTablero.agregarArista("29", "28");
        grafoTablero.agregarArista("29", "30_path");

        grafoTablero.agregarArista("30_path", "29");
        grafoTablero.agregarArista("30_path", "31");

        grafoTablero.agregarArista("31", "30_path");
        grafoTablero.agregarArista("31", "6");

        grafoTablero.agregarArista("32", "6");
        grafoTablero.agregarArista("32", "33");

        grafoTablero.agregarArista("33", "32");
        grafoTablero.agregarArista("33", "34");

        grafoTablero.agregarArista("34", "33");
        grafoTablero.agregarArista("34", "35");

        grafoTablero.agregarArista("35", "34");
        grafoTablero.agregarArista("35", "36");

        grafoTablero.agregarArista("36", "35");
        grafoTablero.agregarArista("36", "7");

        grafoTablero.agregarArista("37", "1");
        grafoTablero.agregarArista("37", "38");

        grafoTablero.agregarArista("38", "37");
        grafoTablero.agregarArista("38", "39");

        grafoTablero.agregarArista("39", "38");
        grafoTablero.agregarArista("39", "40");

        grafoTablero.agregarArista("40", "39");
        grafoTablero.agregarArista("40", "41");

        grafoTablero.agregarArista("41", "40");
        grafoTablero.agregarArista("41", "62");

        grafoTablero.agregarArista("42", "43");
        grafoTablero.agregarArista("43", "42");
        grafoTablero.agregarArista("43", "44");
        grafoTablero.agregarArista("44", "43");
        grafoTablero.agregarArista("44", "45");
        grafoTablero.agregarArista("45", "44");
        grafoTablero.agregarArista("45", "46");
        grafoTablero.agregarArista("46", "45");
        grafoTablero.agregarArista("46", "4");

        grafoTablero.agregarArista("47", "2");
        grafoTablero.agregarArista("47", "48");

        grafoTablero.agregarArista("48", "47");
        grafoTablero.agregarArista("48", "49");

        grafoTablero.agregarArista("49", "48");
        grafoTablero.agregarArista("49", "50");

        grafoTablero.agregarArista("50", "49");
        grafoTablero.agregarArista("50", "51");

        grafoTablero.agregarArista("51", "50");
        grafoTablero.agregarArista("51", "61");

        grafoTablero.agregarArista("52", "53");
        grafoTablero.agregarArista("53", "52");
        grafoTablero.agregarArista("53", "54");
        grafoTablero.agregarArista("54", "53");
        grafoTablero.agregarArista("54", "55");
        grafoTablero.agregarArista("55", "54");
        grafoTablero.agregarArista("55", "56");
        grafoTablero.agregarArista("56", "55");
        grafoTablero.agregarArista("56", "27");

        grafoTablero.agregarArista("57", "3");
        grafoTablero.agregarArista("57", "58");

        grafoTablero.agregarArista("58", "57");
        grafoTablero.agregarArista("58", "59");

        grafoTablero.agregarArista("59", "58");
        grafoTablero.agregarArista("59", "60");

        grafoTablero.agregarArista("60", "59");
        grafoTablero.agregarArista("60", "61");

        grafoTablero.agregarArista("61", "60");
        grafoTablero.agregarArista("61", "51");

        grafoTablero.agregarArista("62", "41");
        grafoTablero.agregarArista("62", "63");

        grafoTablero.agregarArista("63", "62");
        grafoTablero.agregarArista("63", "64");

        grafoTablero.agregarArista("64", "63");
        grafoTablero.agregarArista("64", "65");

        grafoTablero.agregarArista("65", "64");
        grafoTablero.agregarArista("65", "66");

        grafoTablero.agregarArista("66", "65");
        grafoTablero.agregarArista("66", "6");

        grafoTablero.agregarArista("c", "1");
        grafoTablero.agregarArista("c", "3");
        grafoTablero.agregarArista("c", "5");
        grafoTablero.agregarArista("c", "27");
        grafoTablero.agregarArista("c", "42");
        grafoTablero.agregarArista("c", "39");
        grafoTablero.agregarArista("c", "14");
        grafoTablero.agregarArista("c", "47");
    }
}


