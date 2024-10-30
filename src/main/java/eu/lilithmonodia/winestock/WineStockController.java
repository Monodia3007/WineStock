package eu.lilithmonodia.winestock;

// Importing the necessary libraries

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Styles;
import eu.lilithmonodia.winestock.data.Assortment;
import eu.lilithmonodia.winestock.data.BottleSize;
import eu.lilithmonodia.winestock.data.Color;
import eu.lilithmonodia.winestock.data.Wine;
import eu.lilithmonodia.winestock.database.PostgreSQLManager;
import eu.lilithmonodia.winestock.exceptions.Errors;
import eu.lilithmonodia.winestock.exceptions.InvalidYearException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Year;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * WineStockController class controls the Wine Stock application's UI.
 * Responsible for connecting to a PostgreSQL database, fetching wine data,
 * and updating the table view as appropriate.
 */
public class WineStockController {
    private static final Logger LOGGER = LogManager.getLogger(WineStockController.class);
    // Manager for PostgreSQL Database

    @Setter
    @FXML
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
    @FXML
    private Button loginButton;

    /**
     * Initialises the controller, setting the icon image and disabling the import button.
     */
    @FXML
    public void initialize() {
        icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
        importButton.setDisable(true);
        initializeComboBoxes();
        assortmentYearTextField.setText(String.valueOf(Year.now().getValue()));
    }

    private void initializeComboBoxes() {
        wineVolumeComboBox.setItems(FXCollections.observableArrayList(BottleSize.values()));
        wineVolumeComboBox.setValue(BottleSize.BOUTEILLE);
        wineColorComboBox.setItems(FXCollections.observableArrayList(Color.values()));
        wineColorComboBox.setValue(Color.ROUGE);
    }

    /**
     * Imports the database by setting cell value factories and refreshing the view.
     * <p>
     * This method initialises a new Task to perform the database import asynchronously,
     * logging the progress and handling any exceptions that may occur. The import process
     * includes setting the cell value factories and refreshing the view. Upon successful
     * completion, the method logs that the database has been successfully imported.
     * If an exception is thrown during the import process, the method logs an error
     * message and sets the cursor back to its default state.
     */
    @FXML
    public void importDatabase() {
        importButton.getStyleClass().removeAll(Styles.DANGER, Styles.SUCCESS);
        LOGGER.info("Database import initiated.");
        rootPane.getScene().setCursor(Cursor.WAIT);
        try {
            setCellValueFactories();
            refresh();
            LOGGER.info("Database successfully imported.");
            importButton.getStyleClass().add(Styles.SUCCESS);
        } catch (Exception e) {
            handleError(Errors.ERROR_IMPORTING_DATA, Errors.FAILED_TO_IMPORT_DATA_FROM_THE_DATABASE, e);
            importButton.getStyleClass().add(Styles.DANGER);
        } finally {
            rootPane.getScene().setCursor(Cursor.DEFAULT);
        }
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
     * @param tableColor   The TableColumn representing the colour column.
     * @param tablePrice   The TableColumn representing the price column.
     * @param tableComment The TableColumn representing the comment column.
     * @param <T>          The type of objects contained in the TableView.
     */
    private <T> void setCellValueFactories(@NotNull TableColumn<T, ?> tableID,
                                           @NotNull TableColumn<T, ?> tableName,
                                           @NotNull TableColumn<T, ?> tableYear,
                                           @NotNull TableColumn<T, ?> tableVolume,
                                           @NotNull TableColumn<T, ?> tableColor,
                                           @NotNull TableColumn<T, ?> tablePrice,
                                           @NotNull TableColumn<T, ?> tableComment) {
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
        Task<Void> loginTask = new Task<>() {
            /**
             * Executes the method call.
             * <p>
             * This method is an overridden implementation of the call() method in the
             * javafx.concurrent.Task class. It performs the following actions:
             *    1. Clears previous styles of the text fields in the UI.
             *    2. Retrieve the values from the hostField, usernameField, and passwordField.
             *    3. Validates the fields by checking if there are any errors while logging in.
             *    4. Attempts to establish a connection using the provided host, username, and password.
             *
             * @return The method doesn't return anything.
             */
            @Override
            protected @Nullable Void call() {
                List<TextField> fields = Arrays.asList(hostField, usernameField, passwordField);

                Platform.runLater(() -> clearPreviousStyles(fields)); // UI update

                String host = hostField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();

                if (validateFields(fields, Errors.ERROR_LOGGING_IN, host, username, password))
                    return null;

                attemptToEstablishConnection(fields, host, username, password);
                return null;
            }
        };

        new Thread(loginTask).start();
    }

    /**
     * Validates the specified fields by checking if any of them are empty.
     * If any field is empty, it displays the specified error message and highlights the fields.
     *
     * @param fields       The list of text fields to validate.
     * @param errorMessage The error message to display if any field is empty.
     * @param host         The value of the host field.
     * @param username     The value of the username field.
     * @param password     The value of the password field.
     * @return true if any field is empty, false otherwise.
     */
    private boolean validateFields(List<TextField> fields, Errors errorMessage, @NotNull String host, String username, String password) {
        if (host.isEmpty() || username.isEmpty() || password.isEmpty()) {
            handleErrorAndShow(fields, errorMessage, null);
            return true;
        }
        return false;
    }

    /**
     * Attempts to establish a database connection using the specified host, username, and password.
     * <p>
     * Retrieves the port using the getPort() method and constructs the URL for the PostgreSQL database.
     * <p>
     * Sets the cursor to the waiting state while establishing the connection.
     * <p>
     * If the connection is successful, call the connectToDatabaseAndReflectSuccess() method.
     * If an SQLException occurs during the connection attempt,
     * call the handleErrorAndShow() method with the specified error message and the exception.
     * <p>
     * Sets the cursor back to the default state after the connection attempt.
     *
     * @param fields   The list of text fields to use for displaying error messages and highlight fields.
     * @param host     The value of the host field.
     * @param username The value of the username field.
     * @param password The value of the password field.
     */
    private void attemptToEstablishConnection(List<TextField> fields, String host, String username, String password) {
        String url = "jdbc:postgresql://" + host + ":" + getPort() + "/winestock?sslmode=disable";
        setCursorToWait();

        try {
            connectToDatabaseAndReflectSuccess(fields, url, username, password);
        } catch (SQLException e) {
            handleErrorAndShow(fields, Errors.ERROR_LOGGING_IN, e);
            importButton.setDisable(true);
            importButton.getStyleClass().removeAll(Styles.DANGER, Styles.SUCCESS);
            postgreSQLManager = null;
        } finally {
            setCursorToDefault();
        }
    }

    /**
     * Connects to the database using the specified URL, username, and password and reflects the success of the login.
     * <p>
     * Creates a new instance of the PostgreSQLManager class with the specified URL, username, and password.
     * Call the connect() method of the PostgreSQLManager instance to establish the database connection.
     * Enables the importButton.
     * Log the successful login message to the LOGGER.
     * Adds the SUCCESS style class to the loginButton.
     * Calls the setSuccessfulStyles() method to apply successful styles to the specified fields.
     *
     * @param fields   The list of text fields to set successful styles.
     * @param url      The URL of the database.
     * @param username The username for the database connection.
     * @param password The password for the database connection.
     * @throws SQLException If an error occurs while connecting to the database.
     */
    private void connectToDatabaseAndReflectSuccess(List<TextField> fields, String url, String username, String password) throws SQLException {
        postgreSQLManager = new PostgreSQLManager(url, username, password);
        postgreSQLManager.connect();
        importButton.setDisable(false);
        LOGGER.info("Successful login. Connection established.");
        loginButton.getStyleClass().add(Styles.SUCCESS);
        setSuccessfulStyles(fields);
    }

    /**
     * Handles an error
     * and shows it by applying the danger styles to the specified fields
     * and adding the DANGER style class to the loginButton.
     * <p>
     * Calls the handleErrorAndShowStyles()
     * method to apply the danger styles and show the error message for the specified fields and exception.
     * Adds the DANGER style class to the loginButton.
     *
     * @param fields       The list of text fields to apply danger styles and show the error message.
     * @param errorMessage The error message to show.
     * @param e            The SQLException that occurred.
     */
    private void handleErrorAndShow(List<TextField> fields, Errors errorMessage, SQLException e) {
        handleErrorAndShowStyles(fields, errorMessage, e);
        loginButton.getStyleClass().add(Styles.DANGER);
    }

    /**
     * Handles an error
     * and shows it by applying the danger styles to the specified fields.
     * <p>
     * Calls the handleError() method to handle the error with the specified title, message, and exception.
     * Iterates over the list of text fields and applies the danger styles to each field.
     * Removes the success styles from each field.
     *
     * @param fields The list of text fields to apply danger styles.
     * @param title  The title of the error.
     * @param e      The SQLException that occurred.
     */
    private void handleErrorAndShowStyles(@NotNull List<TextField> fields, Errors title, SQLException e) {
        handleError(title, title, e);
        fields.forEach(field -> {
            field.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            field.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        });
    }

    /**
     * Sets the successful styles to the specified fields.
     * <p>
     * Iterates over the list of text fields and applies the success styles to each field.
     * Removes the danger styles from each field.
     *
     * @param fields The list of text fields to apply success styles.
     */
    private void setSuccessfulStyles(@NotNull List<TextField> fields) {
        fields.forEach(field -> {
            field.pseudoClassStateChanged(Styles.STATE_DANGER, false);
            field.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
        });
    }

    /**
     * Clears the previous styles applied to the specified fields.
     * <p>
     * Removes the danger and success styles from the login button and each field in the list.
     *
     * @param fields The list of text fields to clear previous styles.
     */
    private void clearPreviousStyles(@NotNull List<TextField> fields) {
        loginButton.getStyleClass().removeAll(Styles.DANGER, Styles.SUCCESS);
        fields.forEach(field -> field.pseudoClassStateChanged(Styles.STATE_DANGER, false));
    }

    /**
     * Sets the cursor to the wait cursor.
     *
     * <p>
     * This method sets the cursor of the scene to the wait cursor
     * to indicate that the program is currently busy and the user
     * should wait for further action.
     * </p>
     */
    private void setCursorToWait() {
        rootPane.getScene().setCursor(Cursor.WAIT);
    }

    /**
     * Sets the cursor of the root pane's scene to the default cursor.
     *
     * <p>
     * This method sets the cursor of the scene associated with the root pane
     * to the default cursor. The default cursor is the standard cursor that
     * is used in the user interface to indicate that no specific cursor style
     * is applied. This method can be used to reset the cursor after a custom
     * cursor has been set.
     * </p>
     */
    private void setCursorToDefault() {
        rootPane.getScene().setCursor(Cursor.DEFAULT);
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
     * Refreshes the TableView by replacing the items with data from the PostgreSQL database.
     * If an exception occurs, a suitable error handling approach may be applied.
     */
    @FXML
    public void refresh() {
        // Creating a new Task for database refresh operation.
        Task<Void> refreshTask = new Task<>() {
            /**
             * This method is an overridden implementation of the call() method from the Task class.
             * It is used to refresh data in the UI.
             *
             * @return null
             */
            @Override
            protected @Nullable Void call() {
                try {
                    setCursorToWait();
                    if (postgreSQLManager == null) {
                        Platform.runLater(() -> handleError(Errors.ERROR_REFRESHING_DATA, Errors.FAILED_TO_REFRESH_DATA_FROM_THE_DATABASE, null));
                        return null;
                    }

                    Platform.runLater(() -> {
                        try {
                            wineTable.setItems(FXCollections.observableArrayList(postgreSQLManager.getAllWine()));
                            assortmentsTable.setItems(FXCollections.observableArrayList(postgreSQLManager.getAllAssortments()));
                            notAssortmentWinesTable.setItems(FXCollections.observableArrayList(postgreSQLManager.getAllWine()));
                        } catch (SQLException e) {
                            Platform.runLater(() -> handleError(Errors.ERROR_REFRESHING_DATA, Errors.FAILED_TO_REFRESH_DATA_FROM_THE_DATABASE, e));
                        }
                    });

                    // Since refresh() is a UI related operation, it must be run on a JavaFX thread.
                    Platform.runLater(() -> {
                        wineTable.refresh();
                        assortmentsTable.refresh();
                        notAssortmentWinesTable.refresh();
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> handleError(Errors.ERROR_REFRESHING_DATA, Errors.FAILED_TO_REFRESH_DATA_FROM_THE_DATABASE, e));
                }
                setCursorToDefault();
                return null;
            }
        };

        // Starting the task on a new thread.
        new Thread(refreshTask).start();
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
                handleError(Errors.ERROR_CLOSING, Errors.FAILED_TO_CLOSE_POSTGRESQL_CONNECTION, e);
            }
        }
    }

    /**
     * Shows an error dialogue with a specific title and message.
     * If an exception object is provided, it will also display the exception stack trace.
     *
     * @param title   the title of the error dialogue
     * @param message the message to be displayed in the error dialogue
     * @param ex      the exception object (nullable) to display its stack trace
     */
    private void showErrorDialog(@NotNull Errors title, @NotNull Errors message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title.getValue());
        alert.setHeaderText(message.getValue());
        if (ex != null) {
            alert.setContentText(ex.toString());
        }
        alert.showAndWait();
    }

    /**
     * Adds a new wine to the database.
     * <p>
     * It first constructs a Wine object using the provided inputs (name, year, volume, colour, price, and comment),
     * then calls the insertWine method of the PostgreSQLManager class to insert the wine into the database.
     * If the insertion is successful, it updates the currentlySelectedWine variable to the newly added wine,
     * refreshes the wine table, and loads the selected wine.
     * If the insertion fails,
     * it logs an error message and displays an error dialogue with the corresponding error message.
     * If an exception occurs during the process, it logs an error message with the exception stack trace and displays
     * an error dialogue showing the error message and the exception stack trace.
     */
    @FXML
    public void addWine() {
        try {
            Wine wine = createWineFromFields();
            if (postgreSQLManager.insertWine(wine).isEmpty()) {
                handleError(Errors.ERROR_ADDING_WINE, Errors.FAILED_TO_ADD_WINE_TO_THE_DATABASE, null);
                return;
            }
            this.currentlySelectedWine = wine;
        } catch (SQLException | IllegalArgumentException e) {
            handleError(Errors.ERROR_ADDING_WINE, Errors.FAILED_TO_ADD_WINE_TO_THE_DATABASE, e);
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
     * Deletes the currently selected wine from the database.
     * <p>
     * It calls the deleteWine method of the PostgreSQLManager class,
     * passing the currentlySelectedWine as the parameter,
     * to delete the wine from the database.
     * After deletion, it refreshes the wine table and sets the currentlySelectedWine
     * to null.
     * If an exception occurs during the deletion process, it logs an error message with the exception stack trace
     * and displays an error dialogue showing the error message and the exception stack trace.
     */
    @FXML
    public void deleteWine() {
        try {
            if (this.currentlySelectedWine == null) {
                handleError(Errors.ERROR_DELETING_WINE, Errors.FAILED_TO_DELETE_WINE_FROM_THE_DATABASE, null);
                return;
            }
            postgreSQLManager.deleteWine(this.currentlySelectedWine);
        } catch (SQLException e) {
            handleError(Errors.ERROR_DELETING_WINE, Errors.FAILED_TO_DELETE_WINE_FROM_THE_DATABASE, e);
            return;
        }

        refresh();
        currentlySelectedWine = null;
        resetWineFields();
    }

    /**
     * Modifies the currently selected wine in the database.
     * <p>
     * If there is a wine currently selected, it updates the wine's name, year, volume, colour, price, and comment
     * based on the values entered in the corresponding UI fields.
     * It then calls the updateWine method of the PostgreSQLManager class,
     * passing the currentlySelectedWine as the parameter,
     * to update the wine in the database.
     * After the update, it refreshes the wine table to reflect the changes.
     * If an exception occurs during the modification process, it logs an error message with the exception stack trace
     * and displays an error dialogue showing the error message and the exception stack trace.
     * <p>
     * If no wine is currently selected,
     * it logs an error message and displays an error dialogue stating that no wine is selected.
     */
    @FXML
    public void modifyWine() {
        if (this.currentlySelectedWine == null) {
            handleError(Errors.NO_WINE_SELECTED, Errors.ERROR_MODIFYING_WINE, null);
            return;
        }

        try {
            updateSelectedWine();
            postgreSQLManager.updateWine(this.currentlySelectedWine);
        } catch (SQLException | InvalidYearException e) {
            handleError(Errors.ERROR_MODIFYING_WINE, Errors.FAILED_TO_MODIFY_WINE_IN_THE_DATABASE, e);
            return;
        }

        refresh();
    }

    private void handleError(@NotNull Errors title, @NotNull Errors message, Exception e) {
        Platform.runLater(() -> showErrorDialog(title, message, e));
        LOGGER.error(title.getValue());
    }

    /**
     * Updates the currently selected wine with the values entered the UI fields.
     *
     * @throws InvalidYearException If the entered year is invalid.
     */
    private void updateSelectedWine() throws InvalidYearException {
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
     * If a wine is selected, it sets the currentlySelectedWine variable to the selected wine
     * and populates the UI fields with the wine's name, year, volume, colour, price, and comment.
     * <p>
     * If no wine is selected, it logs an error message and displays an error dialogue stating that no wine is selected.
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
            handleError(Errors.ERROR_LOADING_WINE, Errors.NO_WINE_SELECTED, null);
        }
    }

    /**
     * Adds an assortment to the database and updates the UI.
     * <p>
     * Retrieves the year from the assortmentYearTextField and validates it.
     * If the year is empty or not in the format "yyyy", it logs an error message and displays an error dialogue.
     * <p>
     * Inserts the assortment into the database using the postgreSQLManager.
     * If the insertion is unsuccessful, it logs an error message and displays an error dialogue.
     * <p>
     * Retrieves the newly added assortment from the assortmentsTable and sets it as the currently selected assortment.
     * <p>
     * Refreshes the UI to reflect the changes made in the database.
     * <p>
     * Calls the loadSelectedAssortment() method to load the selected assortment into the UI fields.
     * <p>
     * If an exception occurs during the process, it logs an error message and displays an error dialogue.
     */
    @FXML
    public void addAssortment() {
        try {
            String yearText = assortmentYearTextField.getText();
            if (yearText.isEmpty()) {
                handleError(Errors.YEAR_FIELD_IS_EMPTY, Errors.ERROR_ADDING_ASSORTMENT, null);
                return;
            }

            Assortment<Wine> assortment = new Assortment<>(Year.parse(yearText));
            Optional<Long> id = postgreSQLManager.insertAssortment(assortment);
            if (id.isEmpty()) {
                handleError(Errors.ERROR_ADDING_ASSORTMENT, Errors.FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE, null);
                return;
            }

            this.currentlySelectedAssortment = assortment;

        } catch (DateTimeParseException e) {
            handleError(Errors.INVALID_YEAR_FORMAT, Errors.ERROR_ADDING_ASSORTMENT, e);
        } catch (Exception e) {
            handleError(Errors.ERROR_ADDING_ASSORTMENT, Errors.FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE, e);
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
     * If an exception occurs during the process, it logs an error message and displays an error dialogue.
     */
    @FXML
    public void deleteAssortment() {
        try {
            postgreSQLManager.deleteAssortment(this.currentlySelectedAssortment);

        } catch (Exception e) {
            handleError(Errors.ERROR_DELETING_ASSORTMENT, Errors.FAILED_TO_DELETE_ASSORTMENT, e);
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
     * If an exception occurs during the process, it logs an error message and displays an error dialogue.
     */
    @FXML
    public void addWineToAssortment() {
        Wine selectedWine = notAssortmentWinesTable.getSelectionModel().getSelectedItem();
        if (selectedWine == null || this.currentlySelectedAssortment == null) {
            handleError(Errors.ERROR_ADDING_WINE_TO_ASSORTMENT, Errors.NO_WINE_OR_ASSORTMENT_SELECTED, null);
            return;
        }

        if (!this.currentlySelectedAssortment.getYear().equals(selectedWine.getYear())) {
            handleError(Errors.ERROR_ADDING_WINE_TO_ASSORTMENT, Errors.WINE_YEAR_DOES_NOT_MATCH, null);
            return;
        }

        try {
            postgreSQLManager.insertWineInAssortment(selectedWine, (long) this.currentlySelectedAssortment.getId());
        } catch (SQLException e) {
            handleError(Errors.ERROR_ADDING_WINE_TO_ASSORTMENT, Errors.FAILED_TO_ADD_WINE_TO_ASSORTMENT, e);
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
     * If an exception occurs during the process, it logs an error message and displays an error dialogue.
     */
    @FXML
    public void deleteWineFromAssortment() {
        try {
            if (this.currentlySelectedAssortment == null) {
                handleError(Errors.ERROR_DELETING_WINE_FROM_ASSORTMENT, Errors.FAILED_TO_DELETE_WINE_FROM_ASSORTMENT, null);
                return;
            } else if (assortmentWinesTable.getSelectionModel().getSelectedItem() == null) {
                handleError(Errors.ERROR_DELETING_WINE_FROM_ASSORTMENT, Errors.NO_WINE_SELECTED, null);
                return;
            }
            postgreSQLManager.deleteWineInAssortment(assortmentWinesTable.getSelectionModel().getSelectedItem());

        } catch (SQLException e) {
            handleError(Errors.ERROR_DELETING_WINE_FROM_ASSORTMENT, Errors.FAILED_TO_DELETE_WINE_FROM_ASSORTMENT, e);
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
     * If no assortment is selected, logs an error message and displays an error dialogue.
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
            handleError(Errors.ERROR_LOADING_ASSORTMENT, Errors.NO_ASSORTMENT_SELECTED, null);
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
        wineVolumeComboBox.setValue(BottleSize.BOUTEILLE);
        wineColorComboBox.setValue(Color.ROUGE);
        winePriceField.setText("");
        wineCommentField.setText("");
    }

    /**
     * Opens a link to the WineStock GitHub repository.
     * <p>
     * This method checks if the Desktop class is supported and the BROWSE action is supported
     * by the desktop environment. If supported, it opens the WineStock GitHub repository in the
     * default web browser.
     * <p>
     * If the Desktop class or the BROWSE action is not supported, it calls the handleError method
     * with the appropriate error codes and messages.
     * <p>
     * If an IO exception or a URI syntax exception occurs during the browsing process, it calls
     * the handleError method with the appropriate error codes and messages, and the exception that occurred.
     */
    @FXML
    public void openLink() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URL("https://github.com/Monodia3007/WineStock").toURI());
            } else {
                handleError(Errors.ERROR_OPENING_LINK, Errors.FAILED_TO_OPEN_LINK, null);
            }
        } catch (IOException | URISyntaxException e) {
            handleError(Errors.ERROR_OPENING_LINK, Errors.FAILED_TO_OPEN_LINK, e);
        }
    }

    /**
     * Toggles the application theme.
     * <p>
     * This method displays a confirmation dialogue where the user can choose between a dark theme
     * and a light theme. If the user selects the dark theme, the application theme is set to
     * the PrimerDark theme. If the user selects the light theme, the application theme is set to
     * the PrimerLight theme.
     */
    @FXML
    public void toggleTheme() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Changement de thème");
        alert.setHeaderText(null);
        alert.setContentText("Choisissez le thème que vous souhaitez utiliser.");

        ButtonType darkButton = new ButtonType("Thème sombre");
        ButtonType lightButton = new ButtonType("Thème clair");

        alert.getButtonTypes().setAll(darkButton, lightButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == darkButton) {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        } else if (result.isPresent() && result.get() == lightButton) {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        }
    }
}