package Simulator;

import java.util.ArrayList;

/*
 * The interface for the path finding logic that the different NPCs will use.
 * Author: Sean Moor
 */
public interface brain {
    //Make a decision. Returns the direction that they want to move.
    public char move(char[][] terrain, ArrayList<worldObject> objects, int x, int y, int playerX, int playerY);
}
