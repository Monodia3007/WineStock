package eu.lilithmonodia.winestock;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static javafx.application.Platform.exit;

/**
 * The WineStock class is the entry point of the application. It extends the Application class
 * and overrides the start method. It is responsible for initialising and showing the primary stage
 * with the loaded FXML scene and setting the application icon.
 */
public class WineStock extends Application {
    private static final Logger LOGGER = LogManager.getLogger(WineStock.class);

    /**
     * Entry point of the application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        try {
            if (isOperatingSystemMac()) {
                setDockIcon();
            }
        } catch (Exception e) {
            LOGGER.error("Error setting dock icon", e);
        } finally {
            launch(args);
        }
    }

    /**
     * Checks if the operating system is Mac.
     *
     * @return true if the operating system is Mac, false otherwise.
     */
    private static boolean isOperatingSystemMac() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac");
    }

    /**
     * Sets the application's dock icon on supported platforms.
     * <p>
     * This method attempts to set the application's dock icon by reading the "icon.png" file from the
     * application's resource folder. If the icon file is found, it will be used to set the dock icon.
     * If the platform doesn't support setting the dock icon, this method does nothing.
     *
     * @throws IOException If the "icon.png" file is not found or can't be read.
     */
    private static void setDockIcon() throws IOException {
        InputStream stream = WineStock.class.getResourceAsStream("icon.png");
        if (stream != null) {
            BufferedImage image = ImageIO.read(stream);
            if (Taskbar.isTaskbarSupported()) {
                Taskbar.getTaskbar().setIconImage(image);
            }
        } else {
            throw new IOException("Icon file not found");
        }
    }

    /**
     * Starts the application by initialising the primary stage and loading the WineStock.fxml
     * file.
     * <p>
     * The primary stage is then set with the loaded scene and the application title is set
     * as "WineStock".
     * The primary stage is made non-resizable.
     * Finally, the application icon is set
     * and the primary stage is shown.
     *
     * @param primaryStage the primary stage of the application
     * @throws IOException if an I/O error occurs while loading the FXML file
     */
    @Override
    public void start(@NotNull Stage primaryStage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(WineStock.class.getResource("WineStock.fxml"));
        TabPane rootPane = fxmlLoader.load();
        Scene scene = new Scene(rootPane);
        primaryStage.setTitle("WineStock");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(appIcon);
        primaryStage.show();
        WineStockController controller = fxmlLoader.getController();
        controller.setRootPane(rootPane);
        primaryStage.setOnCloseRequest(windowEvent -> {
            ((WineStockController) fxmlLoader.getController()).closeApplication();
            exit();
        });
    }
}