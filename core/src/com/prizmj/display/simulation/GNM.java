package com.prizmj.display.simulation;

import com.prizmj.display.Blueprint;
import com.prizmj.display.parts.Room;
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

    private float computeAreaPolygon(Room room) {
        float sum = 0.0f;

        float westX = room.getX() - room.getWidth();
        float eastX = room.getX() + room.getWidth();
        float northZ = room.getZ() - room.getHeight();
        float southZ = room.getZ() + room.getHeight();

        sum = ((((westX * northZ) - (eastX * northZ)) +
                ((eastX * southZ) - (eastX * northZ)) +
                ((eastX * southZ) - (westX * southZ))) * 0.5f);

        return sum;
    }

    private void computeCentroids(Room room) {
        float x , y;
        float area = computeAreaPolygon(room);

        float westX = room.getX() - room.getWidth();
        float eastX = room.getX() + room.getWidth();
        float northZ = room.getZ() - room.getHeight();
        float southZ = room.getZ() + room.getHeight();

        x = (((westX + eastX) * ((westX * northZ) - (eastX * northZ))) +
                ((eastX + eastX) * ((eastX * southZ) - (eastX * northZ))) +
                ((eastX + westX) * ((eastX * southZ) - (westX * southZ)))) /
                (6 * area);

        y = (((northZ + northZ) * ((westX * northZ) - (eastX * northZ))) +
                ((northZ + southZ) * ((eastX * southZ) - (eastX * northZ))) +
                ((southZ + southZ) * ((eastX * southZ) - (westX * southZ)))) /
                (6 * area);
    }
}
