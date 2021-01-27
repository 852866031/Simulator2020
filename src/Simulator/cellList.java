package Simulator;

import java.util.ArrayList;

// Author: Sean Moor, updated by Jiaxuan Chen
public class cellList {
    public int row;
    public int col;
    private ArrayList<cell> cells=new ArrayList<>();
    private avatar player;
    public cellList(int[][] height, char[][] terrain, avatar player) {
        this.row=height.length;
        this.col=height[0].length;
        this.player=player;
        int c=0, r=0;
        int[] position=new int[2];
        while(r<this.row) {
            position[0]=r;
            while(c<this.col) {
                position[1]=c;
                char type;
                float temperature=0;
                float angle=0;
                cell cur=new cell(position, height[r][c], terrain[r][c], temperature, angle);
                cells.add(cur);
                c++;
            }
            c=0;
            r++;
        }
    }
    public ArrayList<cell> getCells() {return this.cells;}
    public cell getCellAt(int x, int y) {
        int index=x*this.col+y;
        return this.cells.get(index);
    }
    public cell getCellAt(int[] pos) {
        int index=pos[0]*this.col+pos[1];
        return this.cells.get(index);
    }
    public avatar getPlayer() { return player; }
    public void setPlayerAt(int[] oldPos, int[] pos) {
        getCellAt(oldPos).setPlayer(null);
        this.player.x=pos[0];
        this.player.y=pos[1];
        getCellAt(pos).setPlayer(this.player);
    }
    public void setObjAt(Obj obj, int[] pos){
        obj.x=pos[0];
        obj.y=pos[1];
        getCellAt(pos).setObject(obj);
    }
}
