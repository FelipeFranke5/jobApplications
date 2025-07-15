package dev.franke.felipe.job_applications.database.queries.validator;

import dev.franke.felipe.job_applications.database.exception.ConnectionException;
import dev.franke.felipe.job_applications.database.exception.InvalidRequiredParametersException;
import dev.franke.felipe.job_applications.database.exception.SqlExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySqlQueryValidatorTest {

    private AutoCloseable closeable;
    private final MySqlQueryValidator mySqlQueryValidator = new MySqlQueryValidator();

    @Mock
    private Connection databaseConnection;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testCheckCompanyNameInQuery_WhenNameIsValid_ShouldNotThrow() {
        String companyName = "Valid Company";
        assertDoesNotThrow(() -> mySqlQueryValidator.checkCompanyNameInQuery(companyName));
    }

    @Test
    void testCheckCompanyNameInQuery_WhenNameIsInvalid_ShouldThrow() {
        String companyName = "";
        assertThrows(
                InvalidRequiredParametersException.class,
                () -> mySqlQueryValidator.checkCompanyNameInQuery(companyName)
        );
    }

    @Test
    void testCheckNumberOfInsertionsIsGreaterThanZero_WhenNumberIsGreaterThanZero_ShouldNotThrow() {
        int affectedRows = 4;
        assertDoesNotThrow(() -> mySqlQueryValidator.checkNumberOfInsertionsIsGreaterThanZero(affectedRows));
    }

    @Test
    void testCheckNumberOfInsertionsIsGreaterThanZero_WhenNumberIsZero_ShouldThrow() {
        int affectedRows = 0;
        assertThrows(
                SqlExecutionException.class,
                () -> mySqlQueryValidator.checkNumberOfInsertionsIsGreaterThanZero(affectedRows)
        );
    }

    @Test
    void testCheckConnection_WhenConnectionIsValid_ShouldNotThrow() throws SQLException {
        int timeOutInSeconds = 20;
        Mockito.when(databaseConnection.isClosed()).thenReturn(false);
        Mockito.when(databaseConnection.isReadOnly()).thenReturn(false);
        Mockito.when(databaseConnection.isValid(timeOutInSeconds)).thenReturn(true);
        assertDoesNotThrow(() -> mySqlQueryValidator.checkConnection(databaseConnection));
    }

    @Test
    void testCheckConnection_WhenConnectionIsNull_ShouldThrow() {
        Connection nullConnection = null; //
        assertThrows(
                ConnectionException.class,
                () -> mySqlQueryValidator.checkConnection(nullConnection)
        );
    }

    @Test
    void testCheckConnection_WhenConnectionIsClosed_ShouldThrow() throws SQLException {
        Mockito.when(databaseConnection.isClosed()).thenReturn(true);
        assertThrows(
                ConnectionException.class,
                () -> mySqlQueryValidator.checkConnection(databaseConnection)
        );
    }

    @Test
    void testCheckConnection_WhenConnectionIsReadOnly_ShouldThrow() throws SQLException {
        Mockito.when(databaseConnection.isReadOnly()).thenReturn(true);
        assertThrows(
                ConnectionException.class,
                () -> mySqlQueryValidator.checkConnection(databaseConnection)
        );
    }

    @Test
    void testCheckConnectionIsValid_WhenUnexpectedException_ShouldThrow() throws SQLException {
        Mockito.when(databaseConnection.isClosed()).thenThrow(new SQLException("Base exception"));
        assertThrows(
                ConnectionException.class,
                () -> mySqlQueryValidator.checkConnection(databaseConnection)
        );
    }

}