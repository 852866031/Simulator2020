package Simulator;

import java.util.ArrayList;

// Author: Jiaxuan Chen
public class sonicWave {
    private float decayRate;
    //the decay rate of the wave
    private float initialDensity;
    //the start density of the wave
    private cellList cellList;
    //the map
    private int[] dir;
    //the spread direction of the wave
    private int[] pos;
    private double expandRate;
    //important!!!!!!!
    //expand expandRate times per move, this value is better to be smaller than 1;
    //the rate that the wave expands, expand after travel quantity of "expandRate" cells
    unitWave root;
    ArrayList<unitWave> waveSet=new ArrayList<>();
    public sonicWave(int[]pos, float decayRate, float initialDensity, cellList cells, int[] dir, double expandRate){
        this.pos=new int[]{pos[0], pos[1]};
        this.decayRate=decayRate;
        this.initialDensity=initialDensity;
        this.cellList=cells;
        this.dir=new int[]{dir[0], dir[1]};
        this.expandRate=expandRate;
        this.root=new unitWave(initialDensity, 340, decayRate,cells,pos, dir);
        this.waveSet.add(root);
    }
    public void expand(unitWave unit, ArrayList<unitWave> waveSet){
        int[][] checkSet=new int[2][2];
        boolean[] check={false, false};
        checkSet[0]=new int[]{unit.pos[0], unit.pos[1]};
        checkSet[1]=new int[]{unit.pos[0], unit.pos[1]};
        if((unit.dir[0]==-1 && unit.dir[1]==0) || (unit.dir[0]==1 && unit.dir[1]==0)) {
            checkSet[0][1]++;
            checkSet[1][1]--;
        }
        else if((unit.dir[0]==0 && unit.dir[1]==1) || (unit.dir[0]==0 && unit.dir[1]==-1)) {
            checkSet[0][0]++;
            checkSet[1][0]--;
        }
        else if((unit.dir[0]==1 && unit.dir[1]==1) || (unit.dir[0]==-1 && unit.dir[1]==-1)) {
            checkSet[0][1]++;
            checkSet[0][0]--;
            checkSet[1][1]--;
            checkSet[1][0]++;
        }
        else if((unit.dir[0]==1 && unit.dir[1]==-1) || (unit.dir[0]==-1 && unit.dir[1]==1)){
            checkSet[0][1]++;
            checkSet[0][0]++;
            checkSet[1][1]--;
            checkSet[1][0]--;
        }
        for(unitWave u : waveSet){
            if(u.pos[0]==checkSet[0][0] && u.pos[1]==checkSet[0][1]) check[0]=true;
            if(u.pos[0]==checkSet[1][0] && u.pos[1]==checkSet[1][1]) check[1]=true;
        }
        if(checkSet[0][0]>=cellList.row || checkSet[0][0] <0 || checkSet[0][1]>=cellList.col || checkSet[0][1]<0 ) check[0]=true;
        if(checkSet[1][0]>=cellList.row || checkSet[1][0] <0 || checkSet[1][1]>=cellList.col || checkSet[1][1]<0 ) check[1]=true;
        if(check[0]==false) waveSet.add(new unitWave(unit.density, unit.speed, unit.decayRate, unit.cellList, checkSet[0], new int[]{unit.dir[0], unit.dir[1]}));
        if(check[1]==false) waveSet.add(new unitWave(unit.density, unit.speed, unit.decayRate, unit.cellList, checkSet[1], new int[]{unit.dir[0], unit.dir[1]}));
    }
    public void spread(boolean outside){
        //int k=0;
        double expandCounter=0;
        while(this.waveSet.size()>0){
            /*k++;
            if(k>50) {
                System.out.println("infinite");
                break;
            }
            System.out.println("Currently there are "+this.waveSet.size()+" waves in the set");
            for(unitWave u : this.waveSet){
                System.out.println(u);
            }*/
            ArrayList<unitWave> trash=new ArrayList<>();
            for(unitWave u : this.waveSet){ if(!u.waveBeat(outside)) trash.add(u);}
            for(unitWave u : trash) {this.waveSet.remove(u);}
            expandCounter+=expandRate;
            if(expandCounter>=1){
                ArrayList<unitWave> tmp=new ArrayList<>();
                for(unitWave u : this.waveSet) tmp.add(u);
                for(unitWave u : this.waveSet) expand(u, tmp);
                this.waveSet=tmp;
                expandCounter--;
            }
        }
        this.waveSet=new ArrayList<>();
    }
}
