package com.prizmj.display;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;

public class PrizmJ extends ApplicationAdapter {

    private PerspectiveCamera pCamera;
    private float camSpeed = 0.0f;

    private ModelBuilder modelBuilder;
    private ModelBatch modelBatch;
    private Environment environment;

    private RenderManager manager;

    private OrthographicCamera debugCam;
    private SpriteBatch batch; // Used mainly for debug purposes //
    private BitmapFont font;
    private boolean DEBUG = true;

    @Override
    public void create() {
        this.modelBatch = new ModelBatch();

        pCamera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pCamera.position.set(10f, 10f, 10f);
        pCamera.lookAt(0, 0, 0);
        pCamera.update();

        this.environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBuilder = new ModelBuilder();
        this.manager = new RenderManager(this);
        manager.createBasicRoom("GrimaceisA FAGGGGG", 0, 0, 5, 5, Color.RED, 2);
        manager.createBasicRoom("GrimaceisA FAGGGGG (in 3d)", 0, 0, 5, 5, Color.GREEN, 3);
        manager.createBasicRoom("GrimaceisA FAGGGGG (in 3d)", 0, 0, 5, 7, Color.GOLD, 3);
        manager.createBasicRoom("GrimaceisA FAGGGGG (in 3d)", 0, 0, 15, 5, Color.MAGENTA, 3);
        /*model = modelBuilder.createBox(3f, 2.5f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        instance.transform.translate(10, 0, 0);*/
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
            font.draw(batch, String.format("Room Count: %d", manager.getRooms().size), -315, 164);
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
            camSpeed -= 0.33f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
            camSpeed += 0.33f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2))
            pCamera.position.set(10f, 10f, 10f);
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

}
