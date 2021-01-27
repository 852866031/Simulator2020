package Simulator;

import java.util.ArrayList;

// Author: Sean Moor, updated by Jiaxuan Chen
public class worldObject extends Obj{
    //The position of the object in the world.
    //Constructor.
    public worldObject(int x, int y, String name, double mass, boolean movable, cellList cellList) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.movable=movable;
        this.mass=mass;
        this.cellList=cellList;
    }

    public String toString() {
        return x+" , "+y+" , "+name;
    }
    private class objectInfo {

    }
}