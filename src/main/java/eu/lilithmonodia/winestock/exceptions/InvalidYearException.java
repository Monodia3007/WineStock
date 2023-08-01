package eu.lilithmonodia.winestock.exceptions;

/**
 * Exception thrown when the year provided for a wine is not valid.
 * E.g., year is in the future or far in the past.
 */
public class InvalidYearException extends Exception {
    /**
     * Constructs a new InvalidYearException with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidYearException(String message) {
        super(message);
    }
}
