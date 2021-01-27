package Simulator;
import Simulator.GUI.SimController;

import java.util.*;

// Author: Kaige Chen
public class Thermal {

    private float temp;
    private float k; //thermal conductivity, default unit is usually W/m/K, in our case here it will be W/cell/K
    private float heatCapacity;

    public cell Cell;
    public cellList cl;
    public Time tl;
    public boolean tempChange = false;


    /**Get the probability density using the normal distribution, mean should be the temperature at the center */
    private float getDistributionDensity(double mean, double std, double x){
        float density;
        double pow = ((Math.pow((x - mean), 2))*(1/(-2 * Math.pow(std, 2))));

        density = (float) (Math.exp(pow)*(1/(std*Math.sqrt(2*Math.PI))));

        return density;
    }

    /**Thermal conduction using fourier's law*/
    //deltaPosition's default unit is cell, e.g. deltaPosition = 5 cell. Therefore, contactArea's unit is cell^2, e.g. contactArea = 12 cell^2
    private float fourier(float contactTemp, double initialTimeSec, double contactArea, double deltaPosition){
        float heatQ = 0;
        float deltaTime = (float) (this.tl.getCurrTimeSec() - initialTimeSec);
        float deltaTemp = 0;

        //same numerical value in Kelvin or Celsius.
        if(contactTemp > this.getCurrTemp()){
            deltaTemp = contactTemp - this.getCurrTemp();
        }else if(this.getCurrTemp() > contactTemp){
            deltaTemp = this.getCurrTemp() - contactTemp;
        }

        heatQ = (-(deltaTime * deltaTemp * ((float) contactArea) * this.k) / ((float) deltaPosition));
        return heatQ;
    }

    /**Setter and getters for attributes for Thermal*/
    public void setCurrTemp(float temperature){
        this.temp = temperature;
    }

    public float getCurrTemp(){
        return this.temp;
    }

    public float getHeatCapacity(){
        return this.heatCapacity;
    }

    public float getK(){
        return this.k;
    }

    /**Mode for heat transfer, however the methods of conduction, convection or radiation*/
    public void conduction(HeatTransferType type, Thermal HeatFluxSource, double contactArea, boolean outside, Wind wind){// if no wind then it's null

        switch (type.getConductType()){
            case CONDUCTION:
                double deltaPosition = 1; //default at 1 cell

                /**Applying fourier's law to different types of conduction.*/
                if(type.getContactType() == HeatTransferType.contactType.VERTICAL){
                    deltaPosition = HeatFluxSource.Cell.getHeight();
                }else if(type.getContactType() == HeatTransferType.contactType.HORIZONTAL){
                    //Get delta positions
                    if(HeatFluxSource.Cell.getPosition()[0] == this.Cell.getPosition()[0]){
                        deltaPosition = Math.abs(HeatFluxSource.Cell.getPosition()[1] - this.Cell.getPosition()[1]);
                    }else if(HeatFluxSource.Cell.getPosition()[1] == this.Cell.getPosition()[1]){
                        deltaPosition = Math.abs(HeatFluxSource.Cell.getPosition()[0] - this.Cell.getPosition()[0]);
                    }

                }else{
                    throw new IllegalArgumentException("Choose only contact from VERTICAL or HORIZONTAL for conduction.");
                }

                float q = fourier(HeatFluxSource.getCurrTemp(), type.getContactTime(), contactArea, deltaPosition);
                HeatFluxSource.setCurrTemp(HeatFluxSource.getCurrTemp() + (q/HeatFluxSource.getHeatCapacity()));
                this.setCurrTemp(this.getCurrTemp() - (q/this.getHeatCapacity()));

                this.Cell.setTemperature(this.getCurrTemp()); //Updating the new temperature to the current as well
                break;
            case CONVECTION:
                /**We are only working on convection by air, with free convection by air and forced convection by wind. Wind carries heat away. Heat dissipates by time.
                 * In this case the heat flux source should just be the surface of the contact area itself.
                 * Newton's law of cooling: Q = h*A*deltaT, h is convective heat transfer coefficient.
                 */

                //Initialize heat transfer coefficient. by default h of air = 10, Units: W/cell^2/K
                float h = 10;
                float tau = this.heatCapacity / (h * ((float) contactArea));
                double tauToDouble = Double.parseDouble(Float.toString(tau));
                float Q = 0;
                float envTemp = 0;
                int row = this.cl.row;
                int col = this.cl.col;

                while(this.cl.getCellAt(row + 1, col).getTemperature() == HeatFluxSource.getCurrTemp() || this.cl.getCellAt(row, col+1).getTemperature() == HeatFluxSource.getCurrTemp()){
                    boolean condition1 = this.cl.getCellAt(row + 1, col).getTemperature() == HeatFluxSource.getCurrTemp();
                    boolean condition2 = this.cl.getCellAt(row, col+1).getTemperature() == HeatFluxSource.getCurrTemp();

                    if(condition1 && !condition2){
                        envTemp = this.cl.getCellAt(row, col+1).getTemperature();
                        break;
                    }else if(!condition1 && condition2){
                        envTemp = this.cl.getCellAt(row + 1, col).getTemperature();
                        break;
                    }else{
                        row++;
                        col++;
                    }
                }

                //Different types of Convection results in different ways of heat dissipation.
                if(type.getConvectType() == HeatTransferType.convectType.FREE){ //Free convection can happen in both outdoor or indoor settings.

                    //(T(0) - T env)*e^(-t/tau)
                    float timeDepDeltaTemp = (HeatFluxSource.getCurrTemp() - envTemp) * ((float) (Math.exp(-this.tl.getCurrTimeSec() / tauToDouble)));

                    //Convective heat transfer coefficient time the contact area times temperature difference between the surface and the air.
                    Q = h * ((float) contactArea) * timeDepDeltaTemp;

                    this.setCurrTemp(HeatFluxSource.getCurrTemp() - (Q/this.getHeatCapacity()));
                }else if(type.getConvectType() == HeatTransferType.convectType.FORCED && outside && (wind != null)){

                    int dir = Wind.numericalizeWindDirection(wind.getWd());
                    h = (float) (12.12 - 1.16 * wind.getWindSpeed() + 11.6 * Math.sqrt(wind.getWindSpeed()));

                    float timeDepDeltaTemp = (HeatFluxSource.getCurrTemp() - envTemp) * ((float) (Math.exp(-this.tl.getCurrTimeSec() / tauToDouble)));

                    //Convective heat transfer coefficient time the contact area times temperature difference between the surface and the air.
                    Q = h * ((float) contactArea) * timeDepDeltaTemp;

                    float deltaTemp = HeatFluxSource.getCurrTemp() - envTemp;

                    int c = this.cl.col;
                    int r = this.cl.row;

                    while(this.cl.getCellAt(r, c).getTemperature() != envTemp){
                        this.setCurrTemp(HeatFluxSource.getCurrTemp() - (Q/this.getHeatCapacity()));

                        this.cl.getCellAt(r, c).setTemperature(this.getCurrTemp());

                       if(dir == 8){
                            r--;
                        }else if(dir == 7){
                            r--;
                            c--;
                        }else if(dir == 6){
                            c--;
                        }else if(dir == 5){
                            r++;
                            c--;
                        }else if(dir == 4){
                            r++;
                        }else if(dir == 3){
                            r++;
                            c++;
                        }else if(dir == 2){
                            c++;
                        }else if(dir == 1) {
                            r--;
                            c++;
                        }else if(dir == 0){
                            break;
                        }
                    }
                }else{
                    throw new IllegalArgumentException("Choose only FREE convection or FORCED convection.");
                }

                break;
            case RADIATION:
                //We will also use normal distribution here.
                float centerRadTemp = HeatFluxSource.getCurrTemp(); //Now this heat source is imagined as form of radiation.
                float borderTemp = this.getCurrTemp();
                float deltaTemp = centerRadTemp - borderTemp;

                double Aux = Double.parseDouble(Float.toString(deltaTemp));
                int i = new Float(deltaTemp).intValue();

                int Col = this.cl.col;
                int Row = this.cl.row;
                int dir = 8; //indicator of which direction of the spread of heat we are setting up

                while(i > 0){
                    double inc = getDistributionDensity(Aux, 1, Aux) / i;
                    float t = ((float) Math.sqrt(Math.log(inc * Math.sqrt(2 * Math.PI)) * (-2))) + deltaTemp;

                    this.setCurrTemp(borderTemp + t);
                    this.cl.getCellAt(Row, Col).setTemperature(this.temp);

                    i--;

                    if(i == 0){
                        dir--;
                        i = new Float(deltaTemp).intValue();
                    }else if(dir == 8){
                        Row--;
                    }else if(dir == 7){
                        Row--;
                        Col--;
                    }else if(dir == 6){
                        Col--;
                    }else if(dir == 5){
                        Row++;
                        Col--;
                    }else if(dir == 4){
                        Row++;
                    }else if(dir == 3){
                        Row++;
                        Col++;
                    }else if(dir == 2){
                        Col++;
                    }else if(dir == 1) {
                        Row--;
                        Col++;
                    }else if(dir == 0){
                        break;
                    }
                }

                break;
        }
    }

    /**The thermal class has this constructor*/
    public Thermal(float temperature, float k, float heatCapacity, Time tl, cell c, cellList cl) {
        this.setCurrTemp(temperature);
        this.k = k;
        this.heatCapacity = heatCapacity;
        this.tl = tl;
        this.Cell = c;
        this.cl = cl;

        if(this.temp != c.getTemperature()){
            this.tempChange = true;
        }
    }

}
