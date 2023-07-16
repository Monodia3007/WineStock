package eu.lilithmonodia.winestock;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The WineStock class is the entry point of the application. It extends the Application class
 * and overrides the start method. It is responsible for initializing and showing the primary stage
 * with the loaded FXML scene and setting the application icon.
 */
public class WineStock extends Application {
    private static final Logger LOGGER = Logger.getLogger(WineStock.class.getName());
    public static void main(String[] args) {
        try {
            if (isOperatingSystemMac()) {
                setDockIcon();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception while setting the Dock Icon", e);
        } finally {
            launch(args);
        }
    }

    private static boolean isOperatingSystemMac() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac");
    }

    private static void setDockIcon() throws IOException {
        InputStream stream = WineStock.class.getResourceAsStream("icon.png");
        if (stream != null) {
            BufferedImage image = ImageIO.read(stream);
            if(java.awt.Taskbar.isTaskbarSupported()){
                java.awt.Taskbar.getTaskbar().setIconImage(image);
            }
        } else {
            throw new IOException("Icon file not found");
        }
    }

    @Override
    public void start(@NotNull Stage primaryStage) throws IOException {
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