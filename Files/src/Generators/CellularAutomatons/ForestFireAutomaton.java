/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.CellularAutomatons;

import CommonUtils.GenTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  This class implements the forest fire automaton. The approach of this automaton is
 *  related to the modeling of actual forest fires. Hence the name
 *  This class also includes GUI elements as well as the implementation of the logic.
 * @author Ardamelih
 */
public class ForestFireAutomaton extends CellularAutomatonParent {

    private TextField tfNumberOfFires;
    private final int TREE = 1;
    private final int FIRE = 2;
    private final int EMPTY = 3;
    public Timeline timeline;
    private int[][][] cells;
    private int numberOfFires;
    private Label lblFire;
    private GridPane grid;
    public ForestFireAutomaton(Canvas canvas,String name) {
        super(name, canvas);
        this.setTitle(name);
        //GUI components
         lblFire = new Label("Enter the number of fires:");
         lblFire.setFont(Font.font("verdana",12));
         lblExplanation=new Label("This Automaton shows a model inspired by the spread" +
                 "\nof forest fires. You can adjust the number of iterations " +
                 "\nand the initial number of fires on the canvas.");
        lblExplanation.setFont(Font.font("Verdana", 12));
        tfNumberOfFires = new GenTextField();
        tfNumberOfFires.setText("100");
        tfNumberOfFires.setMaxWidth(70);
        setGridLayout();
       Scene scene = new Scene(grid);

        setScene(scene);

        generateButton.setOnAction(e -> {
            try {
                setAlwaysOnTop(false);

                generate();
                this.alwaysOnTopProperty();
            } catch (InterruptedException ex) {
                Logger.getLogger(ForestFireAutomaton.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    private void generate() throws InterruptedException {
        if(timeline!=null){
            timeline.stop();
        }
        MainWindow.MainWindow.setStatus("generating...");
        MainWindow.MainWindow.setSaveMenuStatus(false);
        if(isCellularHeightValid()&&isCellularWidthValid()
                &&isNumberOfIterationsValid()&&isNumberOfFiresValid()) {
            cellSize = (int) pixelChoiceBox.getSelectionModel().getSelectedItem();

            //canvas is built based on the textField input values which are rounded down, if they are not a multiple of the cellsize
            height = height - (height % cellSize);
            width = width - (width % cellSize);

            canvas.setHeight(height);
            canvas.setWidth(width);
            //2 extra rows and columns are added to create an invisible frame that wraps cells
            cells = new int[width / cellSize + 2][height / cellSize + 2][1];
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    cells[i][j][0] = TREE;
                }
            }
            setInitialFire();
            drawFirstGeneration();
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(5300),
                    ae -> drawNextGeneration()));
            timeline.setCycleCount(numberOfIterations);
            timeline.play();

            MainWindow.MainWindow.setStatus("Forest Fire Generator ready...");
        }
        else{
            setAlwaysOnTop(true);
        }
    }
  //start fire on randomly chosen trees 
    private void setInitialFire() {
        boolean occupied = true;
        int highh = cells.length - 2;
        int highw = cells[0].length - 2;
        int a;
        int b;
        while (numberOfFires > 0) {
            a = new Random().nextInt((highh - 1) + 1) + 1;
            b = new Random().nextInt((highw - 1) + 1) + 1;
            if (cells[a][b][0] == FIRE) {
                while (occupied) {
                    a = new Random().nextInt((highh - 1) + 1) + 1;
                    b = new Random().nextInt((highw - 1) + 1) + 1;
                    if (cells[a][b][0] != FIRE) {
                        occupied = false;
                    }
                }
            }
            cells[a][b][0] = FIRE;
            occupied = true;
            --numberOfFires;
        }
    }
    
    private void drawFirstGeneration() {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        //while drawing on the canvas we ignore the wrapping rows and columns
        gc.clearRect(1, 1, canvas.getWidth() - 1, canvas.getHeight() - 1);
        for (int i = 1; i < cells.length - 1; i++) {
            for (int j = 1; j < cells[i].length - 1; j++) {
                if (cells[i][j][0] == TREE) {
                    gc.setFill(Color.GREEN);
                }
                if (cells[i][j][0] == FIRE) {
                    gc.setFill(Color.RED);
                }
                gc.fillRect((i - 1) * cellSize, (j - 1) * cellSize, cellSize, cellSize);
            }
        }
    }
    //apply the rules of forest fire to cells and draw the image
    private void drawNextGeneration() {
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 1; i < cells.length - 1; i++) {
            for (int j = 1; j < cells[i].length - 1; j++) {
                if (cells[i][j][0] == FIRE) {
                    System.out.println(i+"  "+j);
                    cells[i][j][0] = EMPTY;
                    gc.fillRect((i - 1) * cellSize, (j - 1) * cellSize, cellSize, cellSize);
                    gc.fillRect(i - 1 * cellSize, j - 1 * cellSize, cellSize, cellSize);

                } else if (cells[i][j][0] == TREE) {
                    if (hasBurningNeighbours(i, j) == true) {
                        cells[i][j][0] = FIRE;
                        gc.setFill(Color.RED);

                    } else if (new Random().nextFloat() <= 0.25) {
                        cells[i][j][0] = FIRE;
                        gc.setFill(Color.RED);
                    }
                } else {
                    if (cells[i][j][0] == EMPTY) {
                        if (new Random().nextFloat() <= 0.60) {
                            cells[i][j][0] = TREE;
                            gc.setFill(Color.GREEN);
                        }
                    }
                    gc.fillRect((i - 1) * cellSize, (j - 1) * cellSize, cellSize, cellSize); }
                gc.fillRect(i - 1 * cellSize, j - 1 * cellSize, cellSize, cellSize);
            }
        }
    }

    //check if the cell has any burning neighbours.
    private boolean hasBurningNeighbours(int a, int b) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (cells[a + i][b + j][0] == FIRE) {
                    return true;
                }
            }
        }
        return false;
    }
private boolean isNumberOfFiresValid() {
    if (tfNumberOfFires.getText().isEmpty() ) {
        JOptionPane.showMessageDialog(null,
                "The number of fires cannot be empty. Please enter a positive number for this field!");
        MainWindow.MainWindow.setStatus("Forest Fire Generator ready...");
        return false;
    }
    else {
        try {
            if(Integer.parseInt(tfIteration.getText())<1){
                JOptionPane.showMessageDialog(null, "Wrong input! Please enter a positive number of iterations!");
                MainWindow.MainWindow.setStatus("Forest Fire Generator ready...");
                return false;
            }
            else{
                numberOfIterations = Integer.parseInt(tfIteration.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Wrong input! Please enter a positive number of iterations!");
            MainWindow.MainWindow.setStatus("Forest Fire Generator ready...");
            return false;
        }
    }
        return true;
}
    private void setGridLayout() {
        //Layout for Generator window
         grid = new GridPane();

        GridPane.setConstraints(lblExplanation, 1, 0, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblHeight, 1, 1, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfHeight, 1, 1, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblIteration, 1, 3, 2, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblWidth, 1, 2, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfWidth, 1, 2, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblFire, 1, 4, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfNumberOfFires, 1, 4, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblPixel, 1, 5, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(pixelChoiceBox, 1, 5, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(tfIteration, 1, 3, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(generateButton, 1, 6, 1, 1, HPos.RIGHT, VPos.BASELINE);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getChildren().addAll(lblExplanation,lblIteration, lblHeight, tfHeight, lblWidth, tfWidth, lblFire, tfNumberOfFires, lblPixel, pixelChoiceBox, tfIteration, generateButton);

    }

}
