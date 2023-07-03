package eu.lilithmonodia.winestock.database;

import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.configuration.Configuration;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Postgre sql manager.
 */
public class PostgreSQLManager {
    private final String url;
    private final String user;
    private final String password;

    /**
     * Instantiates a new Postgre sql manager.
     *
     * @throws IOException the io exception
     */
    public PostgreSQLManager() throws IOException {
        this.url = Configuration.fromConfig().host();
        this.user = Configuration.fromConfig().user();
        this.password = Configuration.fromConfig().password();
    }

    /**
     * Instantiates a new Postgre sql manager.
     *
     * @param user     the user
     * @param password the password
     * @throws IOException the io exception
     */
    public PostgreSQLManager(String user, String password) throws IOException {
        this.url = Configuration.fromConfig().host();
        this.user = user;
        this.password = password;
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     * @throws SQLException the sql exception
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Gets all wine.
     *
     * @return the all wine
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
     * Insert wine long.
     *
     * @param wine the wine
     * @return the long
     */
    public long insertWine(Wine wine) {
        String SQL = "INSERT INTO wine(name, year, volume, color, price, comment) " + "VALUES(?, ?, ?, ?, ?, ?)";

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

