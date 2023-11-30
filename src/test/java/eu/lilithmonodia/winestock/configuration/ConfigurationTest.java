package eu.lilithmonodia.winestock.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Unit tests for the {@link Configuration} class.
 */
class ConfigurationTest {

    private Configuration configuration;

    @BeforeEach
    public void setUp() throws IOException {
        configuration = Configuration.fromConfig();
    }

    @Test
    void testDatabaseHost() {
        String expected = "jdbc:postgresql://lilith-monodia.eu:5432/winestock?sslmode=disable"; // Please adjust this
        String actual = configuration.databaseHost();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDatabaseUser() {
        String expected = ""; // Please adjust this
        String actual = configuration.databaseUser();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUserPassword() {
        String expected = ""; // Please adjust this
        String actual = configuration.userPassword();
        Assertions.assertEquals(expected, actual);
    }
}
