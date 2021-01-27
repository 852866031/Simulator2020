package Simulator;

// Author: Jiaxuan Chen
public class Sensor {

    public int[] simpleDetectDistanceBySonic(avatar player, map world, boolean outside){
        int[] dir=new int[]{player.getAvatarDir()[0], player.getAvatarDir()[1]};
        int[] le=new int[2];
        int[] re=new int[2];

        if(dir[0]==1 && dir[1]==0) {
            le=new int[]{0, 1};
            re=new int[]{0, -1};
        }
        else if(dir[0]==-1 && dir[1]==0){
            le=new int[]{0, -1};
            re=new int[]{0, 1};
        }
        else if(dir[0]==0 && dir[1]==1){
            le=new int[]{-1, 0};
            re=new int[]{1, 0};
        }
        else if(dir[0]==0 && dir[1]==-1){
            le=new int[]{1, 0};
            re=new int[]{-1, 0};
        }
        else if(dir[0]==1 && dir[1]==1){
            le=new int[]{-1, 1};
            re=new int[]{1, -1};
        }
        else if(dir[0]==1 && dir[1]==-1){
            le=new int[]{1, 1};
            re=new int[]{-1, -1};
        }
        else if(dir[0]==-1 && dir[1]==1){
            le=new int[]{-1, -1};
            re=new int[]{1, 1};
        }
        else {
            le=new int[]{1, -1};
            re=new int[]{-1, 1};
        }
        unitWave mid=new unitWave(world.getWidth()*world.getHeight(),340,1,
                ((map)world).cellList,new int[]{player.x, player.y}, dir);
        unitWave left=new unitWave(world.getWidth()*world.getHeight(),340,1,
                ((map)world).cellList,new int[]{player.x, player.y}, le);
        unitWave right=new unitWave(world.getWidth()*world.getHeight(),340,1,
                ((map)world).cellList,new int[]{player.x, player.y}, re);
        int[] result=new int[3];
        int i=-1,j=-1,k=-1;
        while(i==-1)  {
            i=mid.waveDetect(outside);
            if(i==999) result[0]=999;
            else result[0]=world.getWidth()*world.getHeight()-i;
        }
        while(j==-1)  {
            j=left.waveDetect(outside);
            if(j==999) result[1]=999;
            else result[1]=world.getWidth()*world.getHeight()-j;
        }
        while(k==-1)  {
            k=right.waveDetect(outside);
            if(k==999) result[2]=999;
            else result[2]=world.getWidth()*world.getHeight()-k;
        }
        if(result[0]!=999) System.out.println("Distance between the robot and the object in its facing direction is: "+result[0]);
        else System.out.println("Signal sent to front did not come back");
        if(result[1]!=999) System.out.println("Distance between the robot and the object in its left is: "+result[1]);
        else System.out.println("Signal sent to left did not come back");
        if(result[2]!=999) System.out.println("Distance between the robot and the object in its right is: "+result[2]);
        else System.out.println("Signal sent to right did not come back");
        return result;
    }

    public void useSonicSensorAtFront(float initialDensity, float decayRate , double expandRate, avatar player, map world, boolean outside){
        sonicWave sw=new sonicWave(new int[]{player.x, player.y},decayRate,
                initialDensity, ((map)world).cellList, new int[]{player.getAvatarDir()[0], player.getAvatarDir()[1]}, expandRate);
        sw.spread(outside);
        System.out.println(player.receiveBasket.size()+" receives: ");
        int i=1;
        for(unitWave u : player.receiveBasket){
            i++;
            System.out.println(i+": "+u);
        }
        player.resetReceiveBasket();
    }
}
