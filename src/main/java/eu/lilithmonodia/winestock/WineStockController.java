package eu.lilithmonodia.winestock;

// Importing the necessary libraries

import eu.lilithmonodia.winestock.app.Assortment;
import eu.lilithmonodia.winestock.app.BottleSize;
import eu.lilithmonodia.winestock.app.Color;
import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.database.PostgreSQLManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;
import java.util.Objects;

/**
 * WineStockController class controls the Wine Stock application's UI.
 * Responsible for connecting to a PostgreSQL database, fetching wine data,
 * and updating the table view as appropriate.
 */
public class WineStockController {
    // Manager for PostgreSQL Database
    private PostgreSQLManager postgreSQLManager;

    //FXML Table and Column variables for assortment and wine.
    @FXML
    private TableView<Assortment> assortmentsTable;
    @FXML
    private TableView<Wine> wineTable;

    @FXML
    private TableColumn<Assortment, Double> assortmentTotalPrice;
    @FXML
    private TableColumn<Assortment, Year> assortmentYear;
    @FXML
    private TableColumn<Assortment, String> assortmentWines;

    @FXML
    private TableColumn<Wine, String> wineTableName;
    @FXML
    private TableColumn<Wine, Year> wineTableYear;
    @FXML
    private TableColumn<Wine, BottleSize> wineTableVolume;
    @FXML
    private TableColumn<Wine, Color> wineTableColor;
    @FXML
    private TableColumn<Wine, Double> wineTablePrice;
    @FXML
    private TableColumn<Wine, String> wineTableComment;

    // FXML variables for the UI.
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button importButton;
    @FXML
    private ImageView icon;

    /**
     * Initializes the controller, setting the icon image and disabling the import button.
     */
    @FXML
    public void initialize() {
        icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
        importButton.setDisable(true);
    }

    /**
     * Imports the database by setting cell value factories and refreshing the view.
     */
    public void importDatabase() {
        setCellValueFactories();
        refresh();
    }

    /**
     * Sets cell value factories for each TableColumn in TableView.
     */
    private void setCellValueFactories() {
        setWineTableCellValueFactories();
        setAssortmentTableCellValueFactories();
    }

    /**
     * Sets cell value factories for each TableColumn in Wine TableView.
     */
    private void setWineTableCellValueFactories() {
        wineTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        wineTableYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        wineTableVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        wineTableColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        wineTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        wineTableComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    }

    /**
     * Sets cell value factories for each TableColumn in Assortment TableView.
     */
    private void setAssortmentTableCellValueFactories() {
        assortmentWines.setCellValueFactory(new PropertyValueFactory<>("wineNames"));
        assortmentYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        assortmentTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    /**
     * Attempts to log in the user by establishing a PostgreSQL database connection.
     * On successful login, the importButton is enabled; otherwise, it gets disabled.
     */
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        attemptLogin(username, password);
    }

    /**
     * This method attempts to create a connection to the database using a username and password.
     */
    private void attemptLogin(String username, String password) {
        try {
            postgreSQLManager = new PostgreSQLManager(username, password);
            postgreSQLManager.connect();
            importButton.setDisable(false);
        } catch (IOException | SQLException e) {
            importButton.setDisable(true);
        }
    }

    /**
     * Refreshes the TableView by replacing the items with data from PostgreSQL database.
     * If an exception occurs, a suitable error handling approach may be applied.
     */
    public void refresh() {
        try {
            wineTable.setItems(FXCollections.observableArrayList(postgreSQLManager.getAllWine()));
            assortmentsTable.setItems(FXCollections.observableArrayList(postgreSQLManager.getAllAssortments()));
        } catch (Exception e) {
            // Handle exception
        }
    }
}