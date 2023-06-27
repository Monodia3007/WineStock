package eu.lilithmonodia.winestock.app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BottleSizeTest {
    @Test
    public void testInvalidVolume () {
        assertNull(BottleSize.doubleToBottleSize(39));
    }
}
