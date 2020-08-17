/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.RandomFunctionTree;

import java.util.Random;

/**
 *
 * @author arm
 */
public class FunctionD extends Function {

    @Override
    public double calculate(double x, double y,double z ) {
                int b = new Random(2).nextInt();

 if (b == 0) {
            return Math.sqrt((1 + Math.sin(x * 2 * Math.PI)) / 2 -(1 + Math.sin(y * 2 * Math.PI)) / 2);
        } else {
            return Math.sqrt((1 + Math.cos(x * 2 * Math.PI)) / 2 - (1 + Math.cos(y * 2 * Math.PI)) / 2);
        }    }

    @Override
    public int numberOfParameters() {
        return 2;
    }


}
