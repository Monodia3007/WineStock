package eu.lilithmonodia.winestock.configuration;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public record Configuration(String host, String user, String password) {
    public static Configuration fromConfig() throws IOException {
        InputStream config = Configuration.class.getResourceAsStream("config.json");
        InputStreamReader isReader = new InputStreamReader(config);
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
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
