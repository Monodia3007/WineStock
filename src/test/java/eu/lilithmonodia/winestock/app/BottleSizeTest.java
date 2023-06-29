package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class BottleSizeTest {
    @Test
    public void testInvalidVolume() {
        assertNull(BottleSize.doubleToBottleSize(39));
    }
}
