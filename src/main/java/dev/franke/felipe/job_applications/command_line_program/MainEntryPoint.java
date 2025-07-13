package dev.franke.felipe.job_applications.command_line_program;

import dev.franke.felipe.job_applications.command_line_program.exception.EntryPointException;

import java.util.Scanner;

public class MainEntryPoint implements EntryPoint {

    public static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void startProgram() {
        try {
            System.out.println("Starting the program! Welcome ..");
            outputOptions();
            int selectedOption = getMenuOption();
            assertMenuOptionIsValid(selectedOption);
            redirectUser(selectedOption);
        } catch (Exception exception) {
            System.out.println("Error during execution of the routine!");
            System.out.println("Exception Class Name: " + exception.getClass().getSimpleName());
            System.out.println("Exception Message: " + exception.getMessage());
        } finally {
            SCANNER.close();
        }

    }

    private void redirectUser(int option) {
        switch (option) {
            case 1:
                searchJobApplication();
                break;
            case 2:
                addJobApplication();
                break;
            case 3:
                quitWithoutError();
            default:
                System.out.println("Invalid option..");
                quitWithError();
        }
    }

    @Override
    public void searchJobApplication() {
        SearchJobEntryPoint searchJobEntryPoint = new SearchJobEntryPoint();
        searchJobEntryPoint.start();
        quitWithoutError();
    }

    @Override
    public void addJobApplication() {
        AddJobEntryPoint addJobEntryPoint = new AddJobEntryPoint();
        addJobEntryPoint.start();
        quitWithoutError();
    }

    @Override
    public void quitWithError() {
        System.out.println("Exiting the program with error..");
        throw new EntryPointException("Entry Point Error");
    }

    @Override
    public void quitWithoutError() {
        System.out.println("Exiting the program without error..");
        System.exit(0);
    }

    private void assertMenuOptionIsValid(int option) {
        try {
            if (option < 1 || option > 3) {
                System.out.println("Option is not valid!");
                quitWithError();
            }
        } catch (Exception exception) {
            System.out.println("An error has occurred!");
            System.out.println("Error message: " + exception.getMessage());
            quitWithError();
        }
    }

    private void outputOptions() {
        System.out.println();
        System.out.println("Here are the available options:");
        System.out.println("Type 1 if you want to search for a job application");
        System.out.println("Type 2 if you want to add a job application");
        System.out.println("Type 3 if you want to quit the program");
        System.out.println("Other values will not be accepted and will terminate the program");
        System.out.println();
    }

    private int getMenuOption() {
        System.out.print("Select menu option: ");
        return SCANNER.nextInt();

    }
}
