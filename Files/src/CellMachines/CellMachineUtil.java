
package CellMachines;

import java.util.Random;

/**
 * Added to project for KE2 by Gundula Swidersky
 * Common utilities used by Cell Machines.
 */
public class CellMachineUtil {

    // Helper Method that returns Integer between provided.
    // lower and upper bound.
    public int getRandomNumber(int lowerNumber, int upperNumber) {
        Random random = new Random();
        return random.nextInt(upperNumber - lowerNumber + 1) + lowerNumber;
    }
    
    // Initialize an already created matrix.
    public int[][] initMatrix(int[][] actMatrix) {
        // Init cell matrix
        for (int i=0; i < actMatrix.length; i++) {
            for (int j=0; j < actMatrix[0].length; j++) {
                actMatrix[i][j] = 0;
            }
        }
        return actMatrix;
    }    
    
}
