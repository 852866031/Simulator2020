package Simulator;

import java.util.ArrayList;

// Author: Jiaxuan Chen
public class cell {
    private int[] position;
    private char type;
    private int height;
    private float temperature;
    private double frictionIndex;
    private Obj obj;
    private ArrayList<NPC> npcs;
    //private avatar player;
    private float angle;
    //private ArrayList<keypair<information, density>> informationContained;

    public cell(int[] position, int height, char type, float temperature, float angle) {
        this.position=position;
        this.type=type;
        this.height=height;
        this.obj =null;
        this.npcs=new ArrayList<>();
        this.temperature=temperature;
        this.angle=angle;
        ////////////////////////////////////
        if(type=='s') this.frictionIndex=0;
        else if(type=='g') this.frictionIndex=1;
        //to be modified
    }
    ///////////////////////get
    public int[] getPosition() {return position; }
    public char getType() {return this.type; }
    public int getHeight() { return this.height;}
    public float getTemperature() {return this.temperature;}
    public double getFrictionIndex() {return this.frictionIndex;}
    public Obj getMovableobject() {return this.obj;}

    public ArrayList<NPC> getNpcs() {return this.npcs;}
    public float getAngle() { return angle; }
    public avatar getPlayer(){
        if(this.obj instanceof avatar) return (avatar) obj;
        return null;
    }

    ////////////////////////set
    public void setWater(){
        this.type='w';
    }
    public void setPlayer(avatar player) { this.obj =player; }
    public boolean setObject(Obj worldObject) {
        if(this.obj ==null) {
            this.obj =worldObject;
            return true;
        }
        else return false;
    }
    public void setTemperature(float temperature) {this.temperature=temperature; }

    public void setFrictionIndex(double frictionIndex) {this.frictionIndex = frictionIndex;}

    ////////////////////////check
    public boolean checkPlayer() {
        if(this.obj ==null) return false;
        if(this.obj instanceof avatar) return true;
        return false;
    }
    public boolean checkFalling() {
        return false;
    }
    public boolean hasObj(Obj obj) {
        if(this.obj==null) return false;
        if(this.obj.equals(obj)) return true;
        return false;
    }
    public boolean hasObj(){
        if(this.obj!=null) return true;
        else return false;
    }
    public boolean hasObjButNotPlayer(){
        if(this.obj!=null && !(this.obj instanceof avatar)) return true;
        return false;
    }
}
