package eu.lilithmonodia.winestock.data;

import eu.lilithmonodia.winestock.exceptions.InvalidYearException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class WineTest {
    Wine wine;

    @BeforeEach
    void initialisation() {
        wine = new Wine.Builder("Cabernet", 2019, 75.0, "ROUGE", 150.0).build();
        wine.setId(0);
    }

    @Test
    void wineCreationTest() {
        assertNotNull(wine);
    }

    @Test
    void testGetId() {
        assertEquals(0, wine.getId());
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
    void testSetYearInvalid() {
        assertThrows(InvalidYearException.class, () -> wine.setYear(Year.now().plusYears(1)));
    }

    @Test
    void testGetVolume() {
        assertEquals(BottleSize.BOUTEILLE, wine.getVolume());
    }

    @Test
    void testSetVolume() {
        wine.setVolume(75);
        assertEquals(BottleSize.BOUTEILLE, wine.getVolume());
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
        // otherWine
        Wine otherWine = new Wine.Builder("Cabernet", 2019, 75.0, "ROUGE", 150.0).build();
        assertNotEquals(wine, otherWine); // They are not equal because wine.setVolume(150.0) will set the volume to BottleSize.BOUTEILLE and not MAGNUM
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
        assertEquals(BottleSize.BOUTEILLE, wine.getVolume());  // Test the volume with 75.0 which corresponds to BOUTEILLE
        assertEquals(Color.ROUGE, wine.getColor());
        assertEquals(150.0, wine.getPrice());
        assertEquals("", wine.getComment());  // Default comment value
        assertFalse(wine.isInAssortment());  // Default assortment value
    }

    @Test
    void builderSetCommentCreatesCorrectWineObject() {
        Wine wineWithComment = new Wine.Builder("Pinot Noir", 2021, 75.0, "ROUGE", 120.0).comment("Excellent").build();  // Use 75.0 instead of 150.0

        assertEquals("Pinot Noir", wineWithComment.getName());
        assertEquals(Year.of(2021), wineWithComment.getYear());
        assertEquals(BottleSize.BOUTEILLE, wineWithComment.getVolume());  // Test the volume with 75.0 which corresponds to BOUTEILLE
        assertEquals(Color.ROUGE, wineWithComment.getColor());
        assertEquals(120.0, wineWithComment.getPrice());
        assertEquals("Excellent", wineWithComment.getComment());
        assertFalse(wineWithComment.isInAssortment());  // Default assortment value
    }

    @Test
    void testInvalidColor() {
        assertThrows(IllegalArgumentException.class, () -> new Wine.Builder("Cabernet", 2018, 75.0, "INVALID_COLOR", 150.0).build());
    }

    @Test
    void testInvalidBottleSize() {
        assertThrows(IllegalArgumentException.class, () -> new Wine.Builder("Cabernet", 2018, 999, "ROUGE", 150.0).build());
    }

    @Test
    void testInvalidYearInFutureInBuilder() {
        assertThrows(IllegalArgumentException.class, () -> new Wine.Builder("Cabernet", Year.now().getValue() + 1, 75.0, "ROUGE", 150.0).build());
    }

    @Test
    void testSetVolumeInvalid() {
        assertDoesNotThrow(() -> {
            wine.setVolume(999);
            assertEquals(BottleSize.BOUTEILLE, wine.getVolume());
        });
    }

    @Test
    void testCompareTo() {
        Wine otherWine = new Wine.Builder("Cabernet", 2019, 75.0, "ROUGE", 150.0).build();
        otherWine.setId(1); // This ID is greater than wine's ID, which is 0.

        assertTrue(wine.compareTo(otherWine) < 0); // wine's ID is less than otherWine's ID.

        otherWine.setId(0); // Now, both IDs are equal.

        assertEquals(0, wine.compareTo(otherWine)); // Since both IDs are equal, compareTo should return 0.

        wine.setId(2); // Now, wine's ID is greater than otherWine's ID.

        assertTrue(wine.compareTo(otherWine) > 0); // Since wine's ID is greater, compareTo should return a positive number.
    }
}