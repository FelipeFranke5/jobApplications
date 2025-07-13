package dev.franke.felipe.job_applications.database.exception;

public class InvalidRequiredParametersException extends RuntimeException {
    public InvalidRequiredParametersException(String message) {
        super(message);
    }
}
