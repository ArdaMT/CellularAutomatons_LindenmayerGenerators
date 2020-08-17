package MainWindow;

import Generators.CellularAutomatons.ElementaryCellularAutomaton;
import Generators.CellularAutomatons.ForestFireAutomaton;
import Generators.CellularAutomatons.GameOfLifeAutomaton;
import Generators.Lindenmayer.LindenmayerGenerator;
import Generators.Lindenmayer.LindenmayerStochasticGenerator;
import Generators.CellularAutomatons.SimpleGenerator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/*

here the main window of the program is initialised with its GUI elements and a menu bar.
Furthermore, methods for saving an image, clearing the canvas and quitting the program
are introduced in this class. Menu items for all the image and animation generators are initialised here.
Hier wird das Hauptfenster erzeugt, mit den Bedienelementen in der Menuleiste.
Des Weiteren werden Methoden für die Speicherung des Bildes und den Zugriff 
auf die StatusLeiste bereitgestellt.
Das MenuItem "SaveImage" wird so lange ausgeblendet bis ein Bild erstellt wurde.
Das MenuItem "SimpleGenerator" wird so lange ausgeblendet wie ein Generator 
Fenster geöffnet ist.
Die einzelnen EventHandler werden im Anschluss mit Lambdas realisiert.
 */
public class MainWindow extends Application {

    //Class local attributes
    private static int hue;
    private static int depth;
    private static int seed;
    private Canvas canvas = new Canvas();
    private static Label statusLine = new Label();
    public static String statusText = "";
    public static int generatorId = -1;
    private String[] generatorNames = new String[]{"Simple Generator",
            "Elementary Cellular Automaton", "GameOfLife Automaton",
            "Forest Fire Automaton", "Lindenmayer Generator",
            "Lindenmayer Stochastic Generator", "Random Function Tree Generator"
    };
    private MenuItem simpleGenerator = new MenuItem(generatorNames[0]);
    private MenuItem elementaryCellularAutomaton = new MenuItem(generatorNames[1]);
    private MenuItem gameOfLifeAutomaton = new MenuItem(generatorNames[2]);
    private MenuItem forestFireAutomaton = new MenuItem(generatorNames[3]);
    private MenuItem lindenmayerGenerator = new MenuItem(generatorNames[4]);
    private MenuItem lindenmayerStochasticGenerator = new MenuItem(generatorNames[5]);
    private MenuItem randomFunctionTreeGenerator = new MenuItem(generatorNames[6]);
    private static final MenuItem save = new MenuItem("Save Image");
    public static MenuItem saveAs = new MenuItem("Save as");

    //Constants for the main window size
    private final int HEIGHT = 600, WIDTH = 800;

    @Override
    public void start(Stage primaryStage) {
        //Menu
        Menu fileMenu = new Menu("_File");
        Menu generatorMenu = new Menu("_Generator");

        //Menu Items
        MenuItem clear = new MenuItem("Clear");
        fileMenu.getItems().add(clear);
        save.setDisable(true);
        saveAs.setDisable(true);
        SeparatorMenuItem separator = new SeparatorMenuItem();
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(separator);
        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        fileMenu.getItems().add(saveAs);
        fileMenu.getItems().add(separator2);
        MenuItem quit = new MenuItem("Quit");
        fileMenu.getItems().add(quit);
        generatorMenu.getItems().addAll(simpleGenerator, elementaryCellularAutomaton,
                gameOfLifeAutomaton, forestFireAutomaton, lindenmayerGenerator,
                lindenmayerStochasticGenerator, randomFunctionTreeGenerator);
        //Main Menu Bar
        MenuBar mainbar = new MenuBar();
        mainbar.getMenus().addAll(fileMenu, generatorMenu);

        //ScrollPane
        ScrollPane sp = new ScrollPane();
        BorderPane canvaspane = new BorderPane();
        canvaspane.setCenter(canvas);
        sp.setContent(canvaspane);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);

        //Layout
        BorderPane pane = new BorderPane();
        pane.setTop(mainbar);
        pane.setCenter(sp);
        HBox bottom = new HBox(statusLine);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(5, 5, 5, 5));
        pane.setBottom(bottom);

        Scene scene = new Scene(pane, WIDTH, HEIGHT);

        primaryStage.setTitle("Kurseinheit 3");
        primaryStage.setScene(scene);
        primaryStage.show();

        //EventHandler of the MenuItems, implemented using Lambdas
        primaryStage.setOnCloseRequest(e -> {
            // Both needed to make sure Threads are stopped.
            Platform.exit();
            System.exit(0);
        });

        // Menu item Simple Generator
        simpleGenerator.setOnAction((ActionEvent t) -> {
            generatorId = 0;
            setStatus(generatorNames[generatorId] + " "
                    + MainStatus.READY.toString());
            setMenuItemStatus(true);
            SimpleGenerator simgen = new SimpleGenerator(canvas, generatorNames[generatorId]);
            simgen.show();
            simgen.setOnCloseRequest(e -> {
                simgen.setStopParameters();
                setStatus(MainStatus.OK.toString());
                setMenuItemStatus(false);
            });
        });

        // Menu item elementary Cellular Generator
        elementaryCellularAutomaton.setOnAction((ActionEvent t) -> {
            generatorId = 1;
            setStatus(generatorNames[generatorId] + " "
                    + MainStatus.READY.toString());
            setMenuItemStatus(true);
            ElementaryCellularAutomaton elementaryCellularAutomaton
                    = new ElementaryCellularAutomaton(canvas,
                    generatorNames[generatorId]);
            elementaryCellularAutomaton.show();
            elementaryCellularAutomaton.setOnCloseRequest(e -> {
                elementaryCellularAutomaton.setStopParameters();
                setStatus(MainStatus.OK.toString());
                setMenuItemStatus(false);
            });
        });

        // Menu item Game Of Life Automaton
        gameOfLifeAutomaton.setOnAction((ActionEvent t) -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            canvas.setHeight(0);
            canvas.setWidth(0);
            generatorId = 2;
            setStatus(generatorNames[generatorId] + " "
                    + MainStatus.READY.toString());
            setMenuItemStatus(true);
            GameOfLifeAutomaton gameOfLifeGen
                    = new GameOfLifeAutomaton(canvas,
                    generatorNames[generatorId]);
            gameOfLifeGen.show();
            gameOfLifeGen.setOnCloseRequest(e -> {
                gameOfLifeGen.setStopParameters();
                //stop the animation
                if (gameOfLifeGen.timeline != null) {
                    gameOfLifeGen.timeline.stop();
                }
                setStatus(MainStatus.OK.toString());
                setMenuItemStatus(false);
            });
        });

        // Menu item Forest Fire Automaton
        forestFireAutomaton.setOnAction((ActionEvent t) -> {
            generatorId = 3;
            setStatus(generatorNames[generatorId] + " "
                    + MainStatus.READY.toString());
            setMenuItemStatus(true);
            ForestFireAutomaton forestFireAutomaton
                    = new ForestFireAutomaton(canvas,
                    generatorNames[generatorId]);
            forestFireAutomaton.show();
            forestFireAutomaton.setOnCloseRequest(e -> {
                forestFireAutomaton.setStopParameters();
                //stop the animation
                if (forestFireAutomaton.timeline != null) {
                    forestFireAutomaton.timeline.stop();
                }
                setStatus(MainStatus.OK.toString());
                setMenuItemStatus(false);
            });
        });


        //Menu item Lindenmayer Generator
        lindenmayerGenerator.setOnAction((ActionEvent t) -> {
            generatorId = 4;
            setStatus(generatorNames[generatorId] + " "
                    + MainStatus.READY.toString());
            setMenuItemStatus(true);
            LindenmayerGenerator lGen = new LindenmayerGenerator(canvas,
                    generatorNames[generatorId]);
            lGen.show();
            lGen.setOnCloseRequest(e -> {
                lGen.setStopParameters();
                setStatus(MainStatus.OK.toString());
                setMenuItemStatus(false);
            });

        });
        //Menu item Lindenmayer Stoachistic generator
        lindenmayerStochasticGenerator.setOnAction((ActionEvent t) -> {
            generatorId = 5;
            setStatus(generatorNames[generatorId] + " "
                    + MainStatus.READY.toString());
            setMenuItemStatus(true);
            LindenmayerStochasticGenerator stGen = new LindenmayerStochasticGenerator(canvas,
                    generatorNames[generatorId]);
            stGen.show();
            stGen.setOnCloseRequest(e -> {
                stGen.setStopParameters();
                setStatus(MainStatus.OK.toString());
                setMenuItemStatus(false);
            });
        });

        randomFunctionTreeGenerator.setOnAction((ActionEvent t) -> {
            generatorId = 6;
            setStatus(generatorNames[generatorId] + " "
                    + MainStatus.READY.toString());
            setMenuItemStatus(true);
            Generators.RandomFunctionTree.RandomFunctionTreeGenerator rGen =
                    new Generators.RandomFunctionTree.RandomFunctionTreeGenerator(canvas,
                            generatorNames[generatorId]);
            rGen.show();
            rGen.setOnCloseRequest(e -> {
                rGen.setStopParameters();
                setStatus(MainStatus.OK.toString());
                setMenuItemStatus(false);
            });
        });
        // Event Handler
        clear.setOnAction((ActionEvent t) -> {
            setSaveMenuStatus(true);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            canvas.setHeight(0);
            canvas.setWidth(0);
        });
        save.setOnAction((ActionEvent t) -> {
            saveFileDialog();
        });
        quit.setOnAction((ActionEvent t) -> {
            Stage stage = (Stage) mainbar.getScene().getWindow();
            stage.close();
            // Both needed to make sure Threads are stopped.
            Platform.exit();
            System.exit(0);
        });
    }

    //Save Dialog for Menu
    // Only file type png supported for now.
    private void saveFileDialog() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        fc.setTitle("Save to File");
        File file = fc.showSaveDialog(new Stage());
        saveFile(file);
    }

    //save method for generators -> public
    public void saveFile(File file) {
        if (file != null) {
            WritableImage wi;
            wi = new WritableImage((int) canvas.getWidth(),
                    (int) canvas.getHeight());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(
                        canvas.snapshot(null, wi), null), "png", file);
            } catch (IOException e) {
            }
        }
    }

    // Update status of StatusBar on the main application window.
    public static void updateStatus() {
        if (Platform.isFxApplicationThread()) {
            statusLine.setText(statusText);
        } else {
            Platform.runLater(new Runnable() {
                public void run() {
                    statusLine.setText(statusText);
                }
            });
        }
    }

    //Getter / Setter
    public static void setStatus(String text) {
        if (Platform.isFxApplicationThread()) {
            statusLine.setText(text);
        } else {
            Platform.runLater(new Runnable() {
                public void run() {
                    statusLine.setText(text);
                }
            });
        }
    }

    // Helper method to enable / diable the Generator menu items.
    public void setMenuItemStatus(boolean status) {
        simpleGenerator.setDisable(status);
        elementaryCellularAutomaton.setDisable(status);
        gameOfLifeAutomaton.setDisable(status);
        forestFireAutomaton.setDisable(status);
        lindenmayerGenerator.setDisable(status);
        lindenmayerStochasticGenerator.setDisable(status);
    }

    public static void setSaveMenuStatus(boolean status) {
        save.setDisable(status);
    }

    public static void setSaveMenuDisabled(boolean status) {
        save.setDisable(status);
    }

    public Canvas getCanvas() {
        return canvas;
    }


    public static void setDepth(int d) {
        depth = d;
    }

    public static void setSeed(int s) {
        seed = s;
    }

    public static void setHue(int h) {
        hue = h;
    }
}
