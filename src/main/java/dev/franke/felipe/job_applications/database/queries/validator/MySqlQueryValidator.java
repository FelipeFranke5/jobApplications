package dev.franke.felipe.job_applications.database.queries.validator;

import dev.franke.felipe.job_applications.database.exception.ConnectionException;
import dev.franke.felipe.job_applications.database.exception.InvalidRequiredParametersException;
import dev.franke.felipe.job_applications.database.exception.SqlExecutionException;
import dev.franke.felipe.job_applications.domain.JobApplication;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class MySqlQueryValidator {

    public void checkCompanyNameInQuery(String companyName) {
        JobApplication jobApplication = new JobApplication("name", companyName, "url", LocalDateTime.now());
        checkCompanyNameAgainstConstraints(jobApplication);
    }

    public void checkNumberOfInsertionsIsGreaterThanZero(int affectedRows) {
        if (affectedRows <= 0) {
            throw new SqlExecutionException("Insertion not completed successfully!");
        }
    }

    public void checkConnection(Connection connection) {
        checkConnectionIsNull(connection);
        try {
            checkConnectionIsClosedOrReadOnly(connection);
            checkConnectionIsValid(connection);
        } catch (Exception exception) {
            throw new ConnectionException("Error while validating connection! " + exception.getMessage());
        }
    }

    public void checkJobApplicationIsValid(JobApplication jobApplication) {
        checkJobApplicationIsNull(jobApplication);
        checkJobNameAgainstConstraints(jobApplication);
        checkCompanyNameAgainstConstraints(jobApplication);
        checkUrlAgainstConstraints(jobApplication);
        checkTimestamp(jobApplication);
    }

    private void checkConnectionIsNull(Connection connection) {
        if (connection == null) {
            throw new ConnectionException("Connection cannot be null!");
        }
    }

    private void checkConnectionIsClosedOrReadOnly(Connection connection) throws SQLException {
        if (connection.isClosed() || connection.isReadOnly()) {
            throw new ConnectionException("Connection cannot be closed or read-only!");
        }
    }

    private void checkConnectionIsValid(Connection connection) throws SQLException {
        int timeoutInSeconds = 20;
        if (!connection.isValid(timeoutInSeconds)) {
            throw new ConnectionException("Connection is not valid!");
        }
    }

    private void checkJobApplicationIsNull(JobApplication jobApplication) {
        if (jobApplication == null) {
            throw new InvalidRequiredParametersException("Job Application instance cannot be null!");
        }
    }

    private void checkJobNameAgainstConstraints(JobApplication jobApplication) {
        if (jobApplication.jobName() == null) {
            throw new InvalidRequiredParametersException("Job Name cannot be null!");
        }
        if (jobApplication.jobName().isBlank()) {
            throw new InvalidRequiredParametersException("Job Name cannot be blank!");
        }
        int jobNameMaxLength = 40;
        if (jobApplication.jobName().length() >= jobNameMaxLength) {
            throw new InvalidRequiredParametersException("Job Name cannot exceed " + jobNameMaxLength + " chars!");
        }
        if (jobApplication.jobName().contains("../") || jobApplication.jobName().contains("..\\")) {
            throw new InvalidRequiredParametersException("Job Name cannot have file or directory path!");
        }
        checkInputAgainstMaliciousPatterns(jobApplication.jobName());
        checkInputAgainstMaliciousChars(jobApplication.jobName());
    }

    private void checkCompanyNameAgainstConstraints(JobApplication jobApplication) {
        if (jobApplication.companyName() == null) {
            throw new InvalidRequiredParametersException("Company Name cannot be null!");
        }
        if (jobApplication.companyName().isBlank()) {
            throw new InvalidRequiredParametersException("Company Name cannot be blank!");
        }
        int companyNameMaxLength = 100;
        if (jobApplication.companyName().length() >= companyNameMaxLength) {
            throw new InvalidRequiredParametersException("Company Name cannot exceed " + companyNameMaxLength + " chars!");
        }
        if (jobApplication.companyName().contains("../") || jobApplication.companyName().contains("..\\")) {
            throw new InvalidRequiredParametersException("Company Name cannot have file or directory path!");
        }
        checkInputAgainstMaliciousPatterns(jobApplication.companyName());
        checkInputAgainstMaliciousChars(jobApplication.companyName());
    }

    private void checkUrlAgainstConstraints(JobApplication jobApplication) {
        if (jobApplication.url() == null) {
            throw new InvalidRequiredParametersException("URL cannot be null!");
        }
        if (jobApplication.url().isBlank()) {
            throw new InvalidRequiredParametersException("URL cannot be blank!");
        }
        int urlMaxLength = 1024;
        if (jobApplication.url().length() >= urlMaxLength) {
            throw new InvalidRequiredParametersException("URL cannot exceed " + urlMaxLength + " chars!");
        }
        if (jobApplication.url().contains("../") || jobApplication.url().contains("..\\")) {
            throw new InvalidRequiredParametersException("URL cannot have file or directory path!");
        }
        checkInputAgainstMaliciousPatterns(jobApplication.url());
        checkInputAgainstMaliciousChars(jobApplication.url());
    }

    private void checkTimestamp(JobApplication jobApplication) {
        if (jobApplication.applicationTime() == null) {
            throw new InvalidRequiredParametersException("Timestamp is required! Cannot be null");
        }
        if (jobApplication.applicationTime().isAfter(LocalDateTime.now())) {
            throw new InvalidRequiredParametersException(
                    String.format(
                            "Timestamp of input '%s' cannot be after Timestamp of now '%s'",
                            jobApplication.applicationTime(),
                            LocalDateTime.now()
                    )
            );
        }
    }

    private String[] maliciousPatterns() {
        return new String[] {
                "DROP TABLE", "SELECT \\*", "INSERT INTO", "DELETE FROM",
                "USE", "bash -c", "sh -c", "cmd.exe", "eval", "\\$\\{", "eval\\{"
        };
    }

    private void checkInputAgainstMaliciousPatterns(String input) {
        for (String pattern : maliciousPatterns()) {
            if (Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(input).find()) {
                throw new InvalidRequiredParametersException("Invalid INPUT!");
            }
        }
    }

    private void checkInputAgainstMaliciousChars(String input) {
        String dangerousChars = ";&|<>$\\\"'`(){}[]";
        for (char theChar : dangerousChars.toCharArray()) {
            findInvalidChar(input, theChar);
        }
    }

    private void findInvalidChar(String input, char theChar) {
        if (input.indexOf(theChar) != -1) {
            throw new InvalidRequiredParametersException("Invalid CHAR found: " + theChar + " - Input: " + input);
        }
    }
}
