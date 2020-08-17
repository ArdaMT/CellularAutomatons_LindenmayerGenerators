/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.CellularAutomatons;

import CellMachines.GameOfLifeShapes;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements Conway's Game of Life.
 * This cellular automaton produces small black and white animations of different figures.
 * The class includes both the algorithm as well as the GUI elements.
 * the animation figures have been described in another class called GameOfLifeShapes in the folder CellMachines.
 *
 * @author Ardamelih
 */
public class GameOfLifeAutomaton extends CellularAutomatonParent {


    private ChoiceBox shapeChoiceBox;
    private int[][] shape;
    private int[][] tempCells;
    private boolean random = false;
    public Timeline timeline;
    private GridPane grid;

    //GUI components
    Label lblShape;

    public GameOfLifeAutomaton( Canvas canvas,String name) {
        super(name, canvas);
        this.setTitle(name);
        lblShape = new Label("Select a shape:");
        lblShape.setFont(Font.font("verdana",12));
        lblExplanation = new Label("This automaton produces short animations. You can" +
                                      "\nchoose the pixel size, the animation shape as well" +
                "\nas the number of iterations");
        lblExplanation.setFont(Font.font("Verdana", 12));
        shapeChoiceBox = new ChoiceBox(FXCollections.observableArrayList("Random", "Glider", "Small exploder", "Exploder", "Fish", "Pump",
                "Shooter", "Cell 10"));
        shapeChoiceBox.setValue("Small exploder");

        setGridLayout();
        Scene scene = new Scene(grid);

        setScene(scene);

        generateButton.setOnAction(e -> {

            try {
                cells=null;
                setAlwaysOnTop(false);

                generate();
                this.alwaysOnTopProperty();

            } catch (InterruptedException ex) {
                Logger.getLogger(GameOfLifeAutomaton.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    private void generate() throws InterruptedException {
        if(timeline!=null){
            timeline.stop();
        }
        MainWindow.MainWindow.setStatus("generating...");
        MainWindow.MainWindow.setSaveMenuStatus(false);
            if(isCellularHeightValid()&&isCellularWidthValid()&&isNumberOfIterationsValid()) {

                //height can not be less then width.
                if (height < width) {
                    height = width;
                }



                    if (((String) shapeChoiceBox.getSelectionModel().getSelectedItem()).equals("Random")) {
                        random = true;
                    } else {
                        shape = GameOfLifeShapes.getShape((String) shapeChoiceBox.getSelectionModel().getSelectedItem());
                    }

                cellSize = (int) pixelChoiceBox.getSelectionModel().getSelectedItem();

                //canvas is built based on the textField input values which are rounded down, if they are not a multiple of the cellsize
                height = height - (height % cellSize);
                width = width - (width % cellSize);
                canvas.setHeight(height);
                canvas.setWidth(width);
                //2 extra rows and columns are added to create an invisible frame that wraps cells
                cells = new int[width / cellSize + 2][height / cellSize + 2];
                tempCells=new int[width / cellSize + 2][height / cellSize + 2];
                if (random) {
                    for (int i = 0; i < cells.length; i++) {
                        for (int j = 0; j < cells[i].length; j++) {
                            cells[i][j] = (Math.random() < 0.5) ? 0 : 1;
                        }
                    }
                }
                else {

                    initialiseShape();
                }
                drawGameOfLife();
                timeline = new Timeline(new KeyFrame(
                        Duration.millis(200),
                        ae -> iterate()));
                timeline.setCycleCount(numberOfIterations);
                timeline.play();
                MainWindow.MainWindow.setStatus("Game of Life Generator ready...");
            }else {
                this.setAlwaysOnTop(true);
            }
    }

    private boolean isInputValid() {
        return true;
    }

    //compute number of neighbours for the current cell.
    private int checkNeighbours(int a, int b) {
        int numberOfNeighbours = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                numberOfNeighbours += cells[a + i][b + j];
            }
        }
        //subtract the value of the current Cell from numberOfNeighbours
        numberOfNeighbours -= cells[a][b];
        return numberOfNeighbours;
    }

    //apply the rules of Game of Life to every cell on tempCell
    private void applyRules(int a, int b, int numberOfNeighbours) {
        if (cells[a][b] == 0 && numberOfNeighbours == 3) {
            tempCells[a][b] = 1;
        } else if (cells[a][b] == 1) {
            if (numberOfNeighbours <= 1 || numberOfNeighbours >= 4) {
                tempCells[a][b] = 0;
            }
        }
    }

    private void drawGameOfLife() {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(1, 1, canvas.getWidth() - 1, canvas.getHeight() - 1);
        gc.setFill(Color.BLACK);
        //while drawing on the canvas we ignore the wrapping rows and columns
        for (int i = 1; i < cells.length - 1; i++) {
            for (int j = 1; j < cells[i].length - 1; j++) {
                if (cells[i][j] == 1) {
                    gc.fillRect((i) * cellSize, (j) * cellSize, cellSize, cellSize);
                }
            }
        }
    }
    //iterate through the cells of temporary array tempcells.
    private void iterate() {
        //while applying the rules we ignore the wrapping rows and columns
        //first we copy the elements of the original cells array to the tempCells array.
        for (int i = 1; i < cells.length - 1; i++) {
            for (int j = 1; j < cells[i].length - 1; j++) {
                tempCells[i][j] = cells[i][j];
            }
        }
        //we apply the rules to the original cells array however make the changes on tempCell.
        for (int i = 1; i < tempCells.length - 1; i++) {
            for (int j = 1; j < tempCells[i].length - 1; j++) {
                applyRules(i, j, checkNeighbours(i, j));

            }
        }
        //once we are done with iterating through tempCell we copy the new changed cells back to the original array again.
        for (int i = 1; i < cells.length - 1; i++) {
            for (int j = 1; j < cells[i].length - 1; j++) {
                cells[i][j] = tempCells[i][j];
            }
        }
                drawGameOfLife();
    }
    //Live cells of the chosen shape are planted on cells matrix
    private void initialiseShape() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][i] = 0;
            }
        }
        //take into account the invisible frame while placing the live cells
        for (int i = 0; i < shape.length; i++) {
            cells[shape[i][0] + 1][shape[i][1] + 1] = 1;
        }
    }

    private void setGridLayout(){
        //Layout for Generator window
        grid = new GridPane();
        GridPane.setConstraints(lblExplanation, 1, 0, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblHeight, 1, 1, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfHeight, 1, 1, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblIteration, 1, 3, 2, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblWidth, 1, 2, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfWidth, 1, 2, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblShape, 1, 4, 2, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(shapeChoiceBox, 1, 4, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblPixel, 1, 5, 2, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(pixelChoiceBox, 1, 5, 1, 1, HPos.RIGHT , VPos.BASELINE);
        GridPane.setConstraints(tfIteration, 1, 3, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(generateButton, 1, 7, 1, 1, HPos.RIGHT, VPos.BASELINE);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getChildren().addAll(lblExplanation,lblIteration, lblHeight, tfHeight, lblWidth, tfWidth, lblShape, shapeChoiceBox, lblPixel, pixelChoiceBox, tfIteration, generateButton);

    }
}
