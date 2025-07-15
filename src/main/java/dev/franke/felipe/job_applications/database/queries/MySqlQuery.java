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
            throw new SqlExecutionException("Could not get Prepared Statement." + sqlException.getMessage());
        }
    }

    @Override
    public void insertJobApplication(Connection connection, JobApplication jobApplication) {
        VALIDATOR.checkConnection(connection);
        VALIDATOR.checkJobApplicationIsValid(jobApplication);
        var sql = insertJobApplicationSqlString(jobApplication);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            executeInsertion(jobApplication, preparedStatement);
        } catch (SQLException sqlException) {
            throw new SqlExecutionException("Error while attempting to insert new record: " + sqlException.getMessage());
        }
    }

    private String filterByCompanyNameSqlString() {
        return "SELECT * FROM " + TABLE_NAME + " WHERE company_name = ?";
    }

    private String insertJobApplicationSqlString(JobApplication jobApplication) {
        return "INSERT INTO " + TABLE_NAME + "(job_name, company_name, url, application_time) VALUES (?, ?, ?, ?)";
    }

    private List<JobApplication> getJobApplications(String companyName, PreparedStatement preparedStatement) throws SQLException {
        int companyNameIndex = 1;
        preparedStatement.setString(companyNameIndex, companyName);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return mapResultSetToArrayList(resultSet);
        }
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

    private void executeInsertion(JobApplication jobApplication, PreparedStatement preparedStatement) throws SQLException {
        int jobNameIndex = 1;
        int companyNameIndex = 2;
        int urlIndex = 3;
        int applicationTimeIndex = 4;
        preparedStatement.setString(jobNameIndex, jobApplication.jobName());
        preparedStatement.setString(companyNameIndex, jobApplication.companyName());
        preparedStatement.setString(urlIndex, jobApplication.url());
        preparedStatement.setTimestamp(applicationTimeIndex, Timestamp.from(Instant.now()));
        int affectedRows = preparedStatement.executeUpdate();
        VALIDATOR.checkNumberOfInsertionsIsGreaterThanZero(affectedRows);
    }
}
