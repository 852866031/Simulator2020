package Simulator.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// Author: Isidora Duma
// controls file CreateCellInside.fxml
public class CreateCellInsideController {
    private int x;
    private int y;
    private int friction;
    private int height;
    private boolean box;
    private boolean wall;

    private Double boxMass;

    public CreateCellInsideController(){}

    public CreateCellInsideController(int x, int y){
        this.x = x;
        this.y = y;
    }

    @FXML
    private Label cellIndex;

    @FXML
    private TextField cellHeight;

    @FXML
    private CheckBox wallCheck;

    @FXML
    private CheckBox boxCheck;

    @FXML
    private Button submitBtn;

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
        cellIndex.setText("Cell: (" + x + ", " + y + ")");
        setFriction(0);
        setHeight(0);

        // only show mass and id options if box is selected
        // default is no box
        mass.setVisible(false);
        massLabel.setVisible(false);
        kgLabel.setVisible(false);

        id.setVisible(false);
        idLabel.setVisible(false);
    }

    @FXML
    // when "Submit" is clicked after setting up indoor cell
    public void submit(MouseEvent event){

        int h;
        int f;
        boolean hasOb;
        box = false;
        wall = false;

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

        // check if cell is a wall
        if(wallCheck.isSelected()){
            wall = true;
        }
        else if(boxCheck.isSelected()){
            box = true;
        }

        // check if cell is a box, set it's mass
        // id is not yet updated
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

    // wall checkbox was clicked
    public void wallSelected(ActionEvent event) {

        // wall is chosen, box cannot be
        // remove mass and id options for box
        if(wallCheck.isSelected()){
            boxCheck.setSelected(false);
            mass.setVisible(false);
            massLabel.setVisible(false);
            kgLabel.setVisible(false);
            id.setVisible(false);
            idLabel.setVisible(false);
        }
    }

    // box checkbox was clicked
    public void boxSelected(ActionEvent event) {

        // box is chosen, show mass and id options
        if(boxCheck.isSelected()) {
            wallCheck.setSelected(false);
            mass.setVisible(true);
            mass.setText("2.0");
            massLabel.setVisible(true);
            kgLabel.setVisible(true);
            id.setVisible(true);
            idLabel.setVisible(true);
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

    public Double getBoxMass() {
        return boxMass;
    }

    public void setBoxMass(Double boxMass) {
        this.boxMass = boxMass;
    }
}