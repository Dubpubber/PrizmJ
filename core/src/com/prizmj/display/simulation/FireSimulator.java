package com.prizmj.display.simulation;

import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.prizmj.display.Blueprint;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.models.RoomModel;
import com.prizmj.display.parts.abstracts.Room;
import com.prizmj.display.simulation.components.Edge;
import com.prizmj.display.simulation.components.Vertex;
import com.prizmj.display.simulation.dijkstra.Dijkstra;

/**
 * Created by GrimmityGrammity on 11/16/2016.
 *
 * A Fire Simulator is one of the three key components of PrizmJ.
 *
 * A Fire Simulator simulates the spreading of fire within a Building
 *  and performs optimal path analysis on the burning building.
 *  A Fire Simulator requires a Building to burn and a GNM of that building to
 *  perform analysis.
 *
 */
public class FireSimulator {

    private GNM gnm;
    private Blueprint blueprint;
    private PrizmJ prizmJ;

    private boolean simulationRunning;

    public FireSimulator(PrizmJ prizmJ, Blueprint bprint) {
        gnm = bprint.getGeometricNetworkModel();
        blueprint = bprint;
        this.prizmJ = prizmJ;
        simulationRunning = false;
    }

    public void simulateFire(String name) {

        ModelBuilder builder = prizmJ.getModelBuilder();
        RoomModel room = blueprint.getRoomModelByName(name);
        this.simulationRunning = true;

        room.startSmokeSimulation(builder, 0.85f, 0.0146f, 0.5f, gnm);
        // Firefighters arrive and traverse graph
        Timer.schedule(new Timer.Task() {
           @Override
           public void run() {
//               // Toggle Firefighters on/off
//               //Dijkstra.toggleRunning();
//               // Update the graph based on the smoke density
               gnm.update();

               Array<Vertex> results = Dijkstra.shortestPaths(gnm.getGraph(), gnm.getGraph().getVertexFromRoom(blueprint.getRoomModelByName("f1_basicroom_2").getRoom()));

               results.forEach(vertex -> {
                   System.out.println("Vertex: "+vertex.getRoom().getName()+" : "+vertex.getWeight());
               });
////               gnm.getGraph().getGraph().forEach(((vertex, edges) -> {
////                   System.out.println("Vertex "+vertex.getRoom().getName()+" walking speed: "+vertex.getWalkingSpeed());
////                   System.out.println("Vertex "+vertex.getRoom().getName()+" weight: "+vertex.getWeight());
//               }));
           }
        }, 30, 0.6f,0);



        // Light the building on fire
        //room.startSmokeSimulation(builder, .075f, 0.05f, 1);


    }

    public GNM getGNM() {
        return gnm;
    }

    public void dijkstra() {

    }

    public boolean isSimulationRunning() {
        return simulationRunning;
    }
}
