package com.store;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.*;
import com.store.Movable.MovingObject;

import java.io.IOException;

public class Game extends Thread {
    boolean isRunning = false;
    private Terminal t;
    private TextGraphics g;

    public MovingObject[] objects;

    public Game() {
        isRunning = true;
        DefaultTerminalFactory f = new DefaultTerminalFactory();
        try {
            t = f.createTerminal();
            t.setCursorVisible(false);
            g = t.newTextGraphics();
        } catch (IOException e) {
            e.printStackTrace();
        }

        objects = new MovingObject[] {
          new MovingObject(3, 3, 1, true,"@", 1, TextColor.ANSI.WHITE),
                new MovingObject(3, 3, 2, true,"@", 2, TextColor.ANSI.GREEN),
                new MovingObject(3, 3, 3, true,"@", 3, TextColor.ANSI.RED)
        };
    }

    public int maxColumns() throws IOException {
        return t.getTerminalSize().getColumns();
    }

    public void run() {
        long now; // timestamp в началото на цикъла
        long updateTime; // timestamp след като
        long wait; // толкова време е останало и трябва да се компенсира

        final int TARGET_FPS = 30;
        // 1s = 1,000,000,000ns
        final int nanosecond = 1000000000;
        final int millisecond = nanosecond / 1000000;
        final long OPTIMAL_TIME = nanosecond / TARGET_FPS; // xFPS frames per second 30 per 1s 1 / 30
        // имаме 1секунда за кадър

        while (true) {
            now = System.nanoTime();
            try {
                input(); // И ТРИТЕ МЕТОДА ТРЯБВА ДА СА NON-BLOCKING
                update(); // в нашия случай бихме имали проблем главно с input handling и rendering
                render(); // i.e. да се сетим какво става като кажем Console.readLine() ? ( програмата спира докато не въведем нов ред )

                // 1секунда = 1,000,000,00 нс
                // 1мс = 1,000,000нс
                // 1секунда = 1,000 мс

                updateTime = System.nanoTime() - now;
                wait = (OPTIMAL_TIME - updateTime) / 1000000;

                if(wait > 0.0) Thread.sleep(wait);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void input() {

    }

    public void update() throws IOException {
        for (MovingObject obj : objects) {
            if(obj.rightMoving) obj.position += obj.speed;
            else obj.position -= obj.speed;

            if(obj.position + obj.size >= maxColumns()) obj.rightMoving = false;
            else if (obj.position <= 0) obj.rightMoving = true;
        }
    }

    public void render() throws IOException {
        t.clearScreen();
        for (MovingObject obj : objects) {
            t.setForegroundColor(obj.color);
            g.putString(obj.position, obj.row, obj.toString());
        }
        t.flush();
    }
}
