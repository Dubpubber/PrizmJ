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
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.prizmj.display.models.RoomModel;
import com.prizmj.display.parts.BasicRoom;
import com.prizmj.display.parts.Door;
import com.prizmj.display.parts.Level;

public class PrizmJ extends ApplicationAdapter {

    private PerspectiveCamera pCamera;
    private float camSpeed = 0.25f;

    private ModelBuilder modelBuilder;
    private ModelBatch modelBatch;
    private Environment environment;

    private RenderManager manager;

    private OrthographicCamera debugCam;
    private SpriteBatch batch; // Used mainly for debug purposes //
    private BitmapFont font;
    private boolean DEBUG = true;

    public int currentDimension = 3;

    public static final float WALL_HEIGHT = 2.5f;
    public static final float WALL_THICKNESS = 0.25f;
    public static final float WALL_OFFSET = 0.139f;

    public static final float DOOR_HEIGHT = 1.25f;
    public static final Color DOOR_COLOR = Color.RED;
    public static final float DOOR_DEPTH = 0.924f;

    @Override
    public void create() {
        this.modelBatch = new ModelBatch();
        this.currentDimension = MathUtils.clamp(currentDimension, 2, 3);

        pCamera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pCamera.position.set(10f, 10f, 10f);
        pCamera.lookAt(0, 0, 0);
        pCamera.update();

        this.environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBuilder = new ModelBuilder();
        Level floor = new Level(0);
        Blueprint print = new Blueprint(this, floor);
        print.addRoomPairToSpecificFloor(0, "fagRoom", 0, 0, 0, 12, 4, Color.GREEN);
        print.addRoomPairToSpecificFloor(0, "fagRoom2", 0, 0, 0, 4, 4, Color.RED);
        print.addRoomPairToSpecificFloor(0, "fagRoom3", 2, 0, 0, 4, 4, Color.RED);
        print.addRoomPairToSpecificFloor(0, "fagRoom4", -2, 0, 0, 4, 4, Color.RED);
        print.addRoomPairToSpecificFloor(0, "fagRoom5", 0, 0, 0, 4, 4, Color.RED);
        print.updateModels();

        // Using the method I suggested, was way faster
        try {
            print.attachRoomByAxis("fagRoom", "fagRoom2", Cardinal.NORTH);
            print.attachRoomByAxis("fagRoom", "fagRoom3", Cardinal.NORTH);
            print.attachRoomByAxis("fagRoom", "fagRoom4", Cardinal.NORTH);
            print.attachRoomByAxis("fagRoom2", "fagRoom5", Cardinal.NORTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            print.attachRoomWithPrejudice("fagRoom", new String[] {"fagRoom3", "fagRoom4", "fagRoom5"}, Cardinal.WEST, Cardinal.WEST);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        this.manager = new RenderManager(this, print);
        manager.switchDimension(currentDimension);
        if(DEBUG) {
            debugCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch = new SpriteBatch();
            font = new BitmapFont();
            font.setColor(Color.YELLOW);
        }
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

        if(Gdx.input.isKeyPressed(Input.Keys.W))
            pCamera.translate(0, 0, -camSpeed);
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            pCamera.translate(-camSpeed, 0, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            pCamera.translate(0, 0, camSpeed);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            pCamera.translate(camSpeed, 0, 0);
        if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
            camSpeed -= 0.25f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
            camSpeed += 0.25f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2))
            pCamera.position.set(10f, 10f, 10f);
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            if (currentDimension == 3) {
                manager.switchDimension(2);
                currentDimension = 2;
            } else {
                manager.switchDimension(3);
                currentDimension = 3;
            }
        }
        camSpeed = MathUtils.clamp(camSpeed, 0, 5);
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            DEBUG = !DEBUG;
            if(DEBUG)
                Gdx.graphics.setTitle("PrizmJ - Debug Enabled");
            else
                Gdx.graphics.setTitle("PrizmJ");
        }
        pCamera.update();
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

}
