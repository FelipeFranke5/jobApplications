package dev.franke.felipe.job_applications.database.exception;

public class SqlExecutionException extends RuntimeException {
    public SqlExecutionException(String message) {
        super(message);
    }
}
