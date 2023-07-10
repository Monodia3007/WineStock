package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AssortmentTest {

    private Assortment assortment;
    private Wine wine1;
    private Wine wine2;

    @BeforeEach
    void setUp() {
        assortment = new Assortment();
        wine1 = new Wine("Wine1", 2020, 75, "ROUGE", 50.0);
        wine2 = new Wine("Wine2", 2020, 75, "BLANC", 100.0);
    }

    @Test
    void testAddWine() {
        assertTrue(assortment.add(wine1));
        assertEquals(1, assortment.getWineList().size());
        assertTrue(wine1.isInAssortment());
    }

    @Test
    void testAddWineAlreadyInAssortment() {
        assortment.add(wine1);
        assertFalse(assortment.add(wine1));  // Addition should return false for wine already in assortment
    }

    @Test
    void testRemoveWine() {
        assortment.add(wine1);
        assertTrue(assortment.remove(wine1));
        assertEquals(0, assortment.getWineList().size());
        assertFalse(wine1.isInAssortment());
    }

    @Test
    void testRemoveWineNotInAssortment() {
        assertFalse(assortment.remove(wine1));  // Removal should return false for wine not in assortment
    }

    @Test
    void testGetWineList() {
        assortment.add(wine1);
        assortment.add(wine2);
        assertEquals(Arrays.asList(wine1, wine2), assortment.getWineList());
    }

    @Test
    void testGetPrice() {
        assortment.add(wine1);
        assortment.add(wine2);
        assertEquals(150.0, assortment.getPrice(), 0.001);
    }

    @Test
    void testEqualsAndHashCode() {
        Assortment assortment1 = new Assortment(Collections.singletonList(wine1));
        Assortment assortment2 = new Assortment(Collections.singletonList(wine1));
        Assortment assortment3 = new Assortment(Collections.singletonList(wine2));

        assertEquals(assortment1, assortment2);   // Two assortments with same wine should be equal
        assertEquals(assortment1.hashCode(), assortment2.hashCode());  // and have same hash code

        assertNotEquals(assortment1, assortment3);  // Two assortments with different wines should not be equal
        assertNotEquals(assortment1.hashCode(), assortment3.hashCode());  // and have different hash codes

        assertNotEquals(null, assortment1);  // Assortment should not be equal to null
        assertNotEquals(assortment1, new Object());  // Assortment should not be equal to an object of different type

        Assortment assortment4 = new Assortment(Collections.singletonList(wine1));
        assortment4.remove(wine1);
        assertNotEquals(assortment1, assortment4);  // Assortments with different wines (even if same at some point) should not be equal
        assertNotEquals(assortment1.hashCode(), assortment4.hashCode());  // and have different hash codes
    }
}