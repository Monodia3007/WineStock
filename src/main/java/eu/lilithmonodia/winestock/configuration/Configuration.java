package eu.lilithmonodia.winestock.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The `Configuration` record holds the necessary details required to
 * establish a database connection. This includes the host address,
 * the database username, and password.
 * <p>
 * This record provides a method to read these details from a JSON
 * configuration file named config.json.
 */
public record Configuration(String databaseHost, String databaseUser, String userPassword) {
    /**
     * Reads the configuration file "config.json" and creates a Configuration object based on the values in the file.
     * The configuration file must be located in the same package as the Configuration class.
     *
     * @return a Configuration object with the values from the configuration file.
     *
     * @throws IOException if an I/O error occurs while reading the configuration file.
     */
    @NotNull
    public static Configuration fromConfig() throws IOException {
        InputStream config = Configuration.class.getResourceAsStream("config.json");
        assert config != null;
        String jsonString = IOUtils.toString(config, StandardCharsets.UTF_8);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Configuration.class, (JsonDeserializer<Configuration>) (json, typeOfT, context) -> {
                    JsonObject jsonObject = json.getAsJsonObject();
                    String databaseHost = jsonObject.get("databaseHost").getAsString();
                    String databaseUser = jsonObject.get("databaseUser").getAsString();
                    String userPassword = jsonObject.get("userPassword").getAsString();

                    return new Configuration(databaseHost, databaseUser, userPassword);
                }).create();

        return gson.fromJson(jsonString, Configuration.class);
    }
}
