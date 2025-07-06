package org.example.triviaucab1.module.tablero;

import java.util.*;

public class GrafoTablero {

    private final Map<String, CasillaNode> nodos = new HashMap<>();
    private final Map<CasillaNode, List<CasillaNode>> adyacentes = new HashMap<>();

    public void agregarNodo(CasillaNode nodo) {
        if (!nodos.containsKey(nodo.getId())) {
            nodos.put(nodo.getId(), nodo);
            adyacentes.put(nodo, new ArrayList<>());
        }
    }

    public void agregarArista(String id1, String id2) {
        CasillaNode nodo1 = nodos.get(id1);
        CasillaNode nodo2 = nodos.get(id2);
        if (nodo1 == null || nodo2 == null) {
            System.err.println("❌ Error conectando: " + id1 + " ↔ " + id2);
            return;
        }
        adyacentes.get(nodo1).add(nodo2);
        adyacentes.get(nodo2).add(nodo1);
    }

    public List<CasillaNode> getVecinos(CasillaNode nodo) {
        return adyacentes.getOrDefault(nodo, Collections.emptyList());
    }

    public CasillaNode getNodo(String id) {
        return nodos.get(id);
    }

    public Set<String> getIdsNodos() {
        return nodos.keySet();
    }

    public List<CasillaNode> encontrarDestinosConDFS(CasillaNode inicio, int pasos) {
        Set<CasillaNode> resultados = new HashSet<>();
        dfsExacto(inicio, pasos, new HashSet<>(), resultados);
        return new ArrayList<>(resultados);
    }

    private void dfsExacto(CasillaNode actual, int pasosRestantes, Set<CasillaNode> visitados, Set<CasillaNode> resultados) {
        if (pasosRestantes == 0) {
            resultados.add(actual);
            return;
        }

        visitados.add(actual);
        for (CasillaNode vecino : getVecinos(actual)) {
            if (!visitados.contains(vecino)) {
                dfsExacto(vecino, pasosRestantes - 1, visitados, resultados);
            }
        }
        visitados.remove(actual); // backtracking
    }
}



