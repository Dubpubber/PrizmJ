package com.prizmj.display.simulation.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by BBCommand on 11/17/2016.
 */
public class DirectedGraph {

    // Map of Nodes to outgoing Edges. Each set of edges is stored
    // in an array list.
    private final Map<Node, ArrayList<Edge>> graph = new HashMap<>();

    /**
     * Adds a node to the graph. Does nothing if it is already added.
     *
     * @param node - The node to add
     * @return - True if successful, false otherwise
     */
    public boolean addNode(Node node) {

        // If the node is in the graph, do nothing
        if (graph.containsKey(node))
            return false;

        // Add the node to the graph with an empty set of edges.
        graph.put(node, new ArrayList<Edge>());
        return true;
    }

    /**
     * Adds an edge to the graph. Does nothing if edge's start/end nodes
     *  are not in the graph.
     *
     * @param edge - The edge to add
     * @return
     */
    public void addEdge(Edge edge) {
        // Ensure both nodes exist in the graph
        if (!graph.containsKey(edge.getStart()) || !graph.containsKey(edge.getEnd()))
            throw new NoSuchElementException("Both nodes must exist in the graph.");

        // Add the edge
        graph.get(edge.getStart()).add(edge);
    }

    /**
     * Removes an edge from the graph. Does nothing if node's start/end nodes
     *  are not in the graph.
     *
     * @param edge - The edge to remove
     */
    public void removeEdge(Edge edge) {
        // Ensure both nodes exist in the graph
        if (!graph.containsKey(edge.getStart()) || !graph.containsKey(edge.getEnd()))
            throw new NoSuchElementException("Both nodes must exist in the graph.");

        // Remove the edge
        graph.get(edge.getStart()).remove(edge);
    }

    /**
     * Removes an edge from the graph. Does nothing if start/end nodes
     *  are not in the graph.
     *
     * @param start - Start node of edge to remove
     * @param end - End node of edge to remove
     */
    public void removeEdge(Node start, Node end) {
        // Ensure both nodes exist in the graph
        if (!graph.containsKey(start) || !graph.containsKey(end))
            throw new NoSuchElementException("Both nodes must exist in the graph.");

        // For each edge outgoing from the start node
        graph.get(start).forEach((edge) -> {
            // If the edge connects to end node
            if (edge.getEnd().equals(end))
                // Remove the edge
                graph.get(start).remove(edge);
        });

    }
}
