package Simulator;

import java.text.DecimalFormat;

// Author: Jiaxuan Chen
public class unitWave {
    public float density;
    double decayRate;
    double speed;
    cellList cellList;
    int[] pos;
    int[] dir;
    public boolean reflective=true;
    public unitWave(float initialDensity,double speed, double decayRate, cellList cells, int[] pos, int[] dir ) {
        this.density=initialDensity;
        this.pos=pos;
        this.speed=speed;
        this.decayRate=decayRate;
        this.cellList=cells;
        this.dir=dir;
    }
    public String toString() {
        DecimalFormat df   = new DecimalFormat("######0.00");
        return "Density: "+df.format(this.density)+", Spread Direction: "+"["+this.dir[0]+", "+this.dir[1]+"]"+" at position ["+this.pos[0]+", "+this.pos[1]+"]";
    }
    public void setReflectivity(boolean x) { this.reflective=x; }
    public boolean waveBeat(boolean outside) {
        if(this.density<=0) return false;
        avatar player=this.cellList.getPlayer();
        if(this.dir[0]==-1 && this.dir[1]==0) {
            if(pos[0]>0 && !this.cellList.getCellAt(pos[0]-1,pos[1]).hasObjButNotPlayer()){
                pos[0]-=1;
                this.density-=this.decayRate;
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
            }
            else {
                if(outside && pos[0]<=0) return false;
                this.reflect();
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
                return this.waveBeat(outside);
            }
        }
        else if(this.dir[0]==1 && this.dir[1]==0) {
            if(pos[0]<this.cellList.row-1 && !cellList.getCellAt(pos[0]+1,pos[1]).hasObjButNotPlayer()){
                pos[0]+=1;
                this.density-=this.decayRate;
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
            }
            else {
                if(outside && pos[0]>=this.cellList.row-1) return false;
                this.reflect();
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
                return this.waveBeat(outside);
            }
        }
        else if(this.dir[0]==0 && this.dir[1]==1 )  {
            if(pos[1]<this.cellList.col-1 && !cellList.getCellAt(pos[0],pos[1]+1).hasObjButNotPlayer()){
                pos[1]+=1;
                this.density-=this.decayRate;
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
            }
            else {
                if(outside && pos[1]>=this.cellList.col-1) return false;
                this.reflect();
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
                return this.waveBeat(outside);
            }
        }
        else if(this.dir[0]==0 && this.dir[1]==-1) {
            if(pos[1]>0  && !cellList.getCellAt(pos[0],pos[1]-1).hasObjButNotPlayer()){
                pos[1]-=1;
                this.density-=this.decayRate;
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
            }
            else {
                if(outside && pos[1]<=0) return false;
                this.reflect();
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
                return this.waveBeat(outside);
            }
        }
        else if(this.dir[0]==1 && this.dir[1]==1 ){
            if(pos[0]<this.cellList.row-1 && pos[1]<this.cellList.col-1 && !cellList.getCellAt(pos[0]+1,pos[1]+1).hasObjButNotPlayer()){
                pos[0]+=1;
                pos[1]+=1;
                this.density-=this.decayRate;
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
            }
            else {
                if(outside && !(pos[0]<this.cellList.row-1 && pos[1]<this.cellList.col-1)) return false;
                this.reflect();
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
                return this.waveBeat(outside);
            }
        }
        else if(this.dir[0]==1 && this.dir[1]==-1 ) {
            if(pos[0]<this.cellList.row-1 && pos[1]>0 && !cellList.getCellAt(pos[0]+1,pos[1]-1).hasObjButNotPlayer()){
                pos[0]+=1;
                pos[1]-=1;
                this.density-=this.decayRate;
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
            }
            else {
                if(outside && !(pos[0]<this.cellList.row-1 && pos[1]>0)) return false;
                this.reflect();
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
                return this.waveBeat(outside);
            }
        }
        else if(this.dir[0]==-1 && this.dir[1]==1 ) {
            if(pos[0]>0 && pos[1]<this.cellList.col-1 && !cellList.getCellAt(pos[0]-1,pos[1]+1).hasObjButNotPlayer()){
                pos[0]-=1;
                pos[1]+=1;
                this.density-=this.decayRate;
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
            }
            else {
                if(outside && !(pos[0]>0 && pos[1]<this.cellList.col-1)) return false;
                this.reflect();
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
                return this.waveBeat(outside);
            }
        }
        else if(this.dir[0]==-1 && this.dir[1]==-1 ) {
            if(pos[0]>0 && pos[1]>0 && !cellList.getCellAt(pos[0]-1,pos[1]-1).hasObjButNotPlayer()){
                pos[0]-=1;
                pos[1]-=1;
                this.density-=this.decayRate;
                if(player.comparePos(pos))  {
                    player.obtainInfo(this);
                    return false;
                }
            }
            else {
                if(outside && !(pos[0]>0 && pos[1]>0)) return false;
                this.reflect();
                if(player.comparePos(pos)) {
                    player.obtainInfo(this);
                    return false;
                }
                return this.waveBeat(outside);
            }
        }
        if(this.density<=0) return false;
        return true;
    }
    public int waveDetect(boolean outside) {
        avatar player=this.cellList.getPlayer();
        if(this.dir[0]==-1 && this.dir[1]==0) {
            if(pos[0]>0 && !this.cellList.getCellAt(pos[0]-1,pos[1]).hasObjButNotPlayer()){
                pos[0]-=1;
                this.density-=this.decayRate;
            }
            else {
                if(outside && pos[0]<=0) return 999;
                return (int) this.density;
            }
        }
        else if(this.dir[0]==1 && this.dir[1]==0) {
            if(pos[0]<this.cellList.row-1 && !cellList.getCellAt(pos[0]+1,pos[1]).hasObjButNotPlayer()){
                pos[0]+=1;
                this.density-=this.decayRate;
            }
            else {
                if(outside && pos[0]>=this.cellList.row-1) return 999;
                return (int) this.density;
            }
        }
        else if(this.dir[0]==0 && this.dir[1]==1 )  {
            if(pos[1]<this.cellList.col-1 && !cellList.getCellAt(pos[0],pos[1]+1).hasObjButNotPlayer()){
                pos[1]+=1;
                this.density-=this.decayRate;
            }
            else {
                if(outside && pos[1]>=this.cellList.col-1) return 999;
                return (int) this.density;
            }
        }
        else if(this.dir[0]==0 && this.dir[1]==-1) {
            if(pos[1]>0  && !cellList.getCellAt(pos[0],pos[1]-1).hasObjButNotPlayer()){
                pos[1]-=1;
                this.density-=this.decayRate;
            }
            else {
                if(outside && pos[1]<=0) return 999;
                return (int) this.density;
            }
        }
        else if(this.dir[0]==1 && this.dir[1]==1 ){
            if(pos[0]<this.cellList.row-1 && pos[1]<this.cellList.col-1 && !cellList.getCellAt(pos[0]+1,pos[1]+1).hasObjButNotPlayer()){
                pos[0]+=1;
                pos[1]+=1;
                this.density-=this.decayRate;
            }
            else {
                if(outside && !(pos[0]<this.cellList.row-1 && pos[1]<this.cellList.col-1)) return 999;
                return (int) this.density;
            }
        }
        else if(this.dir[0]==1 && this.dir[1]==-1 ) {
            if(pos[0]<this.cellList.row-1 && pos[1]>0 && !cellList.getCellAt(pos[0]+1,pos[1]-1).hasObjButNotPlayer()){
                pos[0]+=1;
                pos[1]-=1;
                this.density-=this.decayRate;
            }
            else {
                if(outside && !(pos[0]<this.cellList.row-1 && pos[1]>0)) return 999;
                return (int) this.density;
            }
        }
        else if(this.dir[0]==-1 && this.dir[1]==1 ) {
            if(pos[0]>0 && pos[1]<this.cellList.col-1 && !cellList.getCellAt(pos[0]-1,pos[1]+1).hasObjButNotPlayer()){
                pos[0]-=1;
                pos[1]+=1;
                this.density-=this.decayRate;
            }
            else {
                if(outside && !(pos[0]>0 && pos[1]<this.cellList.col-1)) return 999;
                return (int) this.density;
            }
        }
        else if(this.dir[0]==-1 && this.dir[1]==-1 ) {
            if(pos[0]>0 && pos[1]>0 && !cellList.getCellAt(pos[0]-1,pos[1]-1).hasObjButNotPlayer()){
                pos[0]-=1;
                pos[1]-=1;
                this.density-=this.decayRate;
            }
            else {
                if(outside && !(pos[0]>0 && pos[1]>0)) return 999;
                return (int) this.density;
            }
        }
        return -1;
    }

    public boolean reflect(){
        if(this.dir[0]==-1 && this.dir[1]==0) {
            this.dir[0]=1;
        }
        else if(this.dir[0]==1 && this.dir[1]==0) {
            this.dir[0]=-1;
        }
        else if(this.dir[0]==0 && this.dir[1]==1) {
            this.dir[1]=-1;
        }
        else if(this.dir[0]==0 && this.dir[1]==-1) {
            this.dir[1]=1;
        }
        else if(this.dir[0]==1 && this.dir[1]==1){
            if(pos[0]>=this.cellList.row-1 && pos[1]<this.cellList.col-1 ){
                this.dir[0]=-1;
            }
            else if(pos[0]<this.cellList.row-1 && pos[1]>=this.cellList.col-1){
                this.dir[1]=-1;
            }
            else {
                this.dir[0]=-1;
                this.dir[1]=-1;
            }

        }
        else if(this.dir[0]==1 && this.dir[1]==-1) {
            if(pos[0]>=this.cellList.row-1 && pos[1]>0){
                this.dir[0]=-1;
            }
            else if(pos[0]<this.cellList.row-1 && pos[1]<=0){
                this.dir[1]=1;
            }
            else {
                this.dir[0]=-1;
                this.dir[1]=1;
            }
        }
        else if(this.dir[0]==-1 && this.dir[1]==1) {
            if(pos[0]<=0 && pos[1]<this.cellList.col-1){
                this.dir[0]=1;
            }
            else if(pos[0]>0 && pos[1]>=this.cellList.col-1){
                this.dir[1]=-1;
            }
            else {
                this.dir[0]=1;
                this.dir[1]=-1;
            }
        }
        else if(this.dir[0]==-1 && this.dir[1]==-1) {
            if(pos[0]<=0 && pos[1]>0){
                this.dir[0]=1;
            }
            else if(pos[0]>0 && pos[1]<=0){
                this.dir[1]=1;
            }
            else {
                this.dir[0]=1;
                this.dir[1]=1;
            }
        }
        //add code here

        //to do sth with reflect rate
        return true;
    }

}