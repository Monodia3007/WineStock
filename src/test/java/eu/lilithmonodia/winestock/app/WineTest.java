package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * The type Wine test.
 */
public class WineTest {
    private Wine w1, w2;

    /**
     * Initialization.
     */
    @BeforeEach
    public void initialization() {
        w1 = new Wine("Romanée-Conti", 1999, 75, "rouge", 2000);
        w2 = new Wine("Grand Echézeau", 2000, 150, "rouge", 5000, "Étiquette légèrement abimée");
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        Wine wine = new Wine("Grand Echézeau", 2000, 150, "rouge", 5000, "truc");
        assertEquals(wine, w2);
    }

    /**
     * Test not equals.
     */
    @Test
    public void testNotEquals() {
        assertNotEquals(w1, w2);
    }

    /**
     * Test to string.
     */
    @Test
    public void testToString() {
        String expected1 = "Wine{name='Romanée-Conti', year=1999, volume=BOUTEILLE(75.0), color=ROUGE, price=2000.0, comment=''}";
        assertEquals(expected1, w1.toString());
        String expected2 = "Wine{name='Grand Echézeau', year=2000, volume=MAGNUM(150.0), color=ROUGE, price=5000.0, comment='Étiquette légèrement abimée'}";
        assertEquals(expected2, w2.toString());
    }
}
