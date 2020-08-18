package Generators.Lindenmayer;

import CommonUtils.GenTextField;
import Generators.BasicGenerators.Generator;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;

/*
 *This class offers a common basis for the GUI of the two lindenmayer generators that share  many features.
 */
public class LGeneratorsParent extends Generator {
    protected Label lblExplanation;
    protected Button alphabetButton;
    protected Button axiomButton;
    protected Button rulesButton;
    protected Label lblAlphabet;
    protected Label lblAxiom;
    protected Label lblRecursionDepth;
    protected Label lblAngle;
    protected Label lblLength;
    protected Label lblProductionRules;
    protected TextField tfAlphabet;
    protected TextField tfAxiom;
    protected TextArea taProductionRules;
    protected TextField tfLength;
    protected TextField tfAngle;
    protected boolean isAccepted;
    protected int height;
    protected int width;
    protected double length;
    protected double angle;

    public LGeneratorsParent(String name, Canvas canvas) {
        super(name, canvas);

        initCommonInputFields();
        initCommonLabels();
        initCommonButtons();
    }

    private void initCommonLabels() {
        lblExplanation = new Label("Please make sure to enter input in all" +
                "\nthe fields below. The question boxes " +
                "\nprovide information about the respective" +
                "\nfields. Feel free to play with the default" +
                "\ninput values for different images.");
        lblExplanation.setFont(Font.font("Verdana", 12));
        lblHeight = new Label("Canvas Height (50-800): ");
        lblHeight.setFont(Font.font("Verdana", 12));
        lblWidth = new Label("Canvas Width (50-800): ");
        lblWidth.setFont(Font.font("Verdana", 12));
        lblAlphabet = new Label("Alphabet      : ");
        lblAlphabet.setFont(Font.font("Verdana", 12));
        lblAxiom = new Label("Axiom (initiator)      : ");
        lblAxiom.setFont(Font.font("Verdana", 12));
        lblProductionRules = new Label("Production rules      : ");
        lblProductionRules.setFont(Font.font("Verdana", 12));
        lblRecursionDepth = new Label("Recursion depth : ");
        lblRecursionDepth.setFont(Font.font("Verdana", 12));
        lblAngle = new Label("Angle (0-360): ");
        lblAngle.setFont(Font.font("Verdana", 12));
        lblLength = new Label("Length : ");
        lblLength.setFont(Font.font("Verdana", 12));
    }

    public void initCommonInputFields() {
        taProductionRules = new TextArea();
        tfHeight.setText("800");
        tfWidth.setText("800");
        tfAlphabet = new GenTextField();
        taProductionRules = new TextArea();
        taProductionRules.setMaxWidth(70);
        taProductionRules.setPrefRowCount(4);
        tfAlphabet.setMaxWidth(70);
        tfAlphabet.setFont(Font.font("Verdana", 12));
        tfAxiom = new GenTextField();
        tfAxiom.setMaxWidth(70);
        tfAxiom.setFont(Font.font("Verdana", 12));
        tfAngle = new GenTextField();
        tfAngle.setMaxWidth(70);
        tfAngle.setFont(Font.font("Verdana", 12));
        tfAngle.setText("25.47");
        tfLength = new GenTextField();
        tfLength.setMaxWidth(70);
        tfLength.setFont(Font.font("Verdana", 12));
    }

    private void initCommonButtons() {
        alphabetButton = new Button("?");
        alphabetButton.setFont(Font.font("Verdana", 9));
        axiomButton = new Button("?");
        axiomButton.setFont(Font.font("Verdana", 9));
        rulesButton = new Button("?");
        rulesButton.setFont(Font.font("Verdana", 9));
        stopButton = new Button("Stop");
        stopButton.setFont(Font.font("Verdana", 14));
        stopButton.setTextFill(Color.DARKBLUE);
    }



    boolean isAngleInputValid() {
        try {
            if (isEmpty(tfAngle) ) {
                JOptionPane.showMessageDialog(null, "Please choose a number between 0 and 360 for the angle. " +
                "\n Also fractions are allowed. This field cannot be empty.");
                enableButton(generateButton);
                return false;
            }
            else if( Double.parseDouble(tfAngle.getText()) < 0){
                JOptionPane.showMessageDialog(null, "Invalid input! Please choose a number between 0 and 360 for the angle. " +
                        "\n Also fractions are allowed.");
                enableButton(generateButton);
                return false;
            }
            angle = Double.parseDouble(tfAngle.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please choose a number between 0 and 360 for the angle. " +
                    "\n Also fractions are allowed.");
            enableButton(generateButton);
            return false;
        }
        return true;
    }

    boolean isLengthInputValid() {
        try {
            if (isEmpty(tfLength) ) {
                JOptionPane.showMessageDialog(null, "Please enter a positive integer number for the length." +
                        "\nThis field cannot be empty.");
                enableButton(generateButton);
                return false;
            }
            else if( Double.parseDouble(tfLength.getText()) < 1){
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a positive integer number for the length.");
                enableButton(generateButton);
                return false;
            }
            length = Double.parseDouble(tfLength.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a positive integer number for the length.");
            enableButton(generateButton);
            return false;
        }
        return true;
    }

    boolean isLindenmayerHeightValid(){
        return isCanvasHeightValid(50,800);
    }
    boolean isLindenmayerWidthValid(){
        return isCanvasWidthValid(50,800);
    }

    //check if the height input is within a given limit
    private boolean isCanvasHeightValid(int minHeight, int maxHeight){
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
    private boolean isCanvasWidthValid(int minWidth, int maxWidth){
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
