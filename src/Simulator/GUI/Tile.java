package Simulator.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

// Author: Isidora Duma
// represents each cell as GUI object
public class Tile extends StackPane {
    private int x;
    private int y;
    Rectangle border;
    private int cellHeight;
    private double friction;
    public CreateCellInsideController cellInsideController;
    public CreateCellOutsideController cellOutsideController;
    private boolean loadedFromFile;
    Paint tileFill;
    private boolean outside;

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

    public Tile(int x, int y, int simWidth, int simHeight, boolean loadedFromFile, boolean outside){
        this.x = x;
        this.y = y;
        this.loadedFromFile = loadedFromFile;
        this.outside = outside;

        // gives each cell a border
        border = new Rectangle(605 / simWidth - 2, 605 / simHeight - 2);
        border.setStroke(Color.LIGHTGRAY);
        getChildren().add(border);

        setTranslateX(x * (605 / simWidth));
        setTranslateY(y * (605 / simHeight));

    }

    // when cell is clicked, allow for initialization of cell attributes
    public void allowCreate() {
        if(!loadedFromFile) {
            setOnMouseClicked(e -> {
                try {
                    setCellAttributes(this.outside);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    // initializes CreateCellInsideController if inside
    // initializes CreateCellOutsideController if outside
    private void setCellAttributes(boolean outside) throws IOException {
        border.setFill(null);
        Stage cellPrompt = new Stage();
        FXMLLoader loader;
        Parent cellDialog;

        if(outside){
            loader = new FXMLLoader(getClass().getResource("../resources/CreateCellOutside.fxml"));
            loader.setController(cellOutsideController = new CreateCellOutsideController(x,y));
            cellDialog = loader.load();
            cellPrompt.setScene(new Scene(cellDialog, 301, 272));
        }
        else {
            loader = new FXMLLoader(getClass().getResource("../resources/CreateCellInside.fxml"));
            loader.setController(cellInsideController = new CreateCellInsideController(x, y));
            cellDialog = loader.load();
            cellPrompt.setScene(new Scene(cellDialog, 245, 238));
        }

        cellPrompt.setTitle("Create cell");
        cellPrompt.show();

        // when a user hits "Submit" after cell initialization
        cellPrompt.setOnHidden(e -> {

            // indoor
            if(cellInsideController != null) {
                // if box
                if(cellInsideController.isBox()){
                    Image playerImage = new Image(getClass().getResource("../resources/box-zebrawood.png").toString());
                    border.setFill(new ImagePattern(playerImage));
                    setTileFill(new ImagePattern(playerImage));
                }
                // else if wall
                else if(cellInsideController.isWall()){
                    Image playerImage = new Image(getClass().getResource("../resources/metal-box.png").toString());
                    border.setFill(new ImagePattern(playerImage));
                    setTileFill(new ImagePattern(playerImage));
                }
                else {
                    border.setFill(Color.LIGHTGRAY);
                    tileFill = Color.LIGHTGRAY;
                }
            }
            // outdoor
            else if(cellOutsideController != null) {

                // if grass
                if (cellOutsideController.isGrass()) {
                    border.setFill(Color.GREEN);
                    tileFill = Color.GREEN;
                }
                // if water
                else if (cellOutsideController.isWater()) {
                    border.setFill(Color.BLUE);
                    tileFill = Color.BLUE;
                }
                // if sand
                else if (cellOutsideController.isSand()) {
                    border.setFill(Color.BURLYWOOD);
                    tileFill = Color.BURLYWOOD;
                }
                // if concrete
                else {
                    border.setFill(Color.GRAY);
                    tileFill = Color.GRAY;
                }
                // if box
                if (cellOutsideController.isBox()) {
                    Image playerImage = new Image(getClass().getResource("../resources/box-zebrawood.png").toString());
                    border.setFill(new ImagePattern(playerImage));
                    setTileFill(new ImagePattern(playerImage));
                }
                // if wall
                else if (cellOutsideController.isWall()) {
                    Image playerImage = new Image(getClass().getResource("../resources/metal-box.png").toString());
                    border.setFill(new ImagePattern(playerImage));
                    setTileFill(new ImagePattern(playerImage));
                }
                // if tree
                else if (cellOutsideController.isTree()) {
                    Image playerImage = new Image(getClass().getResource("../resources/tree.png").toString());
                    border.setFill(new ImagePattern(playerImage));
                    setTileFill(new ImagePattern(playerImage));
                }
            }
        });
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

    public void setBorder(Rectangle border) {
        this.border = border;
    }

    public int getCellHeight() {
        if(cellInsideController != null){
            cellHeight = cellInsideController.getHeight();
        }
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public double getFriction() {
        if(cellInsideController != null){
            friction = cellInsideController.getFriction();
        }
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public boolean isLoadedFromFile() {
        return loadedFromFile;
    }

    public void setLoadedFromFile(boolean loadedFromFile) {
        this.loadedFromFile = loadedFromFile;
    }

    public Paint getTileFill() {
        return tileFill;
    }

    public void setTileFill(Paint tileFill) {
        this.tileFill = tileFill;
        border.setFill(tileFill);
    }

    public boolean isOutside() {
        return outside;
    }

    public void setOutside(boolean outside) {
        this.outside = outside;
    }

    public boolean isBox() {
        if(cellOutsideController != null) {
            box = cellOutsideController.isBox();
        }
        else if(cellInsideController != null) {
            box = cellInsideController.isBox();
        }
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public boolean isWall() {
        if(cellOutsideController != null) {
            wall = cellOutsideController.isWall();
        }
        else if(cellInsideController != null) {
            wall = cellInsideController.isWall();
        }
        return wall;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public boolean isTree() {
        if(cellOutsideController != null) {
            tree = cellOutsideController.isTree();
        }
        return tree;
    }

    public void setTree(boolean tree) {
        this.tree = tree;
    }

    public double getBoxMass() {
        if(cellOutsideController != null) {
            boxMass = cellOutsideController.getBoxMass();
        }
        else if(cellInsideController != null) {
            boxMass = cellInsideController.getBoxMass();
        }
        return boxMass;
    }

    public void setBoxMass(double boxMass) {
        this.boxMass = boxMass;
    }

    public boolean isWater() {
        if(cellOutsideController != null) {
            water = cellOutsideController.isWater();
        }
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isGrass() {
        if(cellOutsideController != null) {
            grass = cellOutsideController.isGrass();
        }
        return grass;
    }

    public void setGrass(boolean grass) {
        this.grass = grass;
    }

    public boolean isSand() {
        if(cellOutsideController != null) {
            sand = cellOutsideController.isSand();
        }
        return sand;
    }

    public void setSand(boolean sand) {
        this.sand = sand;
    }

    public boolean isAsphalt() {
        if(cellOutsideController != null) {
            asphalt = cellOutsideController.isAsphalt();
        }
        return asphalt;
    }

    public void setAsphalt(boolean asphalt) {
        this.asphalt = asphalt;
    }
}