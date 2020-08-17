/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CellMachines;

/**
 *
 * @author arm
 * 
 * this class contains constants that are double dimensional arrays of shape coordinates.
 */
public class GameOfLifeShapes {

    private static final int[][] GLIDER;
    private static final int[][] SMALLEXPL;
    private static final int[][] EXPLODER;
    private static final int[][] CELL10;
    private static final int[][] FISH;
    private static final int[][] PUMP;
    private static final int[][] SHOOTER;
    // Shapes and their respective initial coordinates in two dimensional int arrays
    static {
        GLIDER = new int[][]{{1, 0}, {2, 1}, {2, 2}, {1, 2}, {0, 2}};
        SMALLEXPL = new int[][]{{0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 3}, {2, 1}, {2, 2}};
        EXPLODER = new int[][]{{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {2, 0}, {2, 4}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}};
        CELL10 = new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {8, 0}, {9, 0}};
        FISH = new int[][]{{0, 1}, {0, 3}, {1, 0}, {2, 0}, {3, 0}, {3, 3}, {4, 0}, {4, 1}, {4, 2}};
        PUMP = new int[][]{{0, 3}, {0, 4}, {0, 5}, {1, 0}, {1, 1}, {1, 5}, {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}, {5, 0}, {5, 1}, {5, 5}, {6, 3}, {6, 4}, {6, 5}};
        SHOOTER = new int[][]{{0, 2}, {0, 3}, {1, 2}, {1, 3}, {8, 3}, {8, 4}, {9, 2}, {9, 4}, {10, 2}, {10, 3}, {16, 4}, {16, 5}, {16, 6}, {17, 4}, {18, 5}, {22, 1}, {22, 2}, {23, 0}, {23, 2}, {24, 0}, {24, 1}, {24, 12}, {24, 13}, {25, 12}, {25, 14}, {26, 12}, {34, 0}, {34, 1}, {35, 0}, {35, 1}, {35, 7}, {35, 8}, {35, 9}, {36, 7}, {37, 8}};
    }

    public static int[][] getShape(String name) {
        if (name.equals("Small exploder")) {
            return SMALLEXPL;
        }
        if (name.equals("Exploder")) {
            return EXPLODER;
        }
        if (name.equals("Cell 10")) {
            return CELL10;
        }
        if (name.equals("Glider")) {
            return GLIDER;
        }
        if (name.equals("Fish")) {
            return FISH;
        }
        if (name.equals("Pump")) {
            return PUMP;
        }
        if (name.equals("Shooter")) {
            return SHOOTER;
        }
        return null;
    }
}
