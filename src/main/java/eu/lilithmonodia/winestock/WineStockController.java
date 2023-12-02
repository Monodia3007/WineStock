package eu.lilithmonodia.winestock;

// Importing the necessary libraries

import eu.lilithmonodia.winestock.data.Assortment;
import eu.lilithmonodia.winestock.data.BottleSize;
import eu.lilithmonodia.winestock.data.Color;
import eu.lilithmonodia.winestock.data.Wine;
import eu.lilithmonodia.winestock.database.PostgreSQLManager;
import eu.lilithmonodia.winestock.exceptions.InvalidYearException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.Year;
import java.time.chrono.IsoChronology;
import java.util.Objects;
import java.util.Optional;

/**
 * WineStockController class controls the Wine Stock application's UI.
 * Responsible for connecting to a PostgreSQL database, fetching wine data,
 * and updating the table view as appropriate.
 */
public class WineStockController {
    private static final Logger LOGGER = LogManager.getLogger(WineStockController.class);
    private static final String NO_WINE_SELECTED = "No wine selected.";
    private static final String FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE = "Failed to add assortment to the database.";
    private static final String FAILED_TO_ADD_WINE_TO_THE_DATABASE = "Failed to add wine to the database.";
    private static final String ERROR_ADDING_ASSORTMENT = "Error adding assortment";
    private static final String ERROR_ADDING_WINE_TO_ASSORTMENT = "Error adding wine to assortment";
    private static final String NO_ASSORTMENT_SELECTED = "No assortment selected.";
    private static final String ERROR_ADDING_WINE = "Error adding wine";
    // Manager for PostgreSQL Database

    private TabPane rootPane;
    private PostgreSQLManager postgreSQLManager;
    private Wine currentlySelectedWine;
    private Assortment<Wine> currentlySelectedAssortment;

    // FXML Tab variables for wines and assortments.
    @FXML
    private TabPane wineTabPane;
    @FXML
    private TabPane assortmentTabPane;
    @FXML
    private Tab wineModifyTab;
    @FXML
    private Tab assortmentModifyTab;

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
    private TextField winePriceField;
    @FXML
    private TextField wineCommentField;
    @FXML
    private ComboBox<BottleSize> wineVolumeComboBox;
    @FXML
    private ComboBox<Color> wineColorComboBox;

    // FXML variables for the Assortment modifying/adding/deleting UI.
    @FXML
    private TextField assortmentYearTextField;
    @FXML
    private TableView<Wine> assortmentWinesTable;
    @FXML
    private TableView<Wine> notAssortmentWinesTable;
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
        wineVolumeComboBox.setItems(FXCollections.observableArrayList(BottleSize.values()));
        wineVolumeComboBox.setValue(BottleSize.BOUTEILLE);
        wineColorComboBox.setItems(FXCollections.observableArrayList(Color.values()));
        wineColorComboBox.setValue(Color.ROUGE);
        assortmentYearTextField.setText(String.valueOf(Year.now().getValue()));
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
        Scene scene = rootPane.getScene();
        scene.setCursor(Cursor.WAIT);
        try {
            setCellValueFactories();
            refresh();
            LOGGER.info("Database successfully imported.");
        } catch (Exception e) {
            LOGGER.error("Failed to import database.", e);
            scene.setCursor(Cursor.DEFAULT);
            showErrorDialog("Error importing database", "Failed to import database.", e);
        }
        scene.setCursor(Cursor.DEFAULT);
    }

    /**
     * Sets cell value factories for each TableColumn in TableView.
     */
    private void setCellValueFactories() {
        setCellValueFactories(assortmentWinesTableID,
                assortmentWinesTableName,
                assortmentWinesTableYear,
                assortmentWinesTableVolume,
                assortmentWinesTableColor,
                assortmentWinesTablePrice,
                assortmentWinesTableComment);
        setCellValueFactories(wineTableID,
                wineTableName,
                wineTableYear,
                wineTableVolume,
                wineTableColor,
                wineTablePrice,
                wineTableComment);
        setCellValueFactories(notAssortmentWinesTableID,
                notAssortmentWinesTableName,
                notAssortmentWinesTableYear,
                notAssortmentWinesTableVolume,
                notAssortmentWinesTableColor,
                notAssortmentWinesTablePrice,
                notAssortmentWinesTableComment);
        setAssortmentTableCellValueFactories();
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
     * Sets cell value factories for each TableColumn in a TableView.
     *
     * @param tableID      The TableColumn representing the ID column.
     * @param tableName    The TableColumn representing the name column.
     * @param tableYear    The TableColumn representing the year column.
     * @param tableVolume  The TableColumn representing the volume column.
     * @param tableColor   The TableColumn representing the color column.
     * @param tablePrice   The TableColumn representing the price column.
     * @param tableComment The TableColumn representing the comment column.
     * @param <T>          The type of objects contained in the TableView.
     */
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
        String url = "jdbc:postgresql://" + hostField.getText() + ":" + getPort() + "/winestock?sslmode=disable";
        Scene scene = rootPane.getScene();
        scene.setCursor(Cursor.WAIT);
        try {
            postgreSQLManager = new PostgreSQLManager(url, usernameField.getText(), passwordField.getText());
            postgreSQLManager.connect();
            importButton.setDisable(false);
            LOGGER.info("Successful login. Connection established.");
        } catch (SQLException e) {
            LOGGER.error("Failed to login and establish database connection.", e);
            showErrorDialog("Error logging in", "Failed to login and establish database connection.", e);
        } finally {
            scene.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Retrieves the port number from the portField text field, which represents the desired
     * port to establish a PostgreSQL database connection with. If the portField is empty,
     * the default port number 5432 will be returned.
     *
     * @return The port number to be used for the database connection.
     */
    private String getPort() {
        String port = portField.getText();
        return port.isEmpty() ? "5432" : port;
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
        } catch (SQLException e) {
            LOGGER.error("Failed to refresh data from the database.", e);
            showErrorDialog("Error refreshing data", "Failed to refresh data from the database.", e);
        }
    }

    /**
     * Closes the application by closing the connection to the PostgreSQL database.
     * If an exception occurs while closing the connection, a suitable error handling approach may be applied.
     */
    public void closeApplication() {
        if (postgreSQLManager != null) {
            try {
                this.postgreSQLManager.close();
            } catch (SQLException e) {
                LOGGER.error("Failed to close PostgreSQLManager", e);
                showErrorDialog("Error closing application", "Failed to close PostgreSQLManager", e);
            }
        }
    }

    /**
     * Shows an error dialog with a specific title and message.
     * If an exception object is provided, it will also display the exception stack trace.
     *
     * @param title   the title of the error dialog
     * @param message the message to be displayed in the error dialog
     * @param ex      the exception object (nullable) to display its stack trace
     */
    private void showErrorDialog(String title, String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        if (ex != null) {
            alert.setContentText(ex.toString());
        }
        alert.showAndWait();
    }

    /**
     * Adds a new wine to the database.
     * <p>
     * It first constructs a Wine object using the provided inputs (name, year, volume, color, price, and comment),
     * then calls the insertWine method of the PostgreSQLManager class to insert the wine into the database.
     * If the insertion is successful, it updates the currentlySelectedWine variable to the newly added wine,
     * refreshes the wine table, and loads the selected wine.
     * If the insertion fails,
     * it logs an error message and displays an error dialog with the corresponding error message.
     * If an exception occurs during the process, it logs an error message with the exception stack trace and displays
     * an error dialog showing the error message and the exception stack trace.
     */
    @FXML
    public void addWine() {
        try {
            Wine wine = createWineFromFields();
            if (postgreSQLManager.insertWine(wine).isEmpty()) {
                handleWineInsertionError();
                return;
            }
            this.currentlySelectedWine = wine;
        } catch (SQLException | IllegalArgumentException e) {
            handleWineInsertionError(e);
            return;
        }
        refresh();
        loadSelectedWine();
    }

    /**
     * Creates a new Wine object using the provided input fields.
     *
     * @return The constructed Wine object.
     */
    private Wine createWineFromFields() {
        String yearFieldText = wineYearField.getText();
        int year = yearFieldText.isEmpty() ? 0 : Integer.parseInt(yearFieldText);

        return new Wine.Builder(
                wineNameField.getText(),
                year,
                wineVolumeComboBox.getValue().getVolume(),
                wineColorComboBox.getValue().name(),
                Double.parseDouble(winePriceField.getText())
        ).comment(wineCommentField.getText()).build();
    }

    /**
     * Handles an error that occurs during wine insertion.
     * Logs an error message and displays an error dialog to the user.
     */
    private void handleWineInsertionError() {
        LOGGER.error(FAILED_TO_ADD_WINE_TO_THE_DATABASE);
        Platform.runLater(() -> showErrorDialog(ERROR_ADDING_WINE, FAILED_TO_ADD_WINE_TO_THE_DATABASE, null));
    }

    /**
     * Handles an error that occurs during wine insertion.
     * Logs an error message and displays an error dialog to the user.
     *
     * @param e the Exception that occurred during wine insertion
     */
    private void handleWineInsertionError(Exception e) {
        LOGGER.error(FAILED_TO_ADD_WINE_TO_THE_DATABASE, e);
        Platform.runLater(() -> showErrorDialog(ERROR_ADDING_WINE, FAILED_TO_ADD_WINE_TO_THE_DATABASE, e));
    }

    /**
     * Deletes the currently selected wine from the database.
     * <p>
     * It calls the deleteWine method of the PostgreSQLManager class,
     * passing the currentlySelectedWine as the parameter,
     * to delete the wine from the database.
     * After deletion, it refreshes the wine table and sets the currentlySelectedWine
     * to null.
     * If an exception occurs during the deletion process, it logs an error message with the exception stack trace
     * and displays an error dialog showing the error message and the exception stack trace.
     */
    @FXML
    public void deleteWine() {
        try {
            if (this.currentlySelectedWine == null) {
                LOGGER.error(NO_WINE_SELECTED);
                Platform.runLater(() -> showErrorDialog("Error deleting wine", NO_WINE_SELECTED, null));
                return;
            }
            postgreSQLManager.deleteWine(this.currentlySelectedWine);
        } catch (SQLException e) {
            LOGGER.error("Failed to delete wine from the database.", e);
            Platform.runLater(() -> showErrorDialog("Error deleting wine", "Failed to delete wine from the database.", e));
            return;
        }

        refresh();
        currentlySelectedWine = null;
        resetWineFields();
    }

    /**
     * Modifies the currently selected wine in the database.
     * <p>
     * If there is a wine currently selected, it updates the wine's name, year, volume, color, price, and comment
     * based on the values entered in the corresponding UI fields.
     * It then calls the updateWine method of the PostgreSQLManager class,
     * passing the currentlySelectedWine as the parameter,
     * to update the wine in the database.
     * After the update, it refreshes the wine table to reflect the changes.
     * If an exception occurs during the modification process, it logs an error message with the exception stack trace
     * and displays an error dialog showing the error message and the exception stack trace.
     * <p>
     * If no wine is currently selected,
     * it logs an error message and displays an error dialog stating that no wine is selected.
     */
    @FXML
    public void modifyWine() {
        if (this.currentlySelectedWine == null) {
            LOGGER.error(NO_WINE_SELECTED);
            Platform.runLater(() -> showErrorDialog("Error modifying wine", NO_WINE_SELECTED, null));
            return;
        }

        try {
            updateSelectedWine();
            postgreSQLManager.updateWine(this.currentlySelectedWine);
        } catch (SQLException | InvalidYearException e) {
            LOGGER.error("Failed to modify wine in the database.", e);
            Platform.runLater(() -> showErrorDialog("Error modifying wine", "Failed to modify wine in the database.", e));
            return;
        }

        refresh();
    }

    /**
     * Updates the currently selected wine with the values entered in the UI fields.
     *
     * @throws InvalidYearException If the entered year is invalid.
     */
    private void updateSelectedWine() throws InvalidYearException{
        this.currentlySelectedWine.setName(wineNameField.getText());
        this.currentlySelectedWine.setYear(Year.of(Integer.parseInt(wineYearField.getText())));
        this.currentlySelectedWine.setVolume(wineVolumeComboBox.getValue().getVolume());
        this.currentlySelectedWine.setColor(Color.valueOf(wineColorComboBox.getValue().name()));
        this.currentlySelectedWine.setPrice(Double.parseDouble(winePriceField.getText()));
        this.currentlySelectedWine.setComment(wineCommentField.getText());
    }

    /**
     * Loads the selected wine into the UI fields.
     * <p>
     * Retrieves the currently selected wine from the wine table.
     * If a wine is selected, it sets the currentlySelectedWine variable to the selected wine,
     * and populates the UI fields with the wine's name, year, volume, color, price, and comment.
     * <p>
     * If no wine is selected, it logs an error message and displays an error dialog stating that no wine is selected.
     */
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
            this.wineTabPane.getSelectionModel().select(wineModifyTab);
        } else {
            LOGGER.error(NO_WINE_SELECTED);
            Platform.runLater(() -> showErrorDialog("Error loading wine", NO_WINE_SELECTED, null));
        }
    }

    /**
     * Adds an assortment to the database and updates the UI.
     * <p>
     * Retrieves the year from the assortmentYearTextField and validates it.
     * If the year is empty or not in the format "yyyy", it logs an error message and displays an error dialog.
     * <p>
     * Inserts the assortment into the database using the postgreSQLManager.
     * If the insertion is unsuccessful, it logs an error message and displays an error dialog.
     * <p>
     * Retrieves the newly added assortment from the assortmentsTable and sets it as the currently selected assortment.
     * <p>
     * Refreshes the UI to reflect the changes made in the database.
     * <p>
     * Calls the loadSelectedAssortment() method to load the selected assortment into the UI fields.
     * <p>
     * If an exception occurs during the process, it logs an error message and displays an error dialog.
     */
    @FXML
    public void addAssortment() {
        try {
            String yearText = assortmentYearTextField.getText();
            if (yearText.isEmpty() || !yearText.matches("\\d{4}")) {
                LOGGER.error("Year field is empty.");
                Platform.runLater(() -> showErrorDialog(ERROR_ADDING_ASSORTMENT, "Year field is empty.", null));
                return;
            }
            Assortment<Wine> assortment = new Assortment<>(Year.parse(yearText));
            Optional<Long> id = postgreSQLManager.insertAssortment(assortment);
            if (id.isEmpty()) {
                LOGGER.error(FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE);
                Platform.runLater(() -> showErrorDialog(ERROR_ADDING_ASSORTMENT, FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE, null));
                return;
            }

            this.currentlySelectedAssortment = assortment;
        } catch (Exception e) {
            LOGGER.error(FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE, e);
            Platform.runLater(() -> showErrorDialog(ERROR_ADDING_ASSORTMENT, FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE, e));
        }

        refresh();
        loadSelectedAssortment();
    }

    /**
     * Deletes the currently selected assortment from the database and updates the UI.
     * <p>
     * Calls the deleteAssortment()
     * method of postgreSQLManager to delete the currently selected assortment from the database.
     * <p>
     * Refreshes the UI to reflect the changes made in the database.
     * <p>
     * Sets the currentlySelectedAssortment to null.
     * <p>
     * If an exception occurs during the process, it logs an error message and displays an error dialog.
     */
    @FXML
    public void deleteAssortment() {
        try {
            postgreSQLManager.deleteAssortment(this.currentlySelectedAssortment);

        } catch (Exception e) {
            LOGGER.error("Failed to delete assortment from the database.", e);
            Platform.runLater(() -> showErrorDialog("Error deleting assortment", "Failed to delete assortment from the database.", e));
            return;
        }

        refresh();
        currentlySelectedAssortment = null;
        resetAssortmentFields();
    }

    /**
     * Adds the selected wine to the currently selected assortment in the database and updates the UI.
     * <p>
     * Calls the insertWineInAssortment()
     * method of postgreSQLManager to add the selected wine to the currently selected assortment in the database.
     * <p>
     * Retrieves the currently selected item from the notAssortmentWinesTable.
     * <p>
     * Retrieves the id of the currently selected assortment and uses it to insert the wine.
     * <p>
     * Refreshes the UI to reflect the changes made in the database.
     * <p>
     * If an exception occurs during the process, it logs an error message and displays an error dialog.
     */
    @FXML
    public void addWineToAssortment() {
        Wine selectedWine = notAssortmentWinesTable.getSelectionModel().getSelectedItem();
        if (selectedWine == null || this.currentlySelectedAssortment == null) {
            LOGGER.error("No wine or assortment selected.");
            Platform.runLater(() -> showErrorDialog(ERROR_ADDING_WINE_TO_ASSORTMENT, "No wine or assortment selected.", null));
            return;
        }

        if (!this.currentlySelectedAssortment.getYear().equals(selectedWine.getYear())) {
            LOGGER.error("Wine year does not match assortment year.");
            Platform.runLater(() -> showErrorDialog(ERROR_ADDING_WINE_TO_ASSORTMENT, "Wine year does not match assortment year.", null));
            return;
        }

        try {
            postgreSQLManager.insertWineInAssortment(selectedWine, (long) this.currentlySelectedAssortment.getId());
        } catch (SQLException e) {
            LOGGER.error("Failed to add wine to assortment in the database.", e);
            Platform.runLater(() -> showErrorDialog(ERROR_ADDING_WINE_TO_ASSORTMENT, "Failed to add wine to assortment in the database.", e));
            return;
        }

        currentlySelectedAssortment.add(selectedWine);
        assortmentWinesTable.setItems(FXCollections.observableArrayList(this.currentlySelectedAssortment));
        refresh();
    }

    /**
     * Deletes the selected wine from the currently selected assortment in the database and updates the UI.
     * <p>
     * Calls the deleteWineInAssortment() method of postgreSQLManager to delete the selected wine
     * from the currently selected assortment in the database.
     * <p>
     * Retrieves the currently selected item from the assortmentWinesTable.
     * <p>
     * Retrieves the id of the currently selected assortment and uses it to delete the wine.
     * <p>
     * Refreshes the UI to reflect the changes made in the database.
     * <p>
     * If an exception occurs during the process, it logs an error message and displays an error dialog.
     */
    @FXML
    public void deleteWineFromAssortment() {
        try {
            postgreSQLManager.deleteWineInAssortment(assortmentWinesTable.getSelectionModel().getSelectedItem());

        } catch (SQLException e) {
            LOGGER.error("Failed to delete wine from assortment in the database.", e);
            Platform.runLater(() -> showErrorDialog("Error deleting wine from assortment", "Failed to delete wine from assortment in the database.", e));
            return;
        }

        currentlySelectedAssortment.remove(assortmentWinesTable.getSelectionModel().getSelectedItem());
        assortmentWinesTable.setItems(FXCollections.observableArrayList(this.currentlySelectedAssortment));
        refresh();
    }

    /**
     * Loads the selected assortment into the UI.
     * <p>
     * Retrieves the currently selected assortment from the assortmentsTable.
     * <p>
     * If a selected assortment exists, set the currentlySelectedAssortment variable to the selected assortment.
     * <p>
     * Sets the items of assortmentWinesTable to an observableArrayList containing the selected assortment.
     * <p>
     * If no assortment is selected, logs an error message and displays an error dialog.
     */
    @FXML
    public void loadSelectedAssortment() {
        Assortment<Wine> selectedAssortment = assortmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAssortment != null) {
            this.currentlySelectedAssortment = selectedAssortment;
            assortmentWinesTable.setItems(FXCollections.observableArrayList(selectedAssortment));
            this.assortmentYearTextField.setText(String.valueOf(selectedAssortment.getYear()));
            this.assortmentTabPane.getSelectionModel().select(assortmentModifyTab);
        } else {
            LOGGER.error(NO_ASSORTMENT_SELECTED);
            Platform.runLater(() -> showErrorDialog("Error loading assortment", NO_ASSORTMENT_SELECTED, null));
        }
    }

    /**
     * Resets the fields related to the assortment.
     * <p>
     * Sets the text in the assortmentYearTextField to the current year obtained from IsoChronology.
     * <p>
     * Clears the items in the assortmentWinesTable by setting it to an empty observableArrayList.
     */
    public void resetAssortmentFields() {
        assortmentYearTextField.setText(String.valueOf(IsoChronology.INSTANCE.dateNow().getYear()));
        assortmentWinesTable.setItems(FXCollections.observableArrayList());
    }

    /**
     * Resets the fields related to a wine.
     * <p>
     * Clears the text in the wineNameField, wineYearField, wineVolumeComboBox,
     * wineColorComboBox, winePriceField, and wineCommentField.
     * <p>
     * Sets the wineVolumeComboBox and wineColorComboBox to null values.
     */
    public void resetWineFields() {
        wineNameField.setText("");
        wineYearField.setText("");
        wineVolumeComboBox.setValue(null);
        wineColorComboBox.setValue(null);
        winePriceField.setText("");
        wineCommentField.setText("");
    }
}