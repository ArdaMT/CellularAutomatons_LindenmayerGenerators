/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.RandomFunctionTree;

import CommonUtils.GenTextField;
import Generators.BasicGenerators.Generator;
import MainWindow.MainStatus;
import MainWindow.MainWindow;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

import static javafx.scene.paint.Color.RED;

/**
 *
 * @author arm
 */
public class RandomFunctionTreeGenerator extends Generator {

    private int canvasHeight;
    private int canvasWidth;
    private int minDepth;
    private int maxDepth;
    private int depth = -1;
    private int seed;
    private int hue;
    private Label lblMinDepth;
    private Label lblMaxDepth;
    private Label lblColourConfiguration;
    private Label lblSeed;
    private Label lblDepth;
    private RadioButton randomSeed;
    private RadioButton inputSeed;
    private RadioButton invisibleButton;
    private GenTextField tfMinDepth;
    private GenTextField tfMaxDepth;
    private GenTextField tfColourConfiguration;
    private GenTextField tfSeed;
    private GenTextField tfDepth;
    private Button automaticGeneratorButton;
    private String treeString = "";
    private int currentPosition = 0;
    private int numberOfChildren;
    private Random rn;
    private int[][] functionValue;
    private ToggleGroup group;
    private CheckBox CustomImageBox;
    public static FunctionGenerator functionGenerator;
    public Node root;

    public RandomFunctionTreeGenerator(Canvas canvas, String name) {
        super(name, canvas);
        this.setTitle(name);
        rn = new Random(8);
        errorText = "";
        inputError = 0;
        errorText = "";
        setStopParameters();
        initLabels();
        initTextFields();
        initButtons();
        setGridLayout();
        Scene scene = new Scene(grid);
        setScene(scene);
    }

    protected void generate() {
        clearCanvas(canvas);
        inputError = 0;
        errorText = "";
        error.setText("");
        setStopParameters();
        MainWindow.setStatus(name + " " + MainStatus.GENERATE.toString());
        MainWindow.setSaveMenuDisabled(false);
        MainWindow.saveAs.setDisable(false);
        try {
            checkInput();
            if (inputError != 0) {
                errorText += "|";
                error.setText(errorText);
                throw new NumberFormatException();
            } else {
                //compute depth using the min and max.  values for depth
                if (tfDepth.isDisabled()) {
                    depth = computeDepth(minDepth, maxDepth);
                }
                createTree(seed, depth);
                computeImage(canvas.getHeight(), canvas.getWidth());
                MainWindow.setStatus(name + " " + MainStatus.FINISHED.toString());
                MainWindow.setDepth(depth);
                MainWindow.setSeed(seed);
                MainWindow.setHue(hue);

            }
        } catch (NumberFormatException ne) {
            setErrorStatus(errorText);
            MainWindow.setStatus("Wrong Entry - " + name + " " + MainStatus.READY.toString());
        }
    }

    //generate 9 automatic images
    private void automaticGenerate() {
        clearCanvas(canvas);
        inputError = 0;
        errorText = "";
        error.setText("");
        setStopParameters();
        MainWindow.setStatus(name + " " + MainStatus.GENERATE.toString());
        MainWindow.setSaveMenuDisabled(false);
        MainWindow.saveAs.setDisable(false);
        //each miniature image has a size of 200x200. there is an empty space of 10 pixel  between the images
        canvas.setWidth(620);
        canvas.setHeight(620);

        int offsetX;
        int offsetY;
        int[] seedArray = new int[9];
        int[] depthArray = new int[9];
        int[] hueArray = new int[9];

        int a = 5;
        for (int i = 0; i < seedArray.length; i++) {
            seedArray[i] = Math.abs((int) System.currentTimeMillis() * a);
            a += 1;
            depthArray[i] = 1 + new Random().nextInt((4 - 1) + 1);
            hueArray[i] = new Random().nextInt(360);

        }

        for (int i = 0; i < seedArray.length; i++) {
            switch (i) {
                case 0:
                    offsetX = 0;
                    offsetY = 0;
                    break;
                case 1:
                    offsetX = 210;
                    offsetY = 0;
                    break;
                case 2:
                    offsetX = 420;
                    offsetY = 0;
                    break;
                case 3:
                    offsetX = 0;
                    offsetY = 210;
                    break;
                case 4:
                    offsetX = 210;
                    offsetY = 210;
                    break;
                case 5:
                    offsetX = 420;
                    offsetY = 210;
                    break;
                case 6:
                    offsetX = 0;
                    offsetY = 420;
                    break;
                case 7:
                    offsetX = 210;
                    offsetY = 420;
                    break;
                case 8:
                    offsetX = 420;
                    offsetY = 420;
                    break;

                default:
                    offsetX = 420;
                    offsetY = 420;
            }
            //set the current seed and depth values;
            seed = seedArray[i];
            depth = depthArray[i];
            hue = hueArray[i];

            createTree(seed, depth);
            computeImage(200, 200, offsetX, offsetY);
        }
        //get the coordinates of the click 
        canvas.setOnMouseClicked((MouseEvent t) -> {
            int x = (int) t.getX();
            int y = (int) t.getY();
            //determine the image matching the above coordinates
            int imageIndex = chooseImage(x, y);
            seed = seedArray[imageIndex];
            depth = depthArray[imageIndex];
            hue = hueArray[imageIndex];
            //enlarge the chosen image
            canvas.setWidth(400);
            canvas.setHeight(400);
            createTree(seed, depth);
            computeImage(canvas.getHeight(), canvas.getWidth());
            MainWindow.setStatus(name + " " + MainStatus.FINISHED.toString());
            MainWindow.setDepth(depth);
            MainWindow.setSeed(seed);
            MainWindow.setHue(hue);
            canvas.setOnMouseClicked(null);

        });
    }

    private void createTree(int seed, int depth) {
        functionGenerator = new FunctionGenerator(seed);
        root = new Node(functionGenerator.getRandomFunction());
        root.createChildren(depth - 1);
    }

    private void initLabels() {
        lblHeight = new Label("Canvas Height : ");
        lblWidth = new Label("Canvas Width : ");
        lblMinDepth = new Label("Minimum Depth : ");
        lblMaxDepth = new Label("Maximum Depth : ");
        lblColourConfiguration = new Label("Colour Hue : ");
        lblDepth = new Label("Depth  : ");
        lblSeed = new Label("Seed : ");
    }

    private void initTextFields() {
        tfHeight = new GenTextField();
        tfHeight.setMaxWidth(70);
        tfHeight.setText("400");
        tfWidth = new GenTextField();
        tfWidth.setMaxWidth(70);
        tfWidth.setText("400");
        tfMinDepth = new GenTextField();
        tfMinDepth.setMaxWidth(70);
        tfMaxDepth = new GenTextField();
        tfMaxDepth.setMaxWidth(70);
        tfSeed = new GenTextField();
        tfSeed.setMaxWidth(70);
        tfSeed.setDisable(true);
        tfColourConfiguration = new GenTextField();
        tfColourConfiguration.setMaxWidth(70);
        tfColourConfiguration.setFont(Font.font("Verdana", 12));

        tfDepth = new GenTextField();
        tfDepth.setMaxWidth(70);
        tfDepth.setFont(Font.font("Verdana", 12));
        tfDepth.setDisable(true);

        error = new Label();
        error.setTextFill(RED);
    }

    private void initButtons() {
        group = new ToggleGroup();
        randomSeed = new RadioButton("random seed");
        inputSeed = new RadioButton("enter seed: ");
        invisibleButton = new RadioButton();
        randomSeed.setToggleGroup(group);
        inputSeed.setToggleGroup(group);
        invisibleButton.setToggleGroup(group);
        CustomImageBox = new CheckBox("Create a custom image");
        CustomImageBox.setFont(Font.font("Verdana", 11));
        CustomImageBox.setTextFill(Color.DARKBLUE);
        generateButton = new Button("Generate");
        generateButton.setFont(Font.font("Verdana", 14));
        generateButton.setTextFill(Color.DARKBLUE);
        automaticGeneratorButton = new Button("Generate series of Images");
        automaticGeneratorButton.setFont(Font.font("Verdana", 14));
        automaticGeneratorButton.setTextFill(Color.DARKBLUE);
        stopButton = new Button("Stop");
        stopButton.setFont(Font.font("Verdana", 14));
        stopButton.setTextFill(Color.DARKBLUE);
        //Event Handler for the buttons
        generateButton.setOnAction(e -> {
            e.consume();
            generate();
        });
        automaticGeneratorButton.setOnAction(e -> {
            automaticGenerate();
        }
        );
        CustomImageBox.setOnAction(e -> {
            {
                if (CustomImageBox.isSelected()) {
                    randomSeed.setDisable(true);
                    group.selectToggle(inputSeed);
                    tfMinDepth.setDisable(true);
                    tfMaxDepth.setDisable(true);
                    tfSeed.setDisable(false);
                    tfDepth.setDisable(false);
                } else {
                    randomSeed.setDisable(false);
                    group.selectToggle(invisibleButton);
                    tfSeed.setDisable(true);
                    tfDepth.setDisable(true);
                    tfMinDepth.setDisable(false);
                    tfMaxDepth.setDisable(false);
                }
            }
        });
        inputSeed.setOnAction(e -> {
            tfSeed.setDisable(false);

        });
        randomSeed.setOnAction(e -> {
            tfSeed.setDisable(true);

        });
        invisibleButton.setOnAction(e -> {
            tfSeed.setDisable(true);

        });
    }

    private void computeImage(double height, double width) {
        computeImage(height, width, 0, 0);
    }

    private void computeImage(double height, double width, int offsetX, int offsetY) {

        System.out.println("seed: " + seed + " dept: " + depth + " hue " + hue);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                double result = root.calculate(j / width, i / height,
                        (j / width + i / height) / 2);
                if (result < 0) {
                    System.out.println("small");
                }
                if (result > 1) {
                    System.out.println("big");
                }
                Color color = Color.hsb(hue, 0.5, result);
                gc.setFill(color);
                gc.fillRect(j + offsetX, i + offsetY, 1, 1);
            }
        }

    }

    private void setGridLayout() {
        grid = new GridPane();
        GridPane.setConstraints(lblWidth, 1, 2, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfWidth, 2, 2, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblHeight, 1, 3, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfHeight, 2, 3, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblMinDepth, 1, 4, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfMinDepth, 2, 4, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblMaxDepth, 1, 5, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfMaxDepth, 2, 5, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblSeed, 4, 2, 1, 1,
                HPos.CENTER, VPos.BASELINE);
        GridPane.setConstraints(randomSeed, 5, 2, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(inputSeed, 5, 3, 1, 1,
                HPos.CENTER, VPos.BASELINE);
        GridPane.setConstraints(tfSeed, 5, 4, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblColourConfiguration, 1, 6, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfColourConfiguration, 2, 6, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblDepth, 4, 5, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(tfDepth, 5, 5, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(error, 1, 12, 2, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(CustomImageBox, 1, 7, 1, 1,
                HPos.CENTER, VPos.BASELINE);
        GridPane.setConstraints(generateButton, 5, 8, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(automaticGeneratorButton, 2, 8, 1, 1,
                HPos.RIGHT, VPos.BASELINE);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getChildren().addAll(lblWidth, tfWidth, lblHeight, tfHeight, lblMinDepth, tfMinDepth, lblMaxDepth,
                tfMaxDepth, lblSeed, randomSeed, inputSeed, tfSeed, generateButton, automaticGeneratorButton, error,
                lblColourConfiguration, tfColourConfiguration, CustomImageBox, lblDepth, tfDepth);
    }

    private void checkInput() {
        if (isEmpty(tfHeight)) {
            canvas.setHeight(0);
        } else {
            canvas.setHeight(Integer.parseInt(tfHeight.getText()));
        }
        if ((canvas.getHeight() < 50) || (canvas.getHeight() > 800)) {
            inputError = 1;
            errorText += "| invalid height ";
        }
        if (isEmpty(tfWidth)) {
            canvas.setWidth(0);
        } else {
            canvas.setWidth(Integer.parseInt(tfWidth.getText()));
        }
        if ((canvas.getWidth() < 50) || (canvas.getWidth() > 800)) {
            inputError = 1;

            errorText += "| invalid width ";
        }
        if (!tfMinDepth.isDisabled()) {
            if (isEmpty(tfMinDepth)) {
                minDepth = 0;
            } else {
                minDepth = Integer.parseInt(tfMinDepth.getText());
            }
            if (minDepth < 1 || minDepth > 8) {
                inputError = 1;

                errorText += "| invalid minimum depth ";
            }
        }
        if (!tfMaxDepth.isDisabled()) {
            if (isEmpty(tfMaxDepth)) {
                maxDepth = 0;
            } else {
                maxDepth = Integer.parseInt(tfMaxDepth.getText());
            }
            if (maxDepth < 2 || maxDepth <= minDepth || maxDepth > 10) {
                inputError = 1;

                errorText += "| invalid maximum depth ";
            }
        }
        if (isEmpty(tfColourConfiguration)) {
            inputError = 1;
            errorText += "| invalid colour hue ";

        } else {
            hue = Integer.parseInt(tfColourConfiguration.getText()) % 360;
        }
        if (group.getSelectedToggle() == null || group.getSelectedToggle() == invisibleButton) {
            tfSeed.setDisable(true);
            errorText += "No seed selected";
        } else if (group.getSelectedToggle() == randomSeed) {

            seed = (int) System.currentTimeMillis();
        } else {
            tfSeed.setDisable(false);
            if (isEmpty(tfSeed)) {

                seed = 0;
            } else {
                seed = Integer.parseInt(tfSeed.getText());

            }
        }

        if (!tfDepth.isDisabled() && tfMinDepth.isDisabled() && tfMaxDepth.isDisabled()) {
            if (isEmpty(tfDepth)) {
                inputError = 1;
                errorText += "| invalid depth";
            } else {
                depth = Integer.parseInt(tfDepth.getText());
            }
        }
    }

    private int computeDepth(int mindep, int maxdep) {

        return mindep + new Random().nextInt((maxdep - mindep) + 1);
    }

    private int chooseImage(int x, int y) {
        if (y < 200) {
            if (x < 200) {
                return 0;
            } else if (x < 410) {
                return 1;
            } else {
                return 2;
            }
        } else if (y < 410) {
            if (x < 200) {
                return 3;
            } else if (x < 410) {
                return 4;
            } else {
                return 5;
            }
        } else {
            if (x < 200) {
                return 6;
            } else if (x < 410) {
                return 7;
            } else {
                return 8;
            }
        }
    }

    public void setDepth(int d){
        depth =d;
    }public void setSeed(int s){
        seed =s;
    }public void setHue(int h){
        hue =h;
    }
}
