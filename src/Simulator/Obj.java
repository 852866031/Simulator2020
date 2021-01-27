package Simulator;



import java.io.IOException;
import java.util.ArrayList;

// Author: Sean Moor and Jiaxuan Chen
public class Obj {
    public int x;
    public int y;
    public String imagePath;
    public String name;
    public double mass;
    public cellList cellList;
    public ArrayList<force> forces=new ArrayList<>();
    public String toString() {
        return x+" , "+y+" , "+imagePath+" , "+name;
    }
    public double collisionFactor=0.5;
    public boolean movable;
    public kinematics kine;

    public Obj() {

    }
    public void initialize(cellList cellList){
        this.cellList=cellList;
        kine=new kinematics(this, 0, -1);
    }

    private int[] moveAvatar(avatar player, char input, map m) throws IOException {
        input = Character.toLowerCase(input);
        int[] oldPos=new int[]{player.x, player.y};
        int[] pos=new int[]{player.x, player.y};
        int[] dir=player.getAvatarDir();
        if(input == 'w') {
            if(dir[0] == -1 && dir[1]==0) {
                if(pos[0]>0 && cellList.getCellAt(pos[0]-1,pos[1]).getType()!='w' && !cellList.getCellAt(pos[0]-1,pos[1]).hasObj()) {
                    pos[0] -= 1;
                    this.cellList.setPlayerAt(oldPos, pos);
                }
            }else {
                player.turnTo('w');
            }
        }else if(input == 's') {
            if(dir[0] == 1 && dir[1]==0) {
                if(pos[0]<m.height-1 && cellList.getCellAt(pos[0]+1,pos[1]).getType()!='w'&& !cellList.getCellAt(pos[0]+1,pos[1]).hasObj()) {
                    pos[0] += 1;
                    this.cellList.setPlayerAt(oldPos, pos);
                }
            }else {
                player.turnTo('s');
            }
        }else if(input == 'a') {
            if(dir[0] == 0 && dir[1]==-1) {
                if(pos[1]>0 && cellList.getCellAt(pos[0],pos[1]-1).getType()!='w' && !cellList.getCellAt(pos[0],pos[1]-1).hasObj()) {
                    pos[1] -= 1;
                    this.cellList.setPlayerAt(oldPos, pos);
                }
            }else {
                player.turnTo('a');
            }
        }
        else if(input == 'd') {
            if(dir[0] == 0 && dir[1]==1) {
                if(pos[1]<m.width-1 && cellList.getCellAt(pos[0],pos[1]+1).getType()!='w' && !cellList.getCellAt(pos[0],pos[1]+1).hasObj()){
                    pos[1] += 1;
                    this.cellList.setPlayerAt(oldPos, pos);
                }
            }else {
                player.turnTo('d');
            }
        }
        else if(input == 'q') {
            if(dir[0] == -1 && dir[1]==-1) {
                if((pos[0] > 0 && pos[1] > 0) && cellList.getCellAt(pos[0]-1,pos[1]-1).getType()!='w' && !cellList.getCellAt(pos[0]-1,pos[1]-1).hasObj()){
                    pos[0]--;
                    pos[1]--;
                    this.cellList.setPlayerAt(oldPos, pos);
                }
            }else {
                player.turnTo('q');
            }
        }
        else if(input == 'e') {
            if(dir[0] == -1 && dir[1]==1) {
                if((pos[0] > 0 && pos[1]< m.width - 1) && cellList.getCellAt(pos[0]-1,pos[1]+1).getType()!='w' && !cellList.getCellAt(pos[0]-1,pos[1]+1).hasObj()){
                    pos[0]--;
                    pos[1]++;
                    this.cellList.setPlayerAt(oldPos, pos);
                }
            }else {
                player.turnTo('e');
            }
        }
        else if(input == 'z') {
            if(dir[0] == 1 && dir[1]==-1) {
                if((pos[0] <m.height-1 && pos[1]>0) && cellList.getCellAt(pos[0]+1,pos[1]-1).getType()!='w' && !cellList.getCellAt(pos[0]+1,pos[1]-1).hasObj()){
                    pos[0]++;
                    pos[1]--;
                    this.cellList.setPlayerAt(oldPos, pos);
                }
            }else {
                player.turnTo('z');
            }
        }
        else if(input == 'c') {
            if(dir[0] == 1 && dir[1]==1) {
                if((pos[0] <m.height-1 && pos[1]<m.width-1) && cellList.getCellAt(pos[0]+1,pos[1]+1).getType()!='w' && !cellList.getCellAt(pos[0]+1,pos[1]+1).hasObj()){
                    pos[0]++;
                    pos[1]++;
                    this.cellList.setPlayerAt(oldPos, pos);
                }
            }else {
                player.turnTo('c');
            }
        }
        else {
            //We should not get here.
        }
        return oldPos;
    }
    public int[] moveObj(char input, map m) throws IOException {
        if(this instanceof avatar) return moveAvatar((avatar) this, input, m);
        else{
            input = Character.toLowerCase(input);
            int[] oldPos=new int[]{this.x, this.y};
            int[] pos=new int[]{this.x, this.y};
            if(input == 'w') {
                if(pos[0]>0 && cellList.getCellAt(pos[0]-1,pos[1]).getType()!='w'&& !cellList.getCellAt(pos[0]-1,pos[1]).hasObj()) {
                    pos[0] -= 1;
                    this.cellList.setObjAt(this, pos);
                }
            }
            else if(input == 's') {
                if(pos[0]<m.height-1 && cellList.getCellAt(pos[0]+1,pos[1]).getType()!='w'&& !cellList.getCellAt(pos[0]+1,pos[1]).hasObj()) {
                    pos[0] += 1;
                    this.cellList.setObjAt(this, pos);
                }
            }
            else if(input == 'a') {
                if(pos[1]>0 && cellList.getCellAt(pos[0],pos[1]-1).getType()!='w' && !cellList.getCellAt(pos[0],pos[1]-1).hasObj()) {
                    pos[1] -= 1;
                    this.cellList.setObjAt(this, pos);
                }
            }
            else if(input == 'd') {
                if(pos[1]<m.width-1 && cellList.getCellAt(pos[0],pos[1]+1).getType()!='w'&& !cellList.getCellAt(pos[0],pos[1]+1).hasObj()){
                    pos[1] += 1;
                    this.cellList.setObjAt(this, pos);
                }
            }
            else if(input == 'q') {
                if((pos[0] > 0 && pos[1] > 0) && cellList.getCellAt(pos[0],pos[1]+1).getType()!='w'&& !cellList.getCellAt(pos[0]-1,pos[1]-1).hasObj()){
                    pos[0]--;
                    pos[1]--;
                    this.cellList.setObjAt(this, pos);
                }
            }
            else if(input == 'e') {
                if((pos[0] > 0 && pos[1]< m.width - 1) && cellList.getCellAt(pos[0],pos[1]+1).getType()!='w'&& !cellList.getCellAt(pos[0]-1,pos[1]+1).hasObj()){
                    pos[0]--;
                    pos[1]++;
                    this.cellList.setObjAt(this, pos);
                }
            }
            else if(input == 'z') {
                if((pos[0] <m.height-1 && pos[1]>0) && cellList.getCellAt(pos[0],pos[1]+1).getType()!='w'&& !cellList.getCellAt(pos[0]+1,pos[1]-1).hasObj()){
                    pos[0]++;
                    pos[1]--;
                    this.cellList.setObjAt(this, pos);
                }
            }
            else if(input == 'c') {
                if((pos[0] <m.height-1 && pos[1]<m.width-1) && cellList.getCellAt(pos[0],pos[1]+1).getType()!='w'&& !cellList.getCellAt(pos[0]+1,pos[1]+1).hasObj()){
                    pos[0]++;
                    pos[1]++;
                    this.cellList.setObjAt(this, pos);
                }
            }
            else {
                //We should not get here.
            }
            return oldPos;
        }
    }

    public boolean withFriction(){
        for(force f : this.forces){
            if(f instanceof friction) return true;
        }
        return false;
    }
    public void clearMotive(){
        motiveForce mf=null;
        for(force f : this.forces){
            if(f instanceof motiveForce) mf=(motiveForce)f;
        }
        if(mf!=null) this.forces.remove(mf);
    }
}