package Simulator;

import java.io.IOException;
import java.util.ArrayList;

// Author: Sean Moor
public class map implements world{

    //The height of the map.
    int height;
    //The width of the map.
    int width;
    //The actual map.
    char worldMap[][];
    //The object list.
    ArrayList<worldObject> objectList = new ArrayList<worldObject>();
    //The list of NPCs.
    ArrayList<NPC> npcList = new ArrayList<NPC>();
    public cellList cellList;
    public avatar player;
    //Variables related to where the player avatar is.

    //A constructor that will take a pre made terrain map. (Usually from our generator class.)
    public map(int height, int width, char[][] ter, int[][] matrix, avatar avatar){
        this.height = height;
        this.width = width;
        this.worldMap = ter;
        this.player=avatar;
        this.cellList=new cellList(matrix, ter, this.player);
    }

    public avatar getPlayer() { return player; }

    //Interface methods.
    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public char getTerrain(int x, int y) {
        return this.worldMap[x][y];
    }

    @Override
    public void displaySim() {
        for(cell cell : this.cellList.getCells()) {
            if(cell.checkPlayer()) {
                int[] dir=cell.getPlayer().getAvatarDir();
                if(dir[0] == 1 && dir[1] == 0) System.out.print('V' + " ");
                else if(dir[0] == -1 && dir[1] == 0) System.out.print('^' + " ");
                else if(dir[0] == 0 && dir[1] == 1) System.out.print('>' + " ");
                else if(dir[0] == 0 && dir[1] == -1) System.out.print('<' + " ");
                else System.out.print('V' + " ");
            }
            else {
                //Draw the terrain.
                System.out.print(cell.getType() + " ");
            }
            if(cell.getPosition()[1]==this.width-1) System.out.println();
        }
    }

    @Override
    public int[] updateSim(Obj mobj, char input) throws IOException {
        int[] oldPos=mobj.moveObj(input, this);
        //Update each of the NPCs in the world.
        for(NPC c : npcList) {
            c.makeMove(worldMap, mobj.x, mobj.y, objectList);
        }
        return oldPos;
    }

    @Override
    public void saveFile(String path) throws IOException {
        // TODO Auto-generated method stub

    }

    //A function to get the the list of world objects.
    public ArrayList<worldObject> getObjects(){
        return objectList;
    }

    //A function that will add a world object.
    public void addObject(worldObject obj) {
        objectList.add(obj);
    }

    //Remove an object from the world.
    public void removeObject(worldObject obj) {
        objectList.remove(obj);
    }


    @Override
    public ArrayList<NPC> getNPCs() {
        return npcList;
    }


    @Override
    public void addNPC(NPC newNPC) {
        npcList.add(newNPC);
    }


    @Override
    public void removeNPC(NPC theNPC) {
        npcList.remove(theNPC);
    }



}
