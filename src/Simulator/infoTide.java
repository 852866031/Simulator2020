package Simulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

// Author: Jiaxuan Chen
public class infoTide {
    private int waveSpeed;
    private float decayRate;
    private float initialDensity;
    private worldObject obj;
    private infoWave root;
    private cellList cells;
    private int[] dir;
    public infoTide(int waveSpeed, float decayRate, float initialDensity, worldObject obj, cellList cells, int[] dir) {
        this.waveSpeed=waveSpeed;
        this.decayRate=decayRate;
        this.initialDensity=initialDensity;
        this.obj=obj;
        this.cells=cells;
        this.dir=dir;
        int num=waveFormula();
        this.root=new infoWave(this);
        infoWave cur=this.root;
        for(int i=0; i<num-1; i++){
            cur.next=new infoWave(this);
            cur=cur.next;
        }
        cur.next=null;
    }
    private int waveFormula(){
        int x=(int) Math.ceil(this.initialDensity/this.decayRate);
        if(x>=this.cells.col*this.cells.row) return this.cells.col*this.cells.row;
        return x;
    }
    public void tiding(int time){
        Queue<infoWave> queue=new LinkedList<>();
        Pipe p=new Pipe(waveFormula());
        p.push(root);
        for(infoWave i : root) queue.add(i);
        while(!queue.isEmpty()) {
            for(infoWave i : p.pipe) {i.waveBeat(); }
            infoWave out=p.push(queue.remove());
            queue.add(out);
        }

    }

    private class infoWave implements Iterable<infoWave>{
        private infoTide tide;
        private float density;
        private int[] pos;
        private int[] dir;
        private boolean reflectable=false;
        public infoWave next;
        public infoWave(infoTide tide) {
            this.tide=tide;
            this.density=this.tide.initialDensity;
            this.pos=new int[2];
            this.pos[0]=this.tide.obj.x;
            this.pos[1]=this.tide.obj.y;
            this.dir=this.tide.dir;
        }
        public void waveBeat() {
            avatar player=this.tide.cells.getPlayer();
            if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
            if(this.dir[0]==-1 && this.dir[1]==0) {
                if(pos[0]>0){
                    pos[0]-=1;
                    if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
                }
                else this.reflect();
            }
            else if(this.dir[0]==1 && this.dir[1]==0) {
                if(pos[0]<this.tide.cells.row-1){
                    pos[0]+=1;
                    if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
                }
                else this.reflect();
            }
            else if(this.dir[0]==0 && this.dir[1]==1) {
                if(pos[1]<this.tide.cells.col-1){
                    pos[1]+=1;
                    if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
                }
                else this.reflect();
            }
            else if(this.dir[0]==0 && this.dir[1]==-1) {
                if(pos[1]>0){
                    pos[1]-=1;
                    if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
                }
                else this.reflect();
            }
            else if(this.dir[0]==1 && this.dir[1]==1){
                if(pos[0]<this.tide.cells.row-1 && pos[1]<this.tide.cells.col-1){
                    pos[0]+=1;
                    pos[0]+=1;
                    if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
                }
                else this.reflect();
            }
            else if(this.dir[0]==1 && this.dir[1]==-1) {
                if(pos[0]<this.tide.cells.row-1 && pos[1]>0){
                    pos[0]+=1;
                    pos[1]-=1;
                    if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
                }
                else this.reflect();
            }
            else if(this.dir[0]==-1 && this.dir[1]==1) {
                if(pos[0]>0 && pos[1]<this.tide.cells.col-1){
                    pos[0]-=1;
                    pos[1]+=1;
                    if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
                }
                else this.reflect();
            }
            else if(this.dir[0]==-1 && this.dir[1]==-1) {
                if(pos[0]>0 && pos[1]>0){
                    pos[0]-=1;
                    pos[1]-=1;
                    if(player.comparePos(pos)) player.obtainInfo(this.tide.obj);
                }
                else this.reflect();
            }
            this.density-=this.tide.decayRate;
            if(this.density<=0) this.resetWave();
        }


        public void resetWave() {
            this.density=this.tide.initialDensity;
            this.pos[0]=this.tide.obj.x;
            this.pos[1]=this.tide.obj.y;
        }


        public boolean reflect(){
            if(!reflectable) {
                this.resetWave();
                return false;
            }
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
                if(pos[0]>=this.tide.cells.row-1 && pos[1]<this.tide.cells.col-1){
                    this.dir[0]=-1;
                }
                else if(pos[0]<this.tide.cells.row-1 && pos[1]>=this.tide.cells.col-1){
                    this.dir[1]=-1;
                }
                else {
                    this.dir[0]=-1;
                    this.dir[1]=-1;
                }
            }
            else if(this.dir[0]==1 && this.dir[1]==-1) {
                if(pos[0]>=this.tide.cells.row-1 && pos[1]>0){
                    this.dir[0]=-1;
                }
                else if(pos[0]<this.tide.cells.row-1 && pos[1]<=0){
                    this.dir[1]=1;
                }
                else {
                    this.dir[0]=-1;
                    this.dir[1]=1;
                }
            }
            else if(this.dir[0]==-1 && this.dir[1]==1) {
                if(pos[0]<=0 && pos[1]<this.tide.cells.col-1){
                    this.dir[0]=1;
                }
                else if(pos[0]>0 && pos[1]>=this.tide.cells.col-1){
                    this.dir[1]=-1;
                }
                else {
                    this.dir[0]=1;
                    this.dir[1]=-1;
                }
            }
            else if(this.dir[0]==-1 && this.dir[1]==-1) {
                if(pos[0]>0 && pos[1]<=0){
                    this.dir[0]=1;
                }
                else if(pos[0]<=0 && pos[1]>0){
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

        @Override
        public Iterator<infoWave> iterator() {
            return new infoWaveIterator();
        }

        public class infoWaveIterator implements Iterator<infoWave> {
            infoWave cur=root;
            public infoWave next(){
                infoWave tmp=cur;
                cur=cur.next;
                return tmp;
            }
            public boolean hasNext() {
                if(cur==null) return false;
                return true;
            }
        }
    }
    private class Pipe {
        int capacity;
        ArrayList<infoWave> pipe=new ArrayList<>();
        public Pipe(int capacity){
            this.capacity=capacity;
        }
        public infoWave push(infoWave i){
            this.pipe.add(i);
            if(this.pipe.size()>capacity){
                return this.pipe.remove(0);
            }
            return null;
        }
    }
}
