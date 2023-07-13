package eu.lilithmonodia.winestock.database;

import eu.lilithmonodia.winestock.app.Assortment;
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
    private static final String WINE_SELECT_SQL = "SELECT * FROM public.wine WHERE in_assortment = false";
    private static final String INSERT_WINE_SQL = "INSERT INTO public.wine(name, year, volume, color, price, comment) " + "VALUES(?, ?, ?, ?, ?, ?)";

    private final String url;
    private final String user;
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
        List<Wine> wines = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(WINE_SELECT_SQL);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                wines.add(new Wine.Builder(
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
        return wines;
    }

    /**
     * Inserts a new wine into the database.
     *
     * @param wine the Wine object to be inserted into the database
     * @return an Optional representing the generated key of the inserted wine, if the insertion was successful,
     * or an empty Optional if the insertion failed
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

    /**
     * Retrieves all assortments from the database.
     *
     * @return a list of Assortment objects containing all the assortments retrieved from the database,
     * or an empty list if the retrieval failed
     */
    public List<Assortment> getAllAssortments() {
        final String ASSORTMENT_SELECT_SQL = "SELECT * FROM assortment";
        final String WINE_SELECT_ASSORTMENT_SQL = "SELECT * FROM wine WHERE wno IN (SELECT wno FROM contains WHERE ano = ?)";

        List<Assortment> assortments = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(ASSORTMENT_SELECT_SQL)) {

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Assortment assortment = new Assortment();

                PreparedStatement pstmtWines = conn.prepareStatement(WINE_SELECT_ASSORTMENT_SQL);
                pstmtWines.setInt(1, resultSet.getInt("ano"));
                ResultSet resultSetWines = pstmtWines.executeQuery();

                while (resultSetWines.next()) {
                    Wine wine = new Wine.Builder(
                            resultSetWines.getString("name"),
                            resultSetWines.getInt("year"),
                            resultSetWines.getDouble("volume"),
                            resultSetWines.getString("color"),
                            resultSetWines.getDouble("price")
                    ).comment(resultSetWines.getString("comment")).build();

                    assortment.add(wine);
                }

                assortments.add(assortment);
            }

        } catch (SQLException e) {
            System.out.println("Error executing select: " + e.getMessage());
        }

        return assortments;
    }
}