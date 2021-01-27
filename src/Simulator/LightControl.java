package Simulator;

import Simulator.GUI.SimController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.stage.Stage;

// Author: Kaige Chen
public class LightControl{
    private Time tl;
    private double Brightness = 1;
    public Light.Distant distant;
    public Lighting lighting;

    /**Prepare and instantiating the light source with distant light*/
    private Light.Distant prepareLight(){
        Light.Distant distantLight = new Light.Distant();
        distantLight.setAzimuth(45.0);
        distantLight.setElevation(30.0);

        return distantLight;
    }

    public double getBrightness(){
        return this.Brightness;
    }
    public void setBrightness(double brightness){
        this.Brightness = brightness;
    }

    public LightControl(Time tl){
        this.tl = tl;
        this.distant = prepareLight();
        this.lighting = new Lighting();

        //Setting the source of the light
        lighting.setLight(this.distant);

        //Get time period of the day information
        tl.displayTime();
        if(tl.getTimePeriodOfDay().equals("Day")){
            //Change brightness
            double dayTimeBrightness = this.Brightness;
            lighting.setDiffuseConstant(this.Brightness);

            this.distant.setAzimuth(90);
            this.distant.setElevation(60);


        }else if(tl.getTimePeriodOfDay().equals("Night")){
            //Change brightness
            lighting.setDiffuseConstant(0.5);

            this.distant.setAzimuth(270);
            this.distant.setElevation(15);
        }


    }

}
