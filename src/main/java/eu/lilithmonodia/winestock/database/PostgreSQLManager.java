package eu.lilithmonodia.winestock.database;

import eu.lilithmonodia.winestock.data.Assortment;
import eu.lilithmonodia.winestock.data.Wine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.LongConsumer;

/**
 * A class that manages PostgreSQL database operations for wines and assortments.
 */
public class PostgreSQLManager {
    private static final Logger LOGGER = LogManager.getLogger(PostgreSQLManager.class);
    private static final String WINE_SELECT_SQL = "SELECT wno, name, year, volume, color, price, color, comment FROM public.wine WHERE ano IS NULL";
    private static final String INSERT_WINE_SQL = "INSERT INTO public.wine(name, year, volume, color, price, comment) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_WINE_SQL = "UPDATE public.wine SET name = ?, year = ?, volume = ?, color = ?, price = ?, comment = ? WHERE wno = ?";
    private static final String DELETE_WINE_SQL = "DELETE FROM public.wine WHERE wno = ?";
    private static final String ASSORTMENT_SELECT_SQL = "SELECT * FROM public.assortment";
    private static final String WINE_SELECT_ASSORTMENT_SQL = "SELECT * FROM public.wine WHERE wine.ano IN (SELECT ano FROM public.assortment WHERE ano = ?)";
    private static final String INSERT_ASSORTMENT_SQL = "INSERT INTO public.assortment(year) VALUES(?)";
    private static final String DELETE_ASSORTMENT_SQL = "DELETE FROM public.assortment WHERE ano = ?";
    private static final String UPDATE_WINE_IN_ASSORTMENT_SQL = "UPDATE public.wine SET ano = ? WHERE wno = ?";


    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    /**
     * Constructs a new PostgreSQLManager instance with the given credentials.
     *
     * @param url      the URL for the database connection
     * @param user     the username for the database connection
     * @param password the password for the database connection
     * @throws SQLException if there is an error establishing the database connection
     */
    public PostgreSQLManager(String url, String user, String password) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connection = DriverManager.getConnection(url, user, password);
        this.connection.setAutoCommit(false);
    }

    /**
     * Establishes a connection to the PostgreSQL database using the provided credentials.
     *
     * @return the Connection object representing the database connection
     * @throws SQLException if there is an error establishing the database connection
     */
    public Connection connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection(url, user, password);
            this.connection.setAutoCommit(false);
        }
        return this.connection;
    }

    /**
     * Retrieves all wine records from the database.
     *
     * @return a List of Wine objects representing the wine records
     * @throws SQLException if an error occurs while retrieving the wine records
     */
    public List<Wine> getAllWine() throws SQLException {
        List<Wine> wines = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(WINE_SELECT_SQL);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                wines.add(new Wine.Builder(
                        resultSet.getInt("wno"),
                        resultSet.getString("name"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("volume"),
                        resultSet.getString("color"),
                        resultSet.getDouble("price")
                ).comment(resultSet.getString("comment")).build());
            }
        }
        return wines;
    }

    /**
     * Inserts a new wine record into the database.
     *
     * @param wine the Wine object representing the wine to be inserted
     * @return an Optional Long representing the ID of the newly inserted wine record,
     * or an empty Optional if the insertion failed
     * @throws SQLException if an error occurs while inserting the wine record
     */
    public Optional<Long> insertWine(Wine wine) throws SQLException {
        return insertWineInternal(wine);
    }

    /**
     * Inserts a new wine record into the database.
     *
     * @param wine the Wine object representing the wine to be inserted
     * @return an Optional Long representing the ID of the newly inserted wine record,
     * or an empty Optional if the insertion failed
     * @throws SQLException if an error occurs while inserting the wine record
     */
    private Optional<Long> insertWineInternal(Wine wine) throws SQLException {
        try (PreparedStatement pstmt = connect().prepareStatement(INSERT_WINE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setParametersInStatement(pstmt, wine);
            Optional<Long> longOptional = handleResponse(pstmt, id -> wine.setId((int) id));
            if (longOptional.isPresent()) {
                connection.commit();
            }
            return longOptional;
        } catch (SQLException e) {
            LOGGER.error("Error executing Wine insert: {}", e.getMessage(), e);
            connection.rollback();
            return Optional.empty();
        }
    }

    /**
     * Sets parameters for the given PreparedStatement to be used with the INSERT_WINE_SQL query.
     *
     * @param pstmt the PreparedStatement object to set parameters for
     * @param wine  the Wine object containing the values for the parameters
     * @throws SQLException if an error occurs while setting the parameters
     */
    private void setParametersInStatement(@NotNull PreparedStatement pstmt, @NotNull Wine wine) throws SQLException {
        pstmt.setString(1, wine.getName());
        pstmt.setInt(2, wine.getYear().getValue());
        pstmt.setDouble(3, wine.getVolume().getVolume());
        pstmt.setString(4, wine.getColor().name());
        pstmt.setDouble(5, wine.getPrice());
        pstmt.setString(6, wine.getComment());
    }

    /**
     * Retrieves all Assortments from the database.
     *
     * @return a List of Assortment objects representing all the assortments in the database
     * @throws SQLException if an error occurs while retrieving the assortments
     */
    public List<Assortment<Wine>> getAllAssortments() throws SQLException {
        List<Assortment<Wine>> assortments = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(ASSORTMENT_SELECT_SQL)) {

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Assortment<Wine> assortment = fetchAssortmentByResultSet(resultSet);
                assortments.add(assortment);
            }
        }
        return assortments;
    }

    /**
     * Fetches an Assortment for a given ResultSet. Reads all wines in the assortment.
     *
     * @param resultSet the ResultSet containing information about the assortment
     * @return an Assortment object representing the assortment extracted from the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    private @NotNull Assortment<Wine> fetchAssortmentByResultSet(@NotNull ResultSet resultSet) throws SQLException {
        Assortment<Wine> assortment = new Assortment<>(resultSet.getInt("ano"), Year.of(resultSet.getInt("year")));
        try (PreparedStatement pstmtWines = connect().prepareStatement(WINE_SELECT_ASSORTMENT_SQL)) {
            pstmtWines.setInt(1, resultSet.getInt("ano"));
            try (ResultSet resultSetWines = pstmtWines.executeQuery()) {
                while (resultSetWines.next()) {
                    assortment.add(getWineFromResultSet(resultSetWines));
                }
            }
        }
        return assortment;
    }

    /**
     * Gets a Wine object from a given ResultSet.
     *
     * @param resultSetWines the ResultSet containing information about the wine
     * @return a Wine object representing the wine extracted from the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    private Wine getWineFromResultSet(@NotNull ResultSet resultSetWines) throws SQLException {
        return new Wine.Builder(
                resultSetWines.getInt("wno"),
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
     * @return an Optional containing the ID of the inserted Assortment, or an empty Optional if the insertion failed
     */
    public Optional<Long> insertAssortment(Assortment<Wine> assortment) {
        try {
            connect();
            Optional<Long> assortmentId = insertAssortmentInternal(assortment);

            if (assortmentId.isPresent()) {
                insertWinesInAssortment(assortment, assortmentId.get());
                connection.commit();
            }
            return assortmentId;
        } catch (SQLException e) {
            LOGGER.error("Error executing insert: {}", e.getMessage(), e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.error("Error rolling back transaction: {}", ex.getMessage(), ex);
            }
        }
        return Optional.empty();
    }

    /**
     * Inserts an Assortment object into the database.
     *
     * @param assortment the Assortment object to be inserted
     * @return an Optional containing the ID of the inserted Assortment, or an empty Optional if the insertion failed
     * @throws SQLException if an error occurs while accessing the database
     */
    private Optional<Long> insertAssortmentInternal(@NotNull Assortment<Wine> assortment) throws SQLException {
        try (PreparedStatement pstmt = connect().prepareStatement(INSERT_ASSORTMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, assortment.getYear().getValue());
            return handleResponse(pstmt, id -> assortment.setId((int) id));
        }
    }

    /**
     * Inserts a list of wines into the assortment with the specified assortment ID.
     *
     * @param assortment   the list of wines to insert
     * @param assortmentId the ID of the assortment to insert the wines into
     * @throws SQLException if an error occurs while accessing the database
     */
    public void insertWinesInAssortment(@NotNull Assortment<Wine> assortment, Long assortmentId) throws SQLException {
        for (Wine wine : assortment) {
            insertWineInAssortment(wine, assortmentId);
        }
    }

    /**
     * Inserts a wine into the assortment with the specified assortment ID.
     *
     * @param wine         the wine to insert
     * @param assortmentId the ID of the assortment to insert the wine into
     * @throws SQLException if an error occurs while accessing the database
     */
    public void insertWineInAssortment(@NotNull Wine wine, Long assortmentId) throws SQLException {
        try (PreparedStatement pstmtContains = connect().prepareStatement(UPDATE_WINE_IN_ASSORTMENT_SQL)) {
            pstmtContains.setLong(1, assortmentId);
            pstmtContains.setLong(2, wine.getId());
            pstmtContains.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("Error executing insert: {}", e.getMessage(), e);
            connection.rollback();
        }
    }

    /**
     * Updates a wine record in the database.
     *
     * @param wine the Wine object representing the wine to be updated
     * @return an Optional Long representing the ID of the updated wine record,
     * or an empty Optional if the update failed
     * @throws SQLException if an error occurs while updating the wine record
     */
    public Optional<Long> updateWine(Wine wine) throws SQLException {
        try (PreparedStatement pstmt = connect().prepareStatement(UPDATE_WINE_SQL)) {
            setParametersInStatement(pstmt, wine);
            pstmt.setInt(7, wine.getId());
            Optional<Long> longOptional = pstmt.executeUpdate() > 0 ? Optional.of((long) wine.getId()) : Optional.empty();
            if (longOptional.isPresent()) {
                connection.commit();
            }
            return longOptional;
        } catch (SQLException e) {
            LOGGER.error("Error executing Wine update: {}", e.getMessage(), e);
            connection.rollback();
            return Optional.empty();
        }
    }

    /**
     * Deletes a wine record from the database.
     *
     * @param wine the Wine object representing the wine to be deleted
     * @return an Optional Long representing the ID of the deleted wine record,
     * or an empty Optional if the deletion failed
     * @throws SQLException if an error occurs while deleting the wine record
     */
    public boolean deleteWine(Wine wine) throws SQLException {

        boolean isDeleted = false;
        try (PreparedStatement pstmt = connect().prepareStatement(DELETE_WINE_SQL)) {
            pstmt.setInt(1, wine.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // If DELETE was successful, commit the transaction
                connection.commit();
                isDeleted = true;
            }
        } catch (SQLException e) {
            LOGGER.error("Error executing Wine delete: {}", e.getMessage(), e);
            connection.rollback();
        }
        return isDeleted;
    }

    /**
     * Deletes an assortment from the database.
     *
     * @param assortment the Assortment object representing the assortment to be deleted
     * @return an Optional Long representing the ID of the deleted assortment,
     * or an empty Optional if the deletion failed
     * @throws SQLException if an error occurs while deleting the assortment
     */
    public Optional<Long> deleteAssortment(Assortment<Wine> assortment) throws SQLException {
        try (PreparedStatement pstmt = connect().prepareStatement(DELETE_ASSORTMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, assortment.getId());
            Optional<Long> longOptional = pstmt.executeUpdate() > 0 ? Optional.of((long) assortment.getId()) : Optional.empty();
            connection.commit();
            return longOptional;
        } catch (SQLException e) {
            LOGGER.error("Error executing Assortment delete: {}", e.getMessage(), e);
            connection.rollback();
            return Optional.empty();
        }
    }

    /**
     * Deletes a wine from the assortment in the database.
     *
     * @param wine the Wine object representing the wine to be deleted
     * @throws SQLException if an SQL exception occurs while deleting the wine
     */
    public void deleteWineInAssortment(Wine wine) throws SQLException {
        try (PreparedStatement pstmt = connect().prepareStatement(UPDATE_WINE_IN_ASSORTMENT_SQL)) {
            pstmt.setNull(1, Types.INTEGER);
            pstmt.setLong(2, wine.getId());
            pstmt.executeUpdate();
            connection.commit();
        }
    }

    /**
     * Handles the response of executing a prepared statement and retrieves the generated keys.
     *
     * @param pstmt      the prepared statement to execute
     * @param idConsumer the consumer to accept the generated ID
     * @return an Optional containing the generated ID if available, otherwise empty
     * @throws SQLException if an error occurs while accessing the database
     */
    private Optional<Long> handleResponse(@NotNull PreparedStatement pstmt, LongConsumer idConsumer) throws SQLException {
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    idConsumer.accept(id);
                    return Optional.of(id);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Closes the connection to the database.
     *
     * @throws SQLException if an error occurs while closing the database connection
     */
    public void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }
}