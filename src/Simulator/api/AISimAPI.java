package Simulator.api;

import java.util.ArrayList;

// Author: Isidora Duma
// to be updated
// called from SimController start method
public class AISimAPI implements AISimInterface {

    public void sendNN(int input[]){

        // send input[] to neural network
    }

    public ArrayList<String> getCommand(){

        // get command from META
        // turn it into arraylist that returns { "MOVE", "2", "FORWARD" }
        // this example means move forward 2 spots
        // the simulator will convert this direction into a movement

        // example
        ArrayList<String> command = new ArrayList<String>();

        command.add("MOVE");
        command.add("2");
        command.add("FORWARD");

        return command;
    }
}
