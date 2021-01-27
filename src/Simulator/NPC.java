package Simulator;

import java.util.ArrayList;

// Author: Sean Moor
public class NPC extends Obj{
    //The position of the NPC in the world.
    //The direction that the NPC is facing.
    char direction;
    //The type of decision making the NPC will use.
    String brain;
    //The name of the NPC.
    String name;
    //The brain object of the NPC.
    brain npcLogic;
    //The default constructor.
    public NPC() {
        //set the position to 5,5 and direction to south.
        x = 5;
        y = 5;
        direction = 's';
        brain = "random";
        name = "NPC";
        npcLogic = new randomBrain();
    }

    //The better constructor.
    public NPC(int x, int y, char dir, String brain, String name) {
        this.x = x;
        this.y = y;
        this.direction = dir;
        this.brain = brain;
        this.name = name;
        switch(brain) {
            case "random":
                this.npcLogic = new randomBrain();
                break;
            default: //Default to random behavior.
                this.npcLogic = new randomBrain();
        }
    }

    //A function to make a move.
    public void makeMove(char[][] terrain, int playerX, int playerY, ArrayList<worldObject> objects) {
        //Get a move from the brain.
        char m = npcLogic.move(terrain, objects, this.x, this.y, playerX, playerY);

        //Move the character if we can.
        switch(m) {
            case 'n':
                if(direction=='n') this.y -= 1;
                else direction = 'n';
                break;
            case 's':
                if(direction=='s') this.y += 1;
                else direction = 's';
                break;
            case 'e':
                if(direction=='e') this.x -= 1;
                else direction = 'e';
                break;
            case 'w':
                if(direction=='w') this.x += 1;
                else direction = 'w';
                break;
            default:
                //Do nothing.
        }

    }

    public String toString() {
        return "" + this.x + " , " + this.y + " , " + direction + " , " + name + " , " + brain;
    }
}

