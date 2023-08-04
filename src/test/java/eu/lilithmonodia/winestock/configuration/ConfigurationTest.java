package eu.lilithmonodia.winestock.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Unit tests for the {@link Configuration} class.
 */
public class ConfigurationTest {

    private Configuration configuration;

    @BeforeEach
    public void setUp() throws IOException {
        configuration = Configuration.fromConfig();
    }

    @Test
    public void testDatabaseHost() {
        String expected = "jdbc:postgresql://lilith-monodia.eu:5432/winestock"; // Please adjust this
        String actual = configuration.databaseHost();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testDatabaseUser() {
        String expected = ""; // Please adjust this
        String actual = configuration.databaseUser();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testUserPassword() {
        String expected = ""; // Please adjust this
        String actual = configuration.userPassword();
        Assertions.assertEquals(expected, actual);
    }
}
