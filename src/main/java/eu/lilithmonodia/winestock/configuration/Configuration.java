package eu.lilithmonodia.winestock.configuration;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The `Configuration` record holds the necessary details required to
 * establish a database connection. This includes the host address,
 * the username and password needed for the connection.
 * <p>
 * This record provides a method to read these details from a JSON
 * configuration file named config.json.
 */
public record Configuration(String host, String user, String password) {
    /**
     * Reads database connection details from a configuration file named config.json.
     * <p>
     * The method reads the host, user, and password fields from the JSON
     * file and returns them as a new `Configuration` instance.
     *
     * @return A Configuration object representing the parsed configuration from the JSON file.
     * @throws IOException If an I/O error occurs while reading the JSON file.
     */
    @NotNull
    public static Configuration fromConfig() throws IOException {
        InputStream config = Configuration.class.getResourceAsStream("config.json");
        assert config != null;
        String jsonString = IOUtils.toString(config, StandardCharsets.UTF_8);
        return new Gson().fromJson(jsonString, Configuration.class);
    }
}
