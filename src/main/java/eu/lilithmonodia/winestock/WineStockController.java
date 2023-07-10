package eu.lilithmonodia.winestock;

import eu.lilithmonodia.winestock.app.BottleSize;
import eu.lilithmonodia.winestock.app.Color;
import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.database.PostgreSQLManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Wine stock controller.
 */
public class WineStockController {
    private final List<Wine> wineList = new ArrayList<>();
    /**
     * The Username.
     */
    String username;
    /**
     * The Password.
     */
    String password;
    /**
     * The Postgre sql manager.
     */
    PostgreSQLManager postgreSQLManager;
    /**
     * The Wine tab.
     */
    @FXML
    TableView<Wine> wineTab;
    /**
     * The Wine tab name.
     */
    @FXML
    TableColumn<Wine, String> wineTabName;
    /**
     * The Wine tab year.
     */
    @FXML
    TableColumn<Wine, Year> wineTabYear;
    /**
     * The Wine tab volume.
     */
    @FXML
    TableColumn<Wine, BottleSize> wineTabVolume;
    /**
     * The Wine tab color.
     */
    @FXML
    TableColumn<Wine, Color> wineTabColor;
    /**
     * The Wine tab price.
     */
    @FXML
    TableColumn<Wine, Double> wineTabPrice;
    /**
     * The Wine tab comment.
     */
    @FXML
    TableColumn<Wine, String> wineTabComment;
    /**
     * The Username field.
     */
    @FXML
    TextField usernameField;
    /**
     * The Password field.
     */
    @FXML
    PasswordField passwordField;
    /**
     * The Import button.
     */
    @FXML
    Button importButton;
    /**
     * The Login button.
     */
    @FXML
    Button loginButton;

    /**
     * Import database.
     */
    public void importDatabase() {
        wineTabName.setCellValueFactory(new PropertyValueFactory<>("name"));
        wineTabYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        wineTabVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        wineTabColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        wineTabPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        wineTabComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        refresh();
    }

    /**
     * Login.
     *
     * @throws IOException the io exception
     */
    public void login() throws IOException {
        this.username = usernameField.getText();
        this.password = passwordField.getText();
        postgreSQLManager = new PostgreSQLManager(usernameField.getText(), passwordField.getText());
        try {
            postgreSQLManager.connect();
            this.importButton.setDisable(false);
        } catch (SQLException e) {
            this.importButton.setDisable(true);
        }
    }

    /**
     * Refresh.
     */
    public void refresh() {
        wineList.clear();
        wineList.addAll(postgreSQLManager.getAllWine());
        wineTab.getItems().clear();
        wineTab.getItems().addAll(wineList);
    }
}
