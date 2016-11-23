package com.prizmj.display;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.prizmj.display.models.RoomModel;
import com.prizmj.display.parts.abstracts.Room;
import com.prizmj.display.simulation.FireSimulator;

public class PrizmJ extends ApplicationAdapter {

    public CameraInputController camController;

    private PerspectiveCamera pCamera;
    private float camSpeed = 0.25f;

    private FireSimulator fireSimulator;

    private ModelBuilder modelBuilder;
    private ModelBatch modelBatch;
    private Environment environment;

    private RenderManager manager;

    private OrthographicCamera debugCam;
    private SpriteBatch batch; // Used mainly for debug purposes //
    private BitmapFont font;
    private boolean DEBUG = true;

    public int currentDimension = 3;
    private int floorplan = 1;

    public static final float WALL_HEIGHT = 2.5f;
    public static final float WALL_THICKNESS = 0.25f;
    public static final float WALL_OFFSET = 0.139f;

    public static final float DOOR_HEIGHT = 1.25f;
    public static final float DOOR_WIDTH = 1f;
    public static final Color DOOR_COLOR = Color.GOLD;

    public static final float STAIRWELL_WIDTH = 4;
    public static final float STAIRWELL_HEIGHT = 3;
    public static final Color STAIRWELL_COLOR = Color.VIOLET;

    @Override
    public void create() {
        this.modelBatch = new ModelBatch();
        this.currentDimension = MathUtils.clamp(currentDimension, 2, 3);

        pCamera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pCamera.position.set(10f, 10f, 10f);
        pCamera.lookAt(0, 0, 0);
        pCamera.update();
        this.camController = new CameraInputController(pCamera);
        this.camController.forwardKey = Input.Keys.W;
        this.camController.backwardKey = Input.Keys.S;
        this.camController.rotateLeftKey = 0;
        this.camController.rotateRightKey = 0;
        Gdx.input.setInputProcessor(camController);

        this.environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        modelBuilder = new ModelBuilder();
        try {
            createBuilding();
        } catch (Exception e) {
            e.printStackTrace();
        }
        manager.switchDimension(Dimension.Environment_3D);
        if(DEBUG) {
            debugCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch = new SpriteBatch();
            font = new BitmapFont();
            font.setColor(Color.YELLOW);
        }
    }

    // lmao
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(pCamera);
        manager.render(modelBatch, environment);
        modelBatch.end();

        if(DEBUG) {
            batch.begin();
            batch.setProjectionMatrix(debugCam.combined);
            font.draw(batch, String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()), -315, 235);
            font.draw(batch, String.format("CamSpeed: %f", camSpeed), -315, 218);
            font.draw(batch, String.format("Total Memory: %dMB", Runtime.getRuntime().totalMemory() / (1024*1024)), -315, 201);
            // font.draw(batch, String.format("Room Count: %d", manager.getNumberofRooms()), -315, 164);
            batch.end();
            debugCam.update();
        }

        /*if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            Vector3 left = camController.camera.direction.cpy().crs(Vector3.Y).nor();
            camController.camera.position.add(left.scl(-camSpeed));
            camController.camera.update();

        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            Vector3 right = camController.camera.direction.cpy().crs(Vector3.Y).nor();
            camController.camera.position.add(right.scl(camSpeed));
            camController.camera.update();
        }*/

        if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
            camSpeed -= 0.25f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
            camSpeed += 0.25f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            if (currentDimension == 3) {
                manager.switchDimension(Dimension.Environment_3D);
                currentDimension = 2;
            } else {
                manager.switchDimension(Dimension.Environment_2D);
                currentDimension = 3;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            floorplan++;
            try {
                createBuilding();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            manager.toggleRooms();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            manager.toggleGraph();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            System.out.println("Running Simulator");
            if (!fireSimulator.isSimulationRunning())
                fireSimulator.simulateFire("f1_stairwell_1");
        }

        camSpeed = MathUtils.clamp(camSpeed, 0, 5);
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            DEBUG = !DEBUG;
            if(DEBUG)
                Gdx.graphics.setTitle("PrizmJ - Debug Enabled");
            else
                Gdx.graphics.setTitle("PrizmJ");
        }
        camController.update();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }

    public ModelBuilder getModelBuilder() {
        return modelBuilder;
    }

    public ModelBatch getModelBatch() {
        return modelBatch;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public int getCurrentDimension() {
        return currentDimension;
    }

    public void createBuilding() throws Exception {
        Blueprint print = new Blueprint(this);
        try {
            switch (floorplan) {
                case 1:
                    print.createHallway("f1_hallway_1", 0, 0, 0, 5, 20, Color.BROWN, true).recreateSmokeCube(modelBuilder, 0);
                    print.createAttachingStairwell("f1_hallway_1", "f1_stairwell_1", 0, 0, 8.5f, Cardinal.EAST);
                    print.createAttachingRoom("f1_hallway_1", "f1_basicroom_1", 0, 0, 1.20f, 10, 12, Color.valueOf("D6CAA9"), Cardinal.EAST);
                    print.createAttachingRoom("f1_basicroom_1", "f1_basicroom_2", -9.05f - WALL_THICKNESS, 0, 0, 6, 3, Color.valueOf("C4703B"), Cardinal.NORTH);
                    print.createAttachingRoom("f1_hallway_1", "f1_basicroom_3", 0, 0, 7.25f + WALL_THICKNESS, 5, 5, Color.GREEN, Cardinal.WEST);
                    print.createAttachingRoom("f1_hallway_1", "f1_basicroom_4", 0, 0, 1.25f, 5, 8, Color.GREEN, Cardinal.WEST);
                    print.createAttachingRoom("f1_hallway_1", "f1_basicroom_5", 0, 0, -6.25f, 5, 7.25f, Color.RED, Cardinal.WEST);
                    break;
                case 2:
                    print.createHallway("f1_hallway_1", 0, 0, 0, 5, 20, Color.BROWN, true).recreateSmokeCube(modelBuilder, 0);
                    print.createHallway("f2_hallway_2", 0, PrizmJ.WALL_HEIGHT, 0, 5, 20, Color.BROWN, true).recreateSmokeCube(modelBuilder, 0);

                    print.createAttachingStairwell("f1_hallway_1", "f1_stairwell_1", 0, 0, 8.5f, Cardinal.EAST);
                    print.createAttachingRoom("f1_hallway_1", "f1_basicroom_1", 0, 0, 1.20f, 10, 12, Color.valueOf("D6CAA9"), Cardinal.EAST);
                    print.createBasicRoom("f1_basicroom_1", -10, 0, 0, 10, 10, Color.CHARTREUSE);
                    print.createAttachingRoom("f1_basicroom_1", "f1_basicroom_2", -9.05f - WALL_THICKNESS, 0, 0, 6, 3, Color.valueOf("C4703B"), Cardinal.NORTH);
                    print.createAttachingRoom("f1_hallway_1", "f1_basicroom_3", 0, 0, 7.25f + WALL_THICKNESS, 5, 5, Color.GREEN, Cardinal.WEST);
                    print.createAttachingRoom("f1_hallway_1", "f1_basicroom_4", 0, 0, 1.25f, 5, 8, Color.GREEN, Cardinal.WEST);
                    print.createAttachingRoom("f1_hallway_1", "f1_basicroom_5", 0, 0, -6.25f, 5, 7.25f, Color.RED, Cardinal.WEST);

                    print.createAttachingStairwell("f1_stairwell_1", "f2_stairwell_2", 0, 0, 0);
                    print.attachRoomByAxis("f2_hallway_2", "f2_stairwell_2", Cardinal.EAST);

                    print.createAttachingRoom("f2_hallway_2", "f2_basicroom_1", 0, PrizmJ.WALL_HEIGHT, 1.20f, 10, 12, Color.valueOf("D6CAA9"), Cardinal.EAST);
                    print.createAttachingRoom("f2_basicroom_1", "f2_basicroom_2", -9.05f - WALL_THICKNESS, PrizmJ.WALL_HEIGHT, 0, 6, 3, Color.valueOf("C4703B"), Cardinal.NORTH);
                    print.createAttachingRoom("f2_hallway_2", "f2_basicroom_3", 0, PrizmJ.WALL_HEIGHT, 7.25f + WALL_THICKNESS, 5, 5, Color.GREEN, Cardinal.WEST);
                    print.createAttachingRoom("f2_hallway_2", "f2_basicroom_3", 0, PrizmJ.WALL_HEIGHT, 1.25f, 5, 8, Color.GREEN, Cardinal.WEST);
                    print.createAttachingRoom("f2_hallway_2", "f2_basicroom_4", 0, PrizmJ.WALL_HEIGHT, -6.25f, 5, 7.25f, Color.RED, Cardinal.WEST);
                    break;
                default:
                    floorplan = 0;
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        print.createGraph();
        this.manager = new RenderManager(print);
        this.fireSimulator = new FireSimulator(this, print);
    }
}
