package eu.lilithmonodia.winestock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The WineStock class is the entry point of the application. It extends the Application class
 * and overrides the start method. It is responsible for initializing and showing the primary stage
 * with the loaded FXML scene and setting the application icon.
 */
public class WineStock extends Application {
    /**
     * The main entry point for the application.
     * <p>
     * Launches the JavaFX application by calling the launch method with the given command line arguments.
     *
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the WineStock application.
     * <p>
     * This method is called when the application is launched and is responsible for initializing the
     * main stage, loading the WineStock.fxml file, setting the scene, setting the application title,
     * configuring the application icon, and displaying the main stage.
     *
     * @param primaryStage the primary stage for the application
     * @throws IOException if the WineStock.fxml file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WineStock.class.getResource("WineStock.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("WineStock");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(appIcon);
        primaryStage.show();
    }
}