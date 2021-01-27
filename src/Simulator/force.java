package Simulator;

import java.util.ArrayList;

// Author: Jiaxuan Chen
public class force {
    public double magnitude;
    public Obj from;
    public Obj to;
    public int angle;

    //this is the angle between the force and the x-axis
    /*
                         ↓
                         ↓
               180-270°  ↓ 90-180°
                 -------------------→    y axis
                         ↓
               270-360°  ↓ 0-90°
                         ↓
                          x axis
     */
    //**********important!!!!!!!!!!!
    //when you want to create a new force object
    //try not to do "force f=new force(...)";
    //pls do "applyForce(from, to, new force(...))";
    //because applyForce will automatically update the friction
    //when from or to obj doesn't exist like friction( from is from the floor) you can fill its place with null
    //*******************************************
    public static void applyForce(force f, Obj from, Obj to){
        if(to!=null) {
            if(f!=null) to.forces.add(f);
            if(!to.withFriction()) {
                jointForceOf(to);
            }
            if(to.kine!=null) to.kine.updateJointForce();
        }
        if(from!=null) {
            if(f!=null) from.forces.add(f.getReaction());
            if(!from.withFriction()) {
                jointForceOf(from);
            }
            if(from.kine!=null) from.kine.updateJointForce();
        }
    }
    public force(Obj from, Obj to, double magnitude, double angle){
        if(angle<0 && angle!=-1) {
            while(angle<0) angle+=360;
        }
        else if(angle>360) {
            while(angle>360) angle-=360;
        }
        this.magnitude=magnitude;
        this.angle= (int) Math.round(angle);
        this.from=from;
        this.to=to;

    }
    public force getReaction(){
        double dir=this.angle+180;
        if(dir>360) dir-=360;
        return new force(this.to, this.from, this.magnitude, dir);
    }
    public static force jointForceOf(Obj obj){
        //this method will analyze the force on an object by FBD

        if(obj.forces.size()==0) return new force(null, null, 0, -1);
        if(obj.forces.size()!=1)  friction.removeFriction(obj);
        double x=0, y=0;
        //x is the magnitude of force on x axis.
        //y is the magnitude of force on y axis.

        for(force f : obj.forces){
            if(f.angle>=0){
                x+=f.magnitude*Math.cos(f.angle*Math.PI/180);
                y+=f.magnitude*Math.sin(f.angle*Math.PI/180);
            }
        }
        double magnitude=Math.sqrt(x*x+y*y);
        double angle= angleConvert(x, y);
        if(magnitude==0) angle=-1;
        force jf=new force(null, null, magnitude, angle);
        if(!obj.withFriction() && obj.forces.size()!=0){
            force jfRe=jf.getReaction();
            double MaxFriction=obj.mass*obj.cellList.getCellAt(obj.x, obj.y).getFrictionIndex()*9.8;
            if(MaxFriction>=jf.magnitude) {
                obj.forces.add(new friction(obj, jf.magnitude, jfRe.angle));
                magnitude=0;
                angle=-1;
            }
            else {
                obj.forces.add(new friction(obj, MaxFriction, jfRe.angle));
                magnitude-=MaxFriction;
            }
        }
        return new force(null, null, magnitude, angle);

    }
    public double directionConvert(Obj from, Obj to){ return angleConvert(to.x-from.x, to.y-from.y); }
    public static double angleConvert(double x, double y){
        if(x==0 && y==0) return -1; //two object is on the same position
        else if(x==0 && y<0) return 270;
        else if(x==0 && y>0) return 90;
        else if(x<0 && y==0) return 180;
        else if(x>0 && y==0) return 0;
        else if(x>0 && y>0) return Math.atan(y/x)*180/Math.PI;
        else if(x>0 && y<0) return Math.atan(Math.abs(x/y))*180/Math.PI+270;
        else if(x<0 && y>0) return Math.atan(Math.abs(x/y))*180/Math.PI+90;
        else if(x<0 && y<0) return Math.atan(Math.abs(y/x))*180/Math.PI+180;
        return -1;// error happened, we should not visit here
    }
    public static int[] angleToDirection(double angle) {
        int[] dir;
        if(angle>=337.5 || angle<22.5) dir= new int[]{1, 0};
        else if(angle>=22.5 && angle<67.5) dir=new int[]{1, 1};
        else if(angle>=67.5 && angle<112.5) dir=new int[]{0, 1};
        else if(angle>=112.5 && angle<157.5) dir=new int[]{-1,1};
        else if(angle>=112.5 && angle<202.5) dir=new int[]{-1,0};
        else if(angle>=202.5 && angle<247.5) dir=new int[]{-1,-1};
        else if(angle>=247.5 && angle<292.5) dir=new int[]{0, -1};
        else dir=new int[]{1,-1};
        return dir;
    }
    public static double directionToAngle(int[] dir) {
        if (dir[0] == 0 && dir[1] == 0) return -1;
        else if (dir[0] == 1 && dir[1] == 0) return 0;
        else if (dir[0] == 1 && dir[1] == 1) return 45;
        else if (dir[0] == 0 && dir[1] == 1) return 90;
        else if (dir[0] == -1 && dir[1] == 1) return 135;
        else if (dir[0] == -1 && dir[1] == 0) return 180;
        else if (dir[0] == -1 && dir[1] == -1) return 225;
        else if (dir[0] == 0 && dir[1] == -1) return 270;
        else return 315;
    }
    public void setMagnitude(double magnitude){ this.magnitude=magnitude;}
    public void setAngle(double angle){
        if(angle<0) {
            while(angle<0) angle+=360;
        }
        else if(angle>360) {
            while(angle>360) angle-=360;
        }
        this.angle= (int) Math.round(angle);
    }
    public void updateAngle(){ this.angle= (int) Math.round(directionConvert(this.from, this.to));}

}
