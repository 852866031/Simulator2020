package Simulator;

import Simulator.GUI.SimController;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

// Author: Jiaxuan Chen
public class kinematics {
    //the moving object
    public Obj mobj;
    //force
    public force jointForce;
    //velocity;
    public double speed;
    public int speedAngle; //when speed angle is -1 it not only means that the obj is not moving but also means that the joint force on it is 0
    //acceleration;
    public double acc;
    public int accAngle;
    //this is the length time that the ointject is moving
    public int energy;


    public kinematics(Obj mobj, double speed, double speedAngle) {
        this.mobj = mobj;
        this.speed = speed;
        this.speedAngle = (int) speedAngle;//if the speed is zero pls set the angle to be -1
        this.jointForce = force.jointForceOf(mobj);
        //we only have 8 directions on a map so we need to convert the angle of the force to one nearest direction
        this.accAngle = (int) this.jointForce.angle;
        //f=ma a=f/m
        this.acc = this.jointForce.magnitude / mobj.mass;
        this.mobj.kine = this;
        this.energy = (int) ( 0.5 * this.mobj.mass * this.speed * this.speed);
    }


    public void updateJointForce(){
        this.jointForce=force.jointForceOf(this.mobj);
        this.accAngle = (int) this.jointForce.angle;
        if(this.speedAngle==-1) this.speedAngle= (int) this.jointForce.angle;
        this.acc = this.jointForce.magnitude / mobj.mass;
        this.mobj.kine = this;
        this.energy = (int) (0.5 * this.mobj.mass * this.speed * this.speed);
    }


    public static boolean collision(Obj a, Obj b) {
        if ((a.movable == false && b.movable == false) ||
                (a.kine.speedAngle==-1 && b.kine.speedAngle==-1)) return false;
        if (a.movable == false || b.movable == false) {
            Obj moving;
            if (a.movable) moving = a;
            else moving = b;
            if (moving.kine.speedAngle==-1) return false;
            int[] dir = force.angleToDirection(moving.kine.speedAngle);
            moving.kine.wallCollision(force.angleToDirection(moving.kine.speedAngle), new int[]{moving.x, moving.y});
            moving.kine.energyDecline();
        } else {
            double totalMass = a.mass + b.mass;
            if(a.kine.speedAngle==-1 && b.kine.speedAngle==-1){
                if(force.jointForceOf(a).magnitude==0 && force.jointForceOf(b).magnitude==0) return false;
                else{
                    Obj joint=new Obj();
                    joint.forces.add(force.jointForceOf(a));
                    joint.forces.add(force.jointForceOf(b));
                    force jfj=force.jointForceOf(joint);
                    double ja=jfj.magnitude/totalMass;
                    a.kine.speedAngle= (int) jfj.angle;
                    a.kine.accAngle= (int) jfj.angle;
                    b.kine.speedAngle= (int) jfj.angle;
                    b.kine.accAngle= (int) jfj.angle;
                    a.kine.acc=ja;
                    b.kine.acc=ja;
                }
            }
            else{
                double aSpeedX = (((a.mass - b.mass) * a.kine.speed * Math.cos(a.kine.speedAngle*Math.PI/180)) +
                        (2 * b.mass * b.kine.speed * Math.cos(b.kine.speedAngle*Math.PI/180))) / totalMass;
                double aSpeedY = (((a.mass - b.mass) * a.kine.speed * Math.sin(a.kine.speedAngle*Math.PI/180)) +
                        (2 * b.mass * b.kine.speed * Math.sin(b.kine.speedAngle*Math.PI/180))) / totalMass;
                double bSpeedX = (((b.mass - a.mass) * b.kine.speed * Math.cos(b.kine.speedAngle*Math.PI/180)) +
                        (2 * a.mass * a.kine.speed * Math.cos(a.kine.speedAngle*Math.PI/180))) / totalMass;
                double bSpeedY = (((b.mass - a.mass) * b.kine.speed * Math.sin(b.kine.speedAngle*Math.PI/180)) +
                        (2 * a.mass * a.kine.speed * Math.sin(a.kine.speedAngle*Math.PI/180))) / totalMass;
                double aSpeed = Math.sqrt(aSpeedX * aSpeedX + aSpeedY * aSpeedY);
                double bSpeed = Math.sqrt(bSpeedX * bSpeedX + bSpeedY * bSpeedY);
                double aAngle = force.angleConvert(aSpeedX, aSpeedY);
                double bAngle = force.angleConvert(bSpeedX, bSpeedY);
                //decompose the velocity and calculate the new velocity by the conservation of momentum
                a.kine.speed = aSpeed;
                a.kine.speedAngle = (int) aAngle;
                b.kine.speed = bSpeed;
                b.kine.speedAngle = (int) bAngle;
            }
        }
        return true;
    }


    public void reverse() {
        int[] dir = force.angleToDirection(this.speedAngle);
        if (dir[0] == -1 && dir[1] == 0) {
            this.speedAngle = 0;
        } else if (dir[0] == 1 && dir[1] == 0) {
            this.speedAngle = 180;
        } else if (dir[0] == 0 && dir[1] == 1) {
            this.speedAngle = 270;
        } else if (dir[0] == 0 && dir[1] == -1) {
            this.speedAngle = 90;
        } else if (dir[0] == 1 && dir[1] == 1) {
            this.speedAngle = 225;
        } else if (dir[0] == 1 && dir[1] == -1) {
            this.speedAngle = 135;
        } else if (dir[0] == -1 && dir[1] == 1) {
            this.speedAngle = 315;
        } else if (dir[0] == -1 && dir[1] == -1) {
            this.speedAngle = 45;
        }
    }

    public void wallCollision(int[] dir, int[] pos) {
        if (dir[0] == -1 && dir[1] == 0) {
            dir[0] = 1;
            this.speedAngle = 0;
        } else if (dir[0] == 1 && dir[1] == 0) {
            dir[0] = -1;
            this.speedAngle = 180;
        } else if (dir[0] == 0 && dir[1] == 1) {
            dir[1] = -1;
            this.speedAngle = 270;
        } else if (dir[0] == 0 && dir[1] == -1) {
            dir[1] = 1;
            this.speedAngle = 90;
        } else if (dir[0] == 1 && dir[1] == 1) {
            if (pos[0] >= this.mobj.cellList.row - 1 && pos[1] < this.mobj.cellList.col - 1) {
                dir[0] = -1;
                this.speedAngle = 135;
            } else if (pos[0] < this.mobj.cellList.row - 1 && pos[1] >= this.mobj.cellList.col - 1) {
                dir[1] = -1;
                this.speedAngle = 315;
            } else {
                dir[0] = -1;
                dir[1] = -1;
                this.speedAngle = 225;
            }
        } else if (dir[0] == 1 && dir[1] == -1) {
            if (pos[0] >= this.mobj.cellList.row - 1 && pos[1] > 0) {
                dir[0] = -1;
                this.speedAngle = 225;
            } else if (pos[0] < this.mobj.cellList.row - 1 && pos[1] <= 0) {
                dir[1] = 1;
                this.speedAngle = 45;
            } else {
                dir[0] = -1;
                dir[1] = 1;
                this.speedAngle = 135;
            }
        } else if (dir[0] == -1 && dir[1] == 1) {
            if (pos[0] <= 0 && pos[1] < this.mobj.cellList.col - 1) {
                dir[0] = 1;
                this.speedAngle = 45;
            } else if (pos[0] > 0 && pos[1] >= this.mobj.cellList.col - 1) {
                dir[1] = -1;
                this.speedAngle = 225;
            } else {
                dir[0] = 1;
                dir[1] = -1;
                this.speedAngle = 315;
            }
        } else if (dir[0] == -1 && dir[1] == -1) {
            if (pos[0] > 0 && pos[1] <= 0) {
                dir[0] = 1;
                this.speedAngle = 315;
            } else if (pos[0] <= 0 && pos[1] > 0) {
                dir[1] = 1;
                this.speedAngle = 135;
            } else {
                dir[0] = 1;
                dir[1] = 1;
                this.speedAngle = 45;
            }
        }
        //add code here
    }

    public void motiveInput(SimController sc, double magnitude, double angle, avatar player, double time) {
        try {

            motiveForce mf=new motiveForce(player, magnitude, angle);
            mf.motivateObj();
            force jf=force.jointForceOf(player);
            if(jf.magnitude!=0) {
                for(int i=0; i<time; i++) {
                    if(!player.kine.move(sc, time)) break;
                }
                player.clearMotive();
                while(player.kine.move(sc, 0));
            }
            else System.out.println("jointForce is zero");
            System.out.println("Finish");
            player.kine.resetKine();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void energyDecline() {
        this.energy = (int) (this.energy * this.mobj.collisionFactor);
        this.speed = Math.sqrt(this.energy * 2 / this.mobj.mass);
    }

    public void updateEnergy() {
        this.energy = (int) (0.5 * this.mobj.mass * this.speed * this.speed);
    }

    public static double timePass(Obj mobj, double oldSpeed){
        double a=mobj.kine.acc;
        if(force.angleToDirection(mobj.kine.speedAngle)[0]!=force.angleToDirection(mobj.kine.accAngle)[0]
                &&force.angleToDirection(mobj.kine.speedAngle)[1]!=force.angleToDirection(mobj.kine.accAngle)[1]){
            a=-1*mobj.kine.acc;
        }
        //so we need to calculate the polynomial: 1=oldSpeed(v)*t+0.5*acceleration(a)*t^2
        //1=vt+0.5at^2
        //we need to t
        //at^2+2vt-2=0;
        double delta=4*oldSpeed*oldSpeed+8*a;
        double t1=(-2*oldSpeed+Math.sqrt(delta))/(2*a);
        double t2=(-2*oldSpeed-Math.sqrt(delta))/(2*a);
        if(t1<1 && t1>0) return t1;
        else if(t2<1 && t2>0) return t2;
        double larger;
        if(t1>t2) larger=t1;
        else larger=t2;
        if(a>0 && larger>1.5) return 999;
        else if(a<0 && t1>1.5 && t2>1.5) return 999;
        else return 1.1;
    }
    public void resetKine(){
        this.mobj.forces.clear();
        this.energy=0;
        this.speedAngle=-1;
        this.speed=0;
        this.accAngle=-1;
        this.acc=0;
        this.jointForce=force.jointForceOf(this.mobj);
    }

    public boolean move(SimController sc, double time) throws IOException, InterruptedException {
        int steps=0;
        //update the magnitude and direction of speed
        if(this.mobj.forces.size()==0) return false;
        boolean noMotive=true;
        for(force f : this.mobj.forces){
            if(f instanceof motiveForce) noMotive=false;
        }
        this.updateJointForce();
        String comment="";
        double x = 0, y = 0;
        if(this.speedAngle!=-1){
            x += this.speed * Math.cos(this.speedAngle*Math.PI/180) + this.acc * Math.cos(this.accAngle*Math.PI/180);
            y += this.speed * Math.sin(this.speedAngle*Math.PI/180) + this.acc * Math.sin(this.accAngle*Math.PI/180);
        }
        else{
            x += this.acc * Math.cos(this.accAngle*Math.PI/180);
            y += this.acc * Math.sin(this.accAngle*Math.PI/180);
        }
        boolean reversed=false;
        double angle=force.angleConvert(x, y);
        int[] dirToGo=force.angleToDirection(angle);
        int[] curDir=force.angleToDirection(this.speedAngle);
        this.speedAngle = (int) force.angleConvert(x, y);
        //now we move towards the nearest direction
        int[] dir = force.angleToDirection(this.speedAngle);
        double secondCounter = 0;

        DecimalFormat df   = new DecimalFormat("######0.00");
        comment+="########################################################################\n";
        comment+="Moving object at position ["+this.mobj.x+", "+this.mobj.y+"]\n";
        comment+="Currently there are "+this.mobj.forces.size()+" forces on the object\n";
        for(force f : this.mobj.forces){
            if(f instanceof friction) comment+="Friction: ";
            else if(f instanceof motiveForce) comment+="Motive: ";
            else comment+="Some force: ";
            comment+="Magnituded: "+df.format(f.magnitude)+" Angle: "+f.angle+" Direction: ["+force.angleToDirection(f.angle)[0]+", "+force.angleToDirection(f.angle)[1]+"]\n";
        }
        comment+="########################################################################\n";
        comment+="Current joint force's magnitude is "+df.format(this.jointForce.magnitude)+" with angle: "+this.jointForce.angle+"\n";
        comment+="Current speed is "+df.format(this.mobj.kine.speed)+" with angle: "+this.mobj.kine.speedAngle+"\n";
        comment+="Current moving direction is ["+dir[0]+" ,"+dir[1]+"]\n";
        comment+="Current acceleration is "+df.format(this.mobj.kine.acc)+"\n";
        System.out.println(comment);
        boolean out=false;
        if(dirToGo[0]==(-1)*curDir[0] && dirToGo[1]==(-1)*curDir[1]){
            if(noMotive){
                out=true;
            }
            reversed=true;
        }
        if(this.speed ==0 && time*time*this.acc*0.5<0.5) return false;
        if(reversed && this.speed-this.acc*0.5<0.5) return false;
        while (secondCounter < 1) {
            //first we update the direction and the energy
            dir = force.angleToDirection(this.speedAngle);
            if ((dir[0] == 0 && dir[1] == 0)) return false;
            double energyChange = this.jointForce.magnitude * 1 * Math.cos(Math.abs(this.accAngle - this.speedAngle)*Math.PI/180);
            //W=force*distance*cos(the angle between the direction of the force and the direction of the speed)
            if(reversed) this.energy=this.energy*(-1);
            this.energy += energyChange;
            double oldSpeed=this.speed;
            this.speed = Math.sqrt(2 * this.energy / this.mobj.mass);
            //now we move one grid
            int[] oldPos=new int[]{this.mobj.x, this.mobj.y};
            if(out) {
                dir[0]=dir[0]*(-1);
                dir[1]=dir[1]*(-1);
            }
            if (dir[0] == 1 && dir[1] == 0) {
                cell destination;
                if (this.mobj.x < this.mobj.cellList.row - 1) {
                    destination = this.mobj.cellList.getCellAt(this.mobj.x + 1, this.mobj.y);
                    if (!destination.hasObj()) {
                        secondCounter += timePass(this.mobj, oldSpeed);
                        if(secondCounter>1.5) break;
                        oldPos=this.mobj.moveObj('s', (map) sc.world);
                        if(oldPos[0]==this.mobj.x && oldPos[1]==this.mobj.y) oldPos=this.mobj.moveObj('s', (map) sc.world);
                        sc.moveAvatarOnScreen(oldPos);

                    }
                    else if (destination.hasObj()) {
                        out=true;
                    }
                }
                else {
                    out=true;
                }
            }
            else if (dir[0] == 1 && dir[1] == 1) {
                cell destination;
                if (this.mobj.x < this.mobj.cellList.row - 1 && this.mobj.y < this.mobj.cellList.col - 1) {
                    destination = this.mobj.cellList.getCellAt(this.mobj.x + 1, this.mobj.y + 1);
                    if (!destination.hasObj()) {
                        secondCounter += timePass(this.mobj, oldSpeed);
                        if(secondCounter>1.5) break;
                        oldPos=this.mobj.moveObj('c', (map) sc.world);
                        if(oldPos[0]==this.mobj.x && oldPos[1]==this.mobj.y) oldPos=this.mobj.moveObj('c', (map) sc.world);
                        sc.moveAvatarOnScreen(oldPos);
                    }
                    else if (destination.hasObj()) {
                        out=true;
                    }
                }
                else {
                    out=true;
                }
            } else if (dir[0] == 0 && dir[1] == 1) {
                cell destination;
                if (this.mobj.y < this.mobj.cellList.col - 1) {
                    destination = this.mobj.cellList.getCellAt(this.mobj.x, this.mobj.y + 1);
                    if (!destination.hasObj()) {
                        secondCounter += timePass(this.mobj, oldSpeed);
                        if(secondCounter>1.5) break;
                        oldPos=this.mobj.moveObj('d', (map) sc.world);
                        if(oldPos[0]==this.mobj.x && oldPos[1]==this.mobj.y) oldPos=this.mobj.moveObj('d', (map) sc.world);
                        sc.moveAvatarOnScreen(oldPos);
                    }
                    else if (destination.hasObj()) {
                        out=true;
                    }
                }
                else {
                    out=true;
                }
            }
            else if (dir[0] == -1 && dir[1] == 1) {
                cell destination;
                if (this.mobj.x > 0 && this.mobj.y < this.mobj.cellList.col - 1) {
                    destination = this.mobj.cellList.getCellAt(this.mobj.x - 1, this.mobj.y + 1);
                    if (!destination.hasObj()) {
                        secondCounter += timePass(this.mobj, oldSpeed);
                        if(secondCounter>1.5) break;
                        oldPos=this.mobj.moveObj('e', (map) sc.world);
                        if(oldPos[0]==this.mobj.x && oldPos[1]==this.mobj.y) oldPos=this.mobj.moveObj('e', (map) sc.world);
                        sc.moveAvatarOnScreen(oldPos);
                    } else if (destination.hasObj()) {
                        out=true;
                    }
                } else {
                    out=true;
                }
            } else if (dir[0] == -1 && dir[1] == 0) {
                cell destination;
                if (this.mobj.x > 0) {
                    destination = this.mobj.cellList.getCellAt(this.mobj.x - 1, this.mobj.y);
                    if (!destination.hasObj()) {
                        secondCounter += timePass(this.mobj, oldSpeed);
                        if(secondCounter>1.5) break;
                        oldPos=this.mobj.moveObj('w', (map) sc.world);
                        if(oldPos[0]==this.mobj.x && oldPos[1]==this.mobj.y) oldPos=this.mobj.moveObj('w', (map) sc.world);
                        sc.moveAvatarOnScreen(oldPos);
                    }
                    else if (destination.hasObj()) {
                        out=true;
                    }
                } else {
                    out=true;
                }
            } else if (dir[0] == -1 && dir[1] == -1) {
                cell destination;
                if (this.mobj.x > 0 && this.mobj.y > 0) {
                    destination = this.mobj.cellList.getCellAt(this.mobj.x - 1, this.mobj.y - 1);
                    if (!destination.hasObj()) {
                        secondCounter += timePass(this.mobj, oldSpeed);
                        if(secondCounter>1.5) break;
                        oldPos=this.mobj.moveObj('q', (map) sc.world);
                        if(oldPos[0]==this.mobj.x && oldPos[1]==this.mobj.y) oldPos=this.mobj.moveObj('q', (map) sc.world);
                        sc.moveAvatarOnScreen(oldPos);
                    } else if (destination.hasObj()) {
                        out=true;
                    }
                } else {
                    out=true;
                }
            } else if (dir[0] == 0 && dir[1] == -1) {
                cell destination;
                if (this.mobj.y > 0) {
                    destination = this.mobj.cellList.getCellAt(this.mobj.x, this.mobj.y - 1);
                    if (!destination.hasObj()) {
                        secondCounter += timePass(this.mobj, oldSpeed);
                        if(secondCounter>1.5) break;
                        oldPos=this.mobj.moveObj('a', (map) sc.world);
                        if(oldPos[0]==this.mobj.x && oldPos[1]==this.mobj.y) oldPos=this.mobj.moveObj('a', (map) sc.world);
                        sc.moveAvatarOnScreen(oldPos);
                    }
                    else if (destination.hasObj()) {
                        out=true;
                    }
                } else {
                    out=true;
                }
            } else {
                cell destination;
                if (this.mobj.x < this.mobj.cellList.row - 1 && this.mobj.y > 0) {
                    destination = this.mobj.cellList.getCellAt(this.mobj.x + 1, this.mobj.y - 1);
                    if (!destination.hasObj()) {
                        secondCounter += timePass(this.mobj, oldSpeed);
                        if(secondCounter>1.5) break;
                        oldPos=this.mobj.moveObj('z', (map) sc.world);
                        if(oldPos[0]==this.mobj.x && oldPos[1]==this.mobj.y) oldPos=this.mobj.moveObj('z', (map) sc.world);
                        sc.moveAvatarOnScreen(oldPos);
                    } else if (destination.hasObj()) {
                        out=true;
                    }

                } else {
                    out=true;
                }
            }
            try{
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            if(out) {
                this.resetKine();
                return false;
            }
        }
        return true;
    }
}
