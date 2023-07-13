package eu.lilithmonodia.winestock;

import eu.lilithmonodia.winestock.app.BottleSize;
import eu.lilithmonodia.winestock.app.Color;
import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.configuration.Configuration;
import eu.lilithmonodia.winestock.database.PostgreSQLManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;

/**
 * This class is responsible for controlling the Wine Stock application's UI.
 * It connects to a PostgreSQL database, fetches wine data, and updates the table view accordingly.
 */
public class WineStockController {

    private PostgreSQLManager postgreSQLManager;

    @FXML
    private TableView<Wine> wineTab;
    @FXML
    private TableColumn<Wine, String> wineTabName;
    @FXML
    private TableColumn<Wine, Year> wineTabYear;
    @FXML
    private TableColumn<Wine, BottleSize> wineTabVolume;
    @FXML
    private TableColumn<Wine, Color> wineTabColor;
    @FXML
    private TableColumn<Wine, Double> wineTabPrice;
    @FXML
    private TableColumn<Wine, String> wineTabComment;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button importButton;
    @FXML
    private Button loginButton;

    /**
     * Imports the database by setting cell value factories and refreshing the view.
     */
    public void importDatabase() {
        setCellValueFactories();
        refresh();
    }

    /**
     * Sets cell value factories for each column in the table view.
     */
    private void setCellValueFactories() {
        wineTabName.setCellValueFactory(new PropertyValueFactory<>("name"));
        wineTabYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        wineTabVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        wineTabColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        wineTabPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        wineTabComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    }

    /**
     * Logs in the user by establishing a connection to the PostgreSQL database using the provided username and password.
     * If the login is successful, the importButton will be enabled; otherwise, it will be disabled.
     */
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            postgreSQLManager = new PostgreSQLManager(username, password);
            postgreSQLManager.connect();
            importButton.setDisable(false);
        } catch (IOException | SQLException e) {
            importButton.setDisable(true);
        }
    }

    /**
     * Refreshes the wineTab by updating the items with data obtained from the PostgreSQL database.
     * If an exception occurs while retrieving the data, it may be handled using a logging system or by displaying an error dialogue.
     */
    public void refresh() {
        try {
            wineTab.setItems(FXCollections.observableArrayList(postgreSQLManager.getAllWine()));
        } catch (Exception e) {
            // Possibly handle the exception with a logging system or displaying an error dialogue
        }
    }
}