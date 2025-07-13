package dev.franke.felipe.job_applications.database.exception;

public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}
