
package MainWindow;

/**
 * Added to project for KE2 by Gundula Swidersky
 * Enum for handling of Generator status that will be set in the main window.
 */
    public enum MainStatus {
        OK, READY, CALCULATE, GENERATE, RUNNING, FINISHED;
        
        @Override
        public String toString() {
            switch(this) {
              case OK:
                  return "Status: OK.";
              case READY: 
                  return "Status: Ready.";
              case CALCULATE: 
                  return "Status: Calculating...";
              case GENERATE: 
                  return "Status: Generating...";
              case RUNNING: 
                  return "Status: Running...";
              case FINISHED: 
                  return "Status: Finished.";
              default: 
                  return " ";
            }
        }
    }
        