package eu.lilithmonodia.winestock.database;

import eu.lilithmonodia.winestock.app.Wine;
import eu.lilithmonodia.winestock.configuration.Configuration;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLManager {
    private final String url;
    private final String user;
    private final String password;

    public PostgreSQLManager() throws IOException {
        this.url = Configuration.fromConfig().host();
        this.user = Configuration.fromConfig().user();
        this.password = Configuration.fromConfig().password();
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     * @throws java.sql.SQLException
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public List<Wine> getAllWine() {
        String SQL = "SELECT * FROM public.wine";
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
}

