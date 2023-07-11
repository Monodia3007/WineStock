package eu.lilithmonodia.winestock.configuration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The `Configuration` record holds the necessary details required to
 * establish a database connection. This includes the host address,
 * the username and password needed for the connection.<br/>
 * This record provides a method to read these details from a JSON
 * configuration file named config.json.
 */
public record Configuration(String host, String user, String password) {
    /**
     * Reads database connection details from a configuration file named config.json.<br/>
     * The method reads the host, user, and password fields from the JSON
     * file and returns them as a new `Configuration` instance.
     *
     * @return a new `Configuration` instance built from the config file details.
     * @throws IOException if an error occurs while reading the config file.
     */
    @Contract(" -> new")
    public static @NotNull Configuration fromConfig() throws IOException {
        InputStream config = Configuration.class.getResourceAsStream("config.json");
        assert config != null;
        InputStreamReader isReader = new InputStreamReader(config);
        BufferedReader reader = new BufferedReader(isReader);
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        String jsonString = sb.toString();
        JSONObject obj = new JSONObject(jsonString);
        return new Configuration(
                obj.getString("database-host"),
                obj.getString("database-user"),
                obj.getString("user-password")
        );
    }
}
