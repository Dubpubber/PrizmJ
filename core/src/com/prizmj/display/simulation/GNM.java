package com.prizmj.display.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.prizmj.display.Blueprint;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.parts.Door;
import com.prizmj.display.parts.Hallway;
import com.prizmj.display.parts.Stairwell;
import com.prizmj.display.parts.abstracts.Room;
import com.prizmj.display.simulation.components.Edge;
import com.prizmj.display.simulation.components.Vertex;

import javax.lang.model.type.PrimitiveType;
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
        // Create vertices for each room and door
        // Connect rooms with their doors
        blueprint.getAllModels().forEach(rm -> {
            if (rm.getRoom() instanceof Hallway) {
                if (((Hallway) rm.getRoom()).getUpDown()) {
                    Vertex north = new Vertex(rm.getRoom().getX(), rm.getRoom().getY(), rm.getRoom().getZ() + (rm.getRoom().getHeight() / 2), rm.getRoom());
                    Vertex south = new Vertex(rm.getRoom().getX(), rm.getRoom().getY(), rm.getRoom().getZ() - (rm.getRoom().getHeight() / 2), rm.getRoom());
                    addVertex(north);
                    addVertex(south);
                    addEdge(new Edge(north, south));
                    addEdge(new Edge(south, north));
                }
            } else {
                Vector2 center = getCenter(rm.getRoom());
                Vertex room = new Vertex(center.x, rm.getRoom().getY() + PrizmJ.WALL_HEIGHT / 2, center.y, rm.getRoom());
                addVertex(room);
                if(rm.getDoors().size > 0) rm.getDoors().forEach(door -> {
                    Vertex vDoor = new Vertex(rm.getRoom().getX() + door.getX(), rm.getRoom().getY() + PrizmJ.WALL_HEIGHT / 2, rm.getRoom().getZ() + door.getZ(), door);
                    addVertex(vDoor);
                    addEdge((new Edge(room, vDoor)));
                    addEdge((new Edge(vDoor, room)));
                });
            }
        });
        // Connect all doors with their secondRoom
        graph.getVertices().forEach(vertex -> {
            if (vertex.getRoom() instanceof Door) {
                Room secondRoom = ((Door) vertex.getRoom()).getSecondRoom().getRoom();
                // If the door connects to another room or stairs
                if (!(secondRoom instanceof Hallway)) {
                    addEdge(new Edge(vertex, graph.getVertexFromRoom(secondRoom)));
                    addEdge(new Edge(graph.getVertexFromRoom(secondRoom), vertex));
                // If the door connects to a hallway
                } else {
                    // To be implemented
                }
            } else if (vertex.getRoom() instanceof Stairwell) {
                if(((Stairwell) vertex.getRoom()).getDownstairs() != null){
                    addEdge(new Edge(vertex, graph.getVertexFromRoom(blueprint.getRoomModelByName(((Stairwell) vertex.getRoom()).getDownstairs()).getRoom())));
                    addEdge(new Edge(vertex, graph.getVertexFromRoom(blueprint.getRoomModelByName(((Stairwell) vertex.getRoom()).getDownstairs()).getRoom())));
                }
            }
        });
    }

    private void addEdge(Edge edge) {
        if (!graph.getEdgesFromVertex(edge.getEnd()).contains(edge, false)) {
            Model line;
            modelBuilder.begin();
            MeshPartBuilder builder = modelBuilder.part(
                    "line",
                    1,
                    VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position,
                    new Material(ColorAttribute.createDiffuse(Color.WHITE)));
            builder.line(edge.getStart().getX(), edge.getStart().getY(), edge.getStart().getZ(),
                    edge.getEnd().getX(), edge.getEnd().getY(), edge.getEnd().getZ());
            line = modelBuilder.end();
            edge.setModel(line);
        }
        graph.addEdge(edge);
    }

    private void addVertex(Vertex vertex) {
        graph.addVertex(vertex);
        vertex.setModel(modelBuilder.createSphere(
                1, 1, 1,
                16, 16,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position
        ));
        vertex.updatePosition();
    }

    public void render(ModelBatch batch, Environment environment) {
        graph.getGraph().forEach((n, a) -> {
            n.render(batch, environment);
            a.forEach(edge -> edge.render(batch, environment));
        });
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
