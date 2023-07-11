/**
 * The 'eu.lilithmonodia.winestock' module defines the main module for the WineStock application.
 * It declares dependencies on other needed modules and specifies which packages are opened
 * and exported for use by other modules.
 * <p>
 * This module has the following dependencies:
 * <p>
 *  - javafx.controls: JavaFX GUI controls for creating user interfaces
 * <p>
 *  - javafx.fxml: JavaFX package for FXML
 * <p>
 *  - java.sql: Core Java package for SQL database management
 * <p>
 *  - org.json: Library to work with JSON data
 * <p>
 *  - org.jetbrains.annotations: JetBrains annotations library
 * <p>
 * <p>
 *  The 'opens' directive is used to open packages to specific modules, meaning they can use reflection
 *  to access types in these packages, even if they are not exported. Here:
 * <p>
 *  - The 'eu.lilithmonodia.winestock' package is opened to the 'javafx.fxml' module (This could be necessary for JavaFX applications)
 * <p>
 *  - The 'eu.lilithmonodia.winestock.app' package is opened to the 'java.base' module
 * <p>
 * <p>
 *  The 'exports' directive exports package for use by other modules:
 * <p>
 *  - The 'eu.lilithmonodia.winestock' package is exported, allowing other modules to read its public types and their
 *    public
 */
module eu.lilithmonodia.winestock {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires org.jetbrains.annotations;

    opens eu.lilithmonodia.winestock to javafx.fxml;
    opens eu.lilithmonodia.winestock.app to java.base;
    exports eu.lilithmonodia.winestock;
}