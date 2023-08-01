package eu.lilithmonodia.winestock.app;

import eu.lilithmonodia.winestock.exceptions.InvalidBottleVolumeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Bottle size test.
 */
public class BottleSizeTest {
    /**
     * Test invalid volume.
     */
    @Test
    public void testInvalidVolume() {
        assertThrows(InvalidBottleVolumeException.class, () -> {
            BottleSize.doubleToBottleSize(39);
        });
    }
}
