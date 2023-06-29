package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssortmentTest {
    private Wine w1, w2, w3, w4;
    private Assortment a1, a2;

    @BeforeEach
    public void initialisation() {
        w1 = new Wine("Romanée-Conti", 1999, 75, "rouge", 2000);
        w2 = new Wine("Grand Echézeau", 2000, 150, "rouge", 5000, "Étiquette légèrement abimée");
        w3 = new Wine("Echézeau", 1995, 3000, "rouge", 1999.99);
        w4 = new Wine("Corton", 2014, 1200, "rouge", 2500);
        a1 = new Assortment();
        a2 = new Assortment(List.of(w3, w4));
    }

    @Test
    public void testAdd() {
        assertTrue(a1.add(w1));
        assertTrue(a2.add(w2));
        assertFalse(a1.add(w2));
    }

    @Test
    public void testRemove() {
        assertFalse(a2.remove(w2));
        assertTrue(a2.remove(w3));
    }

    @Test
    public void testGetPrice() {
        assertEquals(4499.99, a2.getPrice());
    }
}
