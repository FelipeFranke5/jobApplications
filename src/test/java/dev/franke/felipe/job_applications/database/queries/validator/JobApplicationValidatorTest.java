package dev.franke.felipe.job_applications.database.queries.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import dev.franke.felipe.job_applications.database.exception.InvalidRequiredParametersException;
import dev.franke.felipe.job_applications.domain.JobApplication;

class JobApplicationValidatorTest {

    @Test
    void testAssertNotNull_WhenJobApplicationIsNotNull_ShouldNotThrow() {
        assertDoesNotThrow(() -> validValidator().assertNotNull());
    }

    @Test
    void testAssertNotNull_WhenJobApplicationIsNull_ShouldThrow() {
        JobApplication jobApplication = null;
        JobApplicationValidator validator = new JobApplicationValidator(jobApplication);
        assertThrows(InvalidRequiredParametersException.class, () -> validator.assertNotNull());
    }

    @Test
    void testValidateTimestamp_WhenApplicationTimeIsNotNull_ShouldNotThrow() {
        assertDoesNotThrow(() -> validValidator().validateTimestamp());
    }

    @Test
    void testValidateTimestamp_WhenApplicationTimeIsNull_ShouldThrow() {
        assertThrows(InvalidRequiredParametersException.class, () -> validatorWithNullTimestamp().validateTimestamp());
    }

    @Test
    void testValidateTimestamp_WhenApplicationTimeIsNow_ShouldNotThrow() {
        assertDoesNotThrow(() -> validValidator().validateTimestamp());
    }

    @Test
    void testValidateTimestamp_WhenApplicationTimeIsAfterNow_ShouldThrow() {
        assertThrows(InvalidRequiredParametersException.class, () -> validatorWithInvalidTimestamp().validateTimestamp());
    }

    private JobApplicationValidator validValidator() {
        return new JobApplicationValidator(validJobApplication());
    }

    private JobApplicationValidator validatorWithNullTimestamp() {
        return new JobApplicationValidator(jobApplicationWithNullTimestamp());
    }

    private JobApplicationValidator validatorWithInvalidTimestamp() {
        return new JobApplicationValidator(jobApplicationWithInvalidTime());
    }

    private JobApplication jobApplicationWithInvalidTime() {
        return new JobApplication(
            "Job Application Test",
            "Job Company Name Test",
            "https://url.com/",
            LocalDateTime.now().plusDays(3)
        );
    }

    private JobApplication jobApplicationWithNullTimestamp() {
        return new JobApplication(
            "Job Application Test",
            "Job Company Name Test",
            "https://url.com/",
            null
        );
    }

    private JobApplication validJobApplication() {
        return new JobApplication(
            "Job Application Test",
            "Job Company Name Test",
            "https://url.com/",
            LocalDateTime.now()
        );
    }
}