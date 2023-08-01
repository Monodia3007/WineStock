package eu.lilithmonodia.winestock.exceptions;

/**
 * Exception thrown if a wine is already included in an assortment and an attempt is made to add it again.
 */
public class WineAlreadyInAssortmentException extends Exception {
    /**
     * Constructs a new WineAlreadyInAssortmentException with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public WineAlreadyInAssortmentException(String message) {
        super(message);
    }
}