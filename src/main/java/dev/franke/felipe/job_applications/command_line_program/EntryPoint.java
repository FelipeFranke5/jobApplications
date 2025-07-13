package dev.franke.felipe.job_applications.command_line_program;

public interface EntryPoint {
    void startProgram();
    void searchJobApplication();
    void addJobApplication();
    void quitWithError();
    void quitWithoutError();
}
