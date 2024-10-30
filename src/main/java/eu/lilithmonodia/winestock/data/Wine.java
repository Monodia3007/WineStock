package eu.lilithmonodia.winestock.data;

import eu.lilithmonodia.winestock.exceptions.InvalidBottleVolumeException;
import eu.lilithmonodia.winestock.exceptions.InvalidYearException;
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
@Getter @EqualsAndHashCode
public class Wine implements Comparable<Wine> {
    /**
     * Represents a constant variable for an invalid year.
     * Provides a message indicating that the year mustn't be after the current year.
     */
    private static final String YEAR_AFTER_CURRENT_YEAR_INVALID = "Invalid year. The year must not be after the current year.";
    private static final Logger LOGGER = LogManager.getLogger(Wine.class);
    @Setter
    private int id;
    @Setter
    private String name;
    private Year year;
    private BottleSize volume;
    @Setter
    private Color color;
    @Setter
    private double price;
    @Setter
    private String comment;
    @Setter
    private boolean inAssortment;

    /**
     * Private constructor used by the Builder class to create a Wine object.
     *
     * @param builder the builder object containing the wine details
     */
    private Wine(@NotNull Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.year = Year.of(builder.year);
        this.volume = builder.volume;
        this.comment = builder.comment;
        this.color = builder.color;
        this.price = builder.price;
        this.inAssortment = false;
    }

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

    /**
     * The Builder class is responsible for building instances of the Wine class.
     * It provides a convenient way to construct Wine objects with optional parameters.
     */
    public static class Builder {
        private final int id;
        private final String name;
        private final int year;
        private final BottleSize volume;
        private final Color color;
        private final double price;
        private String comment;

        /**
         * Constructs a new Builder object with the specified parameters.
         *
         * @param name   the name of the builder
         * @param year   the year of the builder
         * @param volume the volume of the builder
         * @param color  the colour of the builder
         * @param price  the price of the builder
         */
        public Builder(String name, int year, double volume, String color, double price) {
            this(-1, name, year, volume, color, price);
        }

        /**
         * Constructs a new Builder object with the given parameters and checks the validity of parameters.
         *
         * @param id     the id of the builder
         * @param name   the name of the builder
         * @param year   the year of the builder
         * @param volume the volume of the builder
         * @param color  the colour of the builder
         * @param price  the price of the builder
         * @throws IllegalArgumentException if the year is after the current year, or if the colour is not one of the predefined wine colours
         */
        public Builder(int id, String name, int year, double volume, String color, double price) {
            BottleSize volume1;
            if (year > Year.now().getValue()) {
                throw new IllegalArgumentException(YEAR_AFTER_CURRENT_YEAR_INVALID);
            }

            try {
                volume1 = BottleSize.doubleToBottleSize(volume);
            } catch (InvalidBottleVolumeException e) {
                throw new IllegalArgumentException("Invalid Volume");
            }

            this.volume = volume1;
            try {
                this.color = Color.valueOf(color.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid color. The color must be one of the predefined wine colors.");
            }
            this.id = id;
            this.name = name;
            this.year = year;
            this.price = price;
            this.comment = "";
        }

        /**
         * Adds a comment to the Builder object.
         *
         * @param comment the comment to be added
         * @return the updated Builder object
         */
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Builds a new Wine object based on the current state of the Builder object.
         *
         * @return a new Wine object
         */
        public Wine build() {
            LOGGER.info("Wine instance created with name: {}", this.name);
            return new Wine(this);
        }
    }
}
