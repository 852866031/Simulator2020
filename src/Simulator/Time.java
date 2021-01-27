package Simulator;
import java.math.BigDecimal;
import java.math.RoundingMode;

// Author: Kaige Chen
public class Time{
    private double time = 0; //Default unit is seconds
    private String timePeriodOfDay = "Night";
    private int currDay = 1;

    private long startTime = 0;
    private long pauseTime = 0;
    private long stopTime = 0;
    private long timePaused = 0; //default 0 before pausing

    private boolean tlIsOn = false;
    private boolean tlIsPaused = false;
    private boolean tlResumed = false;

    private int timeRate;

    /**Auxiliary function to get the current time in seconds in the double format using System.currentTimeMillis*/
    private double aux_currRealTimeToSec(){
        long currTimeMillis = System.currentTimeMillis();
        double currTimeSec = ((double) currTimeMillis)/1000;

        return currTimeSec;
    }

    /**Auxiliary function for rounding double values into certain decimals*/
    private static double round(double value, int n) {
        if (n < 0){
            throw new IllegalArgumentException();
        }

        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }

    /**Setting how much faster you want the time to run than real life*/
     public void setTimelineRate(int rate){
         this.timeRate = rate;
     }

     public int getTimelineRate(){
        //return the current time line scale rate
         return this.timeRate;
     }

    public void startTimeLine(){
        this.tlIsOn = true;
        this.tlIsPaused = false;

        this.startTime = System.currentTimeMillis(); //Time started in real life in millis
    }

    public void stopTimeLine(){
        this.tlIsOn = false;
        this.tlIsPaused = false;

        this.stopTime = System.currentTimeMillis(); //Time stopped in real life in millis
    }

    /**Ability to allow the timeline to be paused as well if the simulation is paused*/
    public void pauseTimeLine(){
        this.tlIsOn = false;
        this.tlIsPaused = true;

        this.pauseTime = System.currentTimeMillis(); //Time paused in real life in millis
    }

    /**Ability to allow the timeline to be resumed as well after the simulation is paused*/
    public void resumeTimeLine(){
        if(this.tlIsPaused && !this.tlIsOn){
            this.tlIsPaused = false;
            this.tlIsOn = true;

            this.timePaused = System.currentTimeMillis() - this.pauseTime; //How long it's been paused in milli second.
            this.startTime = this.timePaused + this.startTime;
        }
    }

    /**Get the current time (in the simulation timeline so scaled) only in seconds format since starting time*/
    public double getCurrTimeSec(){
        double currSec = 0;

        if(this.tlIsOn && !this.tlIsPaused){
            currSec = (aux_currRealTimeToSec() - (((double) this.startTime)/1000))*this.timeRate;
        }else if(this.tlIsPaused && this.tlIsOn){
            currSec = (((double) (this.pauseTime - this.startTime))/1000)*this.timeRate;
        }else{
            System.out.println("Error: Timeline has been stopped.");
        }

        return round(currSec, 5);
    }

    /**Get the current time only in minutes format since starting time*/
    public double getCurrTimeMin(){
        double currMin = 0;

        if(this.tlIsOn && !this.tlIsPaused){
            currMin = ((aux_currRealTimeToSec() - (((double) this.startTime)/1000))/60)*this.timeRate;
        }else if(this.tlIsPaused && this.tlIsOn){
            currMin = ((((double) (this.pauseTime - this.startTime))/1000)/60)*this.timeRate;
        }else{
            System.out.println("Error: Timeline has been stopped.");
        }

        return round(currMin, 5);
    }

    /**Get the current time only in hours format since starting time*/
    public double getCurrTimeHour(){
        double currHour = 0;

        if(this.tlIsOn && !this.tlIsPaused){
            currHour = ((aux_currRealTimeToSec() - (((double) this.startTime)/1000))/3600)*this.timeRate;
        }else if(this.tlIsPaused && this.tlIsOn){
            currHour = ((((double) (this.pauseTime - this.startTime))/1000)/3600)*this.timeRate;
        }else{
            System.out.println("Error: Timeline has been stopped.");
        }

        return round(currHour, 5);
    }

    public String getTimePeriodOfDay(){
        return this.timePeriodOfDay;
    }

    /**Getting the proper display of the current time in a legible fashion since the starting time*/
    public String displayTime(){
        String currTime;
        int currHour = 0;
        int currMin = 0;
        int currSec = 0;

        if(this.tlIsOn && !this.tlIsPaused){ //When tl is on or resumed, and not paused
            this.time = (aux_currRealTimeToSec() - (((double) this.startTime)/1000))*this.timeRate;
        }else if(this.tlIsPaused && !this.tlIsOn){ //when tl is not on and still currently under pause
            this.time = (((double) (this.pauseTime - this.startTime))/1000)*this.timeRate;
        }else{
            System.out.println("Error: Timeline has been stopped.");
        }

        //every 1/timeRate seconds tick in real life will be set up as 1s in the simulation below;
        currHour = ((int) this.time) / 3600;
        currMin = (((int) this.time) % 3600) / 60;
        currSec = (((int) this.time) % 3600) % 60;

        currTime = currHour + ":" + currMin + ":" + currSec + " Day: " + this.currDay;

        //Night and Day will be linked with the lighting simulation
        if(currHour > 6 && currHour < 18){
            this.timePeriodOfDay = "Day";
        }else if(currHour < 6 || currHour > 18){
            this.timePeriodOfDay = "Night";
        }

        return currTime;
    }

    /**We want the timeline to be reset every 24 hours, but we don't want the animation to be ran in a loop**/
    public Time(int timeRate){

        setTimelineRate(timeRate);

        if(this.tlIsOn && this.getCurrTimeHour() == 24){
            this.stopTimeLine();
            currDay += 1;
            this.startTimeLine();
        }

    }

}
