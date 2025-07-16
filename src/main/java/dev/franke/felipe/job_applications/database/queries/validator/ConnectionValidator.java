package dev.franke.felipe.job_applications.database.queries.validator;

import dev.franke.felipe.job_applications.database.exception.ConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

public record ConnectionValidator(Connection connection) {

    public void assertNotNull() {
        if (connection() == null) {
            throw new ConnectionException("Connection cannot be null!");
        }
    }

    public void assertNotClosedOrReadOnly() throws SQLException {
        if (connection().isClosed() || connection().isReadOnly()) {
            throw new ConnectionException("Connection cannot be closed or read-only!");
        }
    }

    public void assertIsValid() throws SQLException {
        int timeoutInSeconds = 20;
        if (!connection().isValid(timeoutInSeconds)) {
            throw new ConnectionException("Connection is not valid!");
        }
    }

}
