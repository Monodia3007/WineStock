package eu.lilithmonodia.winestock.app;

import java.util.List;

/**
 * The enum Bottle size.
 */
public enum BottleSize {
    /**
     * Piccola bottle size.
     */
    PICCOLA(20),
    /**
     * Chopine bottle size.
     */
    CHOPINE(25),
    /**
     * Fillette bottle size.
     */
    FILLETTE(37.5),
    /**
     * Medium bottle size.
     */
    MEDIUM(50),
    /**
     * Bouteille bottle size.
     */
    BOUTEILLE(75),
    /**
     * Magnum bottle size.
     */
    MAGNUM(150),
    /**
     * Jerobam bottle size.
     */
    JEROBAM(300),
    /**
     * Rehoboam bottle size.
     */
    REHOBOAM(450),
    /**
     * Mathusalem bottle size.
     */
    MATHUSALEM(600),
    /**
     * Salmanazar bottle size.
     */
    SALMANAZAR(900),
    /**
     * Balthazar bottle size.
     */
    BALTHAZAR(1200),
    /**
     * Nabuchodonosor bottle size.
     */
    NABUCHODONOSOR(1500),
    /**
     * Melchior bottle size.
     */
    MELCHIOR(1800),
    /**
     * Souverain bottle size.
     */
    SOUVERAIN(2625),
    /**
     * Primat bottle size.
     */
    PRIMAT(2700),
    /**
     * Midas bottle size.
     */
    MIDAS(3000);

    private final double volume;

    BottleSize(double volume) {
        this.volume = volume;
    }

    /**
     * Double to bottle size.
     *
     * @param volume the volume
     * @return the bottle size
     */
    public static BottleSize doubleToBottleSize(double volume) {
        List<BottleSize> bottleSizes = List.of(BottleSize.values());
        for (BottleSize bottleSize : bottleSizes) {
            if (bottleSize.getVolume() == volume) return bottleSize;
        }
        return null;
    }

    /**
     * Gets volume.
     *
     * @return the volume
     */
    public double getVolume() {
        return volume;
    }
}
