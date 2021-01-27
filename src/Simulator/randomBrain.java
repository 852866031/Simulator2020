package Simulator;

import java.util.ArrayList;
import java.util.Random;
/*
 * A decision making idea that will cause the NPC to move randomly.
 * Author: Sean Moor
 */

public class randomBrain implements brain {

    @Override
    public char move(char[][] terrain, ArrayList<worldObject> objects, int x, int y, int playerX, int playerY) {
        Random rng = new Random();
        int dir = rng.nextInt(4);
        switch(dir) {
            case 0:
                if(y==0) return 'x';
                char newTer1 = terrain[y-1][x];
                if(newTer1 != '.' && newTer1 != 'w')return 'n';
            case 1:
                if(y==terrain[0].length-1) return 'x';
                char newTer2 = terrain[y+1][x];
                if(newTer2 != '.' && newTer2 != 'w')return 's';
            case 2:
                if(x==0) return 'x';
                char newTer3 = terrain[y][x-1];
                if(newTer3 != '.' && newTer3 != 'w')return 'e';
            case 3:
                if(x==terrain.length-1) return 'x';
                char newTer4 = terrain[y][x+1];
                if(newTer4 != '.' && newTer4 != 'w')return 'w';
            default:
                return 'x';
        }
    }

}
