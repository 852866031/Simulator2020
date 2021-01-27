package Simulator.GUI;

import Simulator.LightControl;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

// Author: Isidora Duma
public class entry extends Application {

    @FXML
    private static Pane gridMap;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent init = FXMLLoader.load(getClass().getResource("../resources/Initialize.fxml"));
        primaryStage.setTitle("Simulator");
        primaryStage.setScene(new Scene(init, 271, 290));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}