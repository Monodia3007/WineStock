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
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
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
    public static final String NO_WINE_SELECTED = "No wine selected.";
    // Manager for PostgreSQL Database

    private TabPane rootPane;
    private PostgreSQLManager postgreSQLManager;
    private Wine currentlySelectedWine;
    private Assortment<Wine> currentlySelectedAssortment;

    //FXML Table and Column variables for assortment and wine.
    @FXML
    private TableView<Assortment<Wine>> assortmentsTable;
    @FXML
    private TableView<Wine> wineTable;

    @FXML
    private TableColumn<Assortment<Wine>, Integer> assortmentID;
    @FXML
    private TableColumn<Assortment<Wine>, Double> assortmentTotalPrice;
    @FXML
    private TableColumn<Assortment<Wine>, Year> assortmentYear;
    @FXML
    private TableColumn<Assortment<Wine>, String> assortmentWines;

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
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button importButton;
    @FXML
    private ImageView icon;

    // FXML variables for the Wine modifying/adding/deleting UI.
    @FXML
    private TextField wineNameField;
    @FXML
    private TextField wineYearField;
    @FXML
    private TextField wineVolumeField;
    @FXML
    private TextField winePriceField;
    @FXML
    private TextField wineCommentField;
    @FXML
    private ComboBox<BottleSize> wineVolumeComboBox;
    @FXML
    private ComboBox<Color> wineColorComboBox;
    @FXML
    private Button wineAddButton;
    @FXML
    private Button wineDeleteButton;
    @FXML
    private Button wineModifyButton;

    // FXML variables for the Assortment modifying/adding/deleting UI.
    @FXML
    private TableView<Wine> assortmentWinesTable;
    @FXML
    private TableView<Wine> notAssortmentWinesTable;
    @FXML
    private Button assortmentAddButton;
    @FXML
    private Button assortmentDeleteButton;
    @FXML
    private TableColumn<Wine, Integer> assortmentWinesTableID;
    @FXML
    private TableColumn<Wine, String> assortmentWinesTableName;
    @FXML
    private TableColumn<Wine, Year> assortmentWinesTableYear;
    @FXML
    private TableColumn<Wine, BottleSize> assortmentWinesTableVolume;
    @FXML
    private TableColumn<Wine, Color> assortmentWinesTableColor;
    @FXML
    private TableColumn<Wine, Double> assortmentWinesTablePrice;
    @FXML
    private TableColumn<Wine, String> assortmentWinesTableComment;
    @FXML
    private TableColumn<Wine, Integer> notAssortmentWinesTableID;
    @FXML
    private TableColumn<Wine, String> notAssortmentWinesTableName;
    @FXML
    private TableColumn<Wine, Year> notAssortmentWinesTableYear;
    @FXML
    private TableColumn<Wine, BottleSize> notAssortmentWinesTableVolume;
    @FXML
    private TableColumn<Wine, Color> notAssortmentWinesTableColor;
    @FXML
    private TableColumn<Wine, Double> notAssortmentWinesTablePrice;
    @FXML
    private TableColumn<Wine, String> notAssortmentWinesTableComment;

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
    @FXML
    public void importDatabase() {
        LOGGER.info("Database import initiated.");

        // Set cursor to wait
        Scene scene = rootPane.getScene(); // replace rootPane with your actual root pane
        scene.setCursor(Cursor.WAIT); // loading cursor

        Task<Void> task = new Task<>() {
            @Override
            public @Nullable Void call() {
                try {
                    setCellValueFactories();
                    refresh();
                    LOGGER.info("Database successfully imported.");
                } catch (Exception e) {
                    LOGGER.error("Failed to import database.", e);
                    // Set cursor to normal
                    Platform.runLater(() -> scene.setCursor(Cursor.DEFAULT));
                    Platform.runLater(() -> showErrorDialog("Error importing database", "Failed to import database.", e));
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
        setAssortmentWinesTableCellValueFactories();
    }

    /**
     * Sets cell value factories for each TableColumn in Wine TableView.
     */
    private void setWineTableCellValueFactories() {
        setCellValueFactories(wineTableID, wineTableName, wineTableYear, wineTableVolume, wineTableColor, wineTablePrice, wineTableComment);
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
     * Sets cell value factories for each TableColumn in Assortment Wines TableView.
     */
    private void setAssortmentWinesTableCellValueFactories() {
        setCellValueFactories(assortmentWinesTableID, assortmentWinesTableName, assortmentWinesTableYear, assortmentWinesTableVolume, assortmentWinesTableColor, assortmentWinesTablePrice, assortmentWinesTableComment);
    }

    private <T> void setCellValueFactories(TableColumn<T, ?> tableID,
                                           TableColumn<T, ?> tableName,
                                           TableColumn<T, ?> tableYear,
                                           TableColumn<T, ?> tableVolume,
                                           TableColumn<T, ?> tableColor,
                                           TableColumn<T, ?> tablePrice,
                                           TableColumn<T, ?> tableComment) {
        tableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        tableVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tableColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    }

    /**
     * Attempts to log in the user by establishing a PostgreSQL database connection.
     * On successful login, the importButton is enabled; otherwise, it gets disabled.
     */
    @FXML
    public void login() {
        if (portField.getText().isEmpty()) {
            portField.setText("5432");
        }
        String url = "jdbc:postgresql://" +
                hostField.getText() +
                ":5432/winestock?sslmode=disable";
        String username = usernameField.getText();
        String password = passwordField.getText();

        Scene scene = rootPane.getScene(); // replace rootPane with your actual root pane
        scene.setCursor(Cursor.WAIT); // loading cursor

        Task<Void> task = new Task<>() {
            @Override
            public @Nullable Void call() {
                attemptLogin(url, username, password);
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
    private void attemptLogin(String url, String username, String password) {
        try {
            postgreSQLManager = new PostgreSQLManager(url ,username, password);
            postgreSQLManager.connect();
            importButton.setDisable(false);
            LOGGER.info("Successful login. Connection established.");
        } catch (SQLException e) {
            importButton.setDisable(true);
            LOGGER.error("Failed to login and establish database connection.", e);
            Platform.runLater(() -> showErrorDialog("Error logging in", "Failed to login and establish database connection.", e));
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
            notAssortmentWinesTable.setItems(FXCollections.observableArrayList(postgreSQLManager.getAllWine()));

        } catch (Exception e) {
            LOGGER.error("Failed to refresh data from the database.", e);
            Platform.runLater(() -> showErrorDialog("Error refreshing data", "Failed to refresh data from the database.", e));
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
                Platform.runLater(() -> showErrorDialog("Error closing application", "Failed to close PostgreSQLManager", e));
            }
        }
    }

    private void showErrorDialog(String title, String message,@Nullable Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        if (ex != null) {
            ex.printStackTrace(pw);
        }
        String exceptionText = sw.toString();
        Label label = new Label("The following exception occurred:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(false);

        VBox expContent = new VBox(label, textArea);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    @FXML
    public void addWine() {
        try {
            postgreSQLManager.insertWine(new Wine.Builder(
                    wineNameField.getText(),
                    Integer.parseInt(wineYearField.getText()),
                    wineVolumeComboBox.getValue().getVolume(),
                    wineColorComboBox.getValue().name(),
                    Double.parseDouble(winePriceField.getText())
            ).comment(wineCommentField.getText()).build());
            refresh();
        } catch (Exception e) {
            LOGGER.error("Failed to add wine to the database.", e);
            Platform.runLater(() -> showErrorDialog("Error adding wine", "Failed to add wine to the database.", e));
        }
    }

    @FXML
    public void deleteWine() {
        try {
            postgreSQLManager.deleteWine(this.currentlySelectedWine);
            refresh();
        } catch (Exception e) {
            LOGGER.error("Failed to delete wine from the database.", e);
            Platform.runLater(() -> showErrorDialog("Error deleting wine", "Failed to delete wine from the database.", e));
        }
    }

    @FXML
    public void modifyWine() {
        if (this.currentlySelectedWine != null) {
            try {
                this.currentlySelectedWine.setName(wineNameField.getText());
                this.currentlySelectedWine.setYear(Year.of(Integer.parseInt(wineYearField.getText())));
                this.currentlySelectedWine.setVolume(wineVolumeComboBox.getValue().getVolume());
                this.currentlySelectedWine.setColor(Color.valueOf(wineColorComboBox.getValue().name()));
                this.currentlySelectedWine.setPrice(Double.parseDouble(winePriceField.getText()));
                this.currentlySelectedWine.setComment(wineCommentField.getText());

                postgreSQLManager.updateWine(this.currentlySelectedWine);

                refresh();
            } catch (Exception e) {
                LOGGER.error("Failed to modify wine in the database.", e);
                Platform.runLater(() -> showErrorDialog("Error modifying wine", "Failed to modify wine in the database.", e));
            }
        } else {
            LOGGER.error(NO_WINE_SELECTED);
            Platform.runLater(() -> showErrorDialog("Error modifying wine", NO_WINE_SELECTED, null));
        }
    }

    @FXML
    public void loadSelectedWine() {
        Wine selectedWine = wineTable.getSelectionModel().getSelectedItem();
        if (selectedWine != null) {
            this.currentlySelectedWine = selectedWine;
            wineNameField.setText(selectedWine.getName());
            wineYearField.setText(String.valueOf(selectedWine.getYear().getValue()));
            wineVolumeComboBox.setValue(selectedWine.getVolume());
            wineColorComboBox.setValue(Color.valueOf(selectedWine.getColor().name()));
            winePriceField.setText(String.valueOf(selectedWine.getPrice()));
            wineCommentField.setText(selectedWine.getComment());
        } else {
            LOGGER.error(NO_WINE_SELECTED);
            Platform.runLater(() -> showErrorDialog("Error loading wine", NO_WINE_SELECTED, null));
        }
    }

    @FXML
    public void addAssortment() {
        try {
            postgreSQLManager.insertAssortment(new Assortment<>());
            refresh();
        } catch (Exception e) {
            LOGGER.error("Failed to add assortment to the database.", e);
            Platform.runLater(() -> showErrorDialog("Error adding assortment", "Failed to add assortment to the database.", e));
        }
    }

    @FXML
    public void deleteAssortment() {
        try {
            postgreSQLManager.deleteAssortment(this.currentlySelectedAssortment);
            refresh();
        } catch (Exception e) {
            LOGGER.error("Failed to delete assortment from the database.", e);
            Platform.runLater(() -> showErrorDialog("Error deleting assortment", "Failed to delete assortment from the database.", e));
        }
    }

    @FXML
    public void addWineToAssortment() {
        try {
            postgreSQLManager.insertWineInAssortment(
                    notAssortmentWinesTable.getSelectionModel().getSelectedItem(),
                    (long)this.currentlySelectedAssortment.getId()
            );
            refresh();
        } catch (Exception e) {
            LOGGER.error("Failed to add wine to assortment in the database.", e);
            Platform.runLater(() -> showErrorDialog("Error adding wine to assortment", "Failed to add wine to assortment in the database.", e));
        }
    }

    @FXML
    public void deleteWineFromAssortment() {
        try {
            postgreSQLManager.deleteWineInAssortment(
                    assortmentWinesTable.getSelectionModel().getSelectedItem(),
                    (long)this.currentlySelectedAssortment.getId()
            );
            refresh();
        } catch (Exception e) {
            LOGGER.error("Failed to delete wine from assortment in the database.", e);
            Platform.runLater(() -> showErrorDialog("Error deleting wine from assortment", "Failed to delete wine from assortment in the database.", e));
        }
    }

    @FXML
    public void loadSelectedAssortment() {
        Assortment<Wine> selectedAssortment = assortmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAssortment != null) {
            this.currentlySelectedAssortment = selectedAssortment;
            assortmentWinesTable.setItems(FXCollections.observableArrayList(selectedAssortment));
        } else {
            LOGGER.error("No assortment selected.");
            Platform.runLater(() -> showErrorDialog("Error loading assortment", "No assortment selected.", null));
        }
    }
}