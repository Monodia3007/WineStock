package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class WineTest {
    Wine wine;

    @BeforeEach
    void setUp() {
        wine = new Wine("Cabernet", 2019, 75.0, "ROUGE", 150.0);
    }

    @Test
    void wineCreationTest() {
        assertNotNull(wine);
    }

    @Test
    void getName() {
        assertEquals("Cabernet", wine.getName());
    }

    @Test
    void setName() {
        wine.setName("Merlot");
        assertEquals("Merlot", wine.getName());
    }

    @Test
    void getYear() {
        assertEquals(Year.of(2019), wine.getYear());
    }

    @Test
    void setYear() {
        wine.setYear(Year.of(2020));
        assertEquals(Year.of(2020), wine.getYear());
    }

    @Test
    void getVolume() {
        assertEquals(BottleSize.BOUTEILLE, wine.getVolume());
    }

    @Test
    void setVolume() {
        wine.setVolume(150.0);
        assertEquals(BottleSize.MAGNUM, wine.getVolume());
    }

    @Test
    void getColor() {
        assertEquals(Color.ROUGE, wine.getColor());
    }

    @Test
    void setColor() {
        wine.setColor(Color.BLANC);
        assertEquals(Color.BLANC, wine.getColor());
    }

    @Test
    void getPrice() {
        assertEquals(150.0, wine.getPrice());
    }

    @Test
    void setPrice() {
        wine.setPrice(200.0);
        assertEquals(200.0, wine.getPrice());
    }

    @Test
    void getComment() {
        assertEquals("", wine.getComment());
    }

    @Test
    void setComment() {
        wine.setComment("Delicious");
        assertEquals("Delicious", wine.getComment());
    }

    @Test
    void isInAssortment() {
        assertFalse(wine.isInAssortment());
    }

    @Test
    void setInAssortment() {
        wine.setInAssortment(true);
        assertTrue(wine.isInAssortment());
    }
}