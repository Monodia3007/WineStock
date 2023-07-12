/**
 * The 'eu.lilithmonodia.winestock' module defines the main module for the WineStock application.
 * <p>
 * It declares dependencies on other needed modules and specifies which packages are opened
 * and exported for use by other modules.
 * <p>
 * This module has the following dependencies:
 * <ul>
 *     <li>javafx.controls: JavaFX GUI controls for creating user interfaces</li>
 *     <li>javafx.fxml: JavaFX package for FXML</li>
 *     <li>java.sql: Core Java package for SQL database management</li>
 *     <li>org.json: Library to work with JSON data</li>
 *     <li>org.jetbrains.annotations: JetBrains annotations library</li>
 * </ul>
 *  The 'opens' directive is used to open packages to specific modules, meaning they can use reflection
 *  to access types in these packages, even if they are not exported. Here:
 * <ul>
 *    <li>The 'eu.lilithmonodia.winestock' package is opened to the 'javafx.fxml' module (This could be necessary for JavaFX applications)</li>
 *    <li>The 'eu.lilithmonodia.winestock.app' package is opened to the 'java.base' module</li>
 * </ul>
 *  The 'exports' directive exports package for use by other modules:
 * <ul>
 *     <li>The 'eu.lilithmonodia.winestock' package is exported, allowing other modules to read its public types and their public</li>
 * </ul>
 */
module eu.lilithmonodia.winestock {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires com.google.gson;
    requires org.apache.commons.io;

    opens eu.lilithmonodia.winestock to javafx.fxml;
    opens eu.lilithmonodia.winestock.app to java.base;
    exports eu.lilithmonodia.winestock;
}