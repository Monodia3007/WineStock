package eu.lilithmonodia.winestock.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

public class WineTest {
    private Wine w1, w2;

    @BeforeEach
    public void initialization () {
        w1 = new Wine("Romanée-Conti", Year.of(1999), 75, "rouge", 2000);
        w2 = new Wine("Grand Echézeau", Year.of(2000), 150, "rouge", 5000, "Étiquette légèrement abimée");
    }

    @Test
    public void testEquals () {
        Wine wine = new Wine("Grand Echézeau", Year.of(2000), 150, "rouge", 5000, "truc");
        assertEquals(wine, w2);
    }

    @Test
    public void testNotEquals () {
        assertNotEquals(w1, w2);
    }

    @Test
    public void testToString () {
        String expected1 = "Wine{name='Romanée-Conti', year=1999, volume=BOUTEILLE(75.0), color=ROUGE, price=2000, comment=''}";
        assertEquals(expected1, w1.toString());
        String expected2 = "Wine{name='Grand Echézeau', year=2000, volume=MAGNUM(150.0), color=ROUGE, price=5000, comment='Étiquette légèrement abimée'}";
        assertEquals(expected2, w2.toString());
    }
}
