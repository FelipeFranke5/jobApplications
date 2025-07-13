package dev.franke.felipe.job_applications.database.queries;

import dev.franke.felipe.job_applications.database.exception.ConnectionException;
import dev.franke.felipe.job_applications.database.exception.DisconnectionException;
import dev.franke.felipe.job_applications.database.exception.InvalidCredentialsException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector implements DatabaseConnector {

    private final String username;
    private final String password;
    private String url;

    public MySqlConnector(String username, String password) {
        this.username = username;
        this.password = password;
        assertUsernameIsValid();
        assertPasswordIsValid();
        setUrl();
    }

    @Override
    public Connection connect() {
        try {
            return DriverManager.getConnection(getUrl());
        } catch (SQLException sqlException) {
            throw new ConnectionException("Failed to initialize! " + sqlException.getMessage());
        }
    }

    @Override
    public void disconnect(Connection connection) {
        if (connection == null) throw new DisconnectionException("Connection cannot be null!");
        try {
            connection.close();
        } catch (SQLException sqlException) {
            throw new DisconnectionException("Could not close connection! " + sqlException.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    private void assertUsernameIsValid() {
        if (usernameIsNullOrBlank()) throw new InvalidCredentialsException("Username cannot be null or blank!");
    }

    private void assertPasswordIsValid() {
        if (passwordIsNullOrBlank()) throw new InvalidCredentialsException("Password cannot be null or blank!");
    }

    private boolean usernameIsNullOrBlank() {
        return getUsername() == null || getUsername().isBlank();
    }

    private boolean passwordIsNullOrBlank() {
        return getPassword() == null || getPassword().isBlank();
    }

    private void setUrl() {
        var initialUrl = "jdbc:mysql://localhost/jobapps?";
        var usernameAndPasswordPart = "user=" + getUsername() + "&password=" + getPassword();
        this.url = initialUrl + usernameAndPasswordPart;
    }
}
