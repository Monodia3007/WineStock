package eu.lilithmonodia.winestock;

// Importing the necessary libraries

import eu.lilithmonodia.winestock.data.Assortment;
import eu.lilithmonodia.winestock.data.BottleSize;
import eu.lilithmonodia.winestock.data.Color;
import eu.lilithmonodia.winestock.data.Wine;
import eu.lilithmonodia.winestock.database.PostgreSQLManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOGGER = LogManager.getLogger(WineStockController.class);
    // Manager for PostgreSQL Database

    private TabPane rootPane;
    private PostgreSQLManager postgreSQLManager;
    //FXML Table and Column variables for assortment and wine.
    @FXML
    private TableView<Assortment> assortmentsTable;
    @FXML
    private TableView<Wine> wineTable;

    @FXML
    private TableColumn<Assortment, Integer> assortmentID;
    @FXML
    private TableColumn<Assortment, Double> assortmentTotalPrice;
    @FXML
    private TableColumn<Assortment, Year> assortmentYear;
    @FXML
    private TableColumn<Assortment, String> assortmentWines;

    @FXML
    private TableColumn<Wine, Integer> wineTableID;
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
     * Sets the root pane of the application.
     *
     * @param rootPane the root pane of the application
     */
    public void setRootPane(TabPane rootPane) {
        this.rootPane = rootPane;
    }

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
     * <p>
     * This method initializes a new Task to perform the database import asynchronously,
     * logging the progress and handling any exceptions that may occur. The import process
     * includes setting the cell value factories and refreshing the view. Upon successful
     * completion, the method logs that the database has been successfully imported.
     * If an exception is thrown during the import process, the method logs an error
     * message and sets the cursor back to its default state.
     */
    public void importDatabase() {
        LOGGER.info("Database import initiated.");

        // Set cursor to wait
        Scene scene = rootPane.getScene(); // replace rootPane with your actual root pane
        scene.setCursor(Cursor.WAIT); // loading cursor

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    setCellValueFactories();
                    refresh();
                    LOGGER.info("Database successfully imported.");
                } catch (Exception e) {
                    LOGGER.error("Failed to import database.", e);
                    // Set cursor to normal
                    Platform.runLater(() -> scene.setCursor(Cursor.DEFAULT));
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> scene.setCursor(Cursor.DEFAULT));

        new Thread(task).start();
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
        wineTableID.setCellValueFactory(new PropertyValueFactory<>("id"));
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
        assortmentID.setCellValueFactory(new PropertyValueFactory<>("id"));
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

        Scene scene = rootPane.getScene(); // replace rootPane with your actual root pane
        scene.setCursor(Cursor.WAIT); // loading cursor

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                attemptLogin(username, password);
                return null;
            }
        };

        task.setOnSucceeded(event -> scene.setCursor(Cursor.DEFAULT));

        task.setOnFailed(event -> scene.setCursor(Cursor.DEFAULT));

        new Thread(task).start();
    }

    /**
     * This method attempts to connect to a PostgreSQL database using the provided username and password.
     * If the login is successful, the importButton will be enabled.
     * If the login fails, the importButton will be disabled and an error message will be logged.
     *
     * @param username the username for logging in to the database
     * @param password the password for logging in to the database
     */
    private void attemptLogin(String username, String password) {
        try {
            postgreSQLManager = new PostgreSQLManager(username, password);
            postgreSQLManager.connect();
            importButton.setDisable(false);
            LOGGER.info("Successful login. Connection established.");
        } catch (IOException | SQLException e) {
            importButton.setDisable(true);
            LOGGER.error("Failed to login and establish database connection.", e);
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
            LOGGER.error("Failed to refresh data from the database.", e);
        }
    }

    /**
     * Closes the application by closing the connection to the PostgreSQL database.
     * If an exception occurs while closing the connection, a suitable error handling approach may be applied.
     */
    public void closeApplication() {
        if (postgreSQLManager != null) {
            try {
                // Call the method in your PostgreSQLManager class to close the database connection
                this.postgreSQLManager.close();
            } catch (SQLException e) {
                LOGGER.error("Failed to close PostgreSQLManager", e); // Handle exception appropriately
            }
        }
    }
    //TODO add an interface for the user to add/modify/delete wines and assortments and a interface
    // to search for wines and assortments
}