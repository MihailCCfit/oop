package ru.nsu.fit.tsukanov.alg.pathfinder;


import ru.nsu.fit.tsukanov.core.EdgeDefault;
import ru.nsu.fit.tsukanov.core.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Dijkstra<V extends Comparable<V>, E> {
    private V startVert;
    private final TreeMap<V, Double> marksTree;
    private final TreeMap<V, EdgeDefault<V,E>>  pathMap;
    private final PriorityQueue<V> heap;

    private final Graph<V,E> graph;
    public Dijkstra(Graph<V,E> graph, V startVert){
        this.startVert = startVert;
        this.marksTree = new TreeMap<>();
        this.heap = new PriorityQueue<>((v1,v2) -> (marksTree.get(v1).compareTo(marksTree.get(v2))));
        this.graph = graph;
        pathMap = new TreeMap<>();
        reuse();
    }
    public void reuse(V start){
        if (start==null){
            throw new NullPointerException();
        }
        if (!graph.containsVertex(start)){
            throw new IllegalArgumentException("No such vertex in graph");
        }
        heap.clear();
        marksTree.clear();
        pathMap.clear();
        startVert = start;
        heap.add(startVert);
        marksTree.put(startVert, 0.0);
        alg();
    }
    public void reuse(){
        reuse(startVert);
    }

    private void relax(EdgeDefault<V,E> edge){
        V start = edge.getEdgeSource();
        V end = edge.getEdgeTarget();
        double res = marksTree.get(start)+ edge.getWeight();
        if(!marksTree.containsKey(end) || res<marksTree.get(end)){
            marksTree.put(end, res);
            pathMap.put(end, edge);
            heap.add(end);
        }
    }
    private void alg(){
        while (!heap.isEmpty()){
            V curr = heap.poll();
            for (EdgeDefault<V, E> edge : graph.outgoingEdgesOf(curr)) {
                relax(edge);
            }
        }
    }



    public double getDistant(V v){
        return marksTree.getOrDefault(v, Double.POSITIVE_INFINITY);
    }

    public List<V> getPathV(V v){
        if (v==null){
            throw new NullPointerException();
        }
        if (!pathMap.containsKey(v)){
            System.out.println(v+"|HE");
            return null;
        }
        LinkedList<V> list = new LinkedList<>();
        V curr = v;
        list.add(v);
        while (curr!=startVert){
            curr = pathMap.get(curr).getEdgeSource();
            list.add(0, curr);
        }
        return list;
    }
    public List<EdgeDefault<V,E>> getPathE(V v){
        if (!pathMap.containsKey(v)){
            return null;
        }
        LinkedList<EdgeDefault<V,E>> list = new LinkedList<>();
        V curr = v;

        while (curr!=startVert){
            list.add(0, pathMap.get(curr));
            curr = pathMap.get(curr).getEdgeSource();
        }
        return list;
    }
    public boolean hasPath(V v){
        if (v==null) {
            return false;
        }
        return pathMap.containsKey(v);
    }


}
