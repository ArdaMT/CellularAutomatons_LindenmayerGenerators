/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.Lindenmayer;

import LSystems.Lindenmayer;
import MainWindow.MainStatus;
import MainWindow.MainWindow;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.swing.*;

/**
 * This class implements the Lindenmayer generator. Mostly the GUI-Elements and the exceptions are being taken care of
 * as well as important variables are being initialised using the input in the current class .
 * The Lindenmayer generator helps create aesthetically pleasing images that look like plants.
 * The Lindenmayer approach is also used in the taxonomy of plants as a realistic depiction of actual plants.
 *
 * @author Ardamelih
 */
public class LindenmayerGenerator extends LGeneratorsParent {
    private ChoiceBox recursionChoiceBox;
    Lindenmayer cellMachine;

    public LindenmayerGenerator(Canvas canvas, String name) {
        super(name, canvas);
        this.setTitle(name);
        setStopParameters();
        cellMachine = new Lindenmayer();
        recursionChoiceBox = new ChoiceBox(FXCollections.observableArrayList(1, 2, 3, 4,
                5, 6, 7, 8, 9));
        recursionChoiceBox.setValue(7);
        //Initialise the GUI elements
        initTextFields();
        initButtons();
        setGridLayout();
        Scene scene = new Scene(grid);
        setScene(scene);
    }

    private void initTextFields() {
        taProductionRules.setText("F=FF,\nX=F-[[X]+X]+F[+FX]-X");
        tfAlphabet.setText("F,X,-,+,[,]");
        tfAxiom.setText("X");
        cellMachine.recursionDepth = (int) recursionChoiceBox.getValue();
        tfLength.setText("1");
    }

    private void initButtons() {
        generateButton.setOnAction(e -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            canvas.setHeight(0);
            canvas.setWidth(0);
            e.consume();
            disableButton(generateButton);
            this.setAlwaysOnTop(false);
            generate();
            if(!isAccepted){
                MainWindow.setStatus(name + " Status : Ready.");
                enableButton(generateButton);
                this.alwaysOnTopProperty();
            }
        });

        stopButton.setOnAction(e -> {
            setStopParameters();
            maxGenerations = 0;
            enableButton(generateButton);
        });

        alphabetButton.setOnAction(e -> {
            e.consume();
            disableButton(generateButton);
            this.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(null, "Please use only capital letters from the english alphabet " +
                    "\n and the following characters to define your alphabet: +,-,[,] " +
                    "\nYou have to include at least one of the letters  X and Y in" +
                    "\nyour alphabet. Each element of your alphabet should be a single " +
                    "\ncharacter or a single letter. Use commas between the elements.");
            this.setAlwaysOnTop(true);
            enableButton(generateButton);
        });

        axiomButton.setOnAction(e -> {
            e.consume();
            disableButton(generateButton);
            this.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(null, "Please use only the capital letters X and/or Y for your input. " +
                    "\n Don't use commas. The following input examples are valid:" +
                    "  \nX, XX, XYX. Duplicates are allowed.");
            this.setAlwaysOnTop(true);
            enableButton(generateButton);
        });

        rulesButton.setOnAction(e -> {
            e.consume();
            disableButton(generateButton);
            this.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(null, "Please use only the elements of your alphabet and your axiom for the" +
                    "\nrules. Best strategy to build your rules is to place an element or a" +
                    "\ncombination of elements on the left and right sides of an equation. You" +
                    "\nshould have at least two rules. If you use square brackets in your rules," +
                    "\nalways make sure to use them as pairs and never as a single. That is" +
                    "\nOK: F(0.30=F[X], F(0.70)=F[], that is not OK: F(0.30)=F[X[, F(0.70)=F[. " +
                    "\nThis means for every opening square bracket you use, you need to use" +
                    "\na closing bracket. Also, a closing bracket can not precede its paired" +
                    "\nopening bracket.That is OK: F(0.30=F[[X][]], F(0.70)=F[]. That is not OK:" +
                    "\nF(0.30=F]X[, F(0.70)=F][. Each rule starts on a new line and ends with " +
                    "\na comma.");
              this.setAlwaysOnTop(true);
            enableButton(generateButton);
        });
    }

    //if the input parameters are valid, use these to create the image with the cellmachine object.
    private void generate() {
        setStopParameters();
        MainWindow.setStatus(name + " "
                + MainStatus.GENERATE.toString());

        MainWindow.setSaveMenuStatus(false);
        isAccepted=isInputValid();
        if (isAccepted) {
            cellMachine.initiateString(cellMachine.axiom);
            cellMachine.setProductionRules(cellMachine.productionRules);
            cellMachine.currentString = cellMachine.buildString();
            cellMachine.recursionDepth = (int) recursionChoiceBox.getValue();
            canvas.setHeight(cellMachine.canvasHeight);
            canvas.setWidth(cellMachine.canvasWidth);

            cellMachine.startX = cellMachine.canvasWidth / 2;
            cellMachine.startY = cellMachine.canvasHeight / 2;

            setCanvasBackground(canvas, Color.WHITE);
            // calculate and display generated development
            MainWindow.statusText = name + " "
                    + MainStatus.RUNNING.toString();
            MainWindow.updateStatus();
            cellMachine.displayGeneration(canvas, cellMachine.currentString,
                    cellMachine.startX, cellMachine.startY);
            enableButton(generateButton);
            MainWindow.statusText = name + " "
                    + MainStatus.FINISHED.toString();
            MainWindow.updateStatus();
        }
        else{
            this.setAlwaysOnTop(true);
        }
    }

    // Check the validity of input parameter values
    private boolean isInputValid() {
        boolean isValid = isLindenmayerHeightValid();
        if (!isValid) {
            return false;
        }
        else{
            cellMachine.canvasHeight=height;
        }
        isValid = isLindenmayerWidthValid();
        if (!isValid) {
            return false;
        }else{
            cellMachine.canvasWidth=width;
        }
        isValid = isAlphabetInputValid();
        if (!isValid) {
            return false;
        }
        isValid = isAxiomInputValid();
        if (!isValid) {
            return false;
        }
        isValid = isProductionRulesInputValid();
        if (!isValid) {
            return false;
        }

        isValid = isAngleInputValid();
        if (!isValid) {
            return false;
        }
        else{
            cellMachine.angle=angle;
        }

        isValid = isLengthInputValid();
        if (!isValid) {
            return false;
        }
        else{
            cellMachine.length=length;
        }
        return true;
    }

    private boolean isAlphabetInputValid() {
        if (isEmpty(tfAlphabet)) {
            cellMachine.alphabet = "";
        }
            cellMachine.alphabet = tfAlphabet.getText();
            cellMachine.alphabet=cellMachine.alphabet.trim();
            cellMachine.alphabet=cellMachine.alphabet.replace(",", "");

        if (cellMachine.isAlphabetValid(cellMachine.alphabet) == false) {
            JOptionPane.showMessageDialog(null, "Please use only capital letters of your choice from the english alphabet" +
                    " \n and the following characters to define your alphabet: +,-,[,]" +
                    "\n Use commas between the elements. This field cannot be empty.");
            enableButton(generateButton);
            return false;
        }
        return true;
    }


    private boolean isAxiomInputValid() {
        if (isEmpty(tfAxiom)) {
            JOptionPane.showMessageDialog(null, "This field cannot be empty. Please use only the capital letters X " +
                    "\nand Y for your axiom. Make sure that the letter you've chosen " +
                    "\nfor your axiom is included in your alphabet. ");
            enableButton(generateButton);
            return false;
        }
            cellMachine.axiom = tfAxiom.getText();

        if (cellMachine.isAxiomValid(cellMachine.axiom, cellMachine.alphabet) == false) {
            JOptionPane.showMessageDialog(null, "Wrong input! Please use only the capital letters X and Y for your axiom." +
                    "\n Make sure that the letter you've chosen for your axiom is included in your alphabet.");
            enableButton(generateButton);
            return false;
        }
        return true;
    }

    private boolean isProductionRulesInputValid() {
        if (isEmpty(taProductionRules)) {
            JOptionPane.showMessageDialog(null, "This field cannot be empty. Please only use the alphabet and axiom " +
                    "\nelements for production rules before attempting to generate an image." +
                    "\nSee the help box for further information. ");
            enableButton(generateButton);
            return false;
        }
            cellMachine.productionRules = taProductionRules.getText();
            cellMachine.productionRules = cellMachine.productionRules.trim();
            cellMachine.productionRules = cellMachine.productionRules.replace("\n", "");

        if (cellMachine.isRuleValid(cellMachine.productionRules, cellMachine.alphabet) == false) {
            JOptionPane.showMessageDialog(null, "Wrong input! Please only use the alphabet and axiom elements " +
                    "\nfor production rules before attempting to generate an image." +
                    "\nSee the help box for further information. ");
            enableButton(generateButton);
            return false;
        }
        return true;
    }

    private void setGridLayout() {
        alphabetButton.setTranslateX(84);
        alphabetButton.setTranslateY(167);
        axiomButton.setTranslateX(131);
        axiomButton.setTranslateY(200);
        rulesButton.setTranslateX(131);
        rulesButton.setTranslateY(233);

        grid = new GridPane();
        GridPane.setConstraints(lblExplanation, 1, 0, 1, 4,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblWidth, 1, 5, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfWidth, 1, 5, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblHeight, 1, 6, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfHeight, 1, 6, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblAlphabet, 1, 7, 1, 1,
                HPos.LEFT, VPos.BASELINE);

        GridPane.setConstraints(tfAlphabet, 1, 7, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblAxiom, 1, 8, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfAxiom, 1, 8, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblProductionRules, 1, 9, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(taProductionRules, 1, 9, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblRecursionDepth, 1, 10, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(recursionChoiceBox, 1, 10, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblAngle, 1, 11, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfAngle, 1, 11, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblLength, 1, 12, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfLength, 1, 12, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(generateButton, 1, 13, 2, 1,
                HPos.CENTER, VPos.BASELINE);
        GridPane.setConstraints(stopButton, 1, 13, 1, 1,
                HPos.RIGHT, VPos.BASELINE);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getChildren().addAll(lblExplanation,lblWidth, tfWidth, lblHeight, tfHeight, lblAlphabet, lblAxiom, axiomButton,lblRecursionDepth,
                lblProductionRules, lblAngle, tfAxiom, tfAlphabet, recursionChoiceBox, taProductionRules,
                tfAngle, lblLength, tfLength, generateButton, stopButton,alphabetButton,rulesButton);
    }

}
