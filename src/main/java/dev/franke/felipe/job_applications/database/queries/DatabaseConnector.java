package dev.franke.felipe.job_applications.database.queries;

import java.sql.Connection;

public interface DatabaseConnector {
    Connection connect();
    void disconnect(Connection connection);
}
