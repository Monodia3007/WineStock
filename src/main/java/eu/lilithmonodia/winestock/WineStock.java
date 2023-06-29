package eu.lilithmonodia.winestock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WineStock extends Application {
    public static void main(String[] args) {
        launch(args);
    }

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
