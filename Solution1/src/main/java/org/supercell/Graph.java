package org.supercell;

import org.json.simple.JSONArray;

import java.util.*;

public class Graph {
    Map<String, List<String>> graph = new HashMap<>();

    public void addNewVertex(String vertex) {
            graph.put(vertex, new LinkedList<>());
    }

    public void addNewEdge(String source, String destination) {
        if(!graph.containsKey(source)) {
            addNewVertex(source);
        }
        if(!graph.containsKey(destination)) {
            addNewVertex(destination);
        }
        graph.get(source).add(destination);
        graph.get(destination).add(source);
    }

    public void removeEdge(String source, String destination) {
        graph.get(source).remove(destination);
        graph.get(destination).remove(source);
    }
    public void containsVertex(String vertex) {
        for (String a: graph.keySet()) {
            if(a.equals(vertex)) {
                return;
            }
        }
        addNewVertex(vertex);
    }
    public JSONArray getAdjacentVertices(String source) {
        JSONArray jsonArray = new JSONArray();
        for ( String a : graph.keySet()) {
            if(a.toString().equals(source.toString())) {
                for (String b : graph.get(a)) {
                    jsonArray.add(b.toString());
                }
            }
        }
        return jsonArray;
    }
}

