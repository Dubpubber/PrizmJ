/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.console;

import com.prizmj.display.PrizmJ;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.prizmj.display.PrizmJ.cmdLine;

/**
 * com.prizmj.console.Command in PrizmJ
 */
public class CommandFactory {

    private PrizmJ prizmJ;

    private String command;
    private String[] args;

    public CommandFactory(PrizmJ prizmJ) {
        this.prizmJ = prizmJ;
        cmdLine.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && cmdLine.getCaretPosition() == 2)
                    e.consume();
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // User intends to submit a command!
                    build(cmdLine.getText().substring(2).split(" "));
                    executeCommand();
                }
            }
        });
        cmdLine.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!cmdLine.getText().startsWith("> "))
                    PrizmJ.clearCommandText();
                cmdLine.setCaretPosition(2);
            }
        });
    }

    public void build(String[] command) {
        setCommand(command[0]);
        setArgs(new String[command.length - 1]);
        if((command.length - 1) != 0) {
            for (int i = 1; i < command.length; i++) {
                args[i - 1] = command[i];
            }
        }
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArgument(int index) {
        return (index > args.length) ? "" : args[index];
    }

    private void executeCommand() {
        switch (getCommand()) {
            default:
                PrizmJ.writeToConsole("Unrecognized command: " + toString());
                break;
        }
    }

    @Override
    public String toString() {
        return (command == null || getCommand().length() == 0) ? "Empty Command" : getCommand();
    }
}