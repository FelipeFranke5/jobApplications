package dev.franke.felipe.job_applications.database.loader;

import dev.franke.felipe.job_applications.database.exception.LoadDriverException;

public class MySqlDriverLoader implements DriverLoader {

    @Override
    public void load() throws LoadDriverException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception exception) {
            throw new LoadDriverException("Error while trying to load driver with message: " + exception.getMessage());
        }
    }
}
