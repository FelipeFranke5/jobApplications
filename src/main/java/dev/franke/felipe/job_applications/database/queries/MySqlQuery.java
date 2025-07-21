package dev.franke.felipe.job_applications.database.queries;

import dev.franke.felipe.job_applications.database.exception.SqlExecutionException;
import dev.franke.felipe.job_applications.database.queries.validator.MySqlQueryValidator;
import dev.franke.felipe.job_applications.domain.JobApplication;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MySqlQuery implements DatabaseQuery {

    private static final String TABLE_NAME = "applications";
    private static final MySqlQueryValidator VALIDATOR = new MySqlQueryValidator();

    @Override
    public List<JobApplication> getJobApplicationsByCompanyName(Connection connection, String companyName) {
        VALIDATOR.checkConnection(connection);
        VALIDATOR.checkCompanyNameInQuery(companyName);
        try (PreparedStatement preparedStatement = connection.prepareStatement(filterByCompanyNameSqlString())) {
            return getJobApplications(companyName, preparedStatement);
        } catch (SQLException sqlException) {
            throw new SqlExecutionException(
                String.format(
                    "Exception of type %s thrown while querying with message '%s' and code = %s", 
                    "SqlExecutionException",
                    sqlException.getMessage(),
                    sqlException.getErrorCode()
                )
            );
        }
    }

    @Override
    public void insertJobApplication(Connection connection, JobApplication jobApplication) {
        VALIDATOR.checkConnection(connection);
        VALIDATOR.checkJobApplicationIsValid(jobApplication);
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertJobApplicationSqlString())) {
            VALIDATOR.checkNumberOfInsertionsIsGreaterThanZero(executeInsertion(jobApplication, preparedStatement));
        } catch (SQLException sqlException) {
            throw new SqlExecutionException(
                String.format(
                    "Could not insert values. SQL Message: %s - Code = %s",
                    sqlException.getMessage(),
                    sqlException.getErrorCode()
                )
            );
        }
    }

    private String filterByCompanyNameSqlString() {
        return "SELECT * FROM " + TABLE_NAME + " WHERE company_name = ?";
    }

    private String insertJobApplicationSqlString() {
        return "INSERT INTO " + TABLE_NAME + "(job_name, company_name, url, application_time) VALUES (?, ?, ?, ?)";
    }

    private List<JobApplication> getJobApplications(String companyName, PreparedStatement preparedStatement) 
    throws SQLException {
        try (ResultSet resultSet = getQueryPreparedStatement(companyName, preparedStatement).executeQuery()) {
            return mapResultSetToArrayList(resultSet);
        }
    }

    private PreparedStatement getQueryPreparedStatement(
        String companyName, PreparedStatement preparedStatement
    ) throws SQLException {
        int companyNameIndex = 1;
        preparedStatement.setString(companyNameIndex, companyName);
        return preparedStatement;
    }

    private List<JobApplication> mapResultSetToArrayList(ResultSet resultSet) throws SQLException {
        List<JobApplication> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(resultSetToJobApplication(resultSet));
        }
        return results;
    }

    private JobApplication resultSetToJobApplication(ResultSet resultSet) throws SQLException {
        return new JobApplication(
                resultSet.getString("job_name"),
                resultSet.getString("company_name"),
                resultSet.getString("url"),
                resultSet.getTimestamp("application_time").toLocalDateTime()
        );
    }

    private int executeInsertion(JobApplication jobApplication, PreparedStatement preparedStatement) 
    throws SQLException {
        preparedStatement = updatePreparedStatement(jobApplication, preparedStatement);
        return preparedStatement.executeUpdate();
    }

    private PreparedStatement updatePreparedStatement(
        JobApplication jobApplication, PreparedStatement preparedStatement
) throws SQLException {
        int jobNameIndex = 1;
        int companyNameIndex = 2;
        int urlIndex = 3;
        int applicationTimeIndex = 4;
        preparedStatement.setString(jobNameIndex, jobApplication.jobName());
        preparedStatement.setString(companyNameIndex, jobApplication.companyName());
        preparedStatement.setString(urlIndex, jobApplication.url());
        preparedStatement.setTimestamp(applicationTimeIndex, Timestamp.from(Instant.now()));
        return preparedStatement;
    }
}
