package eu.lilithmonodia.winestock.database;

import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the PostgreSQL database connection and operations for the winestock application.<br/>
 * It provides functionality to connect to the PostgreSQL database, retrieve wines information, and insert new wine items.<br/>
 * The database credentials can be provided via configuration or directly through the constructor.<br/>
 */
public class PostgreSQLManager {
    private final String url;
    private final String user;
    private final String password;

    /**
     * Default constructor. Initializes a new instance of PostgreSQLManager by reading connection details from the configuration file.
     *
     * @throws IOException if there's an error reading the configuration file.
     */
    public PostgreSQLManager() throws IOException {
        this.url = Configuration.fromConfig().host();
        this.user = Configuration.fromConfig().user();
        this.password = Configuration.fromConfig().password();
    }

    /**
     * Constructor with provided database connection details. Initialize a new instance of PostgreSQLManager with provided user and password.
     *
     * @param user     the username for accessing the PostgreSQL database
     * @param password the password associated with the provided username
     * @throws IOException if there's an error reading the configuration file.
     */
    public PostgreSQLManager(String user, String password) throws IOException {
        this.url = Configuration.fromConfig().host();
        this.user = user;
        this.password = password;
    }

    /**
     * Connects to the PostgreSQL database using the provided connection details.
     *
     * @return a Connection object for interacting with the database
     * @throws SQLException if a database access error occurs or the url is null
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Retrieves a list of all Wine objects in the database where the 'in_assortment' field is false.
     *
     * @return List of Wine instances. If no wine details are found, an empty list is returned.
     */
    public List<Wine> getAllWine() {
        String SQL = "SELECT * FROM public.wine WHERE in_assortment = false";
        ArrayList<Wine> result = new ArrayList<>();

        try (Connection conn = connect()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                result.add(new Wine(
                        resultSet.getString("name"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("volume"),
                        resultSet.getString("color"),
                        resultSet.getDouble("price"),
                        resultSet.getString("comment")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * Adds a new Wine object to the database.
     *
     * @param wine The Wine instance to be stored into the database
     * @return the auto-generated key of the new inserted record; If insertion fails it returns 0
     */
    public long insertWine(@NotNull Wine wine) {
        String SQL = "INSERT INTO public.wine(name, year, volume, color, price, comment) " + "VALUES(?, ?, ?, ?, ?, ?)";

        long id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, wine.getName());
            pstmt.setInt(2, wine.getYear().getValue());
            pstmt.setDouble(3, wine.getVolume().getVolume());
            pstmt.setString(4, wine.getColor().name());
            pstmt.setDouble(5, wine.getPrice());
            pstmt.setString(6, wine.getComment());

            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }
}