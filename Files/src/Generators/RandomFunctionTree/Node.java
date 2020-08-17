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
public class Node {

    String data;
    double returnValue;
    Node left;
    Node middle;
    Node right;

    public Node(Function function) {
        left = null;
        right = null;
        middle = null;
        this.function = function;
    }
private Function function;

   
    public void createChildren(int depth) throws NumberFormatException {
        if (depth < 0) throw new NumberFormatException();
        
        left = new Node(RandomFunctionTreeGenerator.functionGenerator.getRandomFunction());
        if (depth >= 1) {
            left.createChildren(depth - 1);
        }
        
        if (function.numberOfParameters() == 2) {
            right = new Node(RandomFunctionTreeGenerator.functionGenerator.getRandomFunction());
            if (depth >= 1) {
                right.createChildren(depth - 1);
            }        
        } if (function.numberOfParameters() == 3) {
             middle = new Node(RandomFunctionTreeGenerator.functionGenerator.getRandomFunction());
            if (depth >= 1) {
                middle.createChildren(depth - 1); 
                right = new Node(RandomFunctionTreeGenerator.functionGenerator.getRandomFunction());
            if (depth >= 1) {
              //  System.out.println("create children of right node");
                right.createChildren(depth - 1);
        }
        
            }}}
    
    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public double calculate(double x, double y, double z) {
        if (left != null) {   
            //System.out.println("Calculate left child");
            x = left.calculate(x, y,z);
        }
        if (middle != null) {
            //System.out.println("Calculate right child");
            z= middle.calculate(x, y,z);
        } 
        if (right != null) {
            //System.out.println("Calculate right child");
            y = right.calculate(x, y,z);
        }
        
       // System.out.println("Calculate with func=" + function.name());
        return function.calculate(x, y,z);
    }
    
    
    public Node insert(Node root, Node newNode) {
        if (root == null) {
            root = newNode;
        } else {
            if (FunctionGenerator.getNumberofParameters(root.data) == 1) {
                if (root.left == null) {
                    root.right = null;
                    root.left = insert(root.left, newNode);
                }
            } else {
                if (root.left == null) {
                    root.left = insert(root.left, newNode);
                } else {
                    root.right = insert(root.right, newNode);
                }
            }
        }
        return root;
    }
}

