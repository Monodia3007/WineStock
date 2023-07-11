package eu.lilithmonodia.winestock;

import eu.lilithmonodia.winestock.app.BottleSize;
import eu.lilithmonodia.winestock.app.Color;
import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.database.PostgreSQLManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;

/**
 * The WineStockController class is responsible for handling user interactions with UI components.
 * It retrieves wine data from the database and feeds it to the table view.
 * It also provides user login functionality.
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
     * Handles event of Import Database button click.
     * Fetches all wine data from the database and refreshes table view.
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
     * Handles event of Login button click.
     * Connects to the PostgreSQL database using credentials provided in the text fields.
     * Enables or disables the Import Database button depending on success of the connection.
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
     * Fetches all wine data from the database and populates table view with it.
     */
    public void refresh() {
        try {
            wineTab.setItems(FXCollections.observableArrayList(postgreSQLManager.getAllWine()));
        } catch (Exception e) {
            // Possibly handle the exception with a logging system or displaying an error dialogue
        }
    }
}