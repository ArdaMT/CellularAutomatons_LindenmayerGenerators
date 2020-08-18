/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.CellularAutomatons;

import CommonUtils.GenTextField;
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

import javax.swing.*;

/**
 *
 * This class offers an example of simple one dimensional cellular automata using Wolframs elementary cellular automaton rules.
 * The class includes GUI as well as the algorithm implementation.
 * @author Ardamelih
 */
public class ElementaryCellularAutomaton extends CellularAutomatonParent {


    private TextField tfRuleIndex;
    private int currentRuleIndex = -1;
    private String[] rules = new String[256];
    private int[][] ruleSet;
    //cells are placed within given height and width values

    //constructor
    public ElementaryCellularAutomaton(Canvas canvas, String name) {
        super(name, canvas);
        this.setTitle(name);
        buildRules();
        buildRuleSet();

        //GUI components
        Label ruleLabel = new Label("Select a rule between 0 and 255:   ");
        ruleLabel.setFont(Font.font("verdana", 12));
        tfRuleIndex = new GenTextField();
        tfRuleIndex.setText("150");
        tfRuleIndex.setMaxWidth(70);
        lblExplanation = new Label("This automaton generates geometric shapes. By choosing a" +
                "\nrule you can generate different shapes. Only some of the rules" +
                "\nproduce interesting shapes. Feel free test and find them.");
        lblExplanation.setFont(Font.font("Verdana", 12));
        //Layout for Generator window
        GridPane grid = new GridPane();
        GridPane.setConstraints(lblExplanation, 1, 0, 1, 1, HPos.LEFT, VPos.BASELINE);

        GridPane.setConstraints(lblHeight, 1, 1, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfHeight, 1, 1, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(ruleLabel, 1, 3, 2, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblWidth, 1, 2, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfWidth, 1, 2, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblPixel, 1, 4, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(pixelChoiceBox, 1, 4, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(tfRuleIndex, 1, 3, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(generateButton, 1, 5, 2, 1, HPos.RIGHT, VPos.BASELINE);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getChildren().addAll(lblExplanation,ruleLabel, lblHeight, tfHeight, lblWidth, tfWidth, lblPixel, pixelChoiceBox, tfRuleIndex, generateButton);
        Scene scene = new Scene(grid);

        setScene(scene);
        setCanvasBackground(canvas, Color.WHITE);

        generateButton.setOnAction(e -> {
            setAlwaysOnTop(false);
            generate();
            setAlwaysOnTop(true);
        });

    }

    private void generate() {
        MainWindow.MainWindow.setStatus("generating...");
        MainWindow.MainWindow.setSaveMenuStatus(false);
        if (isCellularHeightValid() && isCellularWidthValid() && isRuleIndexValid()) {
            cellSize = (int) pixelChoiceBox.getSelectionModel().getSelectedItem();
            //canvas is built based on the textField input values which are rounded down, if they are not a multiple of the cellsize
            height = height - (height % cellSize);
            width = width - (width % cellSize);
            canvas.setHeight(height);
            canvas.setWidth(width);
            cells = new int[width / cellSize][height / cellSize];
            //assign binary values to the first line. the midcell is 1, the rest is 0.
            for (int i = 0; i < cells[0].length; i++) {
                cells[0][i] = 0;
            }
            int mid = cells[0].length;
            if (mid % 2 == 0) {
                cells[0][cells[0].length / 2] = 1;
            } else {
                cells[0][1 + cells[0].length / 2] = 1;

            }
            //vertical edges of  cells[][] are going to be ignored. They are assigned a constant value of zero
            for (int i = 0; i < cells.length; i++) {
                cells[i][0] = 0;
                cells[i][cells[i].length - 1] = 0;
            }
            paintFirstLine();
            for (int i = 0; i < cells.length - 1; i++) {
                paintNextGeneration(i);
            }
            MainWindow.MainWindow.setStatus("Cellular Automaton Generator ready...");
        } else {

            setAlwaysOnTop(true);
            return;
        }

    }

    //convert all the numbers through 0-255 from decimal to binaryString and save in rules
    private void buildRules() {
        for (int i = 0; i < rules.length; i++) {
            String num = Integer.toBinaryString(i);
            while (num.length() < 8) {
                num = 0 + "" + num;
            }
            rules[i] = num;
        }
    }

    private void buildRuleSet() {
        ruleSet = new int[][]{{1, 1, 1}, {1, 1, 0}, {1, 0, 1}, {1, 0, 0},
                {0, 1, 1}, {0, 1, 0}, {0, 0, 1}, {0, 0, 0}};

    }

    private void paintFirstLine() {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(cells[0].length * cellSize / 2, 0, cellSize, cellSize);
    }

    private void paintNextGeneration(int a) {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        String currentRule = rules[currentRuleIndex];
        for (int i = 1; i < cells[a].length - 1; i++) {
            int middleCell = cells[a][i];
            int leftCell = cells[a][i - 1];
            int rightCell = cells[a][i + 1];
            for (int j = 0; j < ruleSet.length; j++) {
                if (leftCell == ruleSet[j][0] && middleCell == ruleSet[j][1] && rightCell == ruleSet[j][2]) {
                    if (currentRule.charAt(j) == '1') {
                        cells[a + 1][i] = 1;
                        gc.fillRect(i * cellSize, (a + 1) * cellSize, cellSize, cellSize);
                        break;
                    }
                }
            }
        }
    }

    private boolean isRuleIndexValid() {
        try {
            if (isEmpty(tfRuleIndex)) {
                JOptionPane.showMessageDialog(null,
                        "This field cannot be empty. Please choose a rule by entering" +
                                "\nan integer number between 0 and 255.");
                enableButton(generateButton);
                return false;
            }
            else if (Integer.parseInt(tfRuleIndex.getText()) < 0 || Integer.parseInt(tfRuleIndex.getText()) > 255){
                JOptionPane.showMessageDialog(null,
                        "Invalid input! Please choose a rule by entering an integer number between 0 and 255.");
                enableButton(generateButton);
                return false;
            }
            currentRuleIndex = Integer.parseInt(tfRuleIndex.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input! Please choose a rule by entering an integer number between 0 and 255.");
        }
        return true;
    }
}
