package com.prizmj.display.simulation;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.prizmj.display.Blueprint;
import com.prizmj.display.simulation.components.Edge;

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

    private Node[] nodes;
    private Edge[] edges;

    public GNM(Blueprint blueprint) {
        create();
    }

    // TODO: Make enum
    private int create() {
        return 0;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public int getNumNodes() {
        return nodes.length;
    }

    public int getNumEdges() {
        return edges.length;
    }
}
