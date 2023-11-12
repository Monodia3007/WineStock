package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class WineTest {
    Wine wine;

    @BeforeEach
    void initialisation() {
        wine = new Wine.Builder("Cabernet", 2019, 75.0, "ROUGE", 150.0).build();
    }

    @Test
    void wineCreationTest() {
        assertNotNull(wine);
    }

    @Test
    void testGetId() {
        assertEquals(5, wine.getId());
    }

    @Test
    void testSetId() {
        wine.setId(1);
        assertEquals(1, wine.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Cabernet", wine.getName());
    }

    @Test
    void testSetName() {
        wine.setName("Merlot");
        assertEquals("Merlot", wine.getName());
    }

    @Test
    void testGetYear() {
        assertEquals(Year.of(2019), wine.getYear());
    }

    @Test
    void testSetYear() {
        assertDoesNotThrow(() -> {
            wine.setYear(Year.of(2020));
            assertEquals(Year.of(2020), wine.getYear());
        });
    }

    @Test
    void testGetVolume() {
        assertEquals(BottleSize.BOUTEILLE, wine.getVolume());
    }

    @Test
    void testSetVolume() {
        wine.setVolume(150.0);
        assertEquals(BottleSize.MAGNUM, wine.getVolume());
    }

    @Test
    void testGetColor() {
        assertEquals(Color.ROUGE, wine.getColor());
    }

    @Test
    void testSetColor() {
        wine.setColor(Color.BLANC);
        assertEquals(Color.BLANC, wine.getColor());
    }

    @Test
    void testGetPrice() {
        assertEquals(150.0, wine.getPrice());
    }

    @Test
    void testSetPrice() {
        wine.setPrice(200.0);
        assertEquals(200.0, wine.getPrice());
    }

    @Test
    void testGetComment() {
        assertEquals("", wine.getComment());
    }

    @Test
    void testSetComment() {
        wine.setComment("Delicious");
        assertEquals("Delicious", wine.getComment());
    }

    @Test
    void testIsInAssortment() {
        assertFalse(wine.isInAssortment());
    }

    @Test
    void testSetInAssortment() {
        wine.setInAssortment(true);
        assertTrue(wine.isInAssortment());
    }

    @Test
    void testEqualsAndHashCode() {
        Wine otherWine = new Wine.Builder("Cabernet", 2019, 75.0, "ROUGE", 150.0).build();
        assertEquals(wine, otherWine);
        assertEquals(wine.hashCode(), otherWine.hashCode());

        otherWine.setPrice(25.0);
        assertNotEquals(wine, otherWine);
        assertNotEquals(wine.hashCode(), otherWine.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "Wine{name='Cabernet', year=2019, volume=" +
                "BOUTEILLE(75.0)" + ", color=" +
                "ROUGE" + ", price=150.0, comment=''}";
        assertEquals(expectedString, wine.toString());
    }

    @Test
    void builderCreatesCorrectWineObject() {
        assertEquals("Cabernet", wine.getName());
        assertEquals(Year.of(2019), wine.getYear());
        assertEquals(BottleSize.BOUTEILLE, wine.getVolume());
        assertEquals(Color.ROUGE, wine.getColor());
        assertEquals(150.0, wine.getPrice());
        assertEquals("", wine.getComment());  // Default comment value
        assertFalse(wine.isInAssortment());  // Default assortment value
    }

    @Test
    void builderSetCommentCreatesCorrectWineObject() {
        Wine wineWithComment = new Wine.Builder("Pinot Noir", 2021, 75.0, "ROUGE", 120.0).comment("Excellent").build();

        assertEquals("Pinot Noir", wineWithComment.getName());
        assertEquals(Year.of(2021), wineWithComment.getYear());
        assertEquals(BottleSize.BOUTEILLE, wineWithComment.getVolume());
        assertEquals(Color.ROUGE, wineWithComment.getColor());
        assertEquals(120.0, wineWithComment.getPrice());
        assertEquals("Excellent", wineWithComment.getComment());
        assertFalse(wineWithComment.isInAssortment());  // Default assortment value
    }
}