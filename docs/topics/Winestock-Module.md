# Winestock Module

The `eu.lilithmonodia.winestock` module is the main module for the WineStock application. It is responsible for declaring dependencies on other necessary modules and specifying which packages are opened and exported for use by other modules.

## Dependencies

This module depends on the following:

- `javafx.controls`: For creating user interfaces using JavaFX GUI controls
- `javafx.fxml`: For handling JavaFX FXML
- `java.sql`: For SQL database management
- `org.postgresql.jdbc`: For PostgreSQL JDBC driver essential for database connectivity
- `org.jetbrains.annotations`: IntelliJ IDEA annotations library
- `java.desktop`: For interfacing applications with the integrated desktop environment
- `com.google.gson`: For converting Java Objects into JSON and back
- `org.apache.commons.io`: An Apache Commons IO suite providing utilities and component libraries
- `org.apache.logging.log4j`: For logging purposes

## Packages Opened
The eu.lilithmonodia.winestock module opens following packages to specific modules, allowing them to use reflection to access types in these packages:
- eu.lilithmonodia.winestock: This is opened to the javafx.fxml module to enable the JavaFX runtime to access annotated fields and methods within this package.
- eu.lilithmonodia.winestock.data: This is opened to the java.base module, allowing Java's base module to access types within this package.

```java
opens eu.lilithmonodia.winestock to javafx.fxml;
opens eu.lilithmonodia.winestock.data to java.base, javafx.base;
```

## Packages Exported
The eu.lilithmonodia.winestock module exports the following packages for use by other modules:
- eu.lilithmonodia.winestock: Other modules can read its public types and their public members.

```java
exports eu.lilithmonodia.winestock;
```

In conclusion, the eu.lilithmonodia.winestock is responsible for managing all the dependencies, opening and exporting packages related to the WineStock application. It contributes to the modularity and scalability of the application making it maintainable and easy to extend.