package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.Test;
import java.time.Year;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WineTest {

    Wine wineToTest = new Wine("Cabernet", 2021, 75, "ROUGE", 20.0);

    @Test
    public void testNameGetterAndSetter() {
        String testName = "Merlot";
        wineToTest.setName(testName);
        assertEquals(testName, wineToTest.getName());
    }

    @Test
    public void testYearGetterAndSetter() {
        Year testYear = Year.of(1999);
        wineToTest.setYear(testYear);
        assertEquals(testYear, wineToTest.getYear());
    }

    @Test
    public void testPriceGetterAndSetter() {
        double testPrice = 15.0;
        wineToTest.setPrice(testPrice);
        assertEquals(testPrice, wineToTest.getPrice());
    }

    @Test
    public void testEqualsAndHashCode() {
        Wine otherWine = new Wine("Cabernet", 2021, 75.0, "ROUGE", 20.0);
        assertEquals(wineToTest, otherWine);
        assertEquals(wineToTest.hashCode(), otherWine.hashCode());

        otherWine.setPrice(25.0);
        assertNotEquals(wineToTest, otherWine);
        assertNotEquals(wineToTest.hashCode(), otherWine.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "Wine{name='Cabernet', year=2021, volume=" +
                "BOUTEILLE(75.0)" + ", color=" +
                "ROUGE" + ", price=20.0, comment=''}";
        assertEquals(expectedString, wineToTest.toString());
    }
}