package Simulator;

import java.util.ArrayList;

// Author: Jiaxuan Chen
public class friction extends force {
    public Obj on;
    public friction(Obj obj, double magnitude, double angle) {
        super(null, obj, magnitude, angle);
        this.on=this.to;
    }
    public static void removeFriction(Obj obj){
        ArrayList<force> trash=new ArrayList<>();
        for(force f : obj.forces){
            if(f instanceof friction) trash.add(f);
        }
        for(force f : trash) obj.forces.remove(f);
    }
}
