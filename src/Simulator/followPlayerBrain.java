package Simulator;

import java.util.ArrayList;
/*
 * A class that will use Dijkstra's algorithm to make the NPC follow the player.
 * Author: Sean Moor
 */

/*
 * 				STILL NEED TO IMPLEMENT THIS AI LOGIC.
 */

public class followPlayerBrain implements brain {

    @Override
    public char move(char[][] terrain, ArrayList<worldObject> objects, int x, int y, int playerX, int playerY) {
        //The array that marks whether or not a cell has been visited.
        boolean[][] visited = new boolean[terrain.length][terrain[0].length];
        for(int i=0; i<terrain.length; i++) for(int j=0; j<terrain[0].length; j++) visited[i][j] = false;

        //The array of distances.
        int[][] dists = new int[terrain.length][terrain[0].length];
        for(int i=0; i<terrain.length; i++) for(int j=0; j<terrain[0].length; j++) dists[i][j] = Integer.MAX_VALUE;

        //The list that will hold unvisited points with a known distance.
        ArrayList<point> unvisited = new ArrayList<point>();

        point cur = new point(playerX, playerY);
        point target = new point(x,y);
        dists[cur.x][cur.y] =  0;

        while(true) {
            //Mark cur as visited.
            visited[cur.x][cur.y] = true;
            //Look at each of the connected nodes.
            try {
                if(visited[cur.x+1][cur.y] == false) {
                    if(dists[cur.x+1][cur.y] >  dists[cur.x][cur.y]+1) {
                        dists[cur.x+1][cur.y] = dists[cur.x][cur.y]+1;
                        unvisited.add(new point(cur.x+1, cur.y));
                    }
                }
            }catch(Exception e) {

            }
            try {
                if(visited[cur.x-1][cur.y] == false) {
                    if(dists[cur.x-1][cur.y] >  dists[cur.x][cur.y]+1) {
                        dists[cur.x-1][cur.y] = dists[cur.x][cur.y]+1;
                        unvisited.add(new point(cur.x-1, cur.y));
                    }
                }
            }catch(Exception e) {

            }
            try {
                if(visited[cur.x][cur.y+1] == false) {
                    if(dists[cur.x][cur.y+1] >  dists[cur.x][cur.y]+1) {
                        dists[cur.x][cur.y+1] = dists[cur.x][cur.y]+1;
                        unvisited.add(new point(cur.x, cur.y+1));
                    }
                }
            }catch(Exception e) {

            }
            try {
                if(visited[cur.x][cur.y-1] == false) {
                    if(dists[cur.x][cur.y-1] >  dists[cur.x][cur.y]+1) {
                        dists[cur.x][cur.y-1] = dists[cur.x][cur.y]+1;
                        unvisited.add(new point(cur.x, cur.y-1));
                    }
                }
            }catch(Exception e) {

            }
            break; //TODO: Remove.
        }

        return 'n';
    }

    //A helper sub class for Dijkstra's algorithm.
    private class point{
        public int x;
        public int y;
        public point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(point p) {
            if(p.x == this.x && p.y == this.y)return true;
            else return false;
        }
    }

}
