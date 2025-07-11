package org.example.triviaucab1.module.tablero;

import java.util.*;

/**
 * Representa el tablero del juego como un grafo no dirigido.
 * Este grafo permite gestionar las conexiones entre las casillas (nodos)
 * y encontrar rutas o destinos posibles para los jugadores.
 */
public class GrafoTablero {
    /**
     * Un mapa que almacena todas las casillas del tablero, indexadas por su ID único.
     * Permite un acceso rápido a un {@link CasillaNode} dado su identificador.
     */
    private final Map<String, CasillaNode> nodos = new HashMap<>();
    /**
     * Un mapa de adyacencia que representa las conexiones entre las casillas.
     * Cada clave es un {@link CasillaNode} y su valor es un conjunto de {@link CasillaNode}
     * directamente adyacentes a él.
     */
    private final Map<CasillaNode, Set<CasillaNode>> adyacentes = new HashMap<>();

    /**
     * Agrega un nuevo nodo (casilla) al grafo del tablero.
     * Si el nodo ya existe, lo sobrescribe. Inicializa un conjunto vacío
     * de adyacencia para el nuevo nodo si aún no está presente.
     *
     * @param nodo La {@link CasillaNode} a agregar.
     */
    public void agregarNodo(CasillaNode nodo) {
        nodos.put(nodo.getId(), nodo);
        if (!adyacentes.containsKey(nodo)) {
            adyacentes.put(nodo, new HashSet<>());
        }
    }

    /**
     * Agrega una arista (conexión bidireccional) entre dos casillas en el grafo.
     * Si alguno de los nodos no existe en el grafo, la operación no se realiza.
     *
     * @param id1 El ID de la primera casilla.
     * @param id2 El ID de la segunda casilla.
     */
    public void agregarArista(String id1, String id2) {
        CasillaNode nodo1 = nodos.get(id1);
        CasillaNode nodo2 = nodos.get(id2);
        if (nodo1 == null || nodo2 == null) {

            System.err.println("Advertencia: No se pudo agregar arista entre " + id1 + " y " + id2 + ". Uno o ambos nodos no existen.");
            return;
        }

        adyacentes.computeIfAbsent(nodo1, k -> new HashSet<>()).add(nodo2);
        adyacentes.computeIfAbsent(nodo2, k -> new HashSet<>()).add(nodo1);
    }

    /**
     * Obtiene una lista de las casillas directamente adyacentes a un nodo dado.
     *
     * @param nodo El {@link CasillaNode} del cual se quieren obtener los vecinos.
     * @return Una {@link List} de {@link CasillaNode} que son vecinos directos.
     * Devuelve una lista vacía si el nodo no tiene vecinos o no existe en el grafo.
     */
    public List<CasillaNode> getVecinos(CasillaNode nodo) {
        return new ArrayList<>(adyacentes.getOrDefault(nodo, Collections.emptySet()));
    }

    /**
     * Obtiene un conjunto con los IDs de todos los nodos (casillas) presentes en el grafo.
     *
     * @return Un {@link Set} de {@link String} que representan los IDs de las casillas.
     */
    public Set<String> getIdsNodos() {
        return nodos.keySet();
    }

    /**
     * Encuentra todos los nodos que son exactamente 'pasos' de distancia desde un nodo de inicio,
     * utilizando una búsqueda en profundidad (DFS).
     *
     * @param inicio El {@link CasillaNode} desde el cual comenzar la búsqueda.
     * @param pasos El número exacto de pasos a recorrer.
     * @return Una {@link List} de {@link CasillaNode} que son destinos posibles.
     * Devuelve una lista vacía si no hay destinos válidos.
     * @deprecated Este método utiliza DFS para encontrar destinos, pero {@link #encontrarDestinosConPasos(CasillaNode, int)}
     * es preferible por su implementación más robusta para el tablero de juego.
     */
    @Deprecated
    public List<CasillaNode> encontrarDestinosConDFS(CasillaNode inicio, int pasos) {
        Set<CasillaNode> resultados = new HashSet<>();
        Set<CasillaNode> visitados = new HashSet<>();
        visitados.add(inicio);
        dfsExacto(inicio, pasos, visitados, resultados);
        return new ArrayList<>(resultados);
    }

    /**
     * Método auxiliar recursivo para la búsqueda en profundidad (DFS) que encuentra nodos
     * a una distancia exacta.
     *
     * @param actual El {@link CasillaNode} actual en la recursión.
     * @param pasosRestantes El número de pasos restantes por recorrer.
     * @param visitados Un {@link Set} de {@link CasillaNode} ya visitados en el camino actual para evitar ciclos.
     * @param resultados Un {@link Set} donde se acumulan los nodos encontrados a la distancia exacta.
     * @deprecated Este método es un auxiliar de {@link #encontrarDestinosConDFS(CasillaNode, int)}.
     */
    @Deprecated
    private void dfsExacto(CasillaNode actual, int pasosRestantes, Set<CasillaNode> visitados, Set<CasillaNode> resultados) {
        if (pasosRestantes == 0) {
            resultados.add(actual);
            return;
        }

        for (CasillaNode vecino : getVecinos(actual)) {
            if (visitados.add(vecino)) {
                dfsExacto(vecino, pasosRestantes - 1, visitados, resultados);
                visitados.remove(vecino);
            }
        }
    }

    /**
     * Encuentra todos los destinos posibles en el tablero a partir de un nodo inicial
     * y un número exacto de pasos, evitando ciclos en el camino.
     * Esta es la implementación recomendada para el movimiento de fichas en el tablero.
     *
     * @param inicio El {@link CasillaNode} desde el cual comenzar la búsqueda.
     * @param pasos El número exacto de pasos a recorrer desde el inicio.
     * @return Una {@link List} de {@link CasillaNode} que son destinos válidos a la distancia especificada.
     */
    public List<CasillaNode> encontrarDestinosConPasos(CasillaNode inicio, int pasos) {
        Set<CasillaNode> resultados = new HashSet<>();
        LinkedHashSet<CasillaNode> camino = new LinkedHashSet<>();
        camino.add(inicio);
        buscarRecursivo(inicio, pasos, camino, resultados);
        return new ArrayList<>(resultados);
    }

    /**
     * Método auxiliar recursivo para encontrar destinos a un número exacto de pasos.
     * Realiza una búsqueda similar a DFS, pero con un seguimiento de caminos para evitar
     * visitar el mismo nodo en el mismo camino recursivo.
     *
     * @param actual El {@link CasillaNode} actual en la recursión.
     * @param pasosRestantes El número de pasos que aún quedan por recorrer.
     * @param camino El {@link LinkedHashSet} que representa el camino actual desde el inicio hasta {@code actual}.
     * Se usa para evitar ciclos.
     * @param resultados El {@link Set} donde se acumulan los nodos que son destinos válidos.
     */
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
                camino.remove(vecino);
            }
        }
    }
}