package Generators.CellularAutomatons;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/*
In this class a simple generator is created, which only creates a blue circle
that is equal to the  size of the width or height of the canvas depending on
which of these two values is the smaller one.
 */
public class SimpleGenerator extends CellularAutomatonParent {


    //Constructor
    public SimpleGenerator(Canvas canvas, String name) {
        super(name, canvas);
        this.setTitle(name);

    lblExplanation= new Label("This generator creates a blue circle. The circle's diameter" +
            "\nis the smaller of the input values for width and height.");
        lblExplanation.setFont(Font.font("Verdana", 12));
        setStopParameters();

        //Layout for Generator window
        GridPane grid = new GridPane();
        GridPane.setConstraints(lblExplanation, 1, 1, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblHeight, 1, 2, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfHeight, 1, 2, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblWidth, 1, 3, 1, 1, HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfWidth, 1, 3, 1, 1, HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(generateButton, 1, 4, 1, 1, HPos.RIGHT, VPos.BASELINE);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getChildren().addAll(lblExplanation,lblHeight, tfHeight, lblWidth, tfWidth, generateButton);

        Scene scene = new Scene(grid);
        setScene(scene);

        //Event Handler
        generateButton.setOnAction(e -> {
            setAlwaysOnTop(false);
            generate();
            setAlwaysOnTop(true);
        });

            }

    //Methods of Simple Generator
    private void generate() {
        MainWindow.MainWindow.setStatus("generating...");
        MainWindow.MainWindow.setSaveMenuStatus(false);
        if(isCellularHeightValid()&&isCellularWidthValid()) {

                    if (drawCircle(this.canvas, height, width)) {
                        MainWindow.MainWindow.setStatus("Simple Generator ready...");
                    }



        }else{
            setAlwaysOnTop(true);
        }
    }


    private boolean drawCircle(Canvas canvas, int height, int width) {
        canvas.setHeight(height);
        canvas.setWidth(width);

        //smaller of the input values becomes the radius
        int r = (height < width) ? height : width;

        // clear the canvas
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw circle and place it in the middle
        gc.setFill(Color.BLUE);
        gc.setLineWidth(4);
        gc.fillOval((width / 2) - (r / 2) + 5, (height / 2) - (r / 2) + 5, r - 10, r - 10);
        return true;
    }


}//Simple Generator
