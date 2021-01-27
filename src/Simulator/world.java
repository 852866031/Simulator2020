package Simulator;

import java.io.IOException;
import java.util.ArrayList;

// Author: Sean Moor
public interface world {
    //Get the number of rows in the map.
    public int getHeight();

    //The number of columns, the width of the map.
    public int getWidth();

    //A function that will get what is at a certain region.
    public char getTerrain(int x, int y);

    //Save the world to a specified path.
    public void saveFile(String path) throws IOException;

    //A function that will display the simulation.
    public void displaySim();

    //A function that will update the simulation based on user input.
    int[] updateSim(Obj obj, char input) throws IOException;

    //A function that will return an arraylist of world objects.
    public ArrayList<worldObject> getObjects();

    //A function that will add an object to the world.
    public void addObject(worldObject obj);

    //Remove an object from the world.
    public void removeObject(worldObject obj);

    //A function that will get all of the NPCs in this world.
    public ArrayList<NPC> getNPCs();

    //A function that will add an NPC to the world.
    public void addNPC(NPC newNPC);

    //A function that will remove an NPC from the world.
    public void removeNPC(NPC theNPC);

}
