/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
import com.prizmj.console.CommandFactory;
import com.prizmj.display.habitats.EmptyWorld;
import com.prizmj.display.habitats.Habitat;
import com.prizmj.display.models.generic.GenericSensorDropEnv;
import com.prizmj.display.models.generic.GenericSphere;
import com.prizmj.display.models.generic.profiles.SphereProfile;

import javax.swing.*;

public class PrizmJ extends ApplicationAdapter {

    /* CAMERA VARIABLES */
    private OrthographicCamera debugCamera;
    private CameraInputController perspectiveCameraController;
    private PerspectiveCamera perspectiveCamera;

    /* MODEL AND ENVIRONMENT VARIABLES */
    private ModelBuilder modelBuilder;
    private ModelBatch modelBatch;
    private Environment environment;

    /* DEBUG VARIABLES */
    private boolean DEBUG = true;
    private Runtime runtime;
    private SpriteBatch batch;   // Used for rendering debug information on debug screen. (NOTE: Debug must be set to true)
    private BitmapFont font;     // Used for rendering debug text information on debug screen.

    /* COMMAND LINE SPECIFIC VARIABLES */
    public static JTextArea console;
    public static JTextField cmdLine;
    private CommandFactory commandFeedback;

    /* HABITAT */
    private Habitat activeHabitat;

    @Override
    public void create() {
        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();
        environment = new Environment();

        perspectiveCamera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        perspectiveCamera.position.set(10f, 10f, 10f);
        perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.update();

        perspectiveCameraController = new CameraInputController(perspectiveCamera);
        perspectiveCameraController.forwardKey = Input.Keys.W;
        perspectiveCameraController.backwardKey = Input.Keys.S;
        perspectiveCameraController.rotateLeftKey = 0;
        perspectiveCameraController.rotateRightKey = 0;
        Gdx.input.setInputProcessor(perspectiveCameraController);

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        if(DEBUG) {
            debugCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch = new SpriteBatch();
            font = new BitmapFont();
            font.setColor(Color.YELLOW);
        }
        runtime = Runtime.getRuntime();
        commandFeedback = new CommandFactory(this);

        try {
            activeHabitat = new EmptyWorld("emptyworld_1", modelBatch, environment, new GenericSensorDropEnv(this, "test_env"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(perspectiveCamera);
        if(activeHabitat != null) activeHabitat.render();
        modelBatch.end();

        if(DEBUG) {
            batch.begin();
            batch.setProjectionMatrix(debugCamera.combined);
            font.draw(batch, String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()), -395, 375);
            {
                font.draw(batch, "Heap Utilization Statistics", -395, 358);
                font.draw(batch, String.format("Free Memory: %dMB", runtime.freeMemory() / (1024 * 1024)), -390, 341);
                font.draw(batch, String.format("Total Memory: %dMB", runtime.totalMemory() / (1024 * 1024)), -390, 324);
                font.draw(batch, String.format("Used Memory: %dMB", (runtime.totalMemory() - runtime.freeMemory()) / (1024*1024)), -390, 307);
            }
            batch.end();
            debugCamera.update();
        }
        perspectiveCameraController.update();
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

    public static void setConsole(JTextArea area) {
        if(console == null) {
            console = area;
            writeControlHelpToConsole();
            writeToConsole("Click anywhere in the canvas or type 'help' into console to begin.");
        } else
            System.out.println("Please don't set a static object! This is only used upon main creation.");
    }

    public static void setCommand(JTextField field) {
        if(cmdLine == null) {
            cmdLine = field;
            cmdLine.setText("> ");
        } else
            System.out.println("Please don't set a static object! This is only used upon main creation.");
    }

    public static void clearCommandText() {
        cmdLine.setText("> ");
    }

    public static void writeToConsole(String data) {
        console.append("\n> " + data);
    }

    public static void writeControlHelpToConsole() {

    }
}