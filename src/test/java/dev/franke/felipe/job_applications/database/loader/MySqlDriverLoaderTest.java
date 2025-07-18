package dev.franke.felipe.job_applications.database.loader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class MySqlDriverLoaderTest {

    @Test
    void testLoadLoadsSuccessfully() {
        assertDoesNotThrow(() -> {
            MySqlDriverLoader mySqlDriverLoader = new MySqlDriverLoader();
            mySqlDriverLoader.load();
        });
    }
}
