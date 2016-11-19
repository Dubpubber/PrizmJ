package com.prizmj.display.simulation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.prizmj.display.Blueprint;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.parts.abstracts.Room;
import com.prizmj.display.simulation.components.Edge;
import com.prizmj.display.simulation.components.Vertex;

import java.util.UUID;

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

    /**
     * Compiles the edges and vertex' from the given blueprint.
     * @param blueprint - The supplied blueprint to map into the DirectedGraph
     */
    public void compile(Blueprint blueprint) {
        blueprint.getAllModels().forEach(rm -> {
            Vector2 center = getCenter(rm.getRoom());
            addVertex(new Vertex(center.x, PrizmJ.WALL_HEIGHT / 2, center.y, rm.getRoom()));
            if(rm.getDoors().size > 0) rm.getDoors().forEach(door -> {
                // Your changes to snapping doors fucked shit up so make this work
                // Your shitty recreate room fucked shit up fgt
                addVertex(new Vertex(rm.getRoom().getX() + door.getX(), PrizmJ.WALL_HEIGHT / 2, rm.getRoom().getZ() + door.getZ(), door.getFirstRoom().getRoom()));
            });
        });
    }

    private void addEdge(Edge edge) {
        graph.addEdge(edge);
    }

    private void addVertex(Vertex vertex) {
        graph.addVertex(vertex);
        vertex.setModel(modelBuilder.createSphere(
                1, 1, 1,
                16, 16,
                new Material(ColorAttribute.createDiffuse(Color.CYAN)),
                VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position
        ));
        vertex.updatePosition();
        vertex.setId(UUID.randomUUID());
    }

    public void render(ModelBatch batch, Environment environment) {
        graph.getGraph().forEach((n, a) -> n.render(batch, environment));
    }

    public DirectedGraph getGraph() {
        return graph;
    }

    private float computeAreaPolygon(Room room) {
        float sum;

        float westX = room.getX() - room.getWidth();
        float eastX = room.getX() + room.getWidth();
        float northZ = room.getZ() - room.getHeight();
        float southZ = room.getZ() + room.getHeight();

        sum = ((((westX * northZ) - (eastX * northZ)) +
                ((eastX * southZ) - (eastX * northZ)) +
                ((eastX * southZ) - (westX * southZ))) * 0.5f);

        return sum;
    }

    private Vector2 computeCentroids(Room room) {
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
        return new Vector2(x, y);
    }

    /**
     * Why exactly do we need to do the math above? This does virtually the exact same thing 10* faster.
     * Unless the node's height in the representation graph NEEDS to be exactly the center of the room, there's no
     * reason why 'y' can't equal (PrizmJ.WALL_HEIGHT / 2) + room.getY() Don't make this stupid if we don't have too
     * @param room - The room to calculate upon.
     * @return - Returns a vector for the coordinates of the center of the room.
     */
    public Vector2 getCenter(Room room) {
        float x1 = room.getX() - room.getWidth();
        float x2 = room.getX() + room.getWidth();
        float z1 = room.getZ() - room.getHeight();
        float z2 = room.getZ() + room.getHeight();
        return new Vector2((x1 + x2) / 2, (z1 + z2) / 2);
    }
}
