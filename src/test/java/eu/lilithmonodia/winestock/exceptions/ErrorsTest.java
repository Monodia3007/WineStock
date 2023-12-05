package eu.lilithmonodia.winestock.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorsTest {

    @Test
    void shouldReturnCorrectErrorMessageForNoWineSelected() {
        assertEquals("No wine selected.", Errors.NO_WINE_SELECTED.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToAddAssortmentToDatabase() {
        assertEquals("Failed to add assortment to the database.", Errors.FAILED_TO_ADD_ASSORTMENT_TO_THE_DATABASE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToAddWineToDatabase() {
        assertEquals("Failed to add wine to the database.", Errors.FAILED_TO_ADD_WINE_TO_THE_DATABASE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorAddingAssortment() {
        assertEquals("Error adding assortment", Errors.ERROR_ADDING_ASSORTMENT.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorAddingWineToAssortment() {
        assertEquals("Error adding wine to assortment", Errors.ERROR_ADDING_WINE_TO_ASSORTMENT.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForNoAssortmentSelected() {
        assertEquals("No assortment selected.", Errors.NO_ASSORTMENT_SELECTED.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorAddingWine() {
        assertEquals("Error adding wine", Errors.ERROR_ADDING_WINE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorDeletingWineFromAssortment() {
        assertEquals("Error deleting wine from assortment", Errors.ERROR_DELETING_WINE_FROM_ASSORTMENT.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorLoggingIn() {
        assertEquals("Error logging in", Errors.ERROR_LOGGING_IN.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorRefreshingData() {
        assertEquals("Error refreshing data", Errors.ERROR_REFRESHING_DATA.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToRefreshDataFromDatabase() {
        assertEquals("Failed to refresh data from the database.", Errors.FAILED_TO_REFRESH_DATA_FROM_THE_DATABASE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorClosing() {
        assertEquals("Error closing application", Errors.ERROR_CLOSING.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToClosePostgreSQLConnection() {
        assertEquals("Failed to close PostgreSQLManager", Errors.FAILED_TO_CLOSE_POSTGRESQL_CONNECTION.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorImportingData() {
        assertEquals("Error importing database", Errors.ERROR_IMPORTING_DATA.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToImportDataFromDatabase() {
        assertEquals("Failed to import data from the database.", Errors.FAILED_TO_IMPORT_DATA_FROM_THE_DATABASE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorDeletingWine() {
        assertEquals("Error deleting wine", Errors.ERROR_DELETING_WINE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToDeleteWineFromDatabase() {
        assertEquals("Failed to delete wine from the database.", Errors.FAILED_TO_DELETE_WINE_FROM_THE_DATABASE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorModifyingWine() {
        assertEquals("Error modifying wine", Errors.ERROR_MODIFYING_WINE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToModifyWineInDatabase() {
        assertEquals("Failed to modify wine in the database.", Errors.FAILED_TO_MODIFY_WINE_IN_THE_DATABASE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorLoadingWine() {
        assertEquals("Error loading wine", Errors.ERROR_LOADING_WINE.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForYearFieldIsEmpty() {
        assertEquals("Year field is empty.", Errors.YEAR_FIELD_IS_EMPTY.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForErrorDeletingAssortment() {
        assertEquals("Error deleting assortment", Errors.ERROR_DELETING_ASSORTMENT.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToDeleteAssortmentFromDatabase() {
        assertEquals("Failed to delete assortment from the database.", Errors.FAILED_TO_DELETE_ASSORTMENT.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForNoWineOrAssortmentSelected() {
        assertEquals("No wine or assortment selected.", Errors.NO_WINE_OR_ASSORTMENT_SELECTED.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForWineYearDoesNotMatch() {
        assertEquals("Wine year does not match assortment year.", Errors.WINE_YEAR_DOES_NOT_MATCH.getValue());
    }

    @Test
    void shouldReturnCorrectErrorMessageForFailedToAddWineToAssortment() {
        assertEquals("Failed to add wine to assortment in the database.", Errors.FAILED_TO_ADD_WINE_TO_ASSORTMENT.getValue());
    }
}
