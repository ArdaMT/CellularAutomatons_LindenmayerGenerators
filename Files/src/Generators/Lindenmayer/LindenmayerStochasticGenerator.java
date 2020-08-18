/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.Lindenmayer;

import CommonUtils.GenTextField;
import LSystems.LindenmayerStochastic;
import MainWindow.MainStatus;
import MainWindow.MainWindow;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;

/**
 * @author arm
 */
public class LindenmayerStochasticGenerator extends LGeneratorsParent {

    private ChoiceBox strokeTypeChoiceBox;
    private ChoiceBox strokeWidthChoiceBox;
    private Label lblChooseStrokeType;
    private Label lblChooseStrokeWidth;
    private TextField tfRecursionDepth;
    LindenmayerStochastic cellMachine;

    public LindenmayerStochasticGenerator(Canvas canvas, String name) {
        super(name, canvas);
        this.setTitle(name);
        errorText = "";
        setStopParameters();

        cellMachine = new LindenmayerStochastic();

        initLabels();
        initTextFields();
        strokeWidthChoiceBox = new ChoiceBox(FXCollections.observableArrayList("1", "2", "3", "4"));
        strokeTypeChoiceBox = new ChoiceBox(FXCollections.observableArrayList("Line", "Oval", "Rectangle"));
        strokeTypeChoiceBox.setValue("Line");
        strokeWidthChoiceBox.setValue("1");
        initButtons();
        setGridLayout();
        Scene scene = new Scene(grid);
        setScene(scene);
    }

    private void initLabels() {
        lblChooseStrokeType = new Label("Stroke type: ");
        lblChooseStrokeType.setFont(Font.font("Verdana", 12));
        lblChooseStrokeWidth = new Label("Stroke width: ");
        lblChooseStrokeWidth.setFont(Font.font("Verdana", 12));
    }

    private void initTextFields() {
        taProductionRules.setText("F(0.38)=F[-F],\nF=(0.62)=F[+F]");
        tfAlphabet.setText("F,&-,+,[,]");
        tfAxiom.setText("F");
        tfRecursionDepth = new GenTextField();
        tfRecursionDepth.setMaxWidth(70);
        tfRecursionDepth.setFont(Font.font("Verdana", 12));
        tfRecursionDepth.setText("12");
        tfLength.setText("16");
        error = new Label();
        error.setTextFill(Color.RED);
    }

    private void initButtons() {
        //Event Handler for the buttons
        generateButton.setOnAction(e -> {

            e.consume();
            disableButton(generateButton);
            setAlwaysOnTop(false);
            generate();
            if (!isAccepted) {
                this.alwaysOnTopProperty();
                MainWindow.setStatus(name + " Status : Ready.");
                enableButton(generateButton);

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
                    "\n and the following characters to define your alphabet: &,+,-,[,] \nUse commas between the elements.");
            this.setAlwaysOnTop(true);
            enableButton(generateButton);
        });


        axiomButton.setOnAction(e -> {
            e.consume();
            disableButton(generateButton);
            this.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(null, "Please use only a single capital letter of your " +
                    "\nchoice from your alphabet.");
            this.setAlwaysOnTop(true);
            enableButton(generateButton);
        });

        rulesButton.setOnAction(e -> {
            e.consume();
            disableButton(generateButton);
            this.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(null, "Please use only the elements of your alphabet for the rules. A single rule" +
                    "\nstarts with a capital letter followed by a number between 0 and 1 in round" +
                    "\nparantheses. This is the left side of the equation. For the right side you" +
                    "\ncan use any combination of elements in your alphabet. Depending on how" +
                    "\nmany rules you build, make sure that the sum of the numbers you put into the" +
                    "\nparantheses in each rule equals to 1. If you use square brackets in your" +
                    "\nrules, always make sure to use them as pairs and never as a single. That is" +
                    "\nOK: F(0.30=F[X], F(0.70)=F[], that is not OK: F(0.30)=F[X[, F(0.70)=F[. That" +
                    "\nmeans for every opening square bracket you use, you need to use a closing " +
                    "\nbracket. Also, a closing bracket can not precede its paired opening bracket. That " +
                    "\nis OK: F(0.30=F[[X][]], F(0.70)=F[]. That is not OK: F(0.30=F]X[, F(0.70)=F][." +
                    "\nEach rule starts on a new line and ends with a comma.");
            this.setAlwaysOnTop(true);
            enableButton(generateButton);
        });
    }

    //
    private void generate() {

        setStopParameters();
        MainWindow.setStatus(name + " "
                + MainStatus.GENERATE.toString());

        MainWindow.setSaveMenuStatus(false);

        isAccepted = isInputValid();
        if (isAccepted) {
            cellMachine.initiateString(cellMachine.axiom);
            cellMachine.currentString = cellMachine.buildString();
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
        } else {
            this.setAlwaysOnTop(true);
        }
    }

    // Check the validity of input parameter values
    private boolean isInputValid() {
        boolean isValid = isLindenmayerHeightValid();
        if (!isValid) {
            return false;
        } else {
            cellMachine.canvasHeight = height;
        }
        isValid = isLindenmayerWidthValid();
        if (!isValid) {
            return false;
        } else {
            cellMachine.canvasWidth = width;
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

        isValid = isDepthInputValid();
        if (!isValid) {
            return false;
        }

        isValid = isAngleInputValid();
        if (!isValid) {
            return false;
        } else {
            cellMachine.angle = angle;
        }
        isValid = isLengthInputValid();
        if (!isValid) {
            return false;
        } else {
            cellMachine.length = length;
        }

        cellMachine.strokeType = strokeTypeChoiceBox.getValue().toString();
        cellMachine.strokeWidth = Integer.parseInt(strokeWidthChoiceBox.getValue().toString());

        return true;
    }


    private boolean isAlphabetInputValid() {

        if (isEmpty(tfAlphabet)) {
            JOptionPane.showMessageDialog(null, "Alphabet cannot be empty. Please use  capital letters of your choice from the" +
                    "\nenglish alphabet and the following characters to define your alphabet: &,+,-,[,]" +
                    "\nUse commas between the elements.");
            enableButton(generateButton);
            return false;
        }
            cellMachine.alphabet = tfAlphabet.getText();
            cellMachine.alphabet = cellMachine.alphabet.trim();
            cellMachine.alphabet = cellMachine.alphabet.replace(",", "");

        if (cellMachine.isAlphabetValid(cellMachine.alphabet, cellMachine.VARIABLES,
                cellMachine.CONSTANTS) == false || "".equals(cellMachine.alphabet)) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please use  capital letters of your choice from the english" +
                    "\nalphabet and the following characters to define your alphabet: &,+,-,[,]" +
                    "\nUse commas between the elements.");
            enableButton(generateButton);
            return false;
                 }
        return true;
    }


    private boolean isAxiomInputValid() {

        if (isEmpty(tfAxiom)) {

                JOptionPane.showMessageDialog(null, "Axiom cannot be empty. Please use only a single capital " +
                        "\nletter for your axiom. You can use any letter from your alphabet.");
                enableButton(generateButton);
                return false;
            } else {
                cellMachine.axiom = tfAxiom.getText();
            }
            if (cellMachine.isAxiomValid(cellMachine.axiom, cellMachine.alphabet) == false) {

                JOptionPane.showMessageDialog(null, "Invalid input!. Please use only a single capital letter for your axiom." +
                        "\n You can use any letter from your own alphabet as an axiom.");
                enableButton(generateButton);
                return false;
            }
            return true;
        }

        private boolean isProductionRulesInputValid () {
            if (isEmpty(taProductionRules)) {
                JOptionPane.showMessageDialog(null, "Production rules can not be empty. Please only use the alphabet and axiom elements " +
                        "\nfor this field. See the help box for further information. ");
                enableButton(generateButton);
                return false;
            } else {
                cellMachine.stochasticRulesString = taProductionRules.getText();
                cellMachine.stochasticRulesString = cellMachine.stochasticRulesString.trim();
                cellMachine.stochasticRulesString = cellMachine.stochasticRulesString.replace("\n", "");
            }
            if (cellMachine.isRuleValid(cellMachine.stochasticRulesString, cellMachine.alphabet) == false) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please only use the alphabet and axiom elements " +
                        "\nfor production rules. See the help box for further information.");
                enableButton(generateButton);
                return false;
            }
            return true;
        }

        private boolean isDepthInputValid () {
            if(isEmpty(tfRecursionDepth)){
                JOptionPane.showMessageDialog(null, "Recursion depth cannot be empty. Please choose a number between 1 and 15 for this field. " +
                        "\nNumbers between 12 and 15 can sometimes cause performance problems. ");
                enableButton(generateButton);
                return false;
            }
            try {
                cellMachine.recursionDepth = Integer.parseInt(tfRecursionDepth.getText());
                if (cellMachine.recursionDepth < 1 || cellMachine.recursionDepth > 15) {
                    JOptionPane.showMessageDialog(null, "Invalid input! Please choose a number between 1 and 15 for the recursion depth. " +
                            "\nNumbers between 12 and 15 can sometimes cause performance problems.");
                    enableButton(generateButton);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please choose a number between 1 and 15 for the recursion depth." +
                        "\nNumbers between 12 and 15 can sometimes cause performance problems.");
                enableButton(generateButton);
                return false;
            }
            return true;
        }


        private void setGridLayout () {
            alphabetButton.setTranslateX(85);
            alphabetButton.setTranslateY(164);
            axiomButton.setTranslateX(131);
            axiomButton.setTranslateY(198);
            rulesButton.setTranslateX(131);
            rulesButton.setTranslateY(231);
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
            GridPane.setConstraints(tfRecursionDepth, 1, 10, 1, 1,
                    HPos.RIGHT, VPos.BASELINE);
            GridPane.setConstraints(lblAngle, 1, 11, 1, 1,
                    HPos.LEFT, VPos.BASELINE);
            GridPane.setConstraints(tfAngle, 1, 11, 1, 1,
                    HPos.RIGHT, VPos.BASELINE);
            GridPane.setConstraints(lblLength, 1, 12, 1, 1,
                    HPos.LEFT, VPos.BASELINE);
            GridPane.setConstraints(tfLength, 1, 12, 1, 1,
                    HPos.RIGHT, VPos.BASELINE);
            GridPane.setConstraints(lblChooseStrokeType, 1, 13, 1, 1,
                    HPos.LEFT, VPos.BASELINE);
            GridPane.setConstraints(strokeTypeChoiceBox, 1, 13, 1, 1,
                    HPos.RIGHT, VPos.BASELINE);
            GridPane.setConstraints(strokeTypeChoiceBox, 1, 13, 1, 1,
                    HPos.RIGHT, VPos.BASELINE);
            GridPane.setConstraints(lblChooseStrokeWidth, 1, 14, 1, 1,
                    HPos.LEFT, VPos.BASELINE);
            GridPane.setConstraints(strokeWidthChoiceBox, 1, 14, 1, 1,
                    HPos.RIGHT, VPos.BASELINE);

            GridPane.setConstraints(generateButton, 1, 15, 2, 1,
                    HPos.CENTER, VPos.BASELINE);
            GridPane.setConstraints(stopButton, 1, 15, 2, 1,
                    HPos.RIGHT, VPos.BASELINE);

            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.getChildren().addAll(lblExplanation, lblWidth, tfWidth, lblHeight, tfHeight, lblAlphabet, lblAxiom, lblRecursionDepth,
                    lblAngle, tfAxiom, tfAlphabet, tfRecursionDepth, lblProductionRules,
                    taProductionRules, tfAngle, lblLength, lblChooseStrokeType, strokeTypeChoiceBox,
                    lblChooseStrokeWidth, strokeWidthChoiceBox, tfLength, generateButton, stopButton, alphabetButton, axiomButton, rulesButton);
        }

    }


