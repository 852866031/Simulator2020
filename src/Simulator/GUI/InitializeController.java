package Simulator.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

// Author: Isidora Duma
// controls Initialize.fxml
public class InitializeController {

    private File file;

    @FXML
    private TextField simWidth;

    @FXML
    private TextField simHeight;

    @FXML
    private Button fileBtn;

    @FXML
    private CheckBox inside;

    @FXML
    private CheckBox outside;

    @FXML
    public void initialize(){

        inside.setSelected(true);
        simWidth.setText("10");
        simHeight.setText("10");
    }

    // choose a file to load using FileChooser
    public void loadFile(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.setTitle("Open Old Map File");
        File f = fc.showOpenDialog((Stage) simHeight.getScene().getWindow());

        if (f != null) {
            if (!f.getName().endsWith(".csv")) {
                System.out.println("file must be csv");
            } else {
                fileBtn.setText(f.getName());
            file = f;
            }
        }
    }

    // when a user hits "Submit" in the initialization window
    public void enterSubmit(ActionEvent event) throws IOException {

        // file chosen
        if(file != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/Sim.fxml"));
            loader.setController(new SimController(file));
            Parent map = loader.load();
            Stage simStage = new Stage();

            simStage.setTitle("Simulator");
            simStage.setScene(new Scene(map, 866, 629));
            simStage.show();
        }
        // check grid size valid
        else if(simWidth.getText() != null && !simWidth.getText().isEmpty() && simHeight.getText() != null && !simHeight.getText().isEmpty()){
            if(simWidth.getText().matches("\\d*") && simWidth.getText().matches("\\d*")){
                if(Integer.parseInt(simWidth.getText()) >= 10 && Integer.parseInt(simHeight.getText()) >= 10){

                    // loads Simulator
                    // see: FXML file Sim.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/Sim.fxml"));
                    loader.setController(new SimController(Integer.parseInt(simWidth.getText()), Integer.parseInt(simHeight.getText()), outside.isSelected()));
                    Parent map = loader.load();
                    Stage simStage = new Stage();

                    simStage.setTitle("Simulator");
                    simStage.setScene(new Scene(map, 869, 629));
                    simStage.show();

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Grid must be at least 10 x 10 cells.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Grid must be n x m cells where both n and m are positive integers greater than or equal to 10.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Grid must be n x m cells where both n and m are positive integers greater than or equal to 10.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(file != null) {
            // file chosen info message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Before beginning, please set up your robot (load configuration file or set up robot). When you are ready to begin, click start.", ButtonType.OK);
            alert.showAndWait();
        }
        else if(outside.isSelected()) {
            // outdoor info message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Begin customizing your map by clicking on cells and setting wind and temperature in the top right corner." +
                    " When you are happy with your map, click the Map Complete button. You may then set up your robot (Load Configuration File or Set Up Robot)." +
                    " Once you are ready, click start.", ButtonType.OK);
            alert.showAndWait();
        }
        else {
            // indoor info message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Begin customizing your map by clicking on cells and setting temperature in the top right corner (wind and weather only available for outdoor simulations)." +
                    " When you are happy with your map, click the Map Complete button. You may then set up your robot (Load Configuration File or Set Up Robot)." +
                    " Once you are ready to begin, click start.", ButtonType.OK);
            alert.showAndWait();
        }

        Stage stage = (Stage) fileBtn.getScene().getWindow();
        stage.close();
    }


    // if indoor box is selected, don't let outdoor be chosen
    public void insideSelected(ActionEvent event) {
        if(inside.isSelected()){
            outside.setSelected(false);
        }

    }

    // if outdoor box is selected, don't let indoor be chosen
    public void outsideSelected(ActionEvent event) {
        if(outside.isSelected()) {
            inside.setSelected(false);
        }
    }
}
