package Simulator.GUI;

import Simulator.*;
import Simulator.api.AISimAPI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

// Author: Isidora Duma
// Some code added by Jiaxuan Chen and Kaige Chen flagged at each function
// controls file Sim.fxml from resources
public class SimController {

    private int width;
    private int height;
    private Tile[][] grid;
    private File file;
    private double wind;
    private double temp;
    private String windDir;
    public world world;
    private StringBuilder output;
    private boolean started;
    private double startTime;
    private String command;
    XYChart.Series series;
    private boolean outside;
    private RobotConfigController rcController;
    private avatar player;
    private boolean robotSetup;
    private SimController sc;
    private AISimAPI ai;
    Timeline timeline;
    Time time;
    LightControl lightControl;
    private int step;
    private String name;
    private String goal;
    FileSaver fs;
    private int startX;
    private int startY;

    // **************************** CONSTRUCTORS **************************** //
    public SimController() {
    }

    public SimController(int width, int height, boolean outside){
        this.width = width;
        this.height = height;

        grid = new Tile[width][height];

        output = new StringBuilder();

        // outside = true if outside
        // false if inside
        this.outside = outside;
        this.robotSetup = false;
        this.sc=this;
        ai = new AISimAPI();
        time = new Time(1);
        fs = new FileSaver();
    }

    public SimController(File file){
        this.file = file;
        output = new StringBuilder();

        this.robotSetup = false;
        ai = new AISimAPI();
        time = new Time(1);
        fs = new FileSaver();
    }

    // **************************** FXML INITIALIZERS **************************** //

    @FXML
    private Pane gridMap;

    @FXML
    private TextField windText;

    @FXML
    private TextField tempText;

    @FXML
    private TextField heightText;

    @FXML
    private TextField directionText;

    @FXML
    private MenuItem saveMapOpt;

    @FXML
    private MenuItem saveRobotOpt;

    @FXML
    private MenuItem saveOutOpt;

    @FXML
    private LineChart elevationChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private Button loadConfigBtn;

    @FXML
    private Button robotSetupbtn;

    @FXML
    private Label or;

    @FXML
    private Button startBtn;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button endBtn;

    @FXML
    private ChoiceBox<String> weatherOptions;

    @FXML
    private Button mapCompleted;

    @FXML
    // Author: Isidora Duma
    public void initialize(){

        // no file chosen, map must be configured, initialize each cell
        if(file == null) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Tile tile = new Tile(i, j, width, height, false, outside);
                    grid[i][j] = tile;
                    gridMap.getChildren().add(tile);
                    tile.allowCreate();
                }
            }
            // inside, don't allow user to set weather options
            if(!outside) {
                windText.setEditable(false);
                directionText.setEditable(false);
            }
        }
        // file selected, parse file and intialize map
        else {
            parseFile();
            mapCompleted.setVisible(false);
        }

        startBtn.setDisable(true);
        pauseBtn.setDisable(true);
        endBtn.setDisable(true);

        ObservableList<String> weatherTypes = FXCollections.observableArrayList("Custom","Sunny","Cloudy","Rainy","Stormy");
        weatherOptions.setItems(weatherTypes);
        weatherOptions.setValue("Custom");

        // disable weather settings if environment is inside
        if(!outside) {
            weatherOptions.setDisable(true);
            windText.setDisable(true);
            directionText.setDisable(true);
        }

        saveMapOpt.setDisable(true);
        saveOutOpt.setDisable(true);
        saveRobotOpt.setDisable(true);

        // user must create their map first and can only then set up their robot
        if(file == null) {
            robotSetupbtn.setVisible(false);
            or.setVisible(false);
            loadConfigBtn.setVisible(false);
        }

    }


    /* *********************************************************************** //
    // ************************* MEDIA CONTROLS AND ************************** //
    // ************************* WORLD INSTANTIATION ************************* //
    // *********************************************************************** */
    @FXML
    // Author: Isidora Duma
    public void start(MouseEvent event) throws InterruptedException {

        if(!started && robotSetup) {
            started = true;

            // find avatar
            Tile avatar = grid[startX][startY];
            heightText.setText(String.valueOf(avatar.getCellHeight()));

            int[][] heights = new int[width][height];
            char[][] terrain = new char[width][height];
            world = new map(height, width, terrain, heights, player);

            // new sensor at step 0
            // Author of sensor: Jiaxuan Chen
            Sensor sensor = new Sensor();
            step = 0;
            int[] result = sensor.simpleDetectDistanceBySonic(player, (map)world, outside);

            // header of output file
            output.append("[step],[robot/box],[robot name/box id],[mass],[x],[y],[time],[height],[direction],[action],[middle sensor],[left sensor],[right sensor]\n");

            // step 0 of output
            output.append(step + ",robot," + name + "," + player.mass + "," + player.y + "," + player.x + ",0.00," + avatar.getCellHeight() + ",,," + result[0] + "," + result[1] + "," + result[2] + "\n");

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Tile c = grid[i][j];
                    heights[i][j] = c.getCellHeight();
                    ((map) world).cellList.getCellAt(i, j).setTemperature((float) temp);
                    // set terrain/friction here
                    if (!c.isOutside()) {
                        // set friction index for inside ground
                        c.setFriction(0.005);
                        ((map) world).cellList.getCellAt(i, j).setFrictionIndex(0.005);
                        terrain[i][j] = 's';
                    } else if (c.isGrass()) {
                        // set friction for grass
                        c.setFriction(0.0095);
                        ((map) world).cellList.getCellAt(i, j).setFrictionIndex(0.0095);
                        terrain[i][j] = 'g';
                    } else if (c.isSand()) {
                        // set friction for sand
                        c.setFriction(0.0029);
                        ((map) world).cellList.getCellAt(i, j).setFrictionIndex(0.029);
                        terrain[i][j] = 's';
                    } else if (c.isWater()) {
                        ((map) world).cellList.getCellAt(j, i).setWater();
                        terrain[i][j] = 'w';
                    } else if (c.isAsphalt()) {
                        // set friction for asphalt
                        // default
                        c.setFriction(0.0035);
                        ((map) world).cellList.getCellAt(i, j).setFrictionIndex(0.0035);
                        terrain[i][j] = 'c';
                    }

                    if (c.isBox()) {
                        worldObject wo = new worldObject(i, j, "Box", c.getBoxMass(), true, ((map) world).cellList);
                        world.addObject(wo);
                        ((map) world).cellList.getCellAt(j, i).setObject(wo);

                        output.append(step + ",box,boxid," + c.getBoxMass() + "," + c.getX() + "," + c.getY() + ",0.00,"
                                + c.getCellHeight() + "\n");
                    }
                    if (c.isWall()) {
                        worldObject wo = new worldObject(j, i, "Wall", -1, false, ((map) world).cellList);
                        world.addObject(wo);
                        ((map) world).cellList.getCellAt(j, i).setObject(wo);
                    }
                    if (c.isTree()) {
                        worldObject wo = new worldObject(j, i, "Tree", -1, false, ((map) world).cellList);
                        world.addObject(wo);
                        ((map) world).cellList.getCellAt(j, i).setObject(wo);
                    }
                }
            }
            player.initialize(((map) world).cellList);

            time.startTimeLine();
            startTime = time.getCurrTimeSec();

            /*Set up light control here*/
            // author of light control: Kaige Chen
            lightControl = new LightControl(time);

            series = new XYChart.Series();
            elevationChart.setLegendVisible(false);

            series.getData().add(new XYChart.Data("0", avatar.getCellHeight()));
            elevationChart.getData().addAll(series);

            // SIMULATION LOOP //
            timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    int result[] = sensor.simpleDetectDistanceBySonic(player, (map)world, outside);

                    ai.sendNN(result);

                    // { "MOVE","2","LEFT" }
                    // { "MOVE","1","FORWARD" }
                    // ETC...
                    ArrayList<String> input = ai.getCommand();

                    String direction;

                    if(input.get(0).equalsIgnoreCase("MOVE")) {
                        direction = input.get(2);
                    }
                    else {
                        direction = "";
                    }

                    int numMoves = Integer.parseInt(input.get(1));

                    int[] avatarDir = ((map) world).getPlayer().getAvatarDir();
                    int[] oldPos = new int[2];
                    try {
                        DirectionConverter dc = new DirectionConverter();
                        char move;

                        // convert direction to qweasdzc
                        if(avatarDir[0]==0 && avatarDir[1]==1) {    // east
                            move = dc.east(direction);

                        } else if(avatarDir[0]==0 && avatarDir[1]==-1) {    // west
                            move = dc.west(direction);

                        } else if(avatarDir[0]==1 && avatarDir[1]==0) {     // south
                            move = dc.south(direction);

                        }else if(avatarDir[0]==-1 && avatarDir[1]==0){     // north
                            move = dc.north(direction);

                        }else if((avatarDir[0]==-1 && avatarDir[1]==-1)){     // north west
                            move = dc.northWest(direction);
                        }
                        else if(avatarDir[0]==1 && avatarDir[1]==-1) {  // south west
                            move = dc.southWest(direction);
                        }
                        else if(avatarDir[0]==-1 && avatarDir[1]==1) {     // north east
                            move = dc.northEast(direction);
                        }
                        else {  // south east
                            move = dc.southEast(direction);
                        }

                        Tile myAv = null;

                        for(int i=0; i < numMoves; i++) {

                            // updates avatar to new position
                            oldPos = world.updateSim(player, move);
                            step++;

                            myAv = grid[player.y][player.x];
                            result = sensor.simpleDetectDistanceBySonic(player, (map)world, outside);

                            // append current command to output file
                            command = fs.buildOutput(time, step, avatarDir, player, myAv, name, grid, input, result);
                            System.out.println(command);
                            output.append(command);

                            // moves robot on screen
                            moveAvatarOnScreen(oldPos);
                        }

                        series.getData().add(new XYChart.Data(String.valueOf(time.getCurrTimeSec()), myAv.getCellHeight()));

                    } catch (IOException e1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong in the simulation loop. Please restart your program and try again.", ButtonType.OK);
                        alert.showAndWait();
                        return;
                    }
                }
            }));
            // start timeline
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            startBtn.setDisable(true);
            pauseBtn.setDisable(false);
            endBtn.setDisable(false);
        }
        // resume timeline
        if(started) {
            startBtn.setDisable(true);
            pauseBtn.setDisable(false);
            timeline.play();
            time.resumeTimeLine();
        }
    }

    @FXML
    // Author: Isidora Duma
    // when user hits pause, pauses timeline
    public void pause(MouseEvent event){
        if(timeline != null) {
            startBtn.setDisable(false);
            pauseBtn.setDisable(true);
            timeline.pause();
            time.pauseTimeLine();
        }
    }

    @FXML
    // Author: Isidora Duma
    // ends simulation
    public void end(MouseEvent event){

        // no restarting simulation
        startBtn.setDisable(true);
        pauseBtn.setDisable(true);
        endBtn.setDisable(true);
        timeline.stop();
        time.stopTimeLine();
        System.out.println(output);

        // can now save output file
        saveOutOpt.setDisable(false);
    }

    /* *********************************************************************** //
    // ******************** SIMULATION LOOP UPDATE GUI *********************** //
    // *********************************************************************** */

    // Author: Isidora Duma
    // updates gui map with new object positions
    public void moveAvatarOnScreen(int[] oldPos) {

        Tile av = grid[player.y][player.x];
        heightText.setText(String.valueOf(av.getCellHeight()));

        int[] avatarDir = ((map) world).getPlayer().getAvatarDir();

        Image playerImage;

        try {

            // change the direction of the avatar on screen
            if(avatarDir[0]==0 && avatarDir[1]==1) {    // right

                playerImage = new Image(getClass().getResource("../resources/avatar_right.png").toString());

            } else if(avatarDir[0]==0 && avatarDir[1]==-1) {    //left

                playerImage = new Image(getClass().getResource("../resources/avatar_left.png").toString());

            } else if(avatarDir[0]==1 && avatarDir[1]==0) {     // down

                playerImage = new Image(getClass().getResource("../resources/avatar_down.png").toString());

            }else if(avatarDir[0]==-1 && avatarDir[1]==0){     // up

                playerImage = new Image(getClass().getResource("../resources/avatar_up.png").toString());
            }else if(avatarDir[0]==-1 && avatarDir[1]==-1){     // up left

                playerImage = new Image(getClass().getResource("../resources/avatar_upleft.png").toString());
            }else if(avatarDir[0]==-1 && avatarDir[1]==1){     // up right

                playerImage = new Image(getClass().getResource("../resources/avatar_upright.png").toString());
            }else if(avatarDir[0]==1 && avatarDir[1]==-1) {     // down left

                playerImage = new Image(getClass().getResource("../resources/avatar_downleft.png").toString());

            }else {     // down right
                playerImage = new Image(getClass().getResource("../resources/avatar_downright.png").toString());
            }

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Tile c = grid[i][j];

                    if(!(i == player.y && j == player.x)){
                        c.border.setFill(c.getTileFill());
                    }
                    else if(i == player.y && j == player.x){
                        av.border.setFill(new ImagePattern(playerImage));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /* *********************************************************************** //
    // ************************** ROBOT FUNCTIONS **************************** //
    // *********************************************************************** */

    // Author: Isidora Duma
    // called from Set Up Robot button
    // Initializes RobotConfigController
    // sets up robot on submit
    @FXML
    public void robotSetup(MouseEvent event) throws IOException {
        System.out.println(width + "," + height);
        // opens RobotConfig.fxml
        // controlled by RobotConfigController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/RobotConfig.fxml"));
        loader.setController(rcController = new RobotConfigController(width, height));
        Parent map = loader.load();
        Stage robotConfig = new Stage();

        robotConfig.setTitle("Robot configuration");
        robotConfig.setScene(new Scene(map, 368, 277));
        robotConfig.show();

        // get values once robotConfig is closed (only comes here when a user hits submit)
        robotConfig.setOnHidden(e -> {
            // position of robot was not set
            if(rcController.getX() == -1 || rcController.getY() == -1 ) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please go back and set up robot correctly.", ButtonType.OK);
                alert.showAndWait();
                this.robotSetup = false;
                return;
            }

            // intiialize cell
            Tile c = grid[rcController.getX()][rcController.getY()];
            // check if robot was placed on an object
            if(c.isBox() || c.isWater() || c.isWall() || c.isTree()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have placed your robot on an object. Please place it in another cell.", ButtonType.OK);
                alert.showAndWait();
                this.robotSetup = false;
                return;
            }

            // set robot image
            // robot default pointing downwards
            Image playerImage = new Image(getClass().getResource("../resources/avatar_down.png").toString());
            c = grid[rcController.getX()][rcController.getY()];
            c.border.setFill(new ImagePattern(playerImage));

            this.player = new avatar(new int[]{rcController.getY(), rcController.getX()}, new int[]{1, 0}, 90);

            startX = rcController.getX();
            startY = rcController.getY();
            name = rcController.getName();

            StringBuilder g = new StringBuilder();

            // goals:
            // true if chosen
            // 1. find box and input id
            if(rcController.isFindBox()) {
                g.append("findBox");
                if(rcController.isSortBoxes()) {
                    g.append(",sortBoxes");
                }
                goal = g.toString();
            }
            // 2. sort boxes by id
            else if(rcController.isSortBoxes()) {
                g.append("sortBoxes");
                goal = g.toString();
            }
            else {
                goal = "";
            }

            // user no longer allowed to edit robot
            robotSetupbtn.setDisable(true);
            loadConfigBtn.setDisable(true);

            // user allowed to start
            startBtn.setDisable(false);
            this.robotSetup = true;

            // user can now save robot info
            saveRobotOpt.setDisable(false);
        });
    }

    // Author: Isidora Duma
    // called from Load Configuration File
    // loads robot info from imported file
    @FXML
    public void robotFileConfig(MouseEvent event) {

        FileChooser fc = new FileChooser();
        fc.setTitle("Load Robot Configuration File");
        File f = fc.showOpenDialog((Stage) windText.getScene().getWindow());

        if (f != null) {
            if (!f.getName().endsWith(".csv")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "File must be csv.", ButtonType.OK);
                alert.showAndWait();
            } else {
                Path pathToFile = Paths.get(f.getPath());

                // reading csv file
                try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
                    br.readLine(); // [width],[height]

                    String line = br.readLine();
                    String[] wh = line.split(",");

                    // grid not the same... may have conflicts for where robots are placed
                    // better not to allow
                    if(this.width != Integer.parseInt(wh[0]) || this.height != Integer.parseInt(wh[1])) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Robot configuration file must have matching grid size to your simulation.", ButtonType.OK);

                        alert.showAndWait();
                    }
                    else {
                        br.readLine();  // [name of robot],[x],[y],[goals]
                        line = br.readLine();
                        String[] attributes = line.split(",");

                        name = attributes[0];

                        System.out.println(attributes[1]);
                        if(Integer.parseInt(attributes[1]) > this.width -1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Robot starting position out of bounds.", ButtonType.OK);

                            alert.showAndWait();
                            return;
                        }
                        else if(Integer.parseInt(attributes[2]) > this.height -1) {
                            System.out.println(attributes[2]);
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Robot starting position out of bounds.", ButtonType.OK);
                            alert.showAndWait();
                            return;
                        }

                        // first goal in attributes[3]
                        if(attributes.length >= 4) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(attributes[3]);

                            // second goal in attributes[4]
                            if(attributes.length >= 5) {
                                sb.append(attributes[4]);
                            }

                            // set goal to one or both values
                            goal = sb.toString();
                        }
                        else {
                            goal = "";
                        }

                        this.player = new avatar(new int[]{Integer.parseInt(attributes[2]), Integer.parseInt(attributes[1])}, new int[]{1, 0}, 90);
                        startX = Integer.parseInt(attributes[1]);
                        startY = Integer.parseInt(attributes[2]);
                        robotSetup = true;
                        robotSetupbtn.setDisable(true);
                        loadConfigBtn.setDisable(true);
                        startBtn.setDisable(false);

                        for (int i = 0; i < width; i++) {
                            for (int j = 0; j < height; j++) {
                                Tile c = grid[i][j];
                                Image playerImage = new Image(getClass().getResource("../resources/avatar_down.png").toString());
                                if(i == Integer.parseInt(attributes[1]) && j == Integer.parseInt(attributes[2])){
                                    c.border.setFill(new ImagePattern(playerImage));
                                    continue;
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "File error. Please try again with a different file", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            }
        }
    }

    // Author: Isidora Duma
    // called from menu bar
    // saves robot info into a new file
    @FXML
    public void saveRobotFile(ActionEvent event) {
        String robotToSave = "";
        if(rcController == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "This robot already has a config file.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        else {
            robotToSave = fs.saveRobot(this.width, this.height, rcController.getName(), rcController.getX(), rcController.getY(), goal);
        }
        fs.saveFile("Save Robot File", robotToSave, windText);
    }

    /* *********************************************************************** //
    // **************************** MAP FUNCTIONS **************************** //
    // *********************************************************************** */

    // Author: Isidora Duma
    // called from "Map Complete" button
    // saves map info into cells and initializes empty cells
    @FXML
    public void mapCompleted(ActionEvent event) {

        weatherOptions.setDisable(true);

        if (windText.getText().isEmpty() || windText.getText() == null || !windText.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            wind = 0;
        } else {
            wind = Double.parseDouble(windText.getText());
        }

        windText.setText(String.valueOf(wind));
        windText.setEditable(false);

        if (tempText.getText().isEmpty() || tempText.getText() == null || !tempText.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            temp = 0;
        } else {
            temp = Double.parseDouble(tempText.getText());
        }

        tempText.setText(String.valueOf(temp));
        tempText.setEditable(false);

        if (directionText.getText().isEmpty() || directionText.getText() == null || directionText.getText().length() > 1) {
            windDir = "N";
        } else {
            windDir = directionText.getText();
        }

        directionText.setText(String.valueOf(windDir));
        directionText.setEditable(false);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile tile = grid[i][j];
                tile.setLoadedFromFile(true);
                tile.setOnMouseClicked(e -> {
                });

                if (tile.getTileFill() == null && tile.isOutside()) {
                    tile.setTileFill(Color.GRAY);
                } else if (tile.getTileFill() == null && !tile.isOutside()) {
                    tile.setTileFill(Color.LIGHTGRAY);
                }
            }
        }

        mapCompleted.setVisible(false);
        robotSetupbtn.setVisible(true);
        or.setVisible(true);
        loadConfigBtn.setVisible(true);

        saveMapOpt.setDisable(false);
    }

    // Author: Isidora Duma
    // called from menu bar
    // saves user built map
    @FXML
    public void saveMapFile(ActionEvent event){

        String mapToSave;
        if(outside) {
            mapToSave = fs.saveMapOutside(width, height, wind, windDir, temp, grid);
        }
        else {
            mapToSave = fs.saveMapInside(width, height, temp, grid);
        }
        fs.saveFile("Save Map File", mapToSave, windText);
    }

    // Author: Isidora DUma
    // loads all file info into simulator
    private void parseFile(){

        mapCompleted.setVisible(false);
        weatherOptions.setDisable(true);

        Path pathToFile = Paths.get(file.getPath());

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            String line = br.readLine();

            // indoor
            if(line.equals("in")) {
                loadInsideMap(br);
            }
            // outdoor
            else if(line.equals("out")) {
                loadOutsideMap(br);
            }
            // not a valid map
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Not a valid map file. Please try again with a different file.", ButtonType.OK);
                alert.showAndWait();

                return;
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File error. Please try again with a different file.", ButtonType.OK);
            alert.showAndWait();
            return;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File error. Please try again with a different file.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    // Author: Isidora Duma
    // parse indoor map
    private void loadInsideMap(BufferedReader br) {
        try {
            this.outside = false;
            br.readLine();  // [width],[height]
            String line = br.readLine();

            String[] wh = line.split(",");

            this.width = Integer.parseInt(wh[0]);
            this.height = Integer.parseInt(wh[1]);

            System.out.println("width: " + width + " height: " + height);

            grid = new Tile[width][height];

            br.readLine();  // [temp]

            line = br.readLine();

            this.temp = Double.parseDouble(line);

            System.out.println(temp);

            br.readLine();  // [x],[y],[height],[object]

            line = br.readLine();

            // read through all lines,
            // separate elements by commas
            while(line != null){
                String[] attributes = line.split(",");
                boolean box = false;
                if(attributes.length < 3) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Malformed file.", ButtonType.OK);
                    alert.showAndWait();

                    return;
                }
                else if(attributes.length == 4) {
                    box = true;
                }

                String cellIndex = attributes[0];
                String cellHeight = attributes[1];
                String object = attributes[2];

                String[] indexes = cellIndex.split(" ");

                int i = Integer.parseInt(indexes[0]);
                int j = Integer.parseInt(indexes[1]);

                System.out.println("loading cell: " + i + "," + j);

                Tile tile = new Tile(i, j, width, height, true, outside);

                tile.setCellHeight(Integer.parseInt(cellHeight));

                if(object.equalsIgnoreCase("wall")){
                    tile.setWall(true);
                    Image playerImage = new Image(getClass().getResource("../resources/metal-box.png").toString());
                    tile.border.setFill(new ImagePattern(playerImage));
                    tile.setTileFill(new ImagePattern(playerImage));
                }
                else if(object.equalsIgnoreCase("box") && box){
                    tile.setBox(true);
                    //tile.setObstacle(false);
                    Image playerImage = new Image(getClass().getResource("../resources/box-zebrawood.png").toString());
                    tile.border.setFill(new ImagePattern(playerImage));
                    tile.setTileFill(new ImagePattern(playerImage));
                    String boxMass = attributes[3];
                    tile.setBoxMass(Double.parseDouble(boxMass));
                }
                else {
                    tile.border.setFill(Color.LIGHTGRAY);
                    tile.setTileFill(Color.LIGHTGRAY);
                }
                grid[i][j] = tile;
                gridMap.getChildren().add(tile);

                line = br.readLine();
            }

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if(grid[i][j] == null) {
                        Tile tile = new Tile(i, j, width, height, true, outside);
                        grid[i][j] = tile;
                        gridMap.getChildren().add(tile);
                    }
                }
            }
            windText.setText("0.0");
            windText.setDisable(true);
            directionText.setText("N");
            directionText.setDisable(true);

            tempText.setText(String.valueOf(temp));
            tempText.setEditable(false);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File error. Please try again with a different file", ButtonType.OK);
            alert.showAndWait();
            return;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File error. Please try again with a different file", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    // Author: Isidora Duma
    // parse outdoor map
    private void loadOutsideMap(BufferedReader br) {
        try {
            this.outside = true;
            br.readLine();  // [width],[height]
            String line = br.readLine();

            String[] wh = line.split(",");

            this.width = Integer.parseInt(wh[0]);
            this.height = Integer.parseInt(wh[1]);

            grid = new Tile[width][height];

            br.readLine();  // [wind speed],[wind direction],[temp]

            line = br.readLine();

            wh = line.split(",");

            this.wind = Double.parseDouble(wh[0]);
            this.windDir = wh[1];
            this.temp = Double.parseDouble(wh[2]);

            br.readLine();  // [x],[y],[height],[terrain],[object],[mass]

            line = br.readLine();
            while(line != null){
                String[] attributes = line.split(",");
                boolean box = false;
                if(attributes.length < 4) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Malformed file.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                else if(attributes.length == 5) {
                    box = true;
                }

                String cellIndex = attributes[0];
                String cellHeight = attributes[1];
                String terrain = attributes[2];
                String object = attributes[3];

                String[] indexes = cellIndex.split(" ");

                int i = Integer.parseInt(indexes[0]);
                int j = Integer.parseInt(indexes[1]);

                System.out.println("loading cell: " + i + "," + j);

                Tile tile = new Tile(i, j, width, height, true, outside);

                tile.setCellHeight(Integer.parseInt(cellHeight));

                if(terrain.equals("water")) {
                    tile.setWater(true);
                    tile.border.setFill(Color.BLUE);
                    tile.setTileFill(Color.BLUE);
                }
                else if(terrain.equals("grass")) {
                    tile.setGrass(true);

                    tile.border.setFill(Color.GREEN);
                    tile.setTileFill(Color.GREEN);
                }
                else if(terrain.equals("sand")) {
                    tile.setSand(true);

                    tile.border.setFill(Color.BURLYWOOD);
                    tile.setTileFill(Color.BURLYWOOD);
                }
                else {
                    tile.setAsphalt(true);

                    tile.border.setFill(Color.GRAY);
                    tile.setTileFill(Color.GRAY);
                }

                if(object.equalsIgnoreCase("wall")){
                    tile.setWall(true);
                    Image playerImage = new Image(getClass().getResource("../resources/metal-box.png").toString());
                    tile.border.setFill(new ImagePattern(playerImage));
                    tile.setTileFill(new ImagePattern(playerImage));
                }
                else if(object.equalsIgnoreCase("box") && box){
                    tile.setBox(true);
                    Image playerImage = new Image(getClass().getResource("../resources/box-zebrawood.png").toString());
                    tile.border.setFill(new ImagePattern(playerImage));
                    tile.setTileFill(new ImagePattern(playerImage));

                    String boxMass = attributes[4];
                    tile.setBoxMass(Double.parseDouble(boxMass));
                }
                grid[i][j] = tile;
                gridMap.getChildren().add(tile);

                line = br.readLine();
            }

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if(grid[i][j] == null) {
                        Tile tile = new Tile(i, j, width, height, true, outside);
                        grid[i][j] = tile;
                        gridMap.getChildren().add(tile);
                    }
                }
            }

            windText.setText(String.valueOf(wind));
            windText.setEditable(false);

            tempText.setText(String.valueOf(temp));
            tempText.setEditable(false);

            directionText.setText(String.valueOf(windDir));
            directionText.setEditable(false);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File error. Please try again with a different file.", ButtonType.OK);
            alert.showAndWait();
            return;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File error. Please try again with a different file.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    // Author: Isidora Duma
    // Updated and values were set by Kaige Chen
    @FXML   // set weather options
    private void weatherSelected(ActionEvent event) {//updates made for weather here
        Weather.WeatherType weatherType = null;
        if(weatherOptions.getValue().equals("Custom")) {
            // do nothing
        }
        else {
            weatherType = Weather.WeatherType.valueOf(weatherOptions.getValue().toUpperCase());
            Weather weather = new Weather(weatherType, this.outside);

            Random rand = new Random();
            int j;
            Wind.WindDirection randDir = null;

            time.displayTime();
            if (time.getTimePeriodOfDay().equals("Day")) {
                lightControl.setBrightness(1.2);
            }

            // sunny or cloudy
            if (weatherOptions.getValue().equalsIgnoreCase("SUNNY") || weatherOptions.getValue().equalsIgnoreCase("CLOUDY")) {

                // sunny
                if(weatherOptions.getValue().equalsIgnoreCase("SUNNY")) {
                    weather.setWindCondition(10, null);
                }
                // cloudy
                else {
                    weather.setWindCondition(12, null);
                }
                weather.setUpWind(this.outside);
                ArrayList<Wind> winds = weather.getWind().getWinds();

                //these are set as strings
                tempText.setText(String.valueOf(weather.getCurrTemperature()));

                if (!winds.isEmpty()) {
                    int i = rand.nextInt(winds.size());
                    windText.setText(String.valueOf(winds.get(i).getWindSpeed()));
                    directionText.setText(winds.get(i).getWd().name());
                } else {
                    windText.setText("0.0");
                    directionText.setText("N");
                }
            }
            // rainy or stormy
            else {

                j = rand.nextInt(8);
                randDir = Wind.convertNumToWindDirection(Wind.dir[j]);

                // rainy
                if (weatherOptions.getValue().equalsIgnoreCase("RAINY")) {
                    weather.setWindCondition(19, randDir);
                }
                // stormy
                else {
                    weather.setWindCondition(25, randDir);
                }
                weather.setUpWind(this.outside);
                ArrayList<Wind> winds = weather.getWind().getWinds();

                //these are set as strings
                tempText.setText(String.valueOf(weather.getCurrTemperature()));

                int i = rand.nextInt(winds.size());
                windText.setText(String.valueOf(winds.get(i).getWindSpeed()));
                directionText.setText(winds.get(i).getWd().name());
            }
        }
    }

    /* *********************************************************************** //
    // *********************** TERMINATION FUNCTIONS ************************* //
    // *********************************************************************** */
    @FXML   // saves output of simulation
    // Author: Isidora Duma
    public void saveOutputFile(ActionEvent event) {
        fs.saveFile("Save Output File", output.toString(), windText);
    }

    @FXML   // closes GUI from menu bar
    public void close(ActionEvent event) {

        Stage stage = (Stage) windText.getScene().getWindow();
        stage.close();
    }
}
