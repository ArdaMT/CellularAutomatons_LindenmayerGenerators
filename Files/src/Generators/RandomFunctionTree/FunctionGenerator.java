/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.RandomFunctionTree;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author arm
 */
public class FunctionGenerator {
    private final ArrayList<Function> functionList = new ArrayList<>();
    private final Random rn;

    public FunctionGenerator(int seed) {
        rn = new Random(seed);
        functionList.add(new FunctionA());
        functionList.add(new FunctionB());
        functionList.add(new FunctionC());
        functionList.add(new FunctionD());
        functionList.add(new FunctionE());
        functionList.add(new FunctionF());
       
        
    }
    
    public static int getNumberofParameters(String a){
        if(a.equals("A")||a.equals("B")||a.equals("C")){
            return 1;
        }else {
            return 2;
        }
    }
    public Function getRandomFunction() {
        return functionList.get(rn.nextInt(functionList.size()));
    }
 }
