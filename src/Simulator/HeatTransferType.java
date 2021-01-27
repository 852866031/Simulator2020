package Simulator;

// Author: Kaige Chen
public class HeatTransferType {
    private conductType type;
    private contactType t;
    private convectType ConvectType;
    private Time tl;
    private double contactTime; //Time of contact or time of the start of conduction.

    public enum conductType{
        CONDUCTION, //Heat transfer by conduction such as contact.
        CONVECTION, //Heat transfer by liquid or gas such as the atmosphere, water, etc.
        RADIATION //Heat transfer by light in our scenario.
    }

    public enum contactType{
        VERTICAL,
        HORIZONTAL
    }

    public enum convectType{
        FREE,
        FORCED
    }


    public conductType getConductType(){
        return type;
    }

    public void setConductType(conductType type){
        this.type = type;
    }

    public contactType getContactType(){
        return this.t;
    }

    public void setContactType(contactType t){
        this.t = t;
    }

    public convectType getConvectType(){
        return this.ConvectType;
    }

    public void setConvectType(convectType ConvectType){
        this.ConvectType = ConvectType;
    }

    public double getContactTime() {
        return this.contactTime;
    }

    public HeatTransferType(conductType type, Time tl){
        this.type = type;
        this.tl = tl;

        if(this.type == conductType.CONDUCTION){
            contactTime = this.tl.getCurrTimeSec();
            setContactType(this.getContactType());
        }else if(this.type == conductType.CONVECTION){
            setConvectType(this.getConvectType());
        }
    }

}
