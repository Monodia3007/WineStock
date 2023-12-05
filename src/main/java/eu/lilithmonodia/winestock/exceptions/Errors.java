package eu.lilithmonodia.winestock.exceptions;

public enum Errors {
    NO_WINE_SELECTED("No wine selected."),
    FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE("Failed to add assortment to the database."),
    FAILED_TO_ADD_WINE_TO_THE_DATABASE("Failed to add wine to the database."),
    ERROR_ADDING_ASSORTMENT("Error adding assortment"),
    ERROR_ADDING_WINE_TO_ASSORTMENT("Error adding wine to assortment"),
    NO_ASSORTMENT_SELECTED("No assortment selected."),
    ERROR_ADDING_WINE("Error adding wine"),
    ERROR_DELETING_WINE_FROM_ASSORTMENT("Error deleting wine from assortment"),
    ERROR_LOGGING_IN("Error logging in"),
    ERROR_REFRESHING_DATA("Error refreshing data"),
    FAILED_TO_REFRESH_DATA_FROM_THE_DATABASE("Failed to refresh data from the database."),
    ERROR_CLOSING("Error closing application"),
    FAILED_TO_CLOSE_POSTGRESQL_CONNECTION("Failed to close PostgreSQLManager"),
    ERROR_IMPORTING_DATA("Error importing database"),
    FAILED_TO_IMPORT_DATA_FROM_THE_DATABASE("Failed to import data from the database."),
    ERROR_DELETING_WINE("Error deleting wine"),
    FAILED_TO_DELETE_WINE_FROM_THE_DATABASE("Failed to delete wine from the database."),
    ERROR_MODIFYING_WINE("Error modifying wine"),
    FAILED_TO_MODIFY_WINE_IN_THE_DATABASE("Failed to modify wine in the database."),
    ERROR_LOADING_WINE("Error loading wine"),
    YEAR_FIELD_IS_EMPTY("Year field is empty."),
    ERROR_DELETING_ASSORTMENT("Error deleting assortment"),
    FAILED_TO_DELETE_ASSORTMENT("Failed to delete assortment from the database."),
    NO_WINE_OR_ASSORTMENT_SELECTED("No wine or assortment selected."),
    WINE_YEAR_DOES_NOT_MATCH("Wine year does not match assortment year."),
    FAILED_TO_ADD_WINE_TO_ASSORTMENT("Failed to add wine to assortment in the database."),
    FAILED_TO_DELETE_WINE_FROM_ASSORTMENT("Failed to delete wine from assortment in the database."),
    ERROR_LOADING_ASSORTMENT("Error loading assortment"),
    INVALID_YEAR_FORMAT("Invalid year format."),
    ERROR_OPENING_LINK("Error opening link"),
    FAILED_TO_OPEN_LINK("Failed to open link.");
    private final String value;

    Errors(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}