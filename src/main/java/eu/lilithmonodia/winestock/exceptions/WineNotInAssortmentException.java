package eu.lilithmonodia.winestock.exceptions;

/**
 * Exception thrown if an attempt is made to remove a wine from an assortment, but the wine is not in the assortment.
 */
public class WineNotInAssortmentException extends Exception {
    /**
     * Constructs a new WineNotInAssortmentException with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public WineNotInAssortmentException(String message) {
        super(message);
    }
}