package eu.lilithmonodia.winestock.app;

import eu.lilithmonodia.winestock.exceptions.WineAlreadyInAssortmentException;
import eu.lilithmonodia.winestock.exceptions.WineNotInAssortmentException;
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
        assertDoesNotThrow(() -> {
            assertTrue(assortment.add(wine1));
            assertEquals(1, assortment.getWineList().size());
            assertTrue(wine1.isInAssortment());
        });
    }

    @Test
    void testRemoveWine() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assertTrue(assortment.remove(wine1));
            assertEquals(0, assortment.getWineList().size());
            assertFalse(wine1.isInAssortment());
        });
    }

    @Test
    void testGetIdSetId() {
        int newId = 10;
        assertDoesNotThrow(() -> {
            assortment.setId(newId);
            assertEquals(newId, assortment.getId());
        });
    }

    @Test
    void testGetWineList() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);
            assertEquals(Arrays.asList(wine1, wine2), assortment.getWineList());
        });
    }

    @Test
    void testGetPrice() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);
            assertEquals(150.0, assortment.getTotalPrice(), 0.001);
        });
    }

    @Test
    void testGetWineNames() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);

            String wineNames = assortment.getWineNames();

            assertTrue(wineNames.contains(wine1.getName()));
            assertTrue(wineNames.contains(wine2.getName()));
        });
    }

    @Test
    void testGetYear() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);

            assertEquals(wine1.getYear(), assortment.getYear());
        });
    }

    @Test
    void testToString() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);

            assertNotNull(assortment.toString());
            assertTrue(assortment.toString().contains(wine1.getName()));
        });
    }

    @Test
    void testAddWineAlreadyInAssortment() {
        assertThrows(WineAlreadyInAssortmentException.class, () -> {
            assortment.add(wine1);
            assortment.add(wine1);  // Should throw WineAlreadyInAssortmentException
        });
    }

    @Test
    void testRemoveWineNotInAssortment() {
        assertThrows(WineNotInAssortmentException.class, () -> {
            assortment.remove(wine1);  // Should throw WineNotInAssortmentException
        });
    }

    @Test
    void testEqualsObject() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);

            Assortment otherAssortment = new Assortment();
            assertNotEquals(assortment, otherAssortment);

            Wine wine1InOtherAssortment = new Wine.Builder("Wine1", 2020, 75,
                    "ROUGE", 50.0).build();
            otherAssortment.add(wine1InOtherAssortment);

            assertEquals(assortment, otherAssortment);

            Wine wine2InOtherAssortment = new Wine.Builder("Wine2", 2020, 75,
                    "BLANC", 100.0).build();
            otherAssortment.add(wine2InOtherAssortment);
            assertNotEquals(assortment, otherAssortment);
        });
    }

    @Test
    void testNotEqualToNull() {
        assertNotEquals(null, assortment);  // Should not be equal to null
    }

    @Test
    void testNotEqualToDifferentObject() {
        Object obj = new Object();
        assertNotEquals(assortment, obj);  // Should not be equal to another type of object
    }
    @Test
    void testHashCode() {
        Assortment assortment2 = new Assortment();
        assertEquals(assortment.hashCode(), assortment2.hashCode());
    }

    @Test
    void testNotEqualHashCode() {
        assortment.setId(1);
        Assortment assortment2 = new Assortment();
        assertNotEquals(assortment.hashCode(), assortment2.hashCode());
    }
}