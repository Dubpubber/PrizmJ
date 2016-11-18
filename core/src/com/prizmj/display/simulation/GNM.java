package com.prizmj.display.simulation;

import com.prizmj.display.Blueprint;
import com.prizmj.display.simulation.components.DirectedGraph;
import com.prizmj.display.simulation.components.Edge;
import com.prizmj.display.simulation.components.Node;

/**
 * Created by GrimmityGrammity on 11/16/2016.
 *
 * A Geometric Network Model(GNM) is one of the three key components of PrizmJ.
 *
 * A GNM is a topological data model that represents the adjacency,
 *  connectivity, and hierarchical relationship between 3D entities in
 *  graph theory.
 */
public class GNM {

    private DirectedGraph graph;

    public GNM(Blueprint blueprint) {
        create();
    }

    // TODO: Make enum
    private int create() {
        return 0;
    }

    private void addEdge(Edge edge) {
        graph.addEdge(edge);
    }

    private void addNode(Node node) {
        graph.addNode(node);
    }

    public DirectedGraph getGraph() {
        return graph;
    }
}
