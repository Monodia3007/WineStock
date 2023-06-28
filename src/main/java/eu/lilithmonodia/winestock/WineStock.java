package eu.lilithmonodia.winestock;

import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.database.PostgreSQLManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class WineStock extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        PostgreSQLManager manager = new PostgreSQLManager();
        List<Wine> wines = manager.getAllWine();
        System.out.println(wines);
    }
}
