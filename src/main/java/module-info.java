/**
 * The 'eu.lilithmonodia.winestock' module defines the main module for the WineStock application.
 * <p>
 * It declares dependencies on other needed modules and specifies which packages are opened
 * and exported for use by other modules.
 * <p>
 * Dependencies for this module include:
 * <ul>
 *     <li>javafx.controls: JavaFX GUI controls for creating user interfaces</li>
 *     <li>javafx.fxml: JavaFX package for FXML</li>
 *     <li>java.sql: Core Java package for SQL database management</li>
 *     <li>org.postgresql.jdbc: PostgreSQL JDBC driver for database connectivity</li>
 *     <li>org.jetbrains.annotations: JetBrains annotations library</li>
 *     <li>java.desktop: Provides interfaces for interconnecting applications with the integrated desktop environment</li>
 *     <li>com.google.gson: Library for converting Java Objects into JSON and back </li>
 *     <li>org.apache.commons.io: Commons IO suite of utilities and component libraries </li>
 * </ul>
 * <p>
 * The "opens" directive is used to open packages to specific modules, allowing them to use reflection
 * to access types in these packages:
 * <ul>
 *    <li>The 'eu.lilithmonodia.winestock' package is opened to the 'javafx.fxml' module. This allows the JavaFX runtime to access annotated fields and methods within this package.</li>
 *    <li>The 'eu.lilithmonodia.winestock.data' package is opened to the 'java.base' module. This allows Java's base module to access types within this package.</li>
 * </ul>
 * <p>
 * The "exports" directive exports a package for use by other modules:
 * <ul>
 *     <li>The 'eu.lilithmonodia.winestock' package is exported, allowing other modules to read its public types and their public.</li>
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
    requires org.apache.logging.log4j;
    requires org.postgresql.jdbc;
    requires atlantafx.base;

    opens eu.lilithmonodia.winestock to javafx.fxml;
    opens eu.lilithmonodia.winestock.data to java.base, javafx.base;
    exports eu.lilithmonodia.winestock;
}