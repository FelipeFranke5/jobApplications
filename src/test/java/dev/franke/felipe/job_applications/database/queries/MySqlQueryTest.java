package dev.franke.felipe.job_applications.database.queries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dev.franke.felipe.job_applications.database.queries.validator.DatabaseValidator;
import dev.franke.felipe.job_applications.domain.JobApplication;

class MySqlQueryTest {

    private static final String TABLE_NAME = "applications";

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private Connection mockConnection;

    @Mock
    private DatabaseValidator validator;

    @Mock
    private ResultSet mockResultSet;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGetJobApplicationsByCompanyName_WhenNoRecords_ReturnsEmptyList() throws SQLException {
        String companyName = "Any Company";
        int connectionTimeoutInSeconds = 20;
        Mockito.when(mockConnection.isClosed()).thenReturn(false);
        Mockito.when(mockConnection.isReadOnly()).thenReturn(false);
        Mockito.when(mockConnection.isValid(connectionTimeoutInSeconds)).thenReturn(true);
        Mockito.when(mockConnection.prepareStatement(filterByCompanyNameSqlString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);
        MySqlQuery mySqlQuery = getDefaultMySqlQuery();
        List<JobApplication> actualResult = mySqlQuery.getJobApplicationsByCompanyName(mockConnection, companyName);
        int expectedListSize = 0;
        assertEquals(expectedListSize, actualResult.size());
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void testGetJobApplicationsByCompanyName_WhenTwoRecords_ReturnsListOfTwoJobApplications()
    throws SQLException {
        int jobIndex1 = 1;
        int jobIndex2 = 2;
        String jobName1 = "First Job Name";
        String jobName2 = "Second Job Name";
        String companyName = "Any Company";
        String url1 = "https://www.google.com/";
        String url2 = "https://www.google.com/1";
        LocalDateTime applicationTime1 = LocalDateTime.of(2025, 12, 10, 10, 15);
        LocalDateTime applicationTime2 = LocalDateTime.of(2025, 12, 10, 10, 25);
        JobApplication jobApplication1 = new JobApplication(jobName1, companyName, url1, applicationTime1);
        JobApplication jobApplication2 = new JobApplication(jobName2, companyName, url2, applicationTime2);
        int connectionTimeoutInSeconds = 20;
        Mockito.when(mockConnection.isClosed()).thenReturn(false);
        Mockito.when(mockConnection.isReadOnly()).thenReturn(false);
        Mockito.when(mockConnection.isValid(connectionTimeoutInSeconds)).thenReturn(true);
        Mockito.when(mockConnection.prepareStatement(filterByCompanyNameSqlString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getInt("id")).thenReturn(jobIndex1).thenReturn(jobIndex2);
        Mockito.when(mockResultSet.getString("job_name")).thenReturn(jobName1).thenReturn(jobName2);
        Mockito.when(mockResultSet.getString("company_name")).thenReturn(companyName).thenReturn(companyName);
        Mockito.when(mockResultSet.getString("url")).thenReturn(url1).thenReturn(url2);
        Mockito.when(mockResultSet.getTimestamp("application_time"))
                    .thenReturn(Timestamp.valueOf(applicationTime1))
                    .thenReturn(Timestamp.valueOf(applicationTime2));
        MySqlQuery mySqlQuery = getDefaultMySqlQuery();
        List<JobApplication> expectedList = List.of(jobApplication1, jobApplication2);
        List<JobApplication> actualResult = mySqlQuery.getJobApplicationsByCompanyName(mockConnection, companyName);
        int expectedListSize = 2;
        assertEquals(expectedListSize, actualResult.size());
        assertFalse(actualResult.isEmpty());
        assertEquals(expectedList, actualResult);
    }

    private MySqlQuery getDefaultMySqlQuery() {
        return new MySqlQuery(validator);
    }

    private String filterByCompanyNameSqlString() {
        return "SELECT * FROM " + TABLE_NAME + " WHERE company_name = ?";
    }

    private String insertJobApplicationSqlString() {
        return "INSERT INTO " + TABLE_NAME + "(job_name, company_name, url, application_time) VALUES (?, ?, ?, ?)";
    }

}
