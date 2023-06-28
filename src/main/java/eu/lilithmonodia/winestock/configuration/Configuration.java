package eu.lilithmonodia.winestock.configuration;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public record Configuration(String host, String user, String password) {
    public static Configuration fromConfig() throws IOException {
        Path fileName = Path.of("./src/main/resources/config.json");
        String jsonString = Files.readString(fileName);
        JSONObject obj = new JSONObject(jsonString);
        return new Configuration(
                obj.getString("database-host"),
                obj.getString("database-user"),
                obj.getString("user-password")
                );
    }
}
