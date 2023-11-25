# Configuration Record
The Configuration record is a part of the eu.lilithmonodia.winestock.configuration package. It holds the necessary details required to establish a database connection, including the host address, the database username, and password.

```java
public record Configuration(String databaseHost, String databaseUser, String userPassword) { ... }
```

This record provides a method to read these details from a JSON configuration file named config.json.

```java
@NotNull
public static Configuration fromConfig() throws IOException { ... }
```

This method reads the configuration file "config.json" and creates a Configuration object based on the values in the file. The configuration file must be located in the same package as the Configuration class. A Configuration object with the values from the configuration file is returned.  
If an I/O error occurs while reading the configuration file, an IOException is thrown.  
In short, the Configuration record plays a vital role in managing the database connection configuration for the WineStock application, enabling efficient reading of connection details from a configuration file. This scalability allows for easier changes to configuration without requiring code modifications.