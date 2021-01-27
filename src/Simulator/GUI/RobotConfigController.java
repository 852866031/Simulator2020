package Simulator.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// Author: Isidora Duma
// controls RobotConfig.fxml from resources
public class RobotConfigController {
    private int width;
    private int height;
    private String name;
    private int x = -1;
    private int y = -1;
    private boolean findBox;
    private boolean sortBoxes;

    public RobotConfigController(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @FXML
    private Label gridLabel;

    @FXML
    private TextField xPos;

    @FXML
    private TextField yPos;

    @FXML
    private TextField robotName;

    @FXML
    private CheckBox findBoxGoal;

    @FXML
    private CheckBox sortBoxesGoal;

    @FXML
    // show width and height of grid
    public void initialize() {
        gridLabel.setText(width + " x " + height);
    }

    @FXML
    // when "Submit" is clicked on robot config window
    public void submit(MouseEvent event) {

        // position not a valid int
        if(xPos.getText().isEmpty() || !xPos.getText().matches("^\\d+$")
                || yPos.getText().isEmpty() || !yPos.getText().matches("^\\d+$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Position must be a positive integer.", ButtonType.OK);
            alert.showAndWait();
        }
        // out of bounds
        else if(Integer.parseInt(xPos.getText()) > (width - 1)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Position must be within bounds.", ButtonType.OK);
            alert.showAndWait();
        }
        // out of bounds
        else if(Integer.parseInt(yPos.getText()) > (height - 1)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Position must be within bounds.", ButtonType.OK);
            alert.showAndWait();
        }
        // all good
        else {
            // goals
            if(findBoxGoal.isSelected()) {
                findBox = true;
            }

            if(sortBoxesGoal.isSelected()) {
                sortBoxes = true;
            }
            x = Integer.parseInt(xPos.getText());
            y = Integer.parseInt(yPos.getText());

            name = robotName.getText();

            Stage stage = (Stage) gridLabel.getScene().getWindow();
            stage.close();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isFindBox() {
        return findBox;
    }

    public void setFindBox(boolean findBox) {
        this.findBox = findBox;
    }

    public boolean isSortBoxes() {
        return sortBoxes;
    }

    public void setSortBoxes(boolean sortBoxes) {
        this.sortBoxes = sortBoxes;
    }
}
