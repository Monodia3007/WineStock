package eu.lilithmonodia.winestock.database;

import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class manages the interaction with a PostgreSQL database for storing and retrieving Wine objects.
 */
public class PostgreSQLManager {
    /**
     * SQL statement for selecting non-assorted wines from the public.wine table.
     *
     * <p>
     * This SQL statement retrieves all rows from the public.wine table where the
     * in_assortment column is set to false.
     * </p>
     *
     * <p>
     * Example usage:
     * <pre>{@code
     * ResultSet resultSet = statement.executeQuery(WINE_SELECT_SQL);
     * }</pre>
     * </p>
     */
    private static final String WINE_SELECT_SQL = "SELECT * FROM public.wine WHERE in_assortment = false";
    /**
     * SQL statement for inserting a new wine into the database.
     * <p>
     * The SQL statement is used to insert a new record into the "wine" table of the "public" schema.
     * It includes the columns "name", "year", "volume", "color", "price" and "comment".
     * <p>
     * The statement uses parameterized values denoted by question marks (?) that can be dynamically
     * set during the execution of the statement.
     *
     * @see Wine
     */
    private static final String INSERT_WINE_SQL = "INSERT INTO public.wine(name, year, volume, color, price, comment) " + "VALUES(?, ?, ?, ?, ?, ?)";
    /**
     * Represents a private final String variable that stores a URL.
     */
    private final String url;
    /**
     * Represents the username of a user.
     */
    private final String user;
    /**
     * Represents a password.
     * <p>
     * This class encapsulates a password string that is marked as final to ensure immutability.
     * <p>
     * Usage:
     * <ol>
     *     <li>Create an instance of Password using a valid password string.</li>
     *     <li>Access or compare the password as needed.</li>
     * </ol>
     * Example:
     * <pre>{@code
     * String passwordString = "myStrongPassword";
     * Password password = new Password(passwordString);
     *
     * // Access the password
     * String storedPassword = password.getPassword();
     *
     * // Compare the password
     * boolean isMatch = password.matches("myStrongPassword");
     * }</pre>
     */
    private final String password;


    /**
     * Default constructor. Initialize a new instance of PostgreSQLManager using the configuration details from the Configuration class.
     *
     * @throws IOException if there's an error reading the configuration file.
     */
    public PostgreSQLManager() throws IOException {
        this.url = Configuration.fromConfig().databaseHost();
        this.user = Configuration.fromConfig().databaseUser();
        this.password = Configuration.fromConfig().userPassword();
    }

    /**
     * Constructor to create a new instance of PostgreSQLManager with custom user and password.
     *
     * @param user     the username to connect to the PostgreSQL database.
     * @param password the password to connect to the PostgreSQL database.
     * @throws IOException if there's an error reading the configuration file.
     */
    public PostgreSQLManager(String user, String password) throws IOException {
        this.url = Configuration.fromConfig().databaseHost();
        this.user = user;
        this.password = password;
    }

    /**
     * Connects to the PostgreSQL database using the provided URL, username, and password.
     *
     * @return a Connection object representing the connection to the PostgreSQL database.
     * @throws SQLException if there's an error connecting to the database.
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Retrieves a list of all wines from the database.
     *
     * @return a List of Wine objects representing all wines in the database.
     */
    public List<Wine> getAllWine() {
        List<Wine> result = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(WINE_SELECT_SQL);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                result.add(new Wine.Builder(
                        resultSet.getString("name"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("volume"),
                        resultSet.getString("color"),
                        resultSet.getDouble("price")
                ).comment(resultSet.getString("comment")).build());
            }
        } catch (SQLException e) {
            System.out.println("Error executing select: " + e.getMessage());
        }
        return result;
    }

    /**
     * Inserts a new wine into the database.
     *
     * @param wine the Wine object to be inserted into the database
     * @return an Optional representing the generated key of the inserted wine, if the insertion was successful,
     *         or an empty Optional if the insertion failed
     */
    public Optional<Long> insertWine(@NotNull Wine wine) {

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_WINE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, wine.getName());
            pstmt.setInt(2, wine.getYear().getValue());
            pstmt.setDouble(3, wine.getVolume().getVolume());
            pstmt.setString(4, wine.getColor().name());
            pstmt.setDouble(5, wine.getPrice());
            pstmt.setString(6, wine.getComment());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return Optional.of(rs.getLong(1));
                    }
                } catch (SQLException ex) {
                    // Use logging mechanism
                    System.out.println("Error retrieving generated key: " + ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            // Use logging mechanism
            System.out.println("Error executing insert: " + ex.getMessage());
        }
        return Optional.empty();
    }
}