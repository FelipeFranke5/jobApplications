package dev.franke.felipe.job_applications.database.queries.validator;

import dev.franke.felipe.job_applications.database.exception.ConnectionException;
import dev.franke.felipe.job_applications.database.exception.SqlExecutionException;
import dev.franke.felipe.job_applications.domain.JobApplication;

import java.sql.Connection;

public class MySqlQueryValidator implements DatabaseValidator {

    private final static int JOB_NAME_MAX_LENGTH = 40;
    private final static String JOB_NAME_DESCRIPTION = "Job Name";

    private final static int COMPANY_NAME_MAX_LENGTH = 100;
    private final static String COMPANY_NAME_DESCRIPTION = "Company Name";

    private final static int URL_MAX_LENGTH = 1024;
    private final static String URL_DESCRIPTION = "URL";

    @Override
    public void checkCompanyNameInQuery(String companyName) {
        StringValidator validator = StringValidator.getValidator(
                COMPANY_NAME_MAX_LENGTH, companyName, COMPANY_NAME_DESCRIPTION
        );
        StringValidator.validateField(validator);
    }

    @Override
    public void checkNumberOfInsertionsIsGreaterThanZero(int affectedRows) {
        if (affectedRows <= 0) {
            throw new SqlExecutionException("Insertion not completed successfully!");
        }
    }

    @Override
    public void checkConnection(Connection connection) {
        ConnectionValidator connectionValidator = new ConnectionValidator(connection);
        connectionValidator.assertNotNull();
        try {
            connectionValidator.assertNotClosedOrReadOnly();
            connectionValidator.assertIsValid();
        } catch (Exception exception) {
            throw new ConnectionException(
                    String.format(
                            "Exception of type '%s' thrown with message: '%s' during connection validation.",
                            exception.getClass().getSimpleName(),
                            exception.getMessage()
                    )
            );
        }
    }

    @Override
    public void checkJobApplicationIsValid(JobApplication jobApplication) {
        JobApplicationValidator jobApplicationValidator = new JobApplicationValidator(jobApplication);
        jobApplicationValidator.assertNotNull();
        jobApplicationValidator.validateTimestamp();
        StringValidator jobNameValidator = StringValidator.getValidator(
                JOB_NAME_MAX_LENGTH, jobApplication.jobName(), JOB_NAME_DESCRIPTION
        );
        StringValidator companyNameValidator = StringValidator.getValidator(
                COMPANY_NAME_MAX_LENGTH, jobApplication.companyName(), COMPANY_NAME_DESCRIPTION
        );
        StringValidator urlValidator = StringValidator.getValidator(
                URL_MAX_LENGTH, jobApplication.url(), URL_DESCRIPTION
        );
        StringValidator.validateField(jobNameValidator);
        StringValidator.validateField(companyNameValidator);
        StringValidator.validateField(urlValidator);
    }
}
