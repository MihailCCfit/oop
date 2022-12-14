package ru.nsu.fit.tsukanov.graph.implementations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.nsu.fit.tsukanov.graph.core.EdgeDefault;
import ru.nsu.fit.tsukanov.graph.core.Graph;



/**
 * Oriented weighted graph. There is class for edge and vertex.
 * It uses incident matrix for method implementation.
 * Big-O notations:
 * Get:
 * V-V O(1+ev)
 * V-* O(ev)
 * *-* O(E)
 * Contains:
 * V O(1) - because there is treemap
 * E O(ev)
 * Add/Remove:
 * V O(ev)
 * E O(ev)
 * Memory:
 * O(N+E)
 * ev - vertex's edges
 * Good dynamic structure.
 *
 * @param <V> object, that will be vertex.
 * @param <E> object, that contained in Edge.
 * @see EdgeDefault
 * @see Graph
 * @see Vertex
 */
public class GraphIncList<V, E> implements Graph<V, E> {

    private final Map<V, Vertex<V, E>> vertexMap;

    /**
     * creates treeMap.
     */
    public GraphIncList() {
        vertexMap = new HashMap<>();
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
    public Set<EdgeDefault<V, E>> getEdges(V sourceVertex, V targetVertex) {
        if (sourceVertex == null
                || targetVertex == null) {
            return null;
        }
        if (!containsVertex(sourceVertex)
                || !containsVertex(targetVertex)) {
            return null;
        }
        Set<EdgeDefault<V, E>> edgeSet = new HashSet<>();
        for (EdgeDefault<V, E> edge : getVertex(sourceVertex).outEdge) {
            if (edge.getTargetVertex() == targetVertex) {
                edgeSet.add(edge);
            }
        }

        return edgeSet;
    }


    /**
     * Return edge, that connects two vertices and has specified object.
     * Edge is directed from sourceVertex to targetVertex.
     *
     * @param sourceVertex start vertex
     * @param targetVertex end vertex
     * @param obj          object in edge
     * @return edge connects vertices
     */
    @Override
    public EdgeDefault<V, E> getEdge(V sourceVertex, V targetVertex, E obj) {
        EdgeDefault<V, E> edge = new EdgeDefault<>(sourceVertex, targetVertex, obj);
        if (!containsVertex(sourceVertex)
                || !containsVertex(targetVertex)) {
            return null;
        }
        for (EdgeDefault<V, E> tmp : getVertex(sourceVertex).outEdge) {
            if (tmp.equals(edge)) {
                return tmp;
            }
        }
        return null;
    }

    /**
     * Add edge, that connects vertices in direction from sourceVertex to targetVertex.
     * Edge has an object.
     *
     * @param sourceVertex the start vertex for edge
     * @param targetVertex the end vertex for edge
     * @param object       the object that will be placed into edge
     * @return new Edge
     */
    @Override
    public EdgeDefault<V, E> addEdge(V sourceVertex, V targetVertex, E object) {
        return addEdge(sourceVertex, targetVertex, object, EdgeDefault.DEFAULT_WEIGHT);
    }

    /**
     * Add specified edge, if there is no such edge.
     *
     * @param e the edge that will be added to graph
     * @return true, if there are no equal Edge.
     */
    @Override
    public boolean addEdge(EdgeDefault<V, E> e) {
        if (e == null) {
            return false;
        }
        if (containsEdge(e)) {
            var tmp = getEdge(e.getSourceVertex(), e.getTargetVertex(), e.getObject());
            boolean flag = tmp.getWeight() != e.getWeight();
            tmp.setWeight(e.getWeight());
            return flag;
        }
        V source = e.getSourceVertex();
        V target = e.getTargetVertex();
        if (!containsVertex(source)
                || !containsVertex(target)) {
            return false;
        }
        Vertex<V, E> sourceV = vertexMap.get(source);
        Vertex<V, E> targetV = vertexMap.get(target);
        sourceV.outEdge.add(e);
        return targetV.inEdge.add(e);
    }


    /**
     * Create Edge with specified vertices, weight and object.
     *
     * @param sourceVertex the start vertex for edge
     * @param targetVertex the end vertex for edge
     * @param e            the object that will be placed into edge
     * @param weight       the weight that will be placed into edge
     * @return edge
     */
    @Override
    public EdgeDefault<V, E> addEdge(V sourceVertex, V targetVertex, E e, double weight) {
        EdgeDefault<V, E> edge = new EdgeDefault<>(sourceVertex, targetVertex, e, weight);
        return addEdge(edge) ? edge : null;
    }

    /**
     * Add vertex to graph, if there is no such vertex.
     * If it isn't, then return false.
     *
     * @param v the vertex value, or vertex object.
     * @return true, if vertex has been added.
     */
    @Override
    public boolean addVertex(V v) {
        if (v == null) {
            return false;
        }
        if (containsVertex(v)) {
            return false;
        }
        Vertex<V, E> vertex = new Vertex<>();
        vertexMap.put(v, vertex);
        return true;
    }

    /**
     * Check for existing edge between two vertices.
     *
     * @param sourceVertex the start vertex for some edge
     * @param targetVertex the end vertex for some edge
     * @return true, if there is edge from source to target.
     */
    @Override
    public boolean containsEdge(V sourceVertex, V targetVertex) {
        return getEdge(sourceVertex, targetVertex) != null;
    }

    /**
     * Check for exising edge in the graph.
     *
     * @param e the edge for checking
     * @return true, if the graph contains specified edge
     */
    @Override
    public boolean containsEdge(EdgeDefault<V, E> e) {
        if (e == null) {
            return false;
        }
        V source = e.getSourceVertex();
        V target = e.getTargetVertex();
        if (!containsVertex(source)
                || !containsVertex(target)) {
            return false;
        }
        return getVertex(source).outEdge.contains(e);
    }

    /**
     * Check for exising vertex in the graph.
     *
     * @param v the vertex object
     * @return true, if the graph contains specified vertex.
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
        for (V v : vertexSet()) {
            edgeSet.addAll(getVertex(v).outEdge);
        }
        return edgeSet;
    }

    /**
     * Return set of all incoming edges to specified vertex.
     *
     * @param vertex the vertex
     * @return set of all incoming edges to specified vertex.
     */
    @Override
    public Set<EdgeDefault<V, E>> incomingEdgesOf(V vertex) {
        if (!containsVertex(vertex)) {
            return null;
        }
        return Set.copyOf(getVertex(vertex).inEdge);
    }

    /**
     * Return set of all incoming edges to specified vertex.
     *
     * @param vertex the vertex
     * @return set of all incoming edges to specified vertex.
     */
    @Override
    public Set<EdgeDefault<V, E>> outgoingEdgesOf(V vertex) {
        if (!containsVertex(vertex)) {
            return null;
        }
        return Set.copyOf(getVertex(vertex).outEdge);
    }

    /**
     * Remove edge with null object from graph between two specified vertices.
     *
     * @param sourceVertex the source vertex maybe for some edge.
     * @param targetVertex the target vertex maybe for some edge.
     * @return removed edge.
     */
    @Override
    public EdgeDefault<V, E> removeEdge(V sourceVertex, V targetVertex) {
        if (!containsVertex(sourceVertex)
                || !containsVertex(targetVertex)) {
            return null;
        }
        EdgeDefault<V, E> edge = getEdge(sourceVertex, targetVertex);
        return removeEdge(edge) ? edge : null;
    }

    /**
     * Remove specified edge from graph. And check for graph changes.
     *
     * @param e the edge
     * @return true, if there was removing edge from graph.
     */
    @Override
    public boolean removeEdge(EdgeDefault<V, E> e) {
        if (e == null) {
            return false;
        }
        if (!containsEdge(e)) {
            return false;
        }
        V source = e.getSourceVertex();
        V target = e.getTargetVertex();
        getVertex(source).outEdge.remove(e);
        return getVertex(target).inEdge.remove(e);
    }

    /**
     * Remove specified vertex from graph. And check for graph changes.
     *
     * @param v the vertex object
     * @return true, if there was removing vertex from graph.
     */
    @Override
    public boolean removeVertex(V v) {
        if (v == null) {
            return false;
        }
        if (!containsVertex(v)) {
            return false;
        }
        Vertex<V, E> vertex = getVertex(v);

        for (EdgeDefault<V, E> inEdges : vertex.inEdge) {
            var vert = vertexMap.get(inEdges.getSourceVertex());
            if (vert != null) {
                vert.outEdge.remove(inEdges);
            }
        }
        for (EdgeDefault<V, E> outEdge : vertex.outEdge) {
            var vert = vertexMap.get(outEdge.getTargetVertex());
            if (vert != null) {
                vert.outEdge.remove(outEdge);
            }
        }

        vertex.outEdge = null;
        vertex.inEdge = null;
        vertexMap.remove(v);
        return true;
    }

    /**
     * Return set of all vertices from graph.
     *
     * @return set of all vertices from this graph
     */
    @Override
    public Set<V> vertexSet() {
        return new HashSet<>(vertexMap.keySet());
    }

    private Vertex<V, E> getVertex(V vo) {
        return vertexMap.get(vo);
    }

    @Override
    public String toString() {
        return "GraphIncList{" +
                "vertexMap=" + vertexMap +
                '}';
    }
}
