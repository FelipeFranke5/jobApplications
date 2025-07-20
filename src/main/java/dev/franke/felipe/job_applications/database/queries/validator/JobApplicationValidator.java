package dev.franke.felipe.job_applications.database.queries.validator;

import dev.franke.felipe.job_applications.database.exception.InvalidRequiredParametersException;
import dev.franke.felipe.job_applications.domain.JobApplication;

import java.time.LocalDateTime;

public record JobApplicationValidator(JobApplication application) {

    public void assertNotNull() {
        if (application() == null) {
            throw new InvalidRequiredParametersException("Job Application instance cannot be null!");
        }
    }

    public void validateTimestamp() {
        assertApplicationTimeNotNull();
        assertApplicationTimeNotAfterNow();
    }

    private void assertApplicationTimeNotNull() {
        if (application().applicationTime() == null) {
            throw new InvalidRequiredParametersException("Timestamp is required! Cannot be null");
        }
    }

    private void assertApplicationTimeNotAfterNow() {
        if (application().applicationTime().isAfter(LocalDateTime.now())) {
            throw new InvalidRequiredParametersException(
                    String.format(
                            "Timestamp of input '%s' cannot be after Timestamp of now '%s'",
                            application().applicationTime(),
                            LocalDateTime.now()
                    )
            );
        }
    }
}
