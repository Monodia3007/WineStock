package eu.lilithmonodia.winestock.app;

import java.util.List;

public enum BottleSize {
    PICCOLA(20),
    CHOPINE (25),
    FILLETTE (37.5),
    MEDIUM(50),
    BOUTEILLE(75),
    MAGNUM(150),
    JEROBAM(300),
    REHOBOAM(450),
    MATHUSALEM(600),
    SALMANAZAR(900),
    BALTHAZAR(1200),
    NABUCHODONOSOR(1500),
    MELCHIOR(1800),
    SOUVERAIN(2625),
    PRIMAT(2700),
    MIDAS(3000);
    
    private final double volume;
    
    BottleSize (double volume) {
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }
    
    public static BottleSize doubleToBottleSize(double volume) {
        List<BottleSize> bottleSizes = List.of(BottleSize.values());
        for (BottleSize bottleSize: bottleSizes) {
            if (bottleSize.getVolume() == volume) return bottleSize;
        }
        return null;
    }
}
