package com.groupiron;

public class TileData {
    private int x;
    private int y;
    private int plane;
    private String player;

    public TileData(int x, int y, int plane, String player) {
        this.x = x;
        this.y = y;
        this.plane = plane;
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlane() {
        return plane;
    }

    public String getPlayer() {
        return player;
    }
}
