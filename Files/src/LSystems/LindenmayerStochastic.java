/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LSystems;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Stack;

/**
 *Similar to the Lindenmayer.java class this class implements the Lindenmayer algorithm. So, the user gets to
 * define an alphabet, an axiom that initiates the whole procedure and creates rules that are equations with the alphabet elements
 * on both sides of an equation. however,here, with the addition of numerical weights the rules look a bit different.
 * they also produce different shapes and figures
 * @author arm
 */
public class LindenmayerStochastic extends LSystemsParent {


    public int recursionDepth;
    public String strokeType;
    public int strokeWidth;
    public String stochasticRulesString;
    // the "&" sign will be defined as "do nothing".
    public static final String CONSTANTS = new String("&+-[]");

    public static final String VARIABLES = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public double[] probabilityArray;
    public String[] rulesArray;




    public boolean isAlphabetValid(String alphabet, String var, String con) {
        boolean iscorrect = true;
        for (int i = 0; i < alphabet.length(); i++) {
            if (!var.contains("" + alphabet.charAt(i)) && !con.contains(("" + alphabet.charAt(i)))) {
                return iscorrect = false;
            }
        }
        return iscorrect;
    }

    //Should be a single capital letter.
    @Override
    public boolean isAxiomValid(String axiom, String al) {
        String a = "&[]";
        boolean iscorrect = true;
        if (axiom.length() > 1) {
            return iscorrect = false;
        }
        for (int i = 0; i < axiom.length(); i++) {
            if (!al.contains("" + axiom.charAt(i))) {
                return iscorrect = false;
            } else if (a.contains("" + axiom.charAt(i))) {
                return iscorrect = false;
            }
        }
        return iscorrect;
    }
    //looks at the syntax of the rule string and compares it to the alphabet.
    @Override
    public boolean isRuleValid(String sr, String alphabet) {
                System.out.println( " rule string: "+ sr);
        int a = 0;
        int numberofrules = 0;
        double ProbabilitySum = 0;
        boolean iscorrect = true;
        String specialsigns = ".,=()0123456789";
        double currentProbability = 0;
        int left = 0;
        int right = 0;
        for (int i = 0; i < sr.length(); i++) {
            if (sr.charAt(i) == '(') {
                numberofrules += 1;
            }
        }
        probabilityArray = new double[numberofrules];
        rulesArray = new String[numberofrules];
        for (int i = 0; i < sr.length(); i++) {
            if (!alphabet.contains(sr.charAt(i) + "") && !specialsigns.contains(sr.charAt(i) + "")) {
                return iscorrect = false;
            }
            if (sr.charAt(i) == '(') {
                left = i;
            }
             
            if (sr.charAt(i) == ')') {
                right = i;
                try{
                    if(sr.substring(left + 1, right).length()>4){
                        return iscorrect=false;
                    }
                currentProbability = Double.parseDouble(sr.substring(left + 1, right));
                }catch (NumberFormatException e){
                    return iscorrect=false;
                }
                //given probability is a valid double value
                if (currentProbability > 0 && currentProbability < 1.0) {
                    ProbabilitySum += currentProbability;
                } else {
                    return iscorrect = false;
                }
            }

            if (sr.charAt(i) == ',') {
                probabilityArray[a] = currentProbability;
                rulesArray[a] = sr.substring(right+2, i );
            a+=1;
            } 
            if (i == sr.length() - 1) {
                rulesArray[a] = sr.substring(right+2, i + 1);
                probabilityArray[a] = currentProbability;
            a+=1;
            }
        }
        // the sum of all the probabilities should be 1.0 
        if (Math.abs(1.0-ProbabilitySum) > 0.001) {
            return iscorrect = false;
        }
       
            System.out.println();
            System.out.println();
            System.out.println();
        return iscorrect;
    }
    
    @Override
    public String buildString(){
        String tempString="";
        int rulenumber=-1;
        double a=0;
        Random rand = new Random();
        double b= rand.nextDouble();
        //calculate the weighted probability        
        for (int i=0;i<probabilityArray.length;i++){
            if (b<a+probabilityArray[i]){
                rulenumber=i;
                break;
            }
            else{
                a+=probabilityArray[i];
            }
    }

        if (recursionDepth != 0) {
            for (int i = 0; i < currentString.length(); i++) {
                if (axiom.equals("" + currentString.charAt(i))) {  //the current char element should be substituted by the rule.
                    tempString += rulesArray[rulenumber] ;

                } else {
                    tempString += currentString.charAt(i);
                }
            }
            currentString = tempString;
            recursionDepth -= 1;
            System.out.println(" pro :" + probabilityArray[rulenumber]+ " rule "+ rulesArray[rulenumber]);
            System.out.println(" String: " + currentString);
            buildString();
        }
        return currentString;
                
    }
    @Override
    public void displayGeneration(Canvas canvas, String stringStructure, double x, double y) {
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
                    if (VARIABLES.contains(stringStructure.charAt(i) + "")) { //if the current char is an element of VARIABLES, that means draw a line
                        gc.setFill(Color.BLACK);
                        endX = stX + (length * Math.cos(Math.toRadians(d)));
                        endY = stY + (length * Math.sin(Math.toRadians(d)));
                        gc.setLineWidth(strokeWidth);
                        if (strokeType.equals("Line")){
                        gc.strokeLine(stX, stY, endX, endY);
                        }
                        if (strokeType.equals("Oval")){
                            gc.strokeOval(stX, stY, length, length);
                        }
                        if(strokeType.equals("Rectangle")){
                            gc.strokeRect(stX, stY, length, length);
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
