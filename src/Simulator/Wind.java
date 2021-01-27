package Simulator;
import java.io.*;
import java.util.*;
import Simulator.GUI.SimController;

// Author: Kaige Chen
public class Wind {
    private Time tl;
    private Weather weather;

    private double WindSpeed;
    private WindDirection wd; //Default unit: m/s.
    private double AtmosphericDensity = 1.229; //Default unit kg/m^3
    private ArrayList<Wind> winds = new ArrayList<Wind>();

    public static int[] dir = {1, 2, 3, 4, 5, 6, 7, 8};

    /**The integers in dir are corresponding to the enums following below, by orders */
    public enum WindDirection{
        N, NE, E, SE, S, SW, W, NW
    }

    /**Setter and getter for attributes in Wind class*/
    public WindDirection getWd(){
        return wd;
    }
    public void setWd(WindDirection dir){
        this.wd = dir;
    }
    public double getWindSpeed(){
        return this.WindSpeed;
    }
    public void setWindSpeed(double windSpeed){
        this.WindSpeed = windSpeed;
    }
    public ArrayList<Wind> getWinds(){
        return this.winds;
    }

    /**Helper method to help with numericalizing the wind directions*/
    public static int numericalizeWindDirection(WindDirection wd){
        int numWd = 0;
        if(wd == WindDirection.N){
            numWd = 8;
        }else if(wd == WindDirection.NW){
            numWd = 7;
        }else if(wd == WindDirection.W){
            numWd = 6;
        }else if(wd == WindDirection.SW){
            numWd = 5;
        }else if(wd == WindDirection.S){
            numWd = 4;
        }else if(wd == WindDirection.SE){
            numWd = 3;
        }else if(wd == WindDirection.E){
            numWd = 2;
        }else if(wd == WindDirection.NE){
            numWd = 1;
        }else{
            throw new IllegalArgumentException("Please choose a valid wind direction!");
        }

        return numWd;
    }

    /**Helper method to convert numbers into wind directions*/
    public static WindDirection convertNumToWindDirection(int i){
        WindDirection convertedDir;

        if(i == 8){
            convertedDir = WindDirection.N;
        }else if(i == 7){
            convertedDir = WindDirection.NW;
        }else if(i == 6){
            convertedDir = WindDirection.W;
        }else if(i == 5){
            convertedDir = WindDirection.SW;
        }else if(i == 4){
            convertedDir = WindDirection.S;
        }else if(i == 3){
            convertedDir = WindDirection.SE;
        }else if(i == 2){
            convertedDir = WindDirection.SE;
        }else if(i == 1){
            convertedDir = WindDirection.NE;
        }else{
            throw new IllegalArgumentException("Please choose a valid number: between 1 to 8!");
        }

        return convertedDir;
    }

    public void WindForce(double wSpeed, double contactArea, WindDirection dir, Obj objectBlown){
        double forceMagnitude = contactArea * this.AtmosphericDensity * Math.pow(wSpeed, 2);
        double angle = 0;

        if(dir == WindDirection.N){
            angle = 90;
        }else if(dir == WindDirection.NW){
            angle = 135;
        }else if(dir == WindDirection.W){
            angle = 180;
        }else if(dir == WindDirection.SW){
            angle = 225;
        }else if(dir == WindDirection.S){
            angle = 270;
        }else if(dir == WindDirection.SE){
            angle = 315;
        }else if(dir == WindDirection.E){
            angle = 0;
        }else if(dir == WindDirection.NE){
            angle = 45;
        }else{
            throw new IllegalArgumentException("Please choose a valid wind direction!");
        }

        force.applyForce(new force(null, objectBlown, forceMagnitude, angle), null, objectBlown);
        kinematics.collision(null, objectBlown);
    }

    /**Generate winds with randomly assigned wind direction, but with fixed max wind speed. This is used in a calm weather setting*/
    public Wind generateWind(double minWindSpeed, double maxWindSpeed, boolean outside){ //Here we won't assign the max wind speed to be too high.
        Random rand = new Random();
        int i = rand.nextInt(8);
        WindDirection randDir = convertNumToWindDirection(dir[i]);

        //Math.random() * (max - min + 1) + min;
        return new Wind(Math.random() * (maxWindSpeed - minWindSpeed + 1) + minWindSpeed, randDir, outside);
    }

    /**Generate winds with a max wind speed and a preset wind direction. This is used in a predictable weather setting.*/
    public Wind generateWind(double minWindSpeed, double maxWindSpeed, WindDirection wd, boolean outside){
        return new Wind(Math.random() * (maxWindSpeed - minWindSpeed + 1) + minWindSpeed, wd, outside);
    }

    /**Secondary constructor of the Wind class
     * It's better to use this constructor if you want to just generate just a single object of wind.
     */
    public Wind(double WindSpeed, WindDirection wd, boolean outside){
        if(!outside){
            throw new IllegalArgumentException("Must be outside to have wind! Winds only exist in an outdoor environments.");
        }

        //Setting up parameters.
        this.WindSpeed = WindSpeed;
        this.wd = wd;
    }

    /**Main Constructor of the Wind class
     * It's better to use this constructor if you want to generate a system of wind
     */
    public Wind(double WindSpeed, WindDirection wd, boolean outside, Weather weather){
        if(!outside){
            throw new IllegalArgumentException("Must be outside to have wind! Winds only exist in an outdoor environments.");
        }

        //Setting up parameters.
        this.WindSpeed = WindSpeed;
        this.wd = wd;
        this.weather = weather;
        int numAirStreams = 0;

        Random rand = new Random();

        //Generating winds(ArrayList of winds) according to the different weather settings.
        if(weather.getWeatherType().equals(Weather.WeatherType.SUNNY)){
            int num = rand.nextInt(4);

            if(num == 0){
                //do nothing.
            }else if(wd == null){
                for(numAirStreams = num; numAirStreams > 0; numAirStreams--){ //Randomly deciding to generate 0 to 3 numbers of air streams.
                    this.winds.add(generateWind(0, WindSpeed, outside));
                }
            }
        }else if(weather.getWeatherType().equals(Weather.WeatherType.CLOUDY)){
            int num = rand.nextInt(4);

            if(num == 0){
                //do nothing.
            }else if(wd == null){
                for(numAirStreams = num; numAirStreams > 0; numAirStreams--){ //Randomly deciding to generate 0 to 3 numbers of air streams.
                    this.winds.add(generateWind(0, WindSpeed, outside));
                }
            }
        }else if(weather.getWeatherType().equals(Weather.WeatherType.RAINY)){
            for(numAirStreams = rand.nextInt(5) + 1; numAirStreams > 0; numAirStreams--){ //Randomly deciding to generate 1 to 5 numbers of air streams.
                this.winds.add(generateWind((WindSpeed/2), WindSpeed, wd, outside));
            }
        }else if(weather.getWeatherType().equals(Weather.WeatherType.STORMY)){
            for(numAirStreams = (int) (Math.random() * 6 + 5); numAirStreams > 0; numAirStreams--){ //Randomly deciding to generate 5 to 10 numbers of air streams.
                this.winds.add(generateWind((WindSpeed/2), WindSpeed, wd, outside));
            }
        }else{
            throw new IllegalArgumentException("Must choose one of the four types of weathers.");
        }
    }

}
