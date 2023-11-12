package eu.lilithmonodia.winestock.database;

import eu.lilithmonodia.winestock.app.Assortment;
import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.configuration.Configuration;
import eu.lilithmonodia.winestock.exceptions.WineAlreadyInAssortmentException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class that manages PostgreSQL database operations for wines and assortments.
 */
public class PostgreSQLManager {
    private static final Logger LOGGER = Logger.getLogger(PostgreSQLManager.class.getName());
    private static final String WINE_SELECT_SQL = "SELECT * FROM public.wine WHERE in_assortment = false";
    private static final String INSERT_WINE_SQL = "INSERT INTO public.wine(name, year, volume, color, price, comment, in_assortment) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String ASSORTMENT_SELECT_SQL = "SELECT * FROM assortment";
    private static final String WINE_SELECT_ASSORTMENT_SQL = "SELECT * FROM wine WHERE wno IN (SELECT wno FROM contains WHERE ano = ?)";
    private static final String INSERT_ASSORTMENT_SQL = "INSERT INTO public.assortment(year) VALUES(?)";
    private static final String INSERT_CONTAINS_SQL = "INSERT INTO public.contains(wno, ano) VALUES(?, ?)";

    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    /**
     * Constructs a new PostgreSQLManager instance.
     *
     * @throws IOException  if there is an error reading the configuration file
     * @throws SQLException if there is an error establishing the database connection
     */
    public PostgreSQLManager() throws IOException, SQLException {
        Configuration cfg = Configuration.fromConfig();
        this.url = cfg.databaseHost();
        this.user = cfg.databaseUser();
        this.password = cfg.userPassword();
        this.connection = DriverManager.getConnection(url, user, password);
    }

    /**
     * Constructs a new PostgreSQLManager instance with the given credentials.
     *
     * @param user     the username for the database connection
     * @param password the password for the database connection
     *
     * @throws IOException  if there is an error reading the configuration file
     * @throws SQLException if there is an error establishing the database connection
     */
    public PostgreSQLManager(String user, String password) throws IOException, SQLException {
        this.url = Configuration.fromConfig().databaseHost();
        this.user = user;
        this.password = password;
        this.connection = DriverManager.getConnection(url, user, password);
    }

    /**
     * Establishes a connection to the PostgreSQL database using the provided credentials.
     *
     * @return the Connection object representing the database connection
     *
     * @throws SQLException if there is an error establishing the database connection
     */
    public Connection connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection(url, user, password);
        }
        return this.connection;
    }

    /**
     * Retrieves all wine records from the database.
     *
     * @return a List of Wine objects representing the wine records
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
            LOGGER.severe("Error executing select: " + e.getMessage());
        }
        return wines;
    }

    /**
     * Inserts a new wine record into the database.
     *
     * @param wine the Wine object representing the wine to be inserted
     *
     * @return an Optional Long representing the ID of the newly inserted wine record,
     * or an empty Optional if the insertion failed
     *
     * @throws SQLException if an error occurs while inserting the wine record
     */
    public Optional<Long> insertWine(Wine wine) throws SQLException {
        return insertWineInternal(wine);
    }

    /**
     * Inserts a new wine record into the database.
     *
     * @param wine the Wine object representing the wine to be inserted
     *
     * @return an Optional Long representing the ID of the newly inserted wine record,
     * or an empty Optional if the insertion failed
     *
     * @throws SQLException if an error occurs while inserting the wine record
     */
    private Optional<Long> insertWineInternal(Wine wine) throws SQLException {
        try (PreparedStatement pstmt = connect().prepareStatement(INSERT_WINE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setParametersInStatement(pstmt, wine);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return Optional.of(rs.getLong(1));
                    }
                }
            }
            return Optional.empty();
        }
    }

    /**
     * Sets parameters for the given PreparedStatement to be used with the INSERT_WINE_SQL query.
     *
     * @param pstmt the PreparedStatement object to set parameters for
     * @param wine  the Wine object containing the values for the parameters
     *
     * @throws SQLException if an error occurs while setting the parameters
     */
    // Sets parameters for PreparedStatement to be used with INSERT_WINE_SQL query
    private void setParametersInStatement(@NotNull PreparedStatement pstmt, @NotNull Wine wine) throws SQLException {
        pstmt.setString(1, wine.getName());
        pstmt.setInt(2, wine.getYear().getValue());
        pstmt.setDouble(3, wine.getVolume().getVolume());
        pstmt.setString(4, wine.getColor().name());
        pstmt.setDouble(5, wine.getPrice());
        pstmt.setString(6, wine.getComment());
        pstmt.setBoolean(7, wine.isInAssortment());
    }

    /**
     * Retrieves all Assortments from the database.
     *
     * @return a List of Assortment objects representing all the assortments in the database
     */
    public List<Assortment> getAllAssortments() {
        List<Assortment> assortments = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(ASSORTMENT_SELECT_SQL)) {

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Assortment assortment = fetchAssortmentByResultSet(resultSet);
                assortments.add(assortment);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error executing select: " + e.getMessage());
        }

        return assortments;
    }

    /**
     * Fetches an Assortment for a given ResultSet. Reads all wines in the assortment.
     *
     * @param resultSet the ResultSet containing information about the assortment
     *
     * @return an Assortment object representing the assortment extracted from the ResultSet
     *
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    // Fetches Assortment for a given ResultSet. Reads all wines in the assortment.
    private @NotNull Assortment fetchAssortmentByResultSet(@NotNull ResultSet resultSet) throws SQLException {
        Assortment assortment = new Assortment();
        ResultSet resultSetWines;
        try (PreparedStatement pstmtWines = connect().prepareStatement(WINE_SELECT_ASSORTMENT_SQL)) {
            pstmtWines.setInt(1, resultSet.getInt("ano"));
            resultSetWines = pstmtWines.executeQuery();
        }

        while (resultSetWines.next()) {
            try {
                assortment.add(getWineFromResultSet(resultSetWines));
            } catch (WineAlreadyInAssortmentException e) {
                LOGGER.severe("Error adding wine to assortment: " + e.getMessage());
            }
        }
        return assortment;
    }

    /**
     * Gets a Wine object from a given ResultSet.
     *
     * @param resultSetWines the ResultSet containing information about the wine
     *
     * @return a Wine object representing the wine extracted from the ResultSet
     *
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    // Gets a Wine object from a given ResultSet.
    private Wine getWineFromResultSet(@NotNull ResultSet resultSetWines) throws SQLException {
        return new Wine.Builder(
                resultSetWines.getString("name"),
                resultSetWines.getInt("year"),
                resultSetWines.getDouble("volume"),
                resultSetWines.getString("color"),
                resultSetWines.getDouble("price")
        ).comment(resultSetWines.getString("comment")).build();
    }

    /**
     * Inserts an Assortment object into the database.
     *
     * @param assortment the Assortment object to be inserted
     *
     * @return an Optional containing the ID of the inserted Assortment, or an empty Optional if the insertion failed
     *
     * @throws SQLException if an error occurs while accessing the database
     */
    public Optional<Long> insertAssortment(Assortment assortment) throws SQLException {
        try {
            connection.setAutoCommit(false);
            Optional<Long> assortmentId = insertAssortmentInternal(assortment);
            if (assortmentId.isPresent()) {
                insertWinesInAssortment(assortment.getWineList(), assortmentId.get());
            }
            connection.commit();
            return assortmentId;
        } catch (SQLException e) {
            LOGGER.severe("Error executing insert: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.severe("Error rolling back transaction: " + ex.getMessage());
            }
        }
        return Optional.empty();
    }

    /**
     * Inserts an Assortment object into the database.
     *
     * @param assortment the Assortment object to be inserted
     *
     * @return an Optional containing the ID of the inserted Assortment, or an empty Optional if the insertion failed
     *
     * @throws SQLException if an error occurs while accessing the database
     */
    private Optional<Long> insertAssortmentInternal(Assortment assortment) throws SQLException {
        try (PreparedStatement pstmtAssortment = connection.prepareStatement(INSERT_ASSORTMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preprocessInsertAssortmentStatement(pstmtAssortment, assortment);
            return fetchKeyFromGeneratedKeys(pstmtAssortment.executeUpdate(), pstmtAssortment);
        }
    }

    /**
     * Preprocesses the PreparedStatement for inserting an Assortment object into the database.
     *
     * @param pstmtAssortment the PreparedStatement object for inserting the Assortment
     * @param assortment      the Assortment object to be inserted
     *
     * @throws SQLException if an error occurs while accessing the database
     */
    private void preprocessInsertAssortmentStatement(@NotNull PreparedStatement pstmtAssortment, @NotNull Assortment assortment) throws SQLException {
        pstmtAssortment.setInt(1, assortment.getYear().getValue());
    }

    /**
     * Fetches the generated key from the ResultSet of the executed PreparedStatement.
     *
     * @param affectedRows    the number of affected rows by the executed PreparedStatement
     * @param pstmtAssortment the PreparedStatement object that was executed
     *
     * @return an Optional containing the generated key if available, or an empty Optional if no key was generated
     *
     * @throws SQLException if an error occurs while accessing the database
     */
    private Optional<Long> fetchKeyFromGeneratedKeys(int affectedRows, PreparedStatement pstmtAssortment) throws SQLException {
        if (affectedRows > 0) {
            try (ResultSet rs = pstmtAssortment.getGeneratedKeys()) {
                if (rs.next()) {
                    return Optional.of(rs.getLong(1));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Inserts a list of wines into the assortment with the specified assortment ID.
     *
     * @param wines        the list of wines to insert
     * @param assortmentId the ID of the assortment to insert the wines into
     *
     * @throws SQLException if an error occurs while accessing the database
     */
    public void insertWinesInAssortment(@NotNull List<Wine> wines, Long assortmentId) throws SQLException {
        for (Wine wine : wines) {
            insertWineInAssortment(wine, assortmentId);
        }
    }

    /**
     * Inserts a wine into the assortment with the specified assortment ID.
     *
     * @param wine         the wine to insert
     * @param assortmentId the ID of the assortment to insert the wine into
     *
     * @throws SQLException if an error occurs while accessing the database
     */
    public void insertWineInAssortment(Wine wine, Long assortmentId) throws SQLException {
        Optional<Long> wineId = insertWineInternal(wine);
        if (wineId.isPresent()) {
            try (PreparedStatement pstmtContains = connection.prepareStatement(INSERT_CONTAINS_SQL)) {
                pstmtContains.setLong(1, assortmentId);
                pstmtContains.setLong(2, wineId.get());
                pstmtContains.executeUpdate();
            } catch (SQLException ex) {
                LOGGER.severe("Error executing insert: " + ex.getMessage());
            }
        }
    }
}