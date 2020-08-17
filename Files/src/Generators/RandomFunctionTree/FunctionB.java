/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.RandomFunctionTree;

/**
 *
 * @author arm
 */
public class FunctionB extends Function {

    @Override
    public double calculate(double x, double y, double z) {
        if(x<0.5){
            return Math.abs((1 + Math.cos(x * 2 * Math.PI)) / 2 - (1 + Math.sin(x * 2 * Math.PI)) / 2);
    }
        else{
            return Math.sqrt((1 + Math.cos(x * 2 * Math.PI)) / 2 * (1 + Math.sin(x * 2 * Math.PI)) / 2);
        }
    }

    @Override
    public int numberOfParameters() {
        return 1;
    }

}
