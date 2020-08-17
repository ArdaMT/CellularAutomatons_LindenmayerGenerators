/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LSystems;

/**
 *This class implements the Lindenmayer approach for the generation of images. Often these images resebmble the depiction of trees or plants.
 *  user is allowed to create their own Alphabet using a given number of characters and constants. They then go on the  create rules using the alphabet
 * they have just defined. The algorithm translates these rules into lines that are connected in a certain manner and create shapes and figures.
 * @author arm
 */
// www.jjam.de - Lindenmayer System - Version 21.03.2003

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;



public class Lindenmayer extends LSystemsParent{
    //only the chars in the following string are allowed as constants
    public static final String CONSTANTS = new String("XY+-[]");
    //Only the capital letters of the english alphabet except X and Y are used while creating production rules for images.
    public static final String VARIABLES = new String("ABCDEFGHIJKLMNOPQRSTUVWZ");
    public int maxRecursionDepth = 10;

    public double compressingFactor =0.0;

    public HashMap<String, String> productionRulesHash = new HashMap<String, String>();
    public double d = 0;

    public double endX = 0;
    public double endY = 0;

    //Check out the production rules. they are seperated by a comma if there is more than one rule.
    public void setProductionRules(String p) {
        p=p.replace(" ", "");
        if (!p.contains(",")) {
            productionRulesHash.put(p.substring(0, 1), p.substring(2, p.length()));
        } else {
            int equalsign = 0;
            String key = "";
            for (int i = 0; i < p.length(); i++) {
                if (p.charAt(i) == '=') {
                    equalsign = i;
                    key = "" + p.charAt(i - 1);
                } else if (p.charAt(i) == ',') {
                    productionRulesHash.put(key, p.substring(equalsign + 1, i));
                    key = "";
                    equalsign = 0;
                } else if (i == p.length() - 1) {
                    productionRulesHash.put(key, p.substring(equalsign + 1, i + 1));
                    key = "";
                    equalsign = 0;
                }
            }
        }
    }

    //Build  a String recursively using the axiom as a starting sequence and 
    //the production rules for substitution. Return it as soon as the recursionDepth is reached.
    @Override
    public String buildString() {
        String tempString = "";
        if (recursionDepth != 0) {
            for (int i = 0; i < currentString.length(); i++) {
                if (productionRulesHash.containsKey("" + currentString.charAt(i))) {  //the current char element should be substituted by the rule.
                    tempString += productionRulesHash.get("" + currentString.charAt(i));

                } else {
                    tempString += currentString.charAt(i);
                }
            }
            currentString = tempString;

            recursionDepth -= 1;
            buildString();
        }
        return currentString;
    }

    //check if the String al contains any invalid char. if yes, return false.
    public boolean isAlphabetValid(String al) {
        boolean isCorrect = true;
        for (int i = 0; i < al.length(); i++) {
            if (!VARIABLES.contains("" + al.charAt(i)) && !CONSTANTS.contains("" + al.charAt(i))) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    //check if Chars of ax consist only of Chars of al. if not, return false.
    @Override
    public boolean isAxiomValid(String ax, String al) {
        boolean isCorrect = true;
        for (int i = 0; i < ax.length(); i++) {
            if (!al.contains("" + ax.charAt(i))) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;

    }

    //check if pr consist only of chars of al. if not return false.
    @Override
    public boolean isRuleValid(String pr, String al) {
        boolean isCorrect = true;
        if (!pr.contains("=")) {
            return isCorrect == false;
        }
        pr = pr.replace("=", "");
        pr = pr.replace(",", "");
        for (int i = 0; i < pr.length(); i++) {
            if (!al.contains("" + pr.charAt(i))) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    //This method calculates a factor which is then multiplied with length 
    //in order to fit the image on the canvas.
    public void setCompressingFactor(Canvas canvas, String stringStructure,double tempLength, double x, double y) {
        double d = 270;
        double stX=x;
        double stY=y;
        double height;
        double oldheight;
        double width;
        double oldwidth;
        minX = canvas.getWidth() / 2;
        maxX = canvas.getWidth() / 2;
        minY = canvas.getHeight() / 2;
        maxY = canvas.getHeight() / 2;
        Stack stack = new Stack();

        for (int i = 0; i < stringStructure.length(); i++) {
            //find the extreme points in the east, west, north and south for the given stringStructure
            if (VARIABLES.contains(stringStructure.charAt(i) + "")) {
                endX = stX + (length * Math.cos(Math.toRadians(d)));
                endY = stY + (length * Math.sin(Math.toRadians(d)));
                if (Math.min(stX, endX) < minX) {
                    minX = Math.min(stX, endX);
                }
                if (Math.max(stX, endX) > maxX) {
                    maxX = Math.max(stX, endX);
                }
                if (Math.min(stY, endY) < minY) {
                    minY = Math.min(stY, endY);
                }
                if (Math.max(stY, endY) > maxY) {
                    maxY = Math.max(stY, endY);
                }
                stX = endX;
                stY = endY;
            } else if (stringStructure.charAt(i) == '+') {
                d -= angle;
            } //turn right
            else if (stringStructure.charAt(i) == '-') {
                d += angle;
            } else if (stringStructure.charAt(i) == '[') {
                String a = stX + "A" + stY + "B" + d;
                stack.add(a);
            } else if (stringStructure.charAt(i) == ']') {
                String a = (String) stack.peek();
                stX = Double.parseDouble(a.substring(0, a.indexOf("A")));
                stY = Double.parseDouble(a.substring(a.indexOf("A") + 1, a.indexOf("B")));
                d = Double.parseDouble(a.substring(a.indexOf("B") + 1, a.length()));
                stack.pop();
            }
        }
        
    }
    @Override
        public void displayGeneration(Canvas canvas, String stringStructure,double x, double y) {

            Platform.runLater(new Runnable() {
                public void run() {
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    Stack stack = new Stack();

                    double d = 270;
                    double stX = x;
                    double stY = y;
                    double endX = 0;
                    double endY = 0;

                    for (int i = 0; i < stringStructure.length(); i++) {
                        //if the current char is an element of VARIABLES, that means draw a line
                        if (VARIABLES.contains(stringStructure.charAt(i) + "")) {
                            gc.setFill(Color.BLACK);
                            endX = stX + (length * Math.cos(Math.toRadians(d)));
                            endY = stY + (length * Math.sin(Math.toRadians(d)));
                            gc.setLineWidth(1);
                            gc.strokeLine(stX, stY, endX, endY);
                            stX = endX;
                            stY = endY;
                        } else if (stringStructure.charAt(i) == '+') {
                            d -= angle;
                        } //turn right
                        else if (stringStructure.charAt(i) == '-') {
                            d += angle;
                        } else if (stringStructure.charAt(i) == '[') {
                            String a = stX + "A" + stY + "B" + d;
                            stack.add(a);
                        } else if (stringStructure.charAt(i) == ']') {
                            try {
                                String a = (String) stack.peek();
                                stX = Double.parseDouble(a.substring(0, a.indexOf("A")));
                                stY = Double.parseDouble(a.substring(a.indexOf("A") + 1, a.indexOf("B")));
                                d = Double.parseDouble(a.substring(a.indexOf("B") + 1, a.length()));
                                stack.pop();
                            }catch(EmptyStackException e){
                                JOptionPane.showMessageDialog(null, "Please check your use of the square brackets in the rules." +
                                        "\nThe help window next to this input field offers information on that.");
                                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                                canvas.setHeight(0);
                                canvas.setWidth(0);
                                break;
                            }
                        }
                    }
                    canvas.toFront();
                }
            });
        }

}
