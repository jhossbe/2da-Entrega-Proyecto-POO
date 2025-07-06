package org.example.triviaucab1.module.tablero;

import java.util.*;

public class GrafoTablero {
    private final Map<String, CasillaNode> nodos = new HashMap<>();
    private final Map<CasillaNode, Set<CasillaNode>> adyacentes = new HashMap<>();

    public void agregarNodo(CasillaNode nodo) {
        nodos.put(nodo.getId(), nodo);
        if (!adyacentes.containsKey(nodo)) {
            adyacentes.put(nodo, new HashSet<>());
        }
    }

    public void agregarArista(String id1, String id2) {
        CasillaNode nodo1 = nodos.get(id1);
        CasillaNode nodo2 = nodos.get(id2);
        if (nodo1 == null || nodo2 == null) return;

        adyacentes.computeIfAbsent(nodo1, k -> new HashSet<>()).add(nodo2);
        adyacentes.computeIfAbsent(nodo2, k -> new HashSet<>()).add(nodo1);
    }

    public List<CasillaNode> getVecinos(CasillaNode nodo) {
        return new ArrayList<>(adyacentes.getOrDefault(nodo, Collections.emptySet()));
    }

    public CasillaNode getNodo(String id) {
        return nodos.get(id);
    }

    public Set<String> getIdsNodos() {
        return nodos.keySet();
    }

    public List<CasillaNode> encontrarDestinosConDFS(CasillaNode inicio, int pasos) {
        Set<CasillaNode> resultados = new HashSet<>();
        Set<CasillaNode> visitados = new HashSet<>();
        visitados.add(inicio);
        dfsExacto(inicio, pasos, visitados, resultados);
        return new ArrayList<>(resultados);
    }

    private void dfsExacto(CasillaNode actual, int pasosRestantes, Set<CasillaNode> visitados, Set<CasillaNode> resultados) {
        if (pasosRestantes == 0) {
            resultados.add(actual);
            return;
        }

        for (CasillaNode vecino : getVecinos(actual)) {
            if (visitados.add(vecino)) {
                dfsExacto(vecino, pasosRestantes - 1, visitados, resultados);
                visitados.remove(vecino); // backtrack solo si fue agregado
            }
        }
    }

    public List<CasillaNode> encontrarDestinosConPasos(CasillaNode inicio, int pasos) {
        Set<CasillaNode> resultados = new HashSet<>();
        LinkedHashSet<CasillaNode> camino = new LinkedHashSet<>();
        camino.add(inicio);
        buscarRecursivo(inicio, pasos, camino, resultados);
        return new ArrayList<>(resultados);
    }

    private void buscarRecursivo(CasillaNode actual,
                                 int pasosRestantes,
                                 LinkedHashSet<CasillaNode> camino,
                                 Set<CasillaNode> resultados) {
        if (pasosRestantes == 0) {
            resultados.add(actual);
            return;
        }

        Set<CasillaNode> vecinos = adyacentes.getOrDefault(actual, Collections.emptySet());
        for (CasillaNode vecino : vecinos) {
            if (!camino.contains(vecino)) {
                camino.add(vecino);
                buscarRecursivo(vecino, pasosRestantes - 1, camino, resultados);
                camino.remove(vecino); // backtracking
            }
        }
    }

}



