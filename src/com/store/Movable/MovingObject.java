package com.store.Movable;

import com.googlecode.lanterna.TextColor;

public class MovingObject {
    public int row;
    public int position;
    public int size;
    public boolean rightMoving;

    public TextColor color;

    public double speed;

    protected String symbol;

    public MovingObject() {
        symbol = "@";
    }

    public MovingObject(int size, int position, int row, boolean movingDirection, String symbol, double speed, TextColor color) {
        this.row = row; this.position = position;
        this.size = size; this.rightMoving = movingDirection;
        this.symbol = symbol; this.speed = speed;

        this.color = color;
    }

    public String toString() {
        String result = "";
        for(int i = 0; i < size; i++) {
            result += symbol;
        }

        return result;
    }
}
