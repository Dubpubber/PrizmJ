package com.prizmj.display.simulation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.prizmj.display.Blueprint;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.RenderManager;
import com.prizmj.display.parts.abstracts.Room;
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

    private PrizmJ prizmJ;
    private ModelBuilder modelBuilder;

    private DirectedGraph graph;

    private Blueprint blueprint;

    private int retardVar = create();

    public GNM(PrizmJ prizmJ, Blueprint blueprint) {
        this.prizmJ = prizmJ;
        this.blueprint = blueprint;
        this.modelBuilder = prizmJ.getModelBuilder();
        this.graph = new DirectedGraph();
    }

    // TODO: I'm retarded - Grimace
    private int create() {
        return 1 + 2 + 3;
    }

    private void addEdge(Edge edge) {
        graph.addEdge(edge);
    }

    public void addNode(Node node) {
        graph.addNode(node);
        node.setModel(modelBuilder.createSphere(
                2, 2, 2,
                16, 16,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position
        ));
    }

    public void render(ModelBatch batch, Environment environment) {
        graph.getGraph().forEach((n, a) -> {
            n.render(batch, environment);
        });
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
