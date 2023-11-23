package eu.lilithmonodia.winestock.data;

import eu.lilithmonodia.winestock.exceptions.InvalidBottleVolumeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The type Bottle size test.
 */
class BottleSizeTest {
    /**
     * Test invalid volume.
     */
    @Test
    void testInvalidVolume() {
        assertThrows(InvalidBottleVolumeException.class, () -> BottleSize.doubleToBottleSize(39));
    }
}
