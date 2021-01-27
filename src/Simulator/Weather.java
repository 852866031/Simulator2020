package Simulator;
import java.util.*;
import Simulator.GUI.SimController;

// Author: Kaige Chen
public class Weather {
    private WeatherType weatherType;
    private float currTemperature;

    public Wind wind;
    public double windSpeed;
    public Wind.WindDirection windDirection;

    public enum WeatherType{
        SUNNY, //Little to none wind, bright setting.
        CLOUDY, //Little to non wind, gloomy setting.
        RAINY, //Some winds, gloomy setting.
        STORMY //Predictable winds, gloomy setting.
    }

    public WeatherType getWeatherType(){
        return this.weatherType;
    }

    public void setWeatherType(WeatherType weatherType){
        this.weatherType = weatherType;
    }

    public float getCurrTemperature(){
        return this.currTemperature;
    }

    public void setCurrTemperature(float temp){
        this.currTemperature = temp;
    }

    public void setWindCondition(double windSpeed, Wind.WindDirection wd){ //if no wind then put in WindDirection noWind = null in the parameters.
        this.windSpeed = windSpeed;
        this.windDirection = wd;
    }

    public Wind setUpWind(boolean outside){
        this.wind = new Wind(this.windSpeed, this.windDirection, outside, this);
        return this.wind;
    }

    public Wind getWind(){
        return this.wind;
    }

    /**Constructor of the weather class.*/
    public Weather(WeatherType weatherType, boolean outside){
        if(!outside){
            throw new IllegalArgumentException("Must be outside to have wind! Winds only exist in an outdoor environments.");
        }else if(weatherType == null){
            throw new IllegalArgumentException("The type of weather cannot be null.");
        }

        this.weatherType = weatherType;

        if(this.weatherType == WeatherType.SUNNY){
            //Setting the environment temperature(All the cells in the map uniformly): random temperature between 25 degrees and 32 degrees celsius.
            setCurrTemperature(((float) Math.random() * (32 - 25 + 1) + 25));

            //Change the lighting setting:
            //Change the current brightness is 1.2

        }else if(this.weatherType == WeatherType.CLOUDY){
            //Setting the environment temperature(All the cells in the map uniformly): random temperature between 20 degrees and 27 degrees celsius.
            setCurrTemperature(((float) Math.random() * (27 - 20 + 1) + 20));

            //Change the lighting setting: Change the brightness to 1.0

        }else if(this.weatherType == WeatherType.RAINY){
            //Setting the environment temperature(All the cells in the map uniformly): random temperature between 18 degrees and 25 degrees celsius.
            setCurrTemperature(((float) Math.random() * (25 - 18 + 1) + 18));

            //Change the lighting setting: Change the brightness to 1.0


        }else if(this.weatherType == WeatherType.STORMY){
            //Setting the environment temperature(All the cells in the map uniformly): random temperature between 16 degrees and 23 degrees celsius.
            setCurrTemperature(((float) Math.random() * (23 - 16 + 1) + 16));

            //Change the lighting setting: Change the brightness to 0.8

        }else{
            throw new IllegalArgumentException("Must choose one of the four legal weathers: SUNNY, CLOUDY, RAINY, STORMY");
        }
    }


}
