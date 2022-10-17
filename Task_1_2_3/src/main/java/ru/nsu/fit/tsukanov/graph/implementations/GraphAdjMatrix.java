package ru.nsu.fit.tsukanov.graph.implementations;

import java.util.*;
import ru.nsu.fit.tsukanov.graph.core.EdgeDefault;
import ru.nsu.fit.tsukanov.graph.core.Graph;



/**
 * Oriented weighted graph. There is class for edge.
 * It uses adjacency matrix for method implementation.
 * Big-O notations:
 * Get:
 * V-V O(1+ev)
 * V-* O(V+ev)
 * *-* O(V^2 + E)
 * Contains:
 * V O(1) - because there is vertex hashmap
 * E O(1) - because edge contains source and target vertex
 * Add/Remove:
 * V O(N)
 * E O(ev)
 * Memory:
 * O(N^2+E)
 * ev - vertex's edges
 * So it's good for static, but bad for dynamic.
 *
 * @param <V> object, that will be vertex.
 * @param <E> object, that contained in Edge.
 * @see EdgeDefault
 */
public class GraphAdjMatrix<V, E> implements Graph<V, E> {
    private final Stack<Integer> indexesStack;
    private final HashMap<V, Integer> vertexMap;
    private final ArrayList<ArrayList<ArrayList<EdgeDefault<V, E>>>> matrix;

    /**
     * Creates stack for indexing,
     * map for mapping V object to integer
     * and adjacency matrix.
     */
    public GraphAdjMatrix() {
        this.indexesStack = new Stack<>();
        this.vertexMap = new HashMap<>();
        this.matrix = new ArrayList<>();
    }

    private ArrayList<EdgeDefault<V, E>> getListEdges(V sourceVertex, V targetVertex) {
        if (!containsVertex(sourceVertex)
                || !containsVertex(targetVertex)) {
            return null;
        }
        int i = vertexMap.get(sourceVertex);
        int k = vertexMap.get(targetVertex);
        return matrix.get(i).get(k);
    }

    /**
     * Return all edges, that connects two vertices.
     * Edges is directed from sourceVertex to targetVertex.
     *
     * @param sourceVertex start vertex
     * @param targetVertex end vertex
     * @return set of all edges, that connects vertices
     */
    @Override
    public Set<EdgeDefault<V, E>> getAllEdges(V sourceVertex, V targetVertex) {
        var list = getListEdges(sourceVertex, targetVertex);
        if (list == null) {
            return null;
        }
        return new HashSet<>(list);
    }

    /**
     * Return edge, that connects two vertices.
     * Edge is directed from sourceVertex to targetVertex.
     *
     * @param sourceVertex start vertex
     * @param targetVertex end vertex
     * @return edge connects vertices
     */
    @Override
    public EdgeDefault<V, E> getEdge(V sourceVertex, V targetVertex) {
        if (sourceVertex == null || targetVertex == null) {
            return null;
        }
        ArrayList<EdgeDefault<V, E>> l = getListEdges(sourceVertex, targetVertex);
        if (l == null) {
            return null;
        }
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    /**
     * Add edge, that connects vertices in direction from sourceVertex to targetVertex.
     * Edge has an object.
     *
     * @param sourceVertex is start vertex for edge
     * @param targetVertex is end vertex for edge
     * @param object       is object that will be placed into edge
     * @return new Edge
     */
    @Override
    public EdgeDefault<V, E> addEdge(V sourceVertex, V targetVertex, E object) {
        return addEdge(sourceVertex, targetVertex, object, EdgeDefault.DEFAULT_WEIGHT);
    }

    /**
     * Add specify edge, if there is no such edge.
     *
     * @param e is edge that will be added to graph
     * @return true, if there are no equal Edge.
     */
    @Override
    public boolean addEdge(EdgeDefault<V, E> e) {
        if (e == null) {
            return false;
        }
        if (containsEdge(e)) {
            return false;
        }
        var list = getListEdges(e.getSourceVertex(), e.getTargetVertex());
        if (list == null) {
            return false;
        }
        return list.add(e);
    }


    /**
     * Create Edge with specify vertices, weight and object.
     *
     * @param sourceVertex is start vertex for edge
     * @param targetVertex is end vertex for edge
     * @param e            is object that will be placed into edge
     * @param weight       is weight that will be placed into edge
     * @return edge
     */
    @Override
    public EdgeDefault<V, E> addEdge(V sourceVertex, V targetVertex, E e, double weight) {
        EdgeDefault<V, E> edgeDefault = new EdgeDefault<>(sourceVertex, targetVertex, e, weight);
        var list = getListEdges(sourceVertex, targetVertex);
        if (list == null) {
            return null;
        }
        list.add(edgeDefault);
        return edgeDefault;
    }

    /**
     * Add vertex to graph, if there is no such vertex.
     * If it isn't, then return false.
     *
     * @param v is vertex value, or vertex object.
     * @return true, if vertex has been added.
     */
    @Override
    public boolean addVertex(V v) {
        if (v == null) {
            return false;
        }
        if (indexesStack.isEmpty()) {
            indexesStack.push(vertexMap.size());
        }
        if (containsVertex(v)) {
            return false;
        }
        Integer index = indexesStack.pop();
        vertexMap.put(v, index);
        if (index >= matrix.size()) {
            int newSize = matrix.size() + 1;
            ArrayList<ArrayList<EdgeDefault<V, E>>> newCol = new ArrayList<>();
            for (int i = 0; i < newSize; i++) {
                newCol.add(new ArrayList<>());
            }
            matrix.add(newCol);
            for (int i = 0; i < index; i++) {
                matrix.get(i).add(new ArrayList<>());
            }
        }
        return true;
    }

    /**
     * Check for existing edge between two vertices.
     *
     * @param sourceVertex is start vertex for some edge
     * @param targetVertex is end vertex for some edge
     * @return true, if there is edge from source to target.
     */
    @Override
    public boolean containsEdge(V sourceVertex, V targetVertex) {
        var list = getListEdges(sourceVertex, targetVertex);
        if (list == null) {
            return false;
        }
        return !list.isEmpty();
    }

    /**
     * Check for exising edge in the graph.
     *
     * @param e is edge for checking
     * @return true, if the graph contains specify edge
     */
    @Override
    public boolean containsEdge(EdgeDefault<V, E> e) {
        if (e == null) {
            return false;
        }
        if (!containsVertex(e.getEdgeSource())
                || !containsVertex(e.getTargetVertex())) {
            return false;
        }

        return matrix.get(vertexMap.get(e.getEdgeSource()))
                .get(vertexMap.get(e.getEdgeTarget()))
                .contains(e);
    }

    /**
     * Check for exising vertex in the graph.
     *
     * @param v is vertex object
     * @return true, if the graph contains specify vertex.
     */
    @Override
    public boolean containsVertex(V v) {
        if (v == null) {
            return false;
        }
        return vertexMap.containsKey(v);
    }

    /**
     * Return set of all edges from graph.
     *
     * @return set of all edges from this graph.
     */
    @Override
    public Set<EdgeDefault<V, E>> edgeSet() {
        Set<EdgeDefault<V, E>> edgeSet = new HashSet<>();
        for (V v : vertexMap.keySet()) {
            edgeSet.addAll(outgoingEdgesOf(v));
        }
        return edgeSet;
    }

    /**
     * Return set of all incoming edges to specify vertex.
     *
     * @param vertex is vertex
     * @return set of all incoming edges to specify vertex.
     */
    @Override
    public Set<EdgeDefault<V, E>> incomingEdgesOf(V vertex) {
        if (!containsVertex(vertex)) {
            return null;
        }
        int index = vertexMap.get(vertex);
        Set<EdgeDefault<V, E>> edgeDefaultSet = new HashSet<>();
        for (ArrayList<ArrayList<EdgeDefault<V, E>>> list : matrix) {
            edgeDefaultSet.addAll(list.get(index));
        }
        return edgeDefaultSet;
    }

    /**
     * Return set of all incoming edges to specify vertex.
     *
     * @param vertex is vertex
     * @return set of all incoming edges to specify vertex.
     */
    @Override
    public Set<EdgeDefault<V, E>> outgoingEdgesOf(V vertex) {
        if (!containsVertex(vertex)) {
            return null;
        }
        int index = vertexMap.get(vertex);
        ArrayList<ArrayList<EdgeDefault<V, E>>> arrayLists = matrix.get(index);
        Set<EdgeDefault<V, E>> edgeDefaultSet = new HashSet<>();
        for (ArrayList<EdgeDefault<V, E>> arrayList : arrayLists) {
            edgeDefaultSet.addAll(arrayList);
        }

        return edgeDefaultSet;
    }

    /**
     * Remove edge with null object from graph between two specify vertices.
     *
     * @param sourceVertex is source vertex maybe for some edge.
     * @param targetVertex is target vertex maybe for some edge.
     * @return removed edge.
     */
    @Override
    public EdgeDefault<V, E> removeEdge(V sourceVertex, V targetVertex) {
        if (sourceVertex == null || targetVertex == null) {
            return null;
        }
        if (!containsVertex(sourceVertex) || !containsVertex(targetVertex)) {
            return null;
        }
        EdgeDefault<V, E> edge = getEdge(sourceVertex, targetVertex);
        return removeEdge(edge) ? edge : null;
    }

    /**
     * Remove specify edge from graph. And check for graph changes.
     *
     * @param e is edge
     * @return true, if there was removing edge from graph.
     */

    @Override
    public boolean removeEdge(EdgeDefault<V, E> e) {
        if (e == null) {
            return false;
        }
        if (!(containsVertex(e.getSourceVertex())
                && containsVertex(e.getTargetVertex()))) {
            return false;
        }
        int i = vertexMap.get(e.getEdgeSource());
        int k = vertexMap.get(e.getTargetVertex());
        return matrix.get(i).get(k).remove(e);
    }

    /**
     * Remove specify vertex from graph. And check for graph changes.
     *
     * @param v is vertex object
     * @return true, if there was removing vertex from graph.
     */
    @Override
    public boolean removeVertex(V v) {
        if (!containsVertex(v)) {
            return false;
        }
        int index = vertexMap.get(v);
        ArrayList<ArrayList<EdgeDefault<V, E>>> arrayLists = matrix.get(index);
        for (ArrayList<EdgeDefault<V, E>> list : arrayLists) {
            list.clear();
        }
        for (ArrayList<ArrayList<EdgeDefault<V, E>>> lists : matrix) {
            lists.get(index).clear();
        }
        vertexMap.remove(v);
        indexesStack.push(index);
        return true;
    }

    /**
     * Return set of all vertices from graph.
     *
     * @return set of all vertices from this graph
     */
    @Override
    public Set<V> vertexSet() {
        return vertexMap.keySet();
    }
}
