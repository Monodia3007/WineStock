package eu.lilithmonodia.winestock.app;

import eu.lilithmonodia.winestock.exceptions.InvalidBottleVolumeException;
import eu.lilithmonodia.winestock.exceptions.InvalidYearException;
import org.jetbrains.annotations.NotNull;

import java.time.Year;

/**
 * This class represents a specific type of wine which encapsulates various
 * attributes of the wine such as its name, production year, bottle size,
 * color, price, and user comments.
 * <p>
 * The wine can also be part of an assortment (collection of wines).
 */
public class Wine {
    private static final String INVALID_WINE_MESSAGE = "Invalid Wine, delete it promptly";
    private String name;
    private Year year;
    private BottleSize volume;
    private Color color;
    private double price;
    private String comment;
    private boolean inAssortment;

    /**
     * Private constructor used by the Builder class to create a Wine object.
     *
     * @param builder the builder object containing the wine details
     */
    private Wine(@NotNull Builder builder) {
        this.name = builder.name;
        this.year = Year.of(builder.year);
        this.volume = builder.volume;
        this.comment = builder.comment;
        this.color = builder.color;
        this.price = builder.price;
        this.inAssortment = false;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public Year getYear() {
        return year;
    }

    /**
     * Sets the year of an object.
     *
     * @param year the year to be set
     * @throws InvalidYearException if the year is after the current year
     */
    public void setYear(Year year) throws InvalidYearException {
        if (year.isAfter(Year.now())) {
            throw new InvalidYearException("Invalid year. The year must not be after the current year.");
        }
        this.year = year;
    }

    /**
     * Gets volume.
     *
     * @return the volume
     */
    public BottleSize getVolume() {
        return volume;
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
     * Gets color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color;
    }


    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets comment.
     *
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets comment.
     *
     * @param comment the comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Is in assortment boolean.
     *
     * @return the boolean
     */
    public boolean isInAssortment() {
        return inAssortment;
    }

    /**
     * Sets in assortment.
     *
     * @param inAssortment the in assortment
     */
    public void setInAssortment(boolean inAssortment) {
        this.inAssortment = inAssortment;
    }

    /**
     * Determines whether the current `Wine` object equals another object.
     *
     * @param o The object to compare against for equality.
     * @return true if the objects are the same; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wine wine)) return false;

        if (Double.compare(wine.getPrice(), getPrice()) != 0) return false;
        if (!getName().equals(wine.getName())) return false;
        if (!getYear().equals(wine.getYear())) return false;
        if (getVolume() != wine.getVolume()) return false;
        return getColor() == wine.getColor();
    }

    /**
     * Generates a hash code for the `Wine` object.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName().hashCode();
        result = 31 * result + getYear().hashCode();
        result = 31 * result + getVolume().hashCode();
        result = 31 * result + getColor().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
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
     * The Builder class is responsible for building instances of the Wine class.
     * It provides a convenient way to construct Wine objects with optional parameters.
     */
    public static class Builder {
        private final String name;
        private final int year;
        private final BottleSize volume;
        private final Color color;
        private final double price;
        private String comment;  // No default value

        /**
         * Constructs a new Builder object with the given parameters and checks validity of parameters.
         *
         * @param name   the name of the builder
         * @param year   the year of the builder
         * @param volume the volume of the builder
         * @param color  the color of the builder
         * @param price  the price of the builder
         * @throws IllegalArgumentException if the year is after the current year or if the color is not one of the predefined wine colors
         */
        public Builder(String name, int year, double volume, String color, double price) {
            BottleSize volume1;
            if (year > Year.now().getValue()) {
                throw new IllegalArgumentException("Invalid year. The year must not be after the current year.");
            }

            try {
                volume1 = BottleSize.doubleToBottleSize(volume);
            } catch (InvalidBottleVolumeException e) {
                volume1 = BottleSize.BOUTEILLE;
                this.comment = "Invalid Wine, delete it promptly";
            }

            this.volume = volume1;
            try {
                this.color = Color.valueOf(color.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid color. The color must be one of the predefined wine colors.");
            }

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
            return new Wine(this);
        }
    }
}
