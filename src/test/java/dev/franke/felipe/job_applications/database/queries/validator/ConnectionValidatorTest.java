package dev.franke.felipe.job_applications.database.queries.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dev.franke.felipe.job_applications.database.exception.ConnectionException;

class ConnectionValidatorTest {

    private AutoCloseable autoCloseable;

    @Mock
    private Connection mockedConnection;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testAssertNotNull_WhenConnectionIsNotNull_ShouldNotThrow() {
        assertDoesNotThrow(() -> validatorForValidConnection().assertNotNull());
    }

    @Test
    void testAssertNotNull_WhenConnectionIsNull_ShouldThrow() {
        ConnectionValidator validator = new ConnectionValidator(null);
        assertThrows(ConnectionException.class, () -> validator.assertNotNull());
    }

    @Test
    void testAssertNotClosedOrReadOnly_WhenConnectionIsOpen_ShouldNotThrow() throws SQLException {
        Mockito.when(mockedConnection.isClosed()).thenReturn(false);
        Mockito.when(mockedConnection.isReadOnly()).thenReturn(false);
        assertDoesNotThrow(() -> validatorForValidConnection().assertNotClosedOrReadOnly());
    }

    @Test
    void testAssertNotClosedOrReadOnly_WhenConnectionIsClosed_ShouldThrow() throws SQLException {
        Mockito.when(mockedConnection.isClosed()).thenReturn(true);
        Mockito.when(mockedConnection.isReadOnly()).thenReturn(false);
        assertThrows(ConnectionException.class, () -> validatorForValidConnection().assertNotClosedOrReadOnly());
    }

    @Test
    void testAssertNotClosedOrReadOnly_WhenConnectionIsReadOnly_ShouldThrow() throws SQLException {
        Mockito.when(mockedConnection.isClosed()).thenReturn(false);
        Mockito.when(mockedConnection.isReadOnly()).thenReturn(true);
        assertThrows(ConnectionException.class, () -> validatorForValidConnection().assertNotClosedOrReadOnly());
    }

    private ConnectionValidator validatorForValidConnection() {
        return new ConnectionValidator(mockedConnection);
    }

}
