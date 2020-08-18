package Generators.CellularAutomatons;

import CommonUtils.GenTextField;
import Generators.BasicGenerators.Generator;
import javafx.collections.FXCollections;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import javax.swing.*;

/*
This class offers a generator that employs the rules of Wolframs elementary automaton.
Some of these rules help create interesting geometric shapes.
Also all the GUI elements for this generator's window are found in this class.
 */

public class CellularAutomatonParent extends Generator {
    ChoiceBox pixelChoiceBox;
    Label lblPixel;
    Label lblIteration;
    int numberOfIterations;
    TextField tfIteration;

    int cellSize = -1;
    int[][] cells;


    public CellularAutomatonParent(String name, Canvas canvas) {
        super(name, canvas);

        lblHeight = new Label("Canvas Height (50-5000):");
        lblHeight.setFont(Font.font("Verdana", 12));
        lblWidth = new Label("Canvas Width (50-5000):");
        lblWidth.setFont(Font.font("Verdana", 12));
        tfHeight.setText("500");
        tfWidth.setText("500");
        lblPixel = new Label("Select pixel:");
        lblPixel.setFont(Font.font("Verdana", 12));

        lblIteration = new Label("Enter the number of iterations:  ");
        lblIteration.setFont(Font.font("verdana", 12));
        tfIteration = new GenTextField();
        tfIteration.setText("100");
        tfIteration.setMaxWidth(70);
        tfIteration.setFont(Font.font("Verdana", 12));
        pixelChoiceBox = new ChoiceBox(FXCollections.observableArrayList(1, 2, 3, 4,
                5, 6, 7, 8, 9, 10));
        pixelChoiceBox.setValue(1);
    }

    boolean isNumberOfIterationsValid() {
        try {
            if (isEmpty(tfIteration)) {
                JOptionPane.showMessageDialog(null,
                        "This field cannot be empty. Please enter a positive " +
                                "\ninteger number for the number of iterations.");
                MainWindow.MainWindow.setStatus(name + " ready...");
                return false;
            } else if (Integer.parseInt(tfIteration.getText()) < 1) {
                JOptionPane.showMessageDialog(null,
                        "Invalid input! Please enter a positive integer number for the number of iterations.");
            }
            numberOfIterations = Integer.parseInt(tfIteration.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input! Please enter a positive integer number for the number of iterations.");
            MainWindow.MainWindow.setStatus(name + " ready...");
            return false;
        }
        return true;
    }

    boolean isCellularHeightValid() {
        return isCanvasHeightValid(50, 5000);
    }

    boolean isCellularWidthValid() {
        return isCanvasWidthValid(50, 5000);
    }

    //check if the height input is within a given limit
    protected boolean isCanvasHeightValid(int minHeight, int maxHeight){
        try {
            if (isEmpty(tfHeight)){
                JOptionPane.showMessageDialog(null,
                        "This field cannot be empty. Please enter an integer number " +
                                "\nbetween " +minHeight+" and "+maxHeight +" for width.");
                MainWindow.MainWindow.setStatus(name+" ready...");
                return false;
            }
            if(Integer.parseInt(tfHeight.getText()) > maxHeight || Integer.parseInt(tfHeight.getText()) < minHeight){
                JOptionPane.showMessageDialog(null,
                        "Invalid input! Please enter an integer number between "+minHeight+" and "+ maxHeight+" for height.");
            }
            height = Integer.parseInt(tfHeight.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input! Please enter an integer number between "+minHeight+" and "+ maxHeight+" for height.");
            MainWindow.MainWindow.setStatus(name+" ready...");
            return false;
        }
        return true;

    }
    //check if the width input is within a given limit
    protected boolean isCanvasWidthValid(int minWidth, int maxWidth){
        try {
            if (isEmpty(tfWidth)) {
                JOptionPane.showMessageDialog(null,
                        "This field cannot be empty. Please enter an integer number " +
                                "\nbetween " +minWidth+" and "+maxWidth +" for width.");
                MainWindow.MainWindow.setStatus(name+" ready...");
                return false;
            }
            if(Integer.parseInt(tfWidth.getText()) > maxWidth || Integer.parseInt(tfWidth.getText()) < minWidth){
                JOptionPane.showMessageDialog(null,
                        "Invalid input! Please enter an integer number between " +minWidth+" and "+maxWidth +" for width.");
                MainWindow.MainWindow.setStatus(name+" ready...");
            }
            width = Integer.parseInt(tfWidth.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input! Please enter an integer number between " +minWidth+" and "+maxWidth +" for width.");
            MainWindow.MainWindow.setStatus(name+" ready...");
            return false;
        }
        return true;
    }

}
