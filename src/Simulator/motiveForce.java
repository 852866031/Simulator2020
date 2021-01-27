package Simulator;

// Author: Jiaxuan Chen
public class motiveForce extends force{
    public Obj on;
    public motiveForce(Obj obj, double magnitude, double angle) {
        super(null, obj, magnitude, angle);
        this.on=this.to;
    }
    public void motivateObj(){
        force.applyForce(this, null, this.on);
    }
}
