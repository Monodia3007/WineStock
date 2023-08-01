package eu.lilithmonodia.winestock.exceptions;

/**
 * Exception thrown if the set bottle size volume does not match any in the BottleSize enum.
 */
public class InvalidBottleVolumeException extends Exception {
    /**
     * Constructs a new InvalidBottleVolumeException with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidBottleVolumeException(String message) {
        super(message);
    }
}