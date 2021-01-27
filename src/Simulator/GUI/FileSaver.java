package Simulator.GUI;

import Simulator.GUI.Tile;
import Simulator.Movement;
import Simulator.Time;
import Simulator.avatar;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Author: Isidora Duma
// class to save files in the simulator
public class FileSaver {

    private int width;
    private int height;
    private Tile[][] grid;
    private double wind;
    private double windDir;
    private double temp;
    private boolean outside;

    public String saveMapInside(int width, int height, double temp, Tile[][] grid) {
        StringBuilder sb = new StringBuilder();

        // append indoor labels
        sb.append("in\n");

        // width and height
        sb.append("[width],[height]\n");
        sb.append(width + "," + height + "\n");

        // temperature
        sb.append("[temp]\n");
        sb.append(temp + "\n");

        // cell info label
        sb.append("[x] [y],[height],[object],[mass]\n");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile c = grid[i][j];
                sb.append(i + " " + j + "," + c.getCellHeight() + ",");
                if(c.isBox()) {
                    sb.append("box," + c.getBoxMass() + "\n");
                }
                else if(c.isWall()) {
                    sb.append("wall\n");
                }
                else {
                    sb.append("N\n");
                }
            }
        }

        return sb.toString();
    }

    // currently only handles 1 robot
    public String saveRobot(int width, int height, String name, int x, int y, String goal) {
        StringBuilder sb = new StringBuilder();

        sb.append("[width],[height]\n");
        sb.append(width + "," + height + "\n");
        sb.append("[name of robot],[x],[y],[goal]\n");
        sb.append(name + "," + x + "," + y);
        if(!goal.equals("")) {
            sb.append("," + goal);
        }
        sb.append("\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    // saves outdoor map info into csv file
    public String saveMapOutside(int width, int height, double wind, String windDir, double temp, Tile[][] grid) {
        StringBuilder sb = new StringBuilder();

        // outdoor label
        sb.append("out\n");

        // width and height
        sb.append("[width],[height]\n");
        sb.append(width + "," + height + "\n");

        // wind speed, wind dir and temp
        sb.append("[wind speed],[wind direction],[temp]\n");
        sb.append(wind + "," + windDir + "," + temp + "\n");

        // cell info
        sb.append("[x] [y],[height],[terrain],[object],[mass]\n");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile c = grid[i][j];
                sb.append(i + " " + j + "," + c.getCellHeight() + ",");

                // terrain
                if(c.isWater()) {
                    sb.append("water");
                }
                else if(c.isGrass()) {
                    sb.append("grass");
                }
                else if(c.isSand()) {
                    sb.append("sand");
                }
                else {
                    sb.append("asphalt");
                }

                // objects
                if(c.isBox()) {
                    sb.append(",box," + c.getBoxMass() + "\n");
                }
                else if(c.isWall()) {
                    sb.append(",wall\n");
                }
                else if(c.isTree()) {
                    sb.append(",tree\n");
                }
                else {
                    sb.append(",N\n");
                }
            }
        }
        return sb.toString();
    }

    // builds output of simulation into string
    public String buildOutput(Time time, int step, int[] avatarDir, avatar player, Tile avatar, String name, Tile[][] grid, ArrayList<String> input, int[] sensor) {

        String command;
        StringBuilder sb = new StringBuilder();

        // robot information
        command = step + ",robot," + name + "," + player.mass + "," + player.y + "," + player.x + "," + (time.getCurrTimeSec()) + ","
                + avatar.getCellHeight() + "," + input.get(2) + "," + input.get(0) + "," + sensor[0] + "," + sensor[1] + "," + sensor[2] + "\n";

        sb.append(command);

        // box information
        // loop through grid
        // if cell is a movable box, append box information
        // future updates: add all obstacle/object/terrain info
        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid[i].length; j++) {
                Tile tile = grid[i][j];

                if(tile.isBox()) {
                    command = step + ",box,boxid," + tile.getBoxMass() + "," + tile.getX() + "," + tile.getY() + "," + time.getCurrTimeSec() + ","
                            + tile.getCellHeight() + "\n";
                    sb.append(command);
                }
            }
        }
        return sb.toString();
    }

    // saves file onto computer using FileChooser
    public void saveFile(String title, String content, TextField windText) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog((Stage) windText.getScene().getWindow());
        if (file != null) {
            try {
                BufferedWriter br = new BufferedWriter(new FileWriter(file.getPath()));

                br.write(content);
                br.close();

            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "File error. Please try again.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public void setGrid(Tile[][] grid) {
        this.grid = grid;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getWindDir() {
        return windDir;
    }

    public void setWindDir(double windDir) {
        this.windDir = windDir;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
