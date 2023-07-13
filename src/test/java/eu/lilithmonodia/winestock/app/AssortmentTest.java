package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AssortmentTest {

    private Assortment assortment;
    private Wine wine1;
    private Wine wine2;

    @BeforeEach
    void setUp() {
        assortment = new Assortment();
        wine1 = new Wine.Builder("Wine1", 2020, 75, "ROUGE", 50.0).build();
        wine2 = new Wine.Builder("Wine2", 2020, 75, "BLANC", 100.0).build();
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
        assertEquals(150.0, assortment.getTotalPrice(), 0.001);
    }
}