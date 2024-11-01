package eu.lilithmonodia.winestock.data;

import eu.lilithmonodia.winestock.exceptions.InvalidBottleVolumeException;
import eu.lilithmonodia.winestock.exceptions.InvalidYearException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.time.Year;

/**
 * This class represents a specific type of wine which encapsulates various
 * attributes of the wine such as its name, production year, bottle size,
 * colour, price, and user comments.
 * <p>
 * The wine can also be part of an assortment (collection of wines).
 */
@Builder
@Getter
@EqualsAndHashCode
public class Wine implements Comparable<Wine> {
    /**
     * Represents a constant variable for an invalid year.
     * Provides a message indicating that the year mustn't be after the current year.
     */
    private static final String YEAR_AFTER_CURRENT_YEAR_INVALID = "Invalid year. The year must not be after the current year.";
    private static final Logger LOGGER = LogManager.getLogger(Wine.class);
    private @Setter int id;
    private @Setter String name;
    private Year year;
    private BottleSize volume;
    private @Setter Color color;
    private @Setter double price;
    private @Setter String comment;
    private @Setter boolean inAssortment;

    /**
     * Sets the year of an object.
     *
     * @param year the year to be set
     * @throws InvalidYearException if the year is after the current year
     */
    public void setYear(@NotNull Year year) throws InvalidYearException {
        if (year.isAfter(Year.now())) {
            LOGGER.error(YEAR_AFTER_CURRENT_YEAR_INVALID);
            throw new InvalidYearException(YEAR_AFTER_CURRENT_YEAR_INVALID);
        }
        this.year = year;
        LOGGER.info("Year set to: {}", year);
    }

    /**
     * Sets the volume of the bottle.
     *
     * @param volume the volume of the bottle to be set
     */
    public void setVolume(double volume) {
        try {
            this.volume = BottleSize.doubleToBottleSize(volume);
        } catch (InvalidBottleVolumeException e) {
            this.volume = BottleSize.BOUTEILLE;
        }
    }

    /**
     * Returns a string representation of the `Wine` object.
     *
     * @return a string description of the wine.
     */
    @Override
    public String toString() {
        return "Wine{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", volume=" + volume.toString() + '(' + volume.getVolume() + ')' +
                ", color=" + color +
                ", price=" + price +
                ", comment='" + comment + '\'' +
                '}';
    }

    /**
     * Compares this `Wine` object with the specified object for order.
     *
     * @param o the `Wine` object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null.
     */
    @Override
    public int compareTo(@NotNull Wine o) {
        return Integer.compare(this.id, o.id);
    }
}
