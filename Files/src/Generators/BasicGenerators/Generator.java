
package Generators.BasicGenerators;

import CommonUtils.GenDblTextField;
import CommonUtils.GenTextField;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// Common Generator attributes and methods that can be inherited by all
// Generators. Some attributes and methods added to project for KE2
// by Gundula Swidersky
public abstract class Generator extends Stage {

    // Class local attributes
    public String name;
    public Canvas canvas;
    public int maxIterations;
    public int maxGenerations;
    public int inputError;
    public String errorText;
    public boolean stopRun;
    public boolean runInfinite;
    public int genCount;

    // Common Generator parameter window attributes
    protected Button generateButton;
    protected Button stopButton;
    protected GridPane grid;
    protected Label error;
    protected Label lblGridResolution;
    protected Label lblWidth;
    protected Label lblHeight;
    protected GenTextField tfWidth;
    protected GenTextField tfHeight;
    protected int height;
    protected int width;
    protected Label lblExplanation;
    // Constructor
    public Generator(String name, Canvas canvas) {
        super();
        this.name = name;
        this.canvas = canvas;
        this.maxIterations = 1000;
        this.maxGenerations = 1000;
        this.inputError = 0;
        this.errorText = "";
        this.stopRun = false;
        this.runInfinite = false;
        this.genCount = 1;
        initInputFields();
    }

    //initialise the common input fields and buttons
    protected void initInputFields(){
        tfHeight = new GenTextField();
        tfHeight.setMaxWidth(70);
        tfHeight.setFont(Font.font("Verdana", 12));
        tfWidth = new GenTextField();
        tfWidth.setMaxWidth(70);
        tfWidth.setFont(Font.font("Verdana", 12));
        generateButton = new Button("Generate");
        generateButton.setFont(Font.font("Verdana", 14));
        generateButton.setTextFill(Color.DARKBLUE);

    };

    // Method to clear the image on the canvas
    public void clearCanvas(Canvas canvas) {
        Platform.runLater(new Runnable() {
            public void run() {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        });
    }
    
    // Method to set background of canvas
    public void setCanvasBackground(Canvas canvas, Color color) {
        Platform.runLater(new Runnable() {
            public void run() {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setLineWidth(1.0);
                gc.setFill(color);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        });
    }
    
    // Helper Method to disable a button
    public void disableButton(Button actButton) {
        Platform.runLater(new Runnable() {
            public void run() {
                actButton.setDisable(true);
            }
        });
    }
    
    // Helper Method to enable a button
    public void enableButton(Button actButton) {
        Platform.runLater(new Runnable() {
            public void run() {
                actButton.setDisable(false);
            }
        });
    }

    // Helper Method to set parameters to stop the Thread
    public void setStopParameters() {
        runInfinite = false;
        stopRun = true;
    }

    // Helper Method to set error text (parameter input errors)
    public void setErrorStatus(String text) {
        error.setText(text);
    }
    
    // Helper method to check if content of textfield is empty string.
    public boolean isEmpty(GenTextField actTextField) {
        return ("".equals(actTextField.getText().trim()));
    }    

    // Helper method to check if content of textfield is empty string.
    public boolean isEmpty(GenDblTextField actTextField) {
 return ("".equals(actTextField.getText().trim()));
    }   // Helper method to check if content of textfield is empty string.
    public boolean isEmpty(TextField actTextField) {
      return ("".equals(actTextField.getText().trim()));
    }  
    public boolean isEmpty(TextArea actTextField) {
      return ("".equals(actTextField.getText().trim()));
    }

}
