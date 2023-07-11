package eu.lilithmonodia.winestock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Main application class for the WineStock application.
 * <p>
 * It extends the JavaFX Application class and serves as the entry point of the application.
 * <p>
 * Initializes the primary stage with required FXML scenes and elements and launches the application.
 */
public class WineStock extends Application {

    /**
     * Main method serving as the entry point of the application.
     *
     * @param args the input command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Overrides the start method of the Application class.
     * <p>
     * Initializes and shows the primary stage with the loaded FXML scene and sets the application icon.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene is set
     * @throws IOException if loading the FXML file fails
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WineStock.class.getResource("WineStock.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("WineStock");
        primaryStage.setScene(scene);
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(appIcon);
        primaryStage.show();
    }
}
