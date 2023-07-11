package eu.lilithmonodia.winestock;

/**
 * The Launcher class serves as the entry point to the WineStock JavaFX application.
 * This class is mainly used to bypass JavaFX 11 module path access restrictions.
 */
public class Launcher {
    /**
     * The main method which serves as the entry point of the application.
     * This method calls the main method of the WineStock class, effectively starting the JavaFX application.
     *
     * @param args the input command-line arguments
     */
    public static void main(String[] args) {
        WineStock.main(args);
    }
}
