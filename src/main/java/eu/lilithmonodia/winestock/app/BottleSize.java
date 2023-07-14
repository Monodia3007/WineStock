package eu.lilithmonodia.winestock.app;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This `BottleSize` enum represents the possible bottle sizes of the wine.
 * <p>
 * Each enum instance is associated with a specific volume corresponding to the size of the wine bottle.
 * The volume for each bottle size is provided in centiliters.
 * <p>
 * This enum provides a static method `doubleToBottleSize`, which takes a double representing
 * a volume, and returns the corresponding `BottleSize` instance.
 */
public enum BottleSize {
    /**
     * Piccola, or 'small' size. Corresponds to a volume of 20 cl.
     */
    PICCOLA(20),
    /**
     * Chopine, or 'vineyard worker' size. Corresponds to a volume of 25 cl.
     */
    CHOPINE(25),
    /**
     * Fillette, or 'daughter' size. Corresponds to a volume of 37.5 cl.
     */
    FILLETTE(37.5),
    /**
     * Medium size represents a volume of 50 centiliters.
     */
    MEDIUM(50),
    /**
     * Bouteille size represents a volume of 75 centiliters.
     */
    BOUTEILLE(75),
    /**
     * Magnum size represents a volume of 150 centiliters.
     */
    MAGNUM(150),
    /**
     * Jerobam size represents a volume of 300 centiliters.
     */
    JEROBAM(300),
    /**
     * Rehoboam size represents a volume of 450 centiliters.
     */
    REHOBOAM(450),
    /**
     * Mathusalem size represents a volume of 600 centiliters.
     */
    MATHUSALEM(600),
    /**
     * Salmanazar size represents a volume of 900 centiliters.
     */
    SALMANAZAR(900),
    /**
     * Balthazar size represents a volume of 1200 centiliters.
     */
    BALTHAZAR(1200),
    /**
     * Nabuchodonosor size represents a volume of 1500 centiliters.
     */
    NABUCHODONOSOR(1500),
    /**
     * Melchior size represents a volume of 1800 centiliters.
     */
    MELCHIOR(1800),
    /**
     * Souverain size represents a volume of 2625 centiliters.
     */
    SOUVERAIN(2625),
    /**
     * Primat size represents a volume of 2700 centiliters.
     */
    PRIMAT(2700),
    /**
     * Midas size represents a volume of 3000 centiliters.
     */
    MIDAS(3000);

    private final double volume;

    /**
     * Constructs a new instance of `BottleSize` with the provided volume.
     *
     * @param volume the volume associated with the bottle size.
     */
    BottleSize(double volume) {
        this.volume = volume;
    }

    /**
     * Finds and returns the `BottleSize` associated with the provided volume. If no such
     * `BottleSize` exists, this method returns null.
     *
     * @param volume the volume to fetch the corresponding `BottleSize` for.
     * @return the `BottleSize` matching the volume, or null if none match.
     */
    public static @Nullable BottleSize doubleToBottleSize(double volume) {
        List<BottleSize> bottleSizes = List.of(BottleSize.values());
        for (BottleSize bottleSize : bottleSizes) {
            if (bottleSize.getVolume() == volume) return bottleSize;
        }
        return null;
    }

    /**
     * Gets the volume associated with this `BottleSize`.
     *
     * @return the volume of the bottle size.
     */
    public double getVolume() {
        return volume;
    }
}
