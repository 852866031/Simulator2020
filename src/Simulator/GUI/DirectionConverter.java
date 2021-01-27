package Simulator.GUI;

import Simulator.Movement;

// Author: Isidora Duma
// class converts a direction respective to the robot into a geographic coordinate
// coordinates represented with keyboard WASDQEZC keys
public class DirectionConverter {

    public char east(String direction) {
        if(direction.equalsIgnoreCase(String.valueOf(Movement.FORWARD))) {
            return 'd';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.RIGHT))) {
            return 's';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.LEFT))) {
            return 'w';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPRIGHT))) {
            return 'c';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPLEFT))) {
            return 'e';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNRIGHT))) {
            return 'z';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNLEFT))) {
            return 'q';
        }
        else {
            return 0;
        }
    }

    public char west(String direction) {
        if(direction.equalsIgnoreCase(String.valueOf(Movement.FORWARD))) {
            return 'a';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.RIGHT))) {
            return 'w';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.LEFT))) {
            return 's';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPRIGHT))) {
            return 'q';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPLEFT))) {
            return 'z';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNRIGHT))) {
            return 'e';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNLEFT))) {
            return 'c';
        }
        else {
            return 0;
        }
    }

    public char south(String direction) {
        if(direction.equalsIgnoreCase(String.valueOf(Movement.FORWARD))) {
            return 's';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.RIGHT))) {
            return 'a';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.LEFT))) {
            return 'd';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPRIGHT))) {
            return 'z';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPLEFT))) {
            return 'c';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNRIGHT))) {
            return 'q';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNLEFT))) {
            return 'e';
        }
        else {
            return 0;
        }
    }

    public char north(String direction) {
        if(direction.equalsIgnoreCase(String.valueOf(Movement.FORWARD))) {
            return 'w';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.RIGHT))) {
            return 'd';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.LEFT))) {
            return 'a';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPRIGHT))) {
            return 'e';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPLEFT))) {
            return 'q';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNRIGHT))) {
            return 'c';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNLEFT))) {
            return 'z';
        }
        else {
            return 0;
        }
    }

    public char northWest(String direction) {
        if(direction.equalsIgnoreCase(String.valueOf(Movement.FORWARD))) {
            return 'q';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.RIGHT))) {
            return 'e';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.LEFT))) {
            return 'z';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPRIGHT))) {
            return 'w';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPLEFT))) {
            return 'a';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNRIGHT))) {
            return 'd';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNLEFT))) {
            return 's';
        }
        else {
            return 0;
        }
    }

    public char southWest(String direction) {
        if(direction.equalsIgnoreCase(String.valueOf(Movement.FORWARD))) {
            return 'z';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.RIGHT))) {
            return 'q';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.LEFT))) {
            return 'c';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPRIGHT))) {
            return 'a';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPLEFT))) {
            return 's';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNRIGHT))) {
            return 'w';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNLEFT))) {
            return 'd';
        }
        else {
            return 0;
        }
    }

    public char northEast(String direction) {
        if(direction.equalsIgnoreCase(String.valueOf(Movement.FORWARD))) {
            return 'e';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.RIGHT))) {
            return 'c';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.LEFT))) {
            return 'q';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPRIGHT))) {
            return 'd';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPLEFT))) {
            return 'w';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNRIGHT))) {
            return 's';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNLEFT))) {
            return 'a';
        }
        else {
            return 0;
        }
    }

    public char southEast(String direction) {
        if(direction.equalsIgnoreCase(String.valueOf(Movement.FORWARD))) {
            return 'c';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.RIGHT))) {
            return 'z';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.LEFT))) {
            return 'e';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPRIGHT))) {
            return 's';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.UPLEFT))) {
            return 'd';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNRIGHT))) {
            return 'a';
        }
        else if(direction.equalsIgnoreCase(String.valueOf(Movement.DOWNLEFT))) {
            return 'w';
        }
        else {
            return 0;
        }
    }
}