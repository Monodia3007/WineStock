package eu.lilithmonodia.winestock.exceptions;

/**
 * Exception thrown if the set color does not match any in the Color enum.
 */
public class InvalidWineColorException extends Exception {
    /**
     * Constructs a new InvalidWineColorException with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidWineColorException(String message) {
        super(message);
    }
}