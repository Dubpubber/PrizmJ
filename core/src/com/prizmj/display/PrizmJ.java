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
            pCamera.translate(-camSpeed, 0, -camSpeed);
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            pCamera.translate(-camSpeed, 0, camSpeed);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            pCamera.translate(camSpeed, 0, camSpeed);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            pCamera.translate(camSpeed, 0, -camSpeed);
        if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
            camSpeed -= 0.25f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
            camSpeed += 0.25f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2))
            pCamera.position.set(10f, 10f, 10f);
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            if (currentDimension == 3) {
                manager.switchDimension(Dimension.Environment_3D);
                currentDimension = 2;
            } else {
                manager.switchDimension(Dimension.Environment_2D);
                currentDimension = 3;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            floorplan++;
            try {
                createBuilding();
            } catch (Exception e) {
                e.printStackTrace();
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

    public void createBuilding() throws Exception {
        Blueprint print = new Blueprint(this);
        try {
            switch (floorplan) {
                case 1:
                    print.createBasicRoom("FirstRoom", 0, 0, 0, 10, 10, Color.WHITE);
                    print.createAttachingRoom("FirstRoom", "SecondRoom", 10, 10, Color.RED, Cardinal.NORTH);
                    print.createAttachingStairwell("FirstRoom", "Well1", Cardinal.EAST);
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
    }

}
