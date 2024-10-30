package eu.lilithmonodia.winestock.exceptions;

import lombok.Getter;

/**
 * The Errors enum represents a list of error messages that can be used throughout the application.
 * Each enum value represents a specific error message.
 */
@Getter
public enum Errors {
    /**
     * Represents an error when no wine is selected.
     */
    NO_WINE_SELECTED("No wine selected."),

    /**
     * Represents an error when adding an assortment to the database fails.
     */
    FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE("Failed to add assortment to the database."),

    /**
     * Represents an error when adding a wine to the database fails.
     */
    FAILED_TO_ADD_WINE_TO_THE_DATABASE("Failed to add wine to the database."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_ADDING_ASSORTMENT("Error adding assortment"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    ERROR_ADDING_WINE_TO_ASSORTMENT("Error adding wine to assortment"),

    /**
     * Represents an error when deleting an assortment fails.
     */
    NO_ASSORTMENT_SELECTED("No assortment selected."),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    ERROR_ADDING_WINE("Error adding wine"),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_DELETING_WINE_FROM_ASSORTMENT("Error deleting wine from assortment"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    ERROR_LOGGING_IN("Error logging in"),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_REFRESHING_DATA("Error refreshing data"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    FAILED_TO_REFRESH_DATA_FROM_THE_DATABASE("Failed to refresh data from the database."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_CLOSING("Error closing application"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    FAILED_TO_CLOSE_POSTGRESQL_CONNECTION("Failed to close PostgreSQLManager"),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_IMPORTING_DATA("Error importing database"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    FAILED_TO_IMPORT_DATA_FROM_THE_DATABASE("Failed to import data from the database."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_DELETING_WINE("Error deleting wine"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    FAILED_TO_DELETE_WINE_FROM_THE_DATABASE("Failed to delete wine from the database."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_MODIFYING_WINE("Error modifying wine"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    FAILED_TO_MODIFY_WINE_IN_THE_DATABASE("Failed to modify wine in the database."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_LOADING_WINE("Error loading wine"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    YEAR_FIELD_IS_EMPTY("Year field is empty."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_DELETING_ASSORTMENT("Error deleting assortment"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    FAILED_TO_DELETE_ASSORTMENT("Failed to delete assortment from the database."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    NO_WINE_OR_ASSORTMENT_SELECTED("No wine or assortment selected."),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    WINE_YEAR_DOES_NOT_MATCH("Wine year does not match assortment year."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    FAILED_TO_ADD_WINE_TO_ASSORTMENT("Failed to add wine to assortment in the database."),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    FAILED_TO_DELETE_WINE_FROM_ASSORTMENT("Failed to delete wine from assortment in the database."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_LOADING_ASSORTMENT("Error loading assortment"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    INVALID_YEAR_FORMAT("Invalid year format."),

    /**
     * Represents an error when deleting an assortment fails.
     */
    ERROR_OPENING_LINK("Error opening link"),

    /**
     * Represents an error when deleting an assortment from the database fails.
     */
    FAILED_TO_OPEN_LINK("Failed to open link.");

    // The error message associated with the enum value.
    private final String value;

    /**
     * Initializes a new instance of the Errors class with the specified value.
     *
     * @param value The error message to be assigned to the Errors instance.
     */
    Errors(String value) {
        this.value = value;
    }

}