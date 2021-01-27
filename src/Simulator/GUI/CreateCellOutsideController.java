package Simulator.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// Author: Isidora Duma
// controls file CreateCellOutside.fxml
public class CreateCellOutsideController {
    private int x;
    private int y;
    private int friction;
    private int height;

    // obstacles
    private boolean box;
    private boolean wall;
    private boolean tree;

    private double boxMass;

    // terrain
    private boolean water;
    private boolean grass;
    private boolean sand;
    private boolean asphalt;

    public CreateCellOutsideController(int x, int y){
        this.x = x;
        this.y = y;
    }

    @FXML
    private Label cellIndex;

    @FXML
    private TextField cellHeight;

    @FXML
    private Button submitBtn;

    @FXML
    private ChoiceBox<String> terrainChoices;

    @FXML
    private ChoiceBox<String> obstacleChoices;

    @FXML
    private TextField mass;

    @FXML
    private Label massLabel;

    @FXML
    private Label kgLabel;

    @FXML
    private TextField id;

    @FXML
    private Label idLabel;

    @FXML
    public void initialize(){

        // show cell chosen
        cellIndex.setText("Cell: (" + x + ", " + y + ")");
        setFriction(0);
        setHeight(0);

        System.out.println("creating items");

        // set terrain and obstacle options
        ObservableList<String> terrains = FXCollections.observableArrayList("Grass","Sand","Asphalt","Water");
        ObservableList<String> obstacles = FXCollections.observableArrayList("None","Wall","Box","Tree");

        System.out.println(terrains);
        System.out.println(obstacles);

        terrainChoices.setItems(terrains);
        obstacleChoices.setItems(obstacles);

        terrainChoices.setValue("Asphalt");
        obstacleChoices.setValue("None");

        // remove mass and id input visibility
        // put it back when a box is chosen (see function obstacleSelected(...))
        mass.setVisible(false);
        massLabel.setVisible(false);
        kgLabel.setVisible(false);

        id.setVisible(false);
        idLabel.setVisible(false);
    }

    @FXML
    // "Submit" button was clicked after setting up outdoor cell
    public void submit(MouseEvent event){

        box = false;
        wall = false;
        tree = false;
        grass = false;
        water = false;
        sand = false;
        asphalt = false;

        int h;
        // set height attributes
        // 0 if empty
        // do not continue if not numerical
        if(cellHeight.getText().isEmpty() || cellHeight.getText() == null){
            h = 0;
        }
        else if(!cellHeight.getText().matches("^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$")){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Height of cell entered is not a positive numeric value. Please set an appropriate height.", ButtonType.OK);

            alert.showAndWait();
            return;
        }
        else {
            h = Integer.parseInt(cellHeight.getText());
        }

        // set terrain chosen
        if(terrainChoices.getValue().equals("Grass")){
            grass = true;
        }
        else if(terrainChoices.getValue().equals("Sand")){
            sand = true;
        }
        else if(terrainChoices.getValue().equals("Asphalt")){
            asphalt = true;
        }
        else if(terrainChoices.getValue().equals("Water")){
            water = true;
        }

        // set obstacle chosen
        if(obstacleChoices.getValue().equals("None")){
            // do nothing
        }
        else if(obstacleChoices.getValue().equals("Wall")){
            wall = true;
        }
        else if(obstacleChoices.getValue().equals("Box")){
            box = true;
        }
        else if(obstacleChoices.getValue().equals("Tree")){
            tree = true;
        }

        if(box){
            if(!mass.getText().isEmpty() && mass.getText().matches("^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$")){
                boxMass = Double.parseDouble(mass.getText());
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Box mass entered is not a positive numeric value. Please set an appropriate mass.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        }

        setHeight(h);

        Stage stage = (Stage) submitBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    // obstacle was chosen from dropdown menu
    public void obstacleSelected(ActionEvent event){
        // if box chosen, show id and mass options
        if(obstacleChoices.getValue().equals("Box")){
            mass.setVisible(true);
            mass.setText("2.0");
            massLabel.setVisible(true);
            kgLabel.setVisible(true);
            id.setVisible(true);
            idLabel.setVisible(true);
        }
        // otherwise remove id and mass options
        else {
            mass.setVisible(false);
            massLabel.setVisible(false);
            kgLabel.setVisible(false);
            id.setVisible(false);
            idLabel.setVisible(false);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFriction() {
        return friction;
    }

    public void setFriction(int friction) {
        this.friction = friction;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public boolean isWall() {
        return wall;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public boolean isTree() {
        return tree;
    }

    public void setTree(boolean tree) {
        this.tree = tree;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isGrass() {
        return grass;
    }

    public void setGrass(boolean grass) {
        this.grass = grass;
    }

    public boolean isSand() {
        return sand;
    }

    public void setSand(boolean sand) {
        this.sand = sand;
    }

    public boolean isAsphalt() {
        return asphalt;
    }

    public void setAsphalt(boolean asphalt) {
        this.asphalt = asphalt;
    }

    public double getBoxMass() {
        return boxMass;
    }

    public void setBoxMass(double boxMass) {
        this.boxMass = boxMass;
    }
}