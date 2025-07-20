package dev.franke.felipe.job_applications.database.loader;

import dev.franke.felipe.job_applications.database.exception.LoadDriverException;

public interface DriverLoader {
    void load() throws LoadDriverException;
}
