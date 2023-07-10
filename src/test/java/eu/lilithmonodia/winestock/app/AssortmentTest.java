package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Assortment test.
 */
public class AssortmentTest {
    private Wine w1, w2, w3, w4, w5;
    private Assortment a1, a2;

    /**
     * Initialisation.
     */
    @BeforeEach
    public void initialisation() {
        w1 = new Wine("Romanée-Conti", 1999, 75, "rouge", 2000);
        w2 = new Wine("Grand Echézeau", 2000, 150, "rouge", 5000, "Étiquette légèrement abimée");
        w3 = new Wine("Echézeau", 1995, 3000, "rouge", 1999.99);
        w4 = new Wine("Corton", 2014, 1200, "rouge", 2500);
        w5 = new Wine("Corton Charlemagne", 1999, 3000, "rouge", 200000);
        a1 = new Assortment();
        a2 = new Assortment(List.of(w3, w4));
    }

    /**
     * Test add.
     */
    @Test
    public void testAdd() {
        assertTrue(a1.add(w1));
        assertTrue(a2.add(w5));
        assertFalse(a1.add(w2));
        assertFalse(a1.add(w5));
    }

    /**
     * Test remove.
     */
    @Test
    public void testRemove() {
        assertFalse(a2.remove(w2));
        assertTrue(a2.remove(w3));
    }

    /**
     * Test get price.
     */
    @Test
    public void testGetPrice() {
        assertEquals(4499.99, a2.getPrice());
    }
}
