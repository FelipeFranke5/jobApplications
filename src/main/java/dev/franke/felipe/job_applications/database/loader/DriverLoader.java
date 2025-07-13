package dev.franke.felipe.job_applications.database.loader;

import dev.franke.felipe.job_applications.database.exception.ConnectionException;
import dev.franke.felipe.job_applications.database.exception.LoadDriverException;

import java.sql.Connection;

public interface DriverLoader {
    void load() throws LoadDriverException;
}
