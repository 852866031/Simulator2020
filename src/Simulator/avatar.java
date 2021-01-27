package Simulator;

import java.util.ArrayList;

// Author: Jiaxuan Chen
public class avatar extends Obj {
    int[] avatarDir;
    public ArrayList<unitWave> receiveBasket=new ArrayList<>();
    public avatar(int[] pos, int[] dir, double mass) {
        this.avatarDir=dir;
        this.x=pos[0];
        this.y=pos[1];
        this.movable=true;
        this.mass=mass;
    }

    public int[] getAvatarDir() {
        return avatarDir;
    }

    public void obtainInfo(Obj obj) {

    }
    public void obtainInfo(unitWave unitWave) {
        this.receiveBasket.add(unitWave);
    }

    public void resetReceiveBasket() {this.receiveBasket=new ArrayList<>(); }

    public boolean comparePos(int[] pos){
        if(pos[0]==x && pos[1]==y) return true;
        return false;
    }
    public void turnTo(char dir) { ;
        if(dir=='a') this.avatarDir=new int[]{0, -1};
        else if(dir=='d') this.avatarDir=new int[]{0, 1};
        else if(dir=='w') this.avatarDir=new int[]{-1, 0};
        else if(dir=='s') this.avatarDir=new int[]{1, 0};
        else if(dir=='q') this.avatarDir=new int[]{-1, -1};
        else if(dir=='e') this.avatarDir=new int[]{-1, 1};
        else if(dir=='c') this.avatarDir=new int[]{1, 1};
        else if(dir=='z') this.avatarDir=new int[]{1, -1};
    }
}
