/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.prizmj.display.PrizmJ;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DesktopLauncher extends JFrame {

    public DesktopLauncher() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("PrizmJ Portable");
        setResizable(false);
        Container container = getContentPane();
        container.setLayout(null);

        PrizmJ prizmJ = new PrizmJ();
        LwjglAWTCanvas canvas = new LwjglAWTCanvas(prizmJ, config);
        canvas.getCanvas().requestFocus();
        canvas.getCanvas().setBounds(0, 0, 800, 768);
        container.add(canvas.getCanvas());

        // Begin adding console now //
        JTextArea area = new JTextArea();
        JScrollPane scroll = new JScrollPane(area);
        area.setBackground(Color.BLACK);
        area.setForeground(Color.GREEN);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        area.setAutoscrolls(true);
        scroll.setBounds(800, 0, 566, 718);
        container.add(scroll);
        PrizmJ.setConsole(area);

        // Begin add command prompt //
        JTextField commandLine = new JTextField();
        commandLine.setHighlighter(null);
        commandLine.setBackground(Color.GREEN);
        commandLine.setBounds(800, 718, 560, 20);
        container.add(commandLine);
        PrizmJ.setCommand(commandLine);

        pack();
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(DesktopLauncher::new);
    }
}
