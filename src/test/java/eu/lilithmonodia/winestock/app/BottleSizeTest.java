package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The type Bottle size test.
 */
public class BottleSizeTest {
    /**
     * Test invalid volume.
     */
    @Test
    public void testInvalidVolume() {
        assertNull(BottleSize.doubleToBottleSize(39));
    }
}
