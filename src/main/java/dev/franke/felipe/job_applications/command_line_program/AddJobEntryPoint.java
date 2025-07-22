package dev.franke.felipe.job_applications.command_line_program;

import dev.franke.felipe.job_applications.command_line_program.exception.EntryPointException;
import dev.franke.felipe.job_applications.database.queries.MySqlConnector;
import dev.franke.felipe.job_applications.database.queries.MySqlQuery;
import dev.franke.felipe.job_applications.database.queries.validator.MySqlQueryValidator;
import dev.franke.felipe.job_applications.domain.JobApplication;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AddJobEntryPoint {

    public void start() {
        databaseDriverOptionsMenu();
    }

    private void databaseDriverOptionsMenu() {
        outputDatabaseDriverOptions();
        int databaseDriverSelectedOption = getMenuOption();
        assertDatabaseOptionIsValid(databaseDriverSelectedOption);
        if (databaseDriverSelectedOption == 2) {
            System.out.println("Ok.. going back!");
            return;
        }
        initializeInsertion();
    }

    private void outputInsertionOptions() {
        System.out.println();
        System.out.println("What do you want to do now?");
        System.out.println("Type 1 if you want to add a new Job Application");
        System.out.println("Type 2 if you want to quit");
        System.out.println("Other values will not be accepted and will terminate the program");
        System.out.println();
    }

    private void outputDatabaseDriverOptions() {
        System.out.println();
        System.out.println("Main Menu for adding a new job");
        System.out.println("Please select a Database Driver:");
        System.out.println("Type 1 if you want to use MySQL");
        System.out.println("Type 2 if you want to quit the program");
        System.out.println("Other values will not be accepted and will terminate the program");
        System.out.println();
    }

    private int getMenuOption() {
        System.out.print("Please, select the option: ");
        return MainEntryPoint.SCANNER.nextInt();
    }

    private void initializeInsertion() {
        outputInsertionOptions();
        int insertionSelectedOption = getMenuOption();
        assertInsertionOptionIsValid(insertionSelectedOption);
        if (insertionSelectedOption == 2) {
            System.out.println("Going back!");
            databaseDriverOptionsMenu();
        }
        var credentials = getDatabaseCredentials();
        mySqlInsertion(credentials);
        databaseDriverOptionsMenu();
    }

    private void mySqlInsertion(ArrayList<String> credentials) {
        var jobApplication = getJobApplication();
        var mySqlConnector = getSqlConnector(credentials);
        try (Connection connection = mySqlConnector.connect()) {
            MySqlQuery mySqlQuery = new MySqlQuery(new MySqlQueryValidator());
            mySqlQuery.insertJobApplication(connection, jobApplication);
            System.out.println("Added new application..");
        } catch (Exception exception) {
            throw new EntryPointException("Error in Application Insertion: " + exception.getMessage());
        }
    }

    private MySqlConnector getSqlConnector(ArrayList<String> credentials) {
        int usernameIndex = 0;
        int passwordIndex = 1;
        return new MySqlConnector(credentials.get(usernameIndex), credentials.get(passwordIndex));
    }

    private JobApplication getJobApplication() {
        System.out.print("What is the JOB Name? R: ");
        var jobName = getUserText();
        System.out.print("What is the Company Name? R: ");
        var companyName = getUserText();
        System.out.print("What is the application website URL? R: ");
        var url = getUserText();
        return new JobApplication(jobName, companyName, url, LocalDateTime.now());
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

    private void assertInsertionOptionIsValid(int option) {
        try {
            if (option < 1 || option > 2) {
                throw new EntryPointException("Invalid option for Insertion!");
            }
        } catch (Exception exception) {
            throw new EntryPointException("Error in Application Insertion: " + exception.getMessage());
        }
    }

    private void assertDatabaseOptionIsValid(int option) {
        try {
            if (option < 1 || option > 2) {
                throw new EntryPointException("Invalid option for Database Driver!");
            }
        } catch (Exception exception) {
            throw new EntryPointException("Error in Application Insertion: " + exception.getMessage());
        }
    }
}
