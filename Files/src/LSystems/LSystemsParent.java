package LSystems;

import CellMachines.CellMachineUtil;
import javafx.scene.canvas.Canvas;

public abstract class LSystemsParent extends CellMachineUtil {

    public int canvasWidth;
    public int canvasHeight;
    public double angle;
    public int recursionDepth;
    public double length;
    public double minX;
    public double maxX;
    public double minY;
    public double maxY;
    public String alphabet;
    public String productionRules;
    public String axiom;
    public String chosenLSystem;
    public String currentString;
    public double startX;
    public double startY;

    public void initiateString(String a) {
        this.axiom = a;
        currentString = axiom;
    }
    public abstract String buildString();
    public abstract boolean isRuleValid(String pr, String al);
    public abstract boolean isAxiomValid(String ax, String al);
    public abstract void displayGeneration(Canvas canvas, String stringStructure, double x, double y);
}
