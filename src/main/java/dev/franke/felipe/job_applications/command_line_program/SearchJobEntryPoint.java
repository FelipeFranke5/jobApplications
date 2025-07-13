package dev.franke.felipe.job_applications.command_line_program;

import dev.franke.felipe.job_applications.command_line_program.exception.EntryPointException;
import dev.franke.felipe.job_applications.database.queries.MySqlConnector;
import dev.franke.felipe.job_applications.database.queries.MySqlQuery;
import dev.franke.felipe.job_applications.domain.JobApplication;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchJobEntryPoint {

    public void start() {
        outputDatabaseDriverOptions();
        var menuOption = getMenuOption();
        assertDatabaseOptionIsValid(menuOption);
        if (menuOption == 2) {
            System.out.println("Ok.. Exiting");
            return;
        }
        initializeMySqlSearch();
    }

    private void outputSearchOptions() {
        System.out.println();
        System.out.println("What do you want to do now?");
        System.out.println("Type 1 if you want to search by Company Name");
        System.out.println("Type 2 if you want to quit");
        System.out.println("Other values will not be accepted and will terminate the program");
        System.out.println();
    }

    private void outputDatabaseDriverOptions() {
        System.out.println();
        System.out.println("You selected to search for a job application.. good!");
        System.out.println("Please select a Database Driver:");
        System.out.println("Type 1 if you want to use MySQL");
        System.out.println("Type 2 if you want to quit the program");
        System.out.println("Other values will not be accepted and will terminate the program");
        System.out.println();
    }

    private void initializeMySqlSearch() {
        outputSearchOptions();
        int option = getMenuOption();
        assertSearchOptionIsValid(option);
        performSearchOrExit(option);
    }

    private void performSearchOrExit(int menuOption) {
        if (menuOption == 2) {
            System.out.println("Ok.. Exiting");
            return;
        }
        var credentials = getDatabaseCredentials();
        var companyName = getSearchParameter();
        printMySqlResults(getSqlConnector(credentials), companyName);
    }

    private MySqlConnector getSqlConnector(ArrayList<String> credentials) {
        int usernameIndex = 0;
        int passwordIndex = 1;
        return new MySqlConnector(credentials.get(usernameIndex), credentials.get(passwordIndex));
    }

    private void printMySqlResults(MySqlConnector mySqlConnector, String companyName) {
        try (Connection connection = mySqlConnector.connect()) {
            MySqlQuery mySqlQuery = new MySqlQuery();
            var results = mySqlQuery.getJobApplicationsByCompanyName(connection, companyName);
            showMySqlResults(results);
            initializeMySqlSearch();
        } catch (Exception exception) {
            throw new EntryPointException("Error in Search Job Entry Point: " + exception.getMessage());
        }
    }

    private void showMySqlResults(List<JobApplication> applications) {
        if (applications.isEmpty()) {
            System.out.println("Nothing to display!");
            return;
        }
        System.out.println();
        System.out.println("List of All Applications with this filter ..");
        AtomicInteger jobIndex = new AtomicInteger(1);
        applications.forEach(jobApplication -> {
            System.out.println("Index: " + jobIndex.get());
            System.out.println("Job Name: " + jobApplication.jobName());
            System.out.println("Company: " + jobApplication.companyName());
            System.out.println("URL: " + jobApplication.url());
            System.out.println("Application Time: " + jobApplication.applicationTime());
            System.out.println("-----------------------------------------");
            jobIndex.getAndIncrement();
        });
    }

    private String getSearchParameter() {
        System.out.print("Type the company name to look for: ");
        return getUserText();
    }

    private ArrayList<String> getDatabaseCredentials() {
        ArrayList<String> credentials = new ArrayList<>();
        MainEntryPoint.SCANNER.nextLine();
        System.out.print("What is the database username? R: ");
        var username = getUserText();
        System.out.print("What is the database password? R: ");
        var password = getUserText();
        credentials.add(username);
        credentials.add(password);
        System.out.println("Credentials set!");
        return credentials;
    }

    private String getUserText() {
        return MainEntryPoint.SCANNER.nextLine();
    }

    private int getMenuOption() {
        System.out.print("Please, select the option: ");
        return MainEntryPoint.SCANNER.nextInt();
    }

    private void assertSearchOptionIsValid(int option) {
        try {
            if (option < 1 || option > 2) {
                throw new EntryPointException("Invalid option for Searching!");
            }
        } catch (Exception exception) {
            throw new EntryPointException("Error in Search Job Entry Point: " + exception.getMessage());
        }
    }

    private void assertDatabaseOptionIsValid(int option) {
        try {
            if (option < 1 || option > 2) {
                throw new EntryPointException("Invalid option for Database Driver!");
            }
        } catch (Exception exception) {
            throw new EntryPointException("Error in Search Job Entry Point: " + exception.getMessage());
        }
    }
}
