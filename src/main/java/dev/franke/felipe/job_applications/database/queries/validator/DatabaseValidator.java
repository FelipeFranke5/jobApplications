package dev.franke.felipe.job_applications.database.queries.validator;

import java.sql.Connection;

import dev.franke.felipe.job_applications.domain.JobApplication;

public interface DatabaseValidator {
    void checkConnection(Connection connection);
    void checkCompanyNameInQuery(String companyName);
    void checkJobApplicationIsValid(JobApplication jobApplication);
    void checkNumberOfInsertionsIsGreaterThanZero(int affectedRows);
}
