package dev.franke.felipe.job_applications.database.queries;

import dev.franke.felipe.job_applications.domain.JobApplication;

import java.sql.Connection;
import java.util.List;

public interface DatabaseQuery {
    List<JobApplication> getJobApplicationsByCompanyName(Connection connection, String companyName);
    void insertJobApplication(Connection connection, JobApplication jobApplication);
}
